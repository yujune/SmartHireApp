package com.example.hire;

import com.google.firebase.database.Exclude;

public class Employee {

    private String mName;
    private String mPosition;
    private String mImageUrl;
    private String key;
    private String mAddress;
    private String mPhoneNumber;
    private String mEmail;

    public Employee(){
    }

    public Employee(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public Employee(String mName, String mPosition, String mImageUrl) {
        this.mName = mName;
        this.mPosition = mPosition;
        this.mImageUrl = mImageUrl;
    }

    public Employee(String mName, String mImageUrl, String mAddress, String mPhoneNumber, String mEmail) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mAddress = mAddress;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    //exclude from firebase database
    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}