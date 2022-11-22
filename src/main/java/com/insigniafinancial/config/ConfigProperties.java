package com.insigniafinancial.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ConfigProperties {
    private static Properties instance = null;

    public static Properties getInstance() {
        if (instance == null) {
            new ConfigProperties();
        }
        return instance;
    }

    private ConfigProperties() {
        instance = new Properties();
        try {
            instance.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            log.error("config.properties not exists");
            e.printStackTrace();
        }
    }
}