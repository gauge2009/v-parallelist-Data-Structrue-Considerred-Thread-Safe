package com.gauge2009.redis.limit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * <p>
 *     ͨ��zsetʵ�ֻ��������㷨����
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
     * �ж���Ϊ�Ƿ�����
     *
     * @param userId        �û�id
     * @param actionKey     ��Ϊkey
     * @param period        ��������
     * @param maxCount      �������������������ڴ�С��
     * @return
     */
    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = this.key(userId, actionKey);
        long ts = System.currentTimeMillis();
        Pipeline pipe = jedis.pipelined();
        pipe.multi();
        pipe.zadd(key, ts, String.valueOf(ts));
        // �Ƴ���������֮�������
        pipe.zremrangeByScore(key, 0, ts - (period * 1000));
        System.out.println(" # �Ƴ�����ʱ�����0��" + (ts - (period * 1000)) +"֮�������"  );//Redis Zremrangebyscore ���������Ƴ������У�ָ��������score�������ڵ����г�Ա��
        Response<Long> count = pipe.zcard(key);
        // ������Ϊ�Ĺ���ʱ�䣬�������Ϊ�����ݣ�zset����ɾ���Դ˽�ʡ�ڴ�ռ�
        pipe.expire(key, period);
        pipe.exec();
        pipe.close();
        return count.get() <= maxCount;
    }


    /**
     * ����key
     *
     * @param userId
     * @param actionKey
     * @return
     */
    public String key(String userId, String actionKey) {
        return String.format("limit:%s:%s", userId, actionKey);
    }

}