package com.flamingo.comeon.rpc.core.common;

public final class Assert {
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, null);
    }
}
