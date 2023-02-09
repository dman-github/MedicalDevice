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
    }

    public String getAuthorisationStatus() {
        // 0,0
        if (!this.userIsAuthorised && !this.userIsAdmin) {
            return "DisabledOperator";
        }
        // 0,1
        if (!this.userIsAuthorised) {
            return "DisabledAdmin";
        }
        // 1,0
        if (!this.userIsAdmin) {
            return "AuthorisedOperator";
        }
        return "AuthorisedAdmin";
    }

    public boolean shouldUseDoubleTab()  {
        // Double tab is needed to make sure the last column rows values are all aligned
        // This depends on the text length, below are the 2 smallest strings, Disabled Admin and
        // Authorised admin
        if (!this.userIsAuthorised && this.userIsAdmin) {
            // Disabled Admin
            return true;
        }
        if (this.userIsAuthorised && this.userIsAdmin) {
            //Authorised Admin
            return true;
        }
        return false;
    }

    public String getTrainingStatus() {
        if (userIsTrainedOnDevice) {
            return "Trained";
        }
        return "Untrained";

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
