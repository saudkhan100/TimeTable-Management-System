package com.example.tts4;

public class ClassInfo {
    private String courseName;
    private String roomNumber;
    private String block;
    private String teacherName;

    public ClassInfo(String courseName, String roomNumber, String block, String teacherName) {
        this.courseName = courseName;
        this.roomNumber = roomNumber;
        this.block = block;
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getBlock() {
        return block;
    }

    public String getTeacherName() {
        return teacherName;
    }
}

