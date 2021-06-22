package com.example.attandanceapp;

public class User {
    String singinaddress, singtime, day, date, signoutaddress, signouttime, uid, pushId,name,email;
    String singinltd, singinlng, singout_ltd, signout_lng;

    public User() {
    }

    public User(String singinaddress, String singtime, String day, String date, String signoutaddress, String signouttime, String uid, String pushId, String singinltd, String singinlng, String singout_ltd, String signout_lng,String name,String email) {
        this.singinaddress = singinaddress;
        this.singtime = singtime;
        this.day = day;
        this.date = date;
        this.signoutaddress = signoutaddress;
        this.signouttime = signouttime;
        this.uid = uid;
        this.email=email;
        this.pushId = pushId;
        this.singinltd = singinltd;
        this.singinlng = singinlng;
        this.singout_ltd = singout_ltd;
        this.signout_lng = signout_lng;
        this.name=name;
    }

    public String getSinginaddress() {
        return singinaddress;
    }

    public void setSinginaddress(String singinaddress) {
        this.singinaddress = singinaddress;
    }

    public String getSingtime() {
        return singtime;
    }

    public void setSingtime(String singtime) {
        this.singtime = singtime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSignoutaddress() {
        return signoutaddress;
    }

    public void setSignoutaddress(String signoutaddress) {
        this.signoutaddress = signoutaddress;
    }

    public String getSignouttime() {
        return signouttime;
    }

    public void setSignouttime(String signouttime) {
        this.signouttime = signouttime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getSinginltd() {
        return singinltd;
    }

    public void setSinginltd(String singinltd) {
        this.singinltd = singinltd;
    }

    public String getSinginlng() {
        return singinlng;
    }

    public void setSinginlng(String singinlng) {
        this.singinlng = singinlng;
    }

    public String getSingout_ltd() {
        return singout_ltd;
    }

    public void setSingout_ltd(String singout_ltd) {
        this.singout_ltd = singout_ltd;
    }

    public String getSignout_lng() {
        return signout_lng;
    }

    public void setSignout_lng(String signout_lng) {
        this.signout_lng = signout_lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
