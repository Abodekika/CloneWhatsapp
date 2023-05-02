package com.example.chatapp.ModelClass;

public class User {
    String U_id;
    String U_name;
    String U_email;
    String U_phone;
    String U_status;
    String U_imageUri;
    String U_date;
    String U_gender;
    String U_dateOfBirth;

    public User() {

    }

    public User(String u_id, String u_name, String u_email, String u_phone, String u_status, String u_imageUri, String u_date, String u_gender, String u_dateOfBirth) {
        U_id = u_id;
        U_name = u_name;
        U_email = u_email;
        U_phone = u_phone;
        U_status = u_status;
        U_imageUri = u_imageUri;
        U_date = u_date;
        U_gender = u_gender;
        U_dateOfBirth = u_dateOfBirth;
    }


    public String getU_id() {
        return U_id;
    }

    public void setU_id(String u_id) {
        U_id = u_id;
    }

    public String getU_phone() {
        return U_phone;
    }

    public void setU_phone(String u_phone) {
        U_phone = u_phone;
    }

    public String getU_status() {
        return U_status;
    }

    public void setU_status(String u_status) {
        U_status = u_status;
    }

    public String getU_imageUri() {
        return U_imageUri;
    }

    public void setU_imageUri(String u_imageUri) {
        U_imageUri = u_imageUri;
    }

    public String getU_gender() {
        return U_gender;
    }

    public void setU_gender(String u_gender) {
        U_gender = u_gender;
    }

    public String getU_dateOfBirth() {
        return U_dateOfBirth;
    }

    public void setU_dateOfBirth(String u_dateOfBirth) {
        U_dateOfBirth = u_dateOfBirth;
    }

    public String getU_date() {
        return U_date;
    }

    public void setU_date(String u_date) {
        this.U_date = u_date;
    }


    public String getU_name() {
        return U_name;
    }

    public void setU_name(String u_name) {
        this.U_name = u_name;
    }

    public String getU_email() {
        return U_email;
    }

    public void setU_email(String u_email) {
        this.U_email = u_email;
    }


}
