package by.zti.incubator.configurator;

import java.util.Properties;

/**
 * This is a utility interface that should be instantiated by any configurator implementations
 *
 * @author Yan Frankovski
 * @since ZTIU 1.1.0
 * @see by.zti.incubator.configurator.XMLConfigurator
 */
public interface Configurator {
    /**
     * This method should be called on {@code Configurator} instance to read key-value pairs from given source
     * @return {@code Properties} map that contains fieldName-value pairs
     */
    Properties read();
}
