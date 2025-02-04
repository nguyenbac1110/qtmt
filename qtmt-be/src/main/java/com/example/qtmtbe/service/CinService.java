package com.example.qtmtbe.service;

import com.example.qtmtbe.entity.Cin;
import com.example.qtmtbe.entity.Device;
import com.example.qtmtbe.model.CreateDeviceIoTResp;
import com.example.qtmtbe.model.CreateDeviceIotReq;
import com.example.qtmtbe.model.DeviceIot;
import com.example.qtmtbe.model.IoTConfig;
import com.example.qtmtbe.repository.CinRepository;
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
public class CinService {
    @Autowired
    CinRepository cinRepository;

    public List<Cin> getAllByDeviceId(Long id){
       return cinRepository.findAllByDeviceId(id);

    }

}
