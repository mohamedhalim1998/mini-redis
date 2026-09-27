package com.mohamed.halim.miniredis.command;

import java.util.Set;

public enum CommandType {
    PING,
    ECHO,
    SET,
    GET,
    INCR,
    DECR,
    EXPIRE,
    PEXPIRE,
    TTL,
    PTTL,
    PERSIST, 
    LPUSH, 
    RPUSH, 
    LPOP,  
    RPOP, 
    LLEN, 
    LRANGE, 
    SADD, 
    SREM, 
    SISMEMBER, 
    SMEMBERS, 
    SCARD, 
    HSET, 
    HGET, 
    HDEL, 
    HGETALL, 
    HEXISTS, 
    HLEN, 
    TYPE,
    DEL,
    PEXPIREAT,
    KEYS;
    public static Set<CommandType> modifyingCommands = Set.of(
            SET,
            INCR,
            DECR,
            EXPIRE,
            PEXPIRE,
            PERSIST,
            LPUSH,
            RPUSH,
            LPOP,
            RPOP,
            SADD,
            SREM,
            HSET,
            HDEL,
            DEL,
            PEXPIREAT
    );
    public boolean isModifyingCommand() {
        return modifyingCommands.contains(this);
    }
}
