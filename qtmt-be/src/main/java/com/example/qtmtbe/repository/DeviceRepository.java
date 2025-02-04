package com.example.qtmtbe.repository;

import com.example.qtmtbe.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Device findByDeviceCode(String code);
}
