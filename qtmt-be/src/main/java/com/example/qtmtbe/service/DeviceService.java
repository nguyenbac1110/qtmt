package com.example.qtmtbe.service;



import com.example.qtmtbe.entity.Device;
import com.example.qtmtbe.model.CreateDeviceIoTResp;
import com.example.qtmtbe.model.CreateDeviceIotReq;
import com.example.qtmtbe.model.DeviceIot;
import com.example.qtmtbe.model.IoTConfig;
import com.example.qtmtbe.repository.DeviceRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    private IoTConfig iotConfig;

    RestTemplate restTemplate = new RestTemplate();

    public Device createDevice(String name) {
        DeviceIot deviceIot = new DeviceIot();
        //deviceIot.setDeviceName(name);
        deviceIot.setDeviceName(name != null ? name.trim() : "");

        deviceIot.setDeviceType(1);
        CreateDeviceIotReq createDeviceIotReq = new CreateDeviceIotReq();
        List<DeviceIot> deviceIots = new ArrayList<>();
        deviceIots.add(deviceIot);
        createDeviceIotReq.setDeviceList(deviceIots);
        createDeviceIotReq.setAppId(iotConfig.getAppId());
        String url = "https://oneiot.com.vn:9443/public/device/createDevice";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + iotConfig.getToken());

        HttpEntity<CreateDeviceIotReq> entity = new HttpEntity<>(createDeviceIotReq, headers);
        try {
            // Log payload
            System.out.println("URL gọi đến: " + url);
            System.out.println("Payload gửi đi: " + new Gson().toJson(createDeviceIotReq));
            System.out.println("Headers: " + headers);



            // Gửi yêu cầu POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Response: " + response.getBody());
            CreateDeviceIoTResp createDeviceIoTResp = new Gson().fromJson(response.getBody(), CreateDeviceIoTResp.class);
            Device deviveNew = new Device();
            deviveNew.setName(createDeviceIoTResp.getDeviceList().get(0).getDeviceName());
            deviveNew.setDeviceCode(createDeviceIoTResp.getDeviceList().get(0).getDeviceId());
            deviveNew.setCreateTime(new Date());
            deviceRepository.save(deviveNew);
            return deviveNew;
        } else {
            // Trả về lỗi chi tiết
            System.out.println("API thất bại, mã lỗi: " + response.getStatusCode());
            System.out.println("Phản hồi API: " + response.getBody());
            throw new RuntimeException("API trả về lỗi: " + response.getStatusCode());
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xảy ra khi tạo device: " + e.getMessage());
        }
    }



public Page<Device> getAllDevice(Pageable pageable){
       return deviceRepository.findAll(pageable);

    }

}
