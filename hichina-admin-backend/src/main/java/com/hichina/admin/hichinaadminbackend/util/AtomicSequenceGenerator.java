package com.hichina.admin.hichinaadminbackend.util;

import java.util.concurrent.atomic.AtomicLong;


public class AtomicSequenceGenerator implements SequenceGenerator {

    private AtomicLong value = new AtomicLong(1);

    @Override
    public long getNext() {
        return System.currentTimeMillis() + value.getAndIncrement();
    }


    private static AtomicSequenceGenerator instance;

    private AtomicSequenceGenerator(){}

    public static synchronized AtomicSequenceGenerator getInstance() {
        if (instance == null) {
            instance = new AtomicSequenceGenerator();
        }
        return instance;
    }
}
