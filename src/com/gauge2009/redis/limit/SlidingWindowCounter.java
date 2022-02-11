package com.gauge2009.redis.limit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * <p>
 *     通过zset实现滑动窗口算法限流
 * </p>
 *
 * @Author: gauge2009
 * @Date: 2022/1/1
 */
class SimpleSlidingWindowByZSet {

    private Jedis jedis;

    public SimpleSlidingWindowByZSet(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 判断行为是否被允许
     *
     * @param userId        用户id
     * @param actionKey     行为key
     * @param period        限流周期
     * @param maxCount      最大请求次数（滑动窗口大小）
     * @return
     */
    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = this.key(userId, actionKey);
        long ts = System.currentTimeMillis();
        Pipeline pipe = jedis.pipelined();
        pipe.multi();
        pipe.zadd(key, ts, String.valueOf(ts));
        // 移除滑动窗口之外的数据
        pipe.zremrangeByScore(key, 0, ts - (period * 1000));
        System.out.println(" # 移除所有时间戳在0到" + (ts - (period * 1000)) +"之间的数据"  );//Redis Zremrangebyscore 命令用于移除有序集中，指定分数（score）区间内的所有成员。
        Response<Long> count = pipe.zcard(key);
        // 设置行为的过期时间，如果数据为冷数据，zset将会删除以此节省内存空间
        pipe.expire(key, period);
        pipe.exec();
        pipe.close();
        return count.get() <= maxCount;
    }


    /**
     * 限流key
     *
     * @param userId
     * @param actionKey
     * @return
     */
    public String key(String userId, String actionKey) {
        return String.format("limit:%s:%s", userId, actionKey);
    }

}