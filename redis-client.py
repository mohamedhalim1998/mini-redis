import socket
import shlex
import sys

class SimpleRedisClient:
    def __init__(self, host: str = "127.0.0.1", port: int = 6379, timeout: float = 60.0):
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

    def execute(self, *args):
        """Send command and read back the parsed response."""
        if not self.sock:
            self.connect()
        
        payload = self._encode_cmd(*args)
        self.sock.sendall(payload)
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


def run_repl(host="127.0.0.1", port=6379):
    client = SimpleRedisClient(host=host, port=port)
    try:
        client.connect()
        print(f"Connected to Redis at {host}:{port}")
        print("Type commands like SET key 'hello world', GET key, PING, or 'exit' to quit.\n")
    except Exception as e:
        print(f"Could not connect to Redis server: {e}")
        sys.exit(1)

    while True:
        try:
            # Read user input
            raw_input = input(f"{host}:{port}> ").strip()
            if not raw_input:
                continue

            if raw_input.lower() in ("exit", "quit"):
                break

            # Parse input into arguments preserving quoted strings
            args = shlex.split(raw_input)

            # Send command and format print response
            response = client.execute(*args)
            print(format_output(response))

        except KeyboardInterrupt:
            print("\nUse 'exit' or 'quit' to close.")
        except ConnectionError:
            print("Connection to Redis was lost. Reconnecting...")
            try:
                client.connect()
            except Exception as e:
                print(f"Failed to reconnect: {e}")
        except Exception as e:
            print(f"Error: {e}")

    client.close()
    print("Disconnected.")


if __name__ == "__main__":
    run_repl("127.0.0.1", 8080)