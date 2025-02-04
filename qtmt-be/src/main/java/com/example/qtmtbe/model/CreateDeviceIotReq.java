package com.example.qtmtbe.model;

import java.util.List;

public class CreateDeviceIotReq {
    List<DeviceIot> deviceList;
    String appId;

    public List<DeviceIot> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceIot> deviceList) {
        this.deviceList = deviceList;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
