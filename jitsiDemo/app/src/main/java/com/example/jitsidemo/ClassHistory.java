package com.example.jitsidemo;

import java.util.ArrayList;

public class ClassHistory {
    String dates,time,meetName,email;
    ArrayList<String> timeList;

    public ClassHistory(String dates) {
        this.dates = dates;
    }

    public ClassHistory(String dates, String time, String meetName, String email) {
        this.dates = dates;
        this.time = time;
        this.meetName = meetName;
        this.email = email;
    }

    public ClassHistory(String dates, ArrayList<String> timeList) {
        this.dates = dates;
        this.timeList = timeList;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
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

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.timeList = timeList;
    }
}
