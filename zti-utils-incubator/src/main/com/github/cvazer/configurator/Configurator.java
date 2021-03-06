package com.github.cvazer.configurator;

import java.util.Map;

/**
 * This is a utility interface that should be instantiated by any configurator implementations
 *
 * @author Yan Frankovski
 * @since ZTIU 1.1.0
 * @see XMLConfigurator
 */
public interface  Configurator {
    /**
     * This method should be called on {@code Configurator} instance to read key-value pairs from given source by id
     * @param id - {@code String} ref that represents desired profile in xml file for this class
     * @return {@code Properties} map that contains fieldName-value pairs
     */
    Map<String, String> read(String id);
}
