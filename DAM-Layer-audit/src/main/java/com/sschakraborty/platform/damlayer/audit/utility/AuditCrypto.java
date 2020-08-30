package com.sschakraborty.platform.damlayer.audit.utility;

import java.util.Arrays;

public class AuditCrypto {
    public static String encrypt(Object object, byte[] encKey) {
        // TODO: Complete encryption
        return object.toString() + Arrays.toString(encKey);
    }
}