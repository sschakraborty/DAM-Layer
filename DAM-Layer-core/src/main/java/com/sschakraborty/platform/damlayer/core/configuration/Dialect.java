package com.sschakraborty.platform.damlayer.core.configuration;

public enum Dialect {
    MySQL_5(org.hibernate.dialect.MySQL5Dialect.class.getName()),
    MySQL_55(org.hibernate.dialect.MySQL55Dialect.class.getName()),
    MySQL_57(org.hibernate.dialect.MySQL57Dialect.class.getName()),
    MySQL_8(org.hibernate.dialect.MySQL8Dialect.class.getName()),
    POSTGRESQL_9(org.hibernate.dialect.PostgreSQL9Dialect.class.getName()),
    POSTGRESQL_91(org.hibernate.dialect.PostgreSQL91Dialect.class.getName()),
    POSTGRESQL_92(org.hibernate.dialect.PostgreSQL92Dialect.class.getName()),
    POSTGRESQL_93(org.hibernate.dialect.PostgreSQL93Dialect.class.getName()),
    POSTGRESQL_94(org.hibernate.dialect.PostgreSQL94Dialect.class.getName()),
    POSTGRESQL_95(org.hibernate.dialect.PostgreSQL95Dialect.class.getName()),
    POSTGRESQL_10(org.hibernate.dialect.PostgreSQL10Dialect.class.getName());

    private final String dialectClassName;

    Dialect(String dialectClassName) {
        this.dialectClassName = dialectClassName;
    }

    public String getDialectClassName() {
        return dialectClassName;
    }
}