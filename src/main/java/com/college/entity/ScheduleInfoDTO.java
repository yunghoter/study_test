package com.college.entity;

import java.time.LocalTime;

public class ScheduleInfoDTO {
    private String studentFirstName;
    private String studentLastName;
    private String teacherFirstName;
    private String teacherLastName;
    private String courseName;
    private String departmentName;
    private String roomNumber;
    private int roomCapacity;
    private String semester;
    private int year;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor
    public ScheduleInfoDTO(String studentFirstName, String studentLastName, 
                         String teacherFirstName, String teacherLastName, 
                         String courseName, String departmentName, 
                         String roomNumber, int roomCapacity,
                         String semester, int year, 
                         LocalTime startTime, LocalTime endTime) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.teacherFirstName = teacherFirstName;
        this.teacherLastName = teacherLastName;
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.semester = semester;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getTeacherFirstName() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}