import socket
import sys
import time

class SimpleRedisClient:
    def __init__(self, host: str = "127.0.0.1", port: int = 6379, timeout: float = 5.0):
        self.host = host
        self.port = port
        self.timeout = timeout
        self.sock = None
        self.file = None

    def connect(self):
        """Establish TCP connection to the Redis server."""
        self.sock = socket.create_connection((self.host, self.port), timeout=self.timeout)
        self.file = self.sock.makefile("rb")

    def close(self):
        """Close the socket connection."""
        if self.file:
            self.file.close()
            self.file = None
        if self.sock:
            self.sock.close()
            self.sock = None

    def _encode_cmd(self, *args) -> bytes:
        """Encode command arguments into RESP2 Array format."""
        out = [f"*{len(args)}\r\n".encode("utf-8")]
        for arg in args:
            arg_bytes = str(arg).encode("utf-8")
            out.append(f"${len(arg_bytes)}\r\n".encode("utf-8"))
            out.append(arg_bytes + b"\r\n")
        return b"".join(out)

    def _parse_response(self):
        """Parse RESP2 response stream."""
        line = self.file.readline()
        if not line:
            raise ConnectionError("Connection lost")

        prefix, data = chr(line[0]), line[1:-2]  # Remove trailing \r\n

        if prefix == "+":      # Simple String
            return data.decode("utf-8")
        elif prefix == "-":    # Error
            return f"(error) {data.decode('utf-8')}"
        elif prefix == ":":    # Integer
            return int(data)
        elif prefix == "$":    # Bulk String
            length = int(data)
            if length == -1:
                return "(nil)"
            val = self.file.read(length)
            self.file.readline()  # Consume trailing \r\n
            return val.decode("utf-8")
        elif prefix == "*":    # Array
            count = int(data)
            if count == -1:
                return "(empty array)"
            return [self._parse_response() for _ in range(count)]
        else:
            raise ValueError(f"Unknown RESP prefix: {prefix}")

    def send_command(self, *args):
        """Send command without blocking for response (useful for Pub/Sub subscriptions)."""
        if not self.sock:
            self.connect()
        payload = self._encode_cmd(*args)
        self.sock.sendall(payload)

    def read_response(self):
        """Read the next parsed RESP message from the socket stream."""
        return self._parse_response()

    def execute(self, *args):
        """Send command and read back the parsed response (Standard Request-Response)."""
        self.send_command(*args)
        return self._parse_response()


def format_output(res, indent=0):
    """Format RESP output similar to redis-cli."""
    prefix = "  " * indent
    if isinstance(res, list):
        if not res:
            return f"{prefix}(empty list or set)"
        lines = []
        for i, item in enumerate(res, 1):
            if isinstance(item, list):
                lines.append(f"{prefix}{i})")
                lines.append(format_output(item, indent + 1))
            else:
                lines.append(f"{prefix}{i}) \"{item}\"" if isinstance(item, str) else f"{prefix}{i}) {item}")
        return "\n".join(lines)
    elif isinstance(res, str):
        if res.startswith("(error)") or res == "(nil)":
            return f"{prefix}{res}"
        return f"{prefix}\"{res}\""
    else:
        return f"{prefix}(integer) {res}"


def test_pubsub_connections(host="127.0.0.1", port=6379):
    print("==================================================")
    print("       Testing Redis Pub/Sub Connections          ")
    print("==================================================\n")

    # 1. Instantiate 2 Subscriber clients and 1 Publisher client
    sub1 = SimpleRedisClient(host=host, port=port, timeout=60.0)
    sub2 = SimpleRedisClient(host=host, port=port, timeout=60.0)
    publisher = SimpleRedisClient(host=host, port=port, timeout=60.0)

    try:
        # 2. Connect all sockets
        sub1.connect()
        sub2.connect()
        publisher.connect()
        print("[+] Created 3 separate TCP sockets successfully.\n")

        # 3. Subscribe Sub1 to channel 'news'
        print("--- Subscribing Sub1 to 'news' ---")
        sub1.send_command("SUBSCRIBE", "news")
        ack1 = sub1.read_response()
        print("Sub1 Confirmation:")
        print(format_output(ack1))
        print()

        # 4. Subscribe Sub2 to channel 'news' AND 'alerts'
        print("--- Subscribing Sub2 to 'news' and 'alerts' ---")
        sub2.send_command("SUBSCRIBE", "news", "alerts")
        ack2_a = sub2.read_response()
        # ack2_b = sub2.read_response()
        print("Sub2 Confirmations:")
        print(format_output(ack2_a))
        # print(format_output(ack2_b))
        print()

        # 5. Publisher publishes message to 'news'
        print("--- Publishing to 'news' channel ---")
        pub_res1 = publisher.execute("PUBLISH", "news", "Breaking News: Redis Pub/Sub works!")
        print(f"Publisher Output: {format_output(pub_res1)} subscriber(s) received message\n")

        # Read pushed message on Sub1 socket
        msg_sub1 = sub1.read_response()
        print("--> Sub1 Socket Received Push Event:")
        print(format_output(msg_sub1))
        print()

        # Read pushed message on Sub2 socket
        msg_sub2 = sub2.read_response()
        print("--> Sub2 Socket Received Push Event:")
        print(format_output(msg_sub2))
        print()

        # 6. Publisher publishes message to 'alerts'
        print("--- Publishing to 'alerts' channel ---")
        pub_res2 = publisher.execute("PUBLISH", "alerts", "System Alert: CPU overload!")
        print(f"Publisher Output: {format_output(pub_res2)} subscriber(s) received message\n")

        # Read pushed message on Sub2 socket
        msg_alerts = sub2.read_response()
        print("--> Sub2 Socket Received Push Event:")
        print(format_output(msg_alerts))
        print()

        # 7. Test command restriction on Pub/Sub connection
        print("--- Testing Command Blocking on Sub1 ---")
        print("Attempting to run 'GET key' on Sub1 in Pub/Sub mode...")
        sub1.send_command("GET", "my_key")
        err_res = sub1.read_response()
        print("Sub1 Response:")
        print(format_output(err_res))
        print()

    except Exception as e:
        print(f"Error during execution: {e}")

    finally:
        sub1.close()
        sub2.close()
        publisher.close()
        print("[+] All connections closed.")


if __name__ == "__main__":
    test_pubsub_connections("127.0.0.1", 8080)