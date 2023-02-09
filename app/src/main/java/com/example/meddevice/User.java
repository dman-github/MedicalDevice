package com.example.meddevice;

public class User {

    private String userId;
    private int deviceId;
    private UserStatus userStatus;

    public User(String userId, int deviceId, UserStatus userStatus) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.userStatus = userStatus;
    }

    public User(String userId, int deviceId, String userStatusBytes) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.userStatus = new UserStatus(userStatusBytes);
    }


    public String getOutPutString() {
        String tab = "\t";
        String carriageReturn = "\n";
        return getUserId() + tab +
                getDeviceId() + tab +
                getUserStatus().getAuthorisationStatus() + tab +
                getUserStatus().getTrainingStatus()+ carriageReturn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}

