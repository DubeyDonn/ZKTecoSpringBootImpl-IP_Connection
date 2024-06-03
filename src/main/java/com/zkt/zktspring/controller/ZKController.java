package com.zkt.zktspring.controller;

import com.zkt.zktspring.model.Response;
import com.zkt.zktspring.service.ZKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({ "/api" })
@CrossOrigin
public class ZKController {

    @Autowired
    private ZKService zkService;

    @GetMapping({ "/connect" })
    public ResponseEntity<Response<String>> connect() {
        System.out.println("Connecting to ZK terminal");
        try {
            this.zkService.connect();
            Response<String> response = Response.<String>builder().success(true).message("Connected to ZK terminal")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error connecting to ZK terminal");
            Response<String> response = Response.<String>builder().success(false).message(e.getMessage()).build();
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping({ "/createBackup" })
    public ResponseEntity<Response<String>> open() {
        System.out.println("Creating backup");
        try {
            this.zkService.createBackup();
            Response<String> response = Response.<String>builder().success(true).message("Backup created successfully")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error creating backup");
            Response<String> response = Response.<String>builder().success(false).message(e.getMessage()).build();
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping({ "/disconnect" })
    public ResponseEntity<Response<String>> close() {
        System.out.println("Disconnecting from ZK terminal");
        try {
            this.zkService.disconnect();
            Response<String> response = Response.<String>builder().success(true)
                    .message("Disconnected from ZK terminal")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error disconnecting from ZK terminal");
            Response<String> response = Response.<String>builder().success(false).message(e.getMessage()).build();
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/sync")
    public ResponseEntity<Response<String>> sync() {
        System.out.println("Syncing attendance records");
        try {
            this.zkService.sync();
            Response<String> response = Response.<String>builder().success(true).message("Attendance records synced")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error syncing attendance records");
            Response<String> response = Response.<String>builder().success(false).message(e.getMessage()).build();
            return ResponseEntity.ok(response);
        }
    }

}
