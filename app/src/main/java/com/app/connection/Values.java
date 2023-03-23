package com.app.connection;

public class Values {
String inter,battery_per,battery_pow,lan,logi,times,user_mail,number,image;
Values(){}

    public Values(String inter, String battery_per, String battery_pow, String lan, String logi, String times, String user_mail, String number,String image) {
        this.inter = inter;
        this.battery_per = battery_per;
        this.battery_pow = battery_pow;
        this.lan = lan;
        this.logi = logi;
        this.times = times;
        this.user_mail = user_mail;
        this.number = number;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }

    public String getBattery_per() {
        return battery_per;
    }

    public void setBattery_per(String battery_per) {
        this.battery_per = battery_per;
    }


    public String getBattery_pow() {
        return battery_pow;
    }

    public void setBattery_pow(String battery_pow) {
        this.battery_pow = battery_pow;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getLogi() {
        return logi;
    }

    public void setLogi(String logi) {
        this.logi = logi;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
