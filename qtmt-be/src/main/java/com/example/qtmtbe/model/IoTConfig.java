package com.example.qtmtbe.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IoTConfig {

    @Value("${iot.app.httpapi}")
    private String httpApi;

    @Value("${iot.app.mqttbroker}")
    private String mqttBroker;

    @Value("${iot.app.id}")
    private String appId;

    @Value("${iot.app.name}")
    private String appName;

    @Value("${iot.app.token}")
    private String token;

    // Getters
    public String getHttpApi() {
        return httpApi;
    }

    public String getMqttBroker() {
        return mqttBroker;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public String getToken() {
        return token;
    }
}
