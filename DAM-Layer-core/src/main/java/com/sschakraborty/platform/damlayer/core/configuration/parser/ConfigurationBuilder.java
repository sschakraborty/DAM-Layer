package com.sschakraborty.platform.damlayer.core.configuration.parser;

import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import org.hibernate.cfg.Configuration;

import java.util.List;

public interface ConfigurationBuilder {
    /**
     * Creates Hibernate configuration from connector metadata
     *
     * @param metadata The connector metadata
     * @return Hibernate configuration
     */
    Configuration build(ConnectorMetadata metadata, List<Class<? extends Model>> classes);
}