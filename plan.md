# Build Your Redis

## Overview

We're building **mini-redis** — a simplified in-memory data structure server that speaks the Redis protocol (RESP2). It will accept TCP connections, parse RESP commands, and execute them against an in-memory key-value store with support for expiration, multiple data types, persistence, and pub/sub.

By the end, you'll have a working server that any standard Redis client (redis-cli, Jedis, Lettuce) can connect to.

## Phases

### Phase 1: RESP Protocol & Basic Commands (Difficulty: Beginner)

- [ ] Implement RESP2 parser (decode: Simple String, Error, Integer, Bulk String, Array)
- [ ] Implement RESP2 encoder (serialize responses back to RESP format)
- [ ] Start a TCP server that accepts client connections on a configurable port
- [ ] Implement single-threaded event loop (accept → read → execute → respond)
- [ ] Implement `PING` / `ECHO` commands
- [ ] Implement `SET key value` and `GET key` (strings only)
- [ ] Implement `DEL key [key ...]` and `EXISTS key [key ...]`
- [ ] Implement `INCR` / `DECR` (atomic integer operations on string values)

**Goal**: A working TCP server that can parse RESP commands, store/retrieve string values, and respond correctly. `redis-cli` can connect and execute basic commands.

**Tests**: Run with `mvn test -Dgroups=phase1`

**Hints**:
- Start by making the RESP parser work in isolation before integrating with networking
- Use `java.nio.channels.ServerSocketChannel` with a `Selector` for non-blocking I/O, or start simple with blocking I/O and a single connection
- The key space is just a `ConcurrentHashMap<String, RedisObject>` (or plain HashMap if single-threaded)

---

### Phase 2: Expiration & TTL (Difficulty: Beginner)

- [ ] Implement `SET key value EX seconds` and `SET key value PX milliseconds`
- [ ] Implement `EXPIRE key seconds` / `PEXPIRE key milliseconds`
- [ ] Implement `TTL key` / `PTTL key` (report remaining time-to-live)
- [ ] Implement lazy expiration (check TTL on every key access)
- [ ] Implement active expiration (background task that periodically samples and removes expired keys)
- [ ] Implement `PERSIST key` (remove expiration)

**Goal**: Keys can expire automatically. Both lazy (on-access) and active (periodic sweep) expiration strategies work correctly.

**Tests**: Run with `mvn test -Dgroups=phase2`

**Hints**:
- Store expiration times in a separate map: `Map<String, Long>` (key → absolute timestamp in millis)
- For active expiration, use a scheduled task that samples N random keys from the expires map
- Be careful with time: use `System.currentTimeMillis()` consistently, or inject a `Clock` for testability

---

### Phase 3: Data Structures (Difficulty: Intermediate)

- [ ] Implement List commands: `LPUSH`, `RPUSH`, `LPOP`, `RPOP`, `LLEN`, `LRANGE`
- [ ] Implement Set commands: `SADD`, `SREM`, `SISMEMBER`, `SMEMBERS`, `SCARD`
- [ ] Implement Hash commands: `HSET`, `HGET`, `HDEL`, `HGETALL`, `HEXISTS`, `HLEN`
- [ ] Implement type checking (return WRONGTYPE error when command targets wrong type)
- [ ] Implement `TYPE key` command
- [ ] Implement `KEYS pattern` (glob-style pattern matching)

**Goal**: Support Redis's core collection types. Each data structure is stored as a distinct type in the key space, with proper type-checking and error messages.

**Tests**: Run with `mvn test -Dgroups=phase3`

**Hints**:
- Use a tagged union / sealed interface for value types: `StringValue`, `ListValue`, `SetValue`, `HashValue`
- Lists map naturally to `LinkedList<String>` or `ArrayDeque<String>`
- Sets map to `HashSet<String>`, Hashes to `HashMap<String, String>`
- For KEYS pattern matching, convert Redis glob syntax (`*`, `?`, `[abc]`) to regex

---

### Phase 4: Persistence (Difficulty: Intermediate)

- [ ] Implement AOF writer: append every write command in RESP format to a file
- [ ] Implement AOF replay: on startup, read and re-execute commands from the AOF file
- [ ] Implement `BGSAVE`-style RDB: serialize current state to a binary/JSON snapshot file
- [ ] Implement RDB loading on startup (if AOF not present)
- [ ] Implement configurable fsync policy (always, everysec, no)
- [ ] Implement AOF rewrite (compact the AOF by writing current state as minimal commands)

**Goal**: Data survives server restarts. The server loads state from AOF (preferred) or RDB on startup.

**Tests**: Run with `mvn test -Dgroups=phase4`

**Hints**:
- AOF is the simpler path: just write each mutating command as a RESP-encoded line to a file
- For RDB, you can use Java serialization or a custom binary format — don't over-engineer this
- AOF rewrite: iterate all keys and emit the minimal SET/LPUSH/SADD commands that recreate current state
- Use a `BufferedOutputStream` and control when `flush()`/`fsync()` is called

---

### Phase 5: Pub/Sub & Pipelining (Difficulty: Advanced)

- [ ] Implement `SUBSCRIBE channel [channel ...]` — register connection for messages
- [ ] Implement `PUBLISH channel message` — fan out to all subscribers, return subscriber count
- [ ] Implement `UNSUBSCRIBE channel [channel ...]`
- [ ] Implement pipelining: process multiple commands from a single read without waiting between them
- [ ] Implement `MULTI` / `EXEC` / `DISCARD` (basic transaction support — queue commands, execute atomically)
- [ ] Implement `INFO` command (return server stats: uptime, connected clients, keys count, memory usage)

**Goal**: Support real-time messaging via pub/sub, efficient batch command execution via pipelining, and basic transaction guarantees.

**Tests**: Run with `mvn test -Dgroups=phase5`

**Hints**:
- Pub/Sub requires tracking which connections subscribe to which channels: `Map<String, Set<Connection>>`
- A subscribed connection is in a special state — it can only run SUBSCRIBE/UNSUBSCRIBE/PING
- Pipelining mostly works for free if your parser handles multiple commands per read buffer
- For MULTI/EXEC: queue commands in a list, then execute them all sequentially on EXEC

---

## Success Criteria

How to know you've successfully completed the project:

- All tests pass: `mvn test`
- `redis-cli` can connect and execute commands from all phases
- Keys expire correctly (both on access and via background sweep)
- Data persists across server restarts (via AOF or RDB)
- Multiple clients can pub/sub simultaneously
- Pipelined commands return correct ordered responses
