package com.example.jitsidemo;

public class ClassProfile {
    private String meetName;
    private String time;

    public String getmeetName() {
        return meetName;
    }

    public void setmeetName(String meetName) {
        this.meetName = meetName;
    }

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }

    ClassProfile(String meetName, String time){
        this.time=time;
        this.meetName=meetName;
    }
}
