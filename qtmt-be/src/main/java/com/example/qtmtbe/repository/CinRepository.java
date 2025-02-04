package com.example.qtmtbe.repository;

import com.example.qtmtbe.entity.Cin;
import com.example.qtmtbe.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CinRepository extends JpaRepository<Cin, Long> {

    List<Cin> findAllByDeviceId(Long id);
}
