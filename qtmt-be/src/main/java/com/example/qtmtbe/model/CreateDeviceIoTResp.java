package com.example.qtmtbe.model;

import java.util.List;

public class CreateDeviceIoTResp {
    private Integer errorCode;
    private String errorMsg;
    private Integer total;
    private List<DeviceIot> deviceList;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DeviceIot> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceIot> deviceList) {
        this.deviceList = deviceList;
    }
}
