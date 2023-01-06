package com.example.jitsidemo;

public class ClassProfile {
    private String meetName;
    private String email;

    public String getmeetName() {
        return meetName;
    }

    public void setmeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    ClassProfile(String meetName, String email){
        this.email=email;
        this.meetName=meetName;
    }
}
