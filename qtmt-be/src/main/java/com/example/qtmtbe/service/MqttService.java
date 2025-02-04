package com.example.qtmtbe.service;

import com.example.qtmtbe.entity.Cin;
import com.example.qtmtbe.entity.Device;
import com.example.qtmtbe.model.DeviceData;
import com.example.qtmtbe.model.IoTConfig;
import com.example.qtmtbe.repository.CinRepository;
import com.example.qtmtbe.repository.DeviceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MqttService {
    @Autowired
    private IoTConfig iotConfig;
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    CinRepository cinRepository;

    public void initMqttClient() throws MqttException {
        MqttClient client = new MqttClient(iotConfig.getMqttBroker(), iotConfig.getAppId());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true); // Xóa session trước đó
        options.setUserName(iotConfig.getAppId());
        options.setPassword(iotConfig.getToken().toCharArray());
        // Đăng ký callback để xử lý sự kiện
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println("Message received on topic " + topic + ": " + mqttMessage.toString());
                // Tạo ObjectMapper để phân tích cú pháp JSON
                ObjectMapper objectMapper = new ObjectMapper();

                // Phân tích JSON thành JsonNode
                JsonNode rootNode = objectMapper.readTree(mqttMessage.toString());

                // Lấy giá trị của "con"
                JsonNode cinJson = rootNode.path("pc").path("m2m:sgn").path("nev").path("rep").path("m2m:cin");

                JsonNode conNode = cinJson.path("con");
                String conValue = conNode.asText();
                System.out.println("conValue: " + conValue);

                // parse conValue thành đối tượng DeviceData
                DeviceData deviceData = objectMapper.readValue(conValue, DeviceData.class);

                // Lấy giá trị của "cr"
                JsonNode crNode = cinJson.path("cr");
                // Lấy giá trị của trường "cr"
                String crValue = crNode.asText();

                System.out.println("crNode: " + crValue);

                Device device = deviceRepository.findByDeviceCode(crValue);
                Cin cin = new Cin();
                cin.setDeviceId(device.getId());
                cin.setHumi(deviceData.getHumi());
                cin.setTemp(deviceData.getTemp());
                cin.setTime(new Date().getTime());
                cinRepository.save(cin);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        System.out.println("Connecting to broker: " + iotConfig.getMqttBroker());
        client.connect(options);
        System.out.println("Connected");

        //subscribe
        //String topicReq = "/oneM2M/resp/" + iotConfig.getAppId() + "/in-cse/json";
        String topicReq = "oneM2M/req/+/" + iotConfig.getAppId() + "/json";
        client.subscribe(topicReq);
        System.out.println("Subscribed to topic: " + topicReq);

    }
}
