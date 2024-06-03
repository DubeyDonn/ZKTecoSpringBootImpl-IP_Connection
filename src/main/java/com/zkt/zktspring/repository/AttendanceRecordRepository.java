package com.zkt.zktspring.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zkt.zktspring.core.TableManager;
import com.zkt.zktspring.sdk.commands.AttendanceRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class AttendanceRecordRepository {

    private TableManager tableManager;

    public AttendanceRecordRepository() {
        this.tableManager = new TableManager("attendance_record");
    }

    public List<AttendanceRecord> getAllAttendanceRecords() {

        List<Map<String, Object>> attendance_records = tableManager.getAll();
        List<AttendanceRecord> attendance_recordList = new ArrayList<>();

        for (Map<String, Object> attendance_record : attendance_records) {
            AttendanceRecord newAttendanceRecord = AttendanceRecord.convertMapToAttendanceRecord(attendance_record);

            attendance_recordList.add(newAttendanceRecord);
        }

        return attendance_recordList;

    }

    public AttendanceRecord getAttendanceRecordById(Long id) {

        Map<String, Object> attendance_recordRow = tableManager.getById(id);

        if (attendance_recordRow == null) {
            return null;
        }

        AttendanceRecord attendance_record = AttendanceRecord.convertMapToAttendanceRecord(attendance_recordRow);
        return attendance_record;

    }

    public int insertAttendanceRecord(AttendanceRecord attendance_record) {
        Map<String, Object> attendance_recordData = AttendanceRecord.convertAttendanceRecordToMap(attendance_record);

        return tableManager.insert(attendance_recordData);
    }

    public int insertMultipleAttendanceRecords(List<AttendanceRecord> attendance_records) {
        List<Map<String, Object>> attendance_recordData = new ArrayList<>();

        Map<String, Object> filter = new HashMap<>();
        filter.put("order_by", "recordTime DESC");
        filter.put("limit", 1);

        List<Map<String, Object>> latestAttendanceRecord = tableManager.getByFilter(filter);

        if (latestAttendanceRecord.size() == 0) {
            for (AttendanceRecord attendance_record : attendance_records) {

                Map<String, Object> attendance_recordMap = AttendanceRecord
                        .convertAttendanceRecordToMap(attendance_record);

                attendance_recordData.add(attendance_recordMap);
            }
        } else {

            for (AttendanceRecord attendance_record : attendance_records) {

                Map<String, Object> attendance_recordMap = AttendanceRecord
                        .convertAttendanceRecordToMap(attendance_record);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime newRecordDateTime = LocalDateTime.parse(attendance_record.getRecordTime(), formatter);

                if (newRecordDateTime.compareTo((LocalDateTime) latestAttendanceRecord.get(0).get("recordTime")) <= 0) {
                    continue;
                }

                attendance_recordData.add(attendance_recordMap);
            }
        }

        return tableManager.insertMultiple(attendance_recordData);

    }

    public int updateAttendanceRecord(AttendanceRecord attendance_record, Long id) {
        Map<String, Object> attendance_recordData = AttendanceRecord.convertAttendanceRecordToMap(attendance_record);

        return tableManager.update(attendance_recordData, id);
    }

    public int deleteAttendanceRecord(Long id) {
        return tableManager.delete(id);
    }
}
