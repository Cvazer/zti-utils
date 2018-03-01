package com.github.cvazer.configurator;

import java.io.File;

/**
 * Abstract factory class for {@code Configurator} instances
 * @author Yan Frankovski
 * @since ZTIU 1.1.1
 * @see Configurator
 */
public abstract class ConfiguratorFactory {

    public static Configurator getInstance(File file){
        if (file.getName().contains(".xml")){
            return new XMLConfigurator(file);
        } else {
            throw new IllegalArgumentException("Not XML file!");
        }
    }

}
