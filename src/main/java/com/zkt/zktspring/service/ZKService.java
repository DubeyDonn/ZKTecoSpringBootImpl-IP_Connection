package com.zkt.zktspring.service;

import com.zkt.zktspring.repository.AttendanceRecordRepository;
import com.zkt.zktspring.repository.UserInfoRepository;
import com.zkt.zktspring.sdk.Enum.CommandReplyCodeEnum;
import com.zkt.zktspring.sdk.commands.AttendanceRecord;
import com.zkt.zktspring.sdk.commands.UserInfo;
import com.zkt.zktspring.sdk.commands.ZKCommandReply;
import com.zkt.zktspring.sdk.terminal.ZKTerminal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ZKService {

    private final ZKTerminal terminal;

    private final UserInfoRepository userInfoRepository;

    private final AttendanceRecordRepository attendanceRecordRepository;

    // change the COM key to the key of the ZK terminal for authentication
    // add the IP address, port number of the ZK terminal here
    private final int comKey = 100;
    private final String ipAddress = "192.168.0.28";
    private final int port = 4370;

    public ZKService() {
        this.terminal = new ZKTerminal(ipAddress, port);
        this.userInfoRepository = new UserInfoRepository();
        this.attendanceRecordRepository = new AttendanceRecordRepository();
    }

    public ZKCommandReply connect() throws Exception {

        ZKCommandReply reply = terminal.connect();
        if (terminal.connectAuth(comKey).getCode() == CommandReplyCodeEnum.CMD_ACK_OK) {
            return reply;
        } else {
            throw new Exception("Connection failed");
        }
    }

    public void createBackup() {
        terminal.createBackup();
    }

    public void disconnect() throws Exception {
        terminal.disconnect();
    }

    public List<AttendanceRecord> getAttendanceRecords() throws Exception {
        System.out.println("Getting attendance records");
        terminal.disableDevice();
        return terminal.getAttendanceRecords();
    }

    public List<UserInfo> getUsers() throws Exception {
        return terminal.getAllUsers();
    }

    public void sync() throws Exception {
        this.connect();
        this.attendanceRecordRepository.insertMultipleAttendanceRecords(getAttendanceRecords());
        this.disconnect();
        this.connect();
        this.userInfoRepository.insertMultipleUsers(getUsers());
        this.disconnect();
    }
}
