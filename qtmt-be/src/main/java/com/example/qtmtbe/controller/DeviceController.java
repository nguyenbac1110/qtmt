package com.example.qtmtbe.controller;

import com.example.qtmtbe.entity.Device;
import com.example.qtmtbe.model.DeviceIot;
import com.example.qtmtbe.service.CinService;
import com.example.qtmtbe.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    CinService cinService;

    @PostMapping("/create-device")
    public ResponseEntity createDevice(@RequestBody DeviceIot deviceIot) {
        return new ResponseEntity<>(deviceService.createDevice(deviceIot.getDeviceName()), HttpStatus.OK);
    }

    @GetMapping("/get-all-device")
    public ResponseEntity getAllDevice(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                       @RequestParam(name = "sort", required = false, defaultValue = "id,desc") String sortBy) {
        String[] part = sortBy.split(",", 2);
        Sort sortable = Sort.by(Sort.Direction.valueOf(part[1].toUpperCase()), part[0]);
        Pageable pageable = PageRequest.of(page, size, sortable);
        Page<Device> devices = deviceService.getAllDevice(pageable);
        System.out.println(devices.get());
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @GetMapping("/get-all-cin-by-device/{deviceId}")
    public ResponseEntity getAllCinByDeviceId(@PathVariable Long deviceId) {
        return new ResponseEntity<>(cinService.getAllByDeviceId(deviceId), HttpStatus.OK);

    }
}
