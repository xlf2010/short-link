package com.xlf.util;

import io.seruco.encoding.base62.Base62;

public class Base62Util {
    private static final String CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println(now);
        System.out.println(Long.toBinaryString(now));
        System.out.println(encode(now));
    }

    public static String encode(long id) {
        int longByteLength = 8;
        byte[] bs = new byte[longByteLength];
        for (int i = longByteLength - 1; i >= 0; i--) {
            bs[i] = (byte) (id & 0xFF);
            id >>>= 8;
        }
        return encode(bs);
    }

    public static String encode(byte[] data) {
        byte[] encode = Base62.createInstance().encode(data);
        return new String(encode);
    }
}
