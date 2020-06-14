package com.sschakraborty.platform.damlayer.core.configuration.parser;

import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.marker.Model;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Properties;

public class ConfigurationBuilderImpl implements ConfigurationBuilder {
    private static final String CONNECTION_POOL_PROVIDER = "org.hibernate.hikaricp.internal.HikariCPConnectionProvider";

    @Override
    public Configuration build(ConnectorMetadata metadata, List<Class<? extends Model>> classes) {
        final Configuration configuration = new Configuration();
        configuration.setProperties(buildProperties(metadata));
        classes.forEach(configuration::addAnnotatedClass);
        return configuration;
    }

    private Properties buildProperties(ConnectorMetadata metadata) {
        final Properties properties = new Properties();
        properties.put("hibernate.connection.username", metadata.getUsername());
        properties.put("hibernate.connection.password", metadata.getPassword());
        properties.put("hibernate.connection.autocommit", String.valueOf(metadata.isAutoCommit()));
        properties.put("hibernate.show_sql", String.valueOf(metadata.isDebugMode()));
        properties.put("hibernate.dialect", metadata.getDialectClass());
        properties.put("hibernate.connection.driver_class", metadata.getDriverClass());
        properties.put("hibernate.connection.url", buildConnectionURL(metadata));
        properties.put("hibernate.hbm2ddl.auto", metadata.getDdlMode());
        properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(metadata.getMaxPoolSize()));
        properties.put("hibernate.hikari.idleTimeout", String.valueOf(metadata.getIdleTimeout()));
        properties.put("hibernate.hikari.connectionTimeout", String.valueOf(metadata.getConnectionTimeout()));
        properties.put("hibernate.hikari.minimumIdle", "20");
        properties.put("hibernate.connection.provider_class", CONNECTION_POOL_PROVIDER);
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    private String buildConnectionURL(ConnectorMetadata metadata) {
        final StringBuilder url = new StringBuilder(metadata.getConnectionURLPrefix());
        url.append("//").append(metadata.getConnectionHost()).append(":");
        url.append(metadata.getConnectionPort()).append("/").append(metadata.getDatabaseName());
        return url.toString();
    }
}