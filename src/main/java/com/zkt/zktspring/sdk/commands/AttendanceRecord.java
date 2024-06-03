package com.zkt.zktspring.sdk.commands;

import com.zkt.zktspring.sdk.Enum.AttendanceStateEnum;
import com.zkt.zktspring.sdk.Enum.AttendanceTypeEnum;

import java.util.Date;

public class AttendanceRecord {
    private int userSN;
    private String userID;
    private AttendanceTypeEnum verifyType;
    private String recordTime;
    private AttendanceStateEnum verifyState;

    public AttendanceRecord(int userSN, String userID, AttendanceTypeEnum verifyType, String recordTime,
            AttendanceStateEnum verifyState) {
        this.userSN = userSN;
        this.userID = userID;
        this.verifyType = verifyType;
        this.recordTime = recordTime;
        this.verifyState = verifyState;
    }

    // Getter and Setter methods for each field

    public int getUserSN() {
        return userSN;
    }

    public void setUserSN(int userSN) {
        this.userSN = userSN;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public AttendanceTypeEnum getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(AttendanceTypeEnum verifyType) {
        this.verifyType = verifyType;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public AttendanceStateEnum getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(AttendanceStateEnum verifyState) {
        this.verifyState = verifyState;
    }

    // @Override
    // public String toString() {
    // return "AttendanceRecord{" +
    // "userSN=" + userSN +
    // ", userID='" + userID + '\'' +
    // ", verifyType=" + verifyType +
    // ", recordTime=" + recordTime +
    // ", verifyState=" + verifyState +
    // '}';
    // }

    public static AttendanceRecord convertMapToAttendanceRecord(java.util.Map<String, Object> attendance_record) {
        int userSN = (int) attendance_record.get("userSN");
        String userID = (String) attendance_record.get("userID");
        AttendanceTypeEnum verifyType = AttendanceTypeEnum.valueOf((String) attendance_record.get("verifyType"));
        String recordTime = (String) attendance_record.get("recordTime");
        AttendanceStateEnum verifyState = AttendanceStateEnum.valueOf((String) attendance_record.get("verifyState"));

        return new AttendanceRecord(userSN, userID, verifyType, recordTime, verifyState);
    }

    public static java.util.Map<String, Object> convertAttendanceRecordToMap(AttendanceRecord attendance_record) {
        java.util.Map<String, Object> attendance_recordData = new java.util.HashMap<>();

        attendance_recordData.put("userSN", attendance_record.getUserSN());
        attendance_recordData.put("userID", attendance_record.getUserID());
        attendance_recordData.put("verifyType", attendance_record.getVerifyType().name());
        attendance_recordData.put("recordTime", attendance_record.getRecordTime());
        attendance_recordData.put("verifyState", attendance_record.getVerifyState().name());

        return attendance_recordData;
    }

}
