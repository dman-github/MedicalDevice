package com.example.meddevice;

public class UserStatus {
    private Boolean userIsAuthorised;
    private Boolean userIsTrainedOnDevice;
    private Boolean userIsAdmin;

    public UserStatus(Boolean userIsAuthorised, Boolean userIsTrainedOnDevice, Boolean userIsAdmin) {
        this.userIsAuthorised = userIsAuthorised;
        this.userIsTrainedOnDevice = userIsTrainedOnDevice;
        this.userIsAdmin = userIsAdmin;
    }
    public UserStatus(String userStatusBytes) {
        int data = Integer.decode(userStatusBytes);
        this.userIsAuthorised = ((data & 0x80) != 0);
        this.userIsTrainedOnDevice = ((data & 0x40) != 0);
        // user is administrator when 0
        this.userIsAdmin = ((data & 0x20) == 0);
        /*System.out.printf("userIsAuthorised:%b, userIsTrainedOnDevice:%b, userIsAdmin:%b%n",
                userIsAuthorised,
                userIsTrainedOnDevice,
                userIsAdmin);*/
    }

    private int convertToInt(String userStatusBytes) {
        return Integer.decode(userStatusBytes);
    }

    public Boolean getUserIsAuthorised() {
        return userIsAuthorised;
    }

    public void setUserIsAuthorised(Boolean userIsAuthorised) {
        this.userIsAuthorised = userIsAuthorised;
    }

    public Boolean getUserIsTrainedOnDevice() {
        return userIsTrainedOnDevice;
    }

    public void setUserIsTrainedOnDevice(Boolean userIsTrainedOnDevice) {
        this.userIsTrainedOnDevice = userIsTrainedOnDevice;
    }

    public Boolean getUserIsAdmin() {
        return userIsAdmin;
    }

    public void setUserIsAdmin(Boolean userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }
}
