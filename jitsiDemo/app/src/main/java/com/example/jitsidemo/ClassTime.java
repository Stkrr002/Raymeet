package com.example.jitsidemo;

public class ClassTime {
    private String time,meetName,email;

    public ClassTime(String time, String meetName) {
        this.time = time;
        this.meetName = meetName;
    }

    public ClassTime(String time, String meetName, String email) {
        this.time = time;
        this.meetName = meetName;
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
