package com.sschakraborty.platform.damlayer.audit.utility;

public class AuditCrypto {
    public static String encrypt(Object object, String encKey) {
        // TODO: Complete encryption
        return object.toString() + encKey;
    }
}