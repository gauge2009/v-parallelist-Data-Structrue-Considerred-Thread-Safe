package com.gauge2009.redis.limit;


import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 *
 */
class TestSimpleSlidingWindowByZSet {

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("192.168.52.128", 46379);
        jedis.auth("sparksubmit666");
        SimpleSlidingWindowByZSet slidingWindow = new SimpleSlidingWindowByZSet(jedis);
        for (int i = 1; i <= 15; i++) {
            boolean actionAllowed = slidingWindow.isActionAllowed("hmz", "access_yy", 60, 5);
            TimeUnit.MILLISECONDS.sleep(1);//从测试输出的数据可以看出，起到了限流的效果，从第5次以后的请求操作都是失败的. 有时候第6次也会成功， 这个问题的原因是我们测试System.currentTimeMillis()的毫秒可能相同，而且此时value也是System.currentTimeMillis()也相同，会导致zset中元素覆盖！  sol： 在循环中睡眠1毫秒即可，测试结果符合预期！
            System.out.println("第" + i +"次开播 —— " + (actionAllowed ? "成功" : "失败"));
        }

        jedis.close();
    }

}