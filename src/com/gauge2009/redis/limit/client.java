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
            TimeUnit.MILLISECONDS.sleep(1);//�Ӳ�����������ݿ��Կ���������������Ч�����ӵ�5���Ժ�������������ʧ�ܵ�. ��ʱ���6��Ҳ��ɹ��� ��������ԭ�������ǲ���System.currentTimeMillis()�ĺ��������ͬ�����Ҵ�ʱvalueҲ��System.currentTimeMillis()Ҳ��ͬ���ᵼ��zset��Ԫ�ظ��ǣ�  sol�� ��ѭ����˯��1���뼴�ɣ����Խ������Ԥ�ڣ�
            System.out.println("��" + i +"�ο��� ���� " + (actionAllowed ? "�ɹ�" : "ʧ��"));
        }

        jedis.close();
    }

}