package com.ddschool.utils;

import java.util.Random;

/**
 * ���������
 * <p/>
 * Created by wangchenlong on 16/1/24.
 */
public class RandomGenerator {
    private static final Random RANDOM = new Random();

    // �������
    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    // �Ͻ����
    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    // �Ͻ����
    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}