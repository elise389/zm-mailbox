package com.zimbra.cs.mailbox;

import java.util.Set;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.mailbox.Mailbox.MailboxData;
import com.zimbra.cs.mailbox.redis.RedisBackedMap;
import com.zimbra.cs.mailbox.redis.RedisUtils;

public class RedisMailboxState extends MailboxState {

    private RedissonClient client;
    private RedisBackedMap<String, Object> redisMap;

    public RedisMailboxState(MailboxData data, TransactionCacheTracker tracker) {
        super(data, tracker);
    }

    @Override
    protected void init() {
        client = RedissonClientHolder.getInstance().getRedissonClient();
        RMap<String, Object> redisHash = client.getMap(RedisUtils.createAccountRoutedKey(data.accountId, "MAILBOX"));
        redisMap = new RedisBackedMap<>(redisHash, cacheTracker);
        super.init();
    }
    @Override
    protected SynchronizedField<?> initField(MailboxField field) {
        switch(field.getType()) {
        case BOOL:
            return new RedisField<Boolean>(field);
        case INT:
            return new RedisField<Integer>(field);
        case LONG:
            return new RedisField<Long>(field);
        case SET:
            return new RedisField<Set<String>>(field);
        case STRING:
        default:
            return new RedisField<String>(field);
        }
    }

    private class RedisField<T> implements MailboxState.SynchronizedField<T> {

        private String hashKey;
        private MailboxField field;

        public RedisField(MailboxField field) {
            this.field = field;
            this.hashKey = field.name();
        }

        @SuppressWarnings("unchecked")
        @Override
        public T value() {
            T val = (T) redisMap.get(hashKey);
            ZimbraLog.mailbox.debug("got %s=%s from redis for mailbox %s", hashKey, val, data.accountId);
            return val;
        }

        @Override
        public void set(T val) {
            redisMap.put(hashKey, val);
            ZimbraLog.mailbox.debug("set %s=%s for mailbox %s", hashKey, val, data.accountId);
        }

        @SuppressWarnings("unchecked")
        @Override
        public T setIfNotExists(T value) {
            try {
                T prevValue = (T) redisMap.getMap().putIfAbsent(hashKey, value);
                if (prevValue == null) {
                    ZimbraLog.mailbox.debug("set %s=%s for account %s", hashKey, value, data.accountId);
                    return value;
                } else {
                    ZimbraLog.mailbox.debug("%s already set to %s for account %s", hashKey, prevValue, data.accountId);
                    return prevValue;
                }
            } catch (RedisException e) {
                ZimbraLog.mailbox.error("unable to set mailbox field %s=%s for account %s", hashKey, value, data.accountId);
                return value;
            }
        }
    }

    public static class Factory implements MailboxState.Factory {

        @Override
        public MailboxState getMailboxState(MailboxData data, TransactionCacheTracker tracker) {
            return new RedisMailboxState(data, tracker);
        }

    }
}
