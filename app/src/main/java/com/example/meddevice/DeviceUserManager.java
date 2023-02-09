package com.example.meddevice;

import android.content.res.Resources;
import io.reactivex.rxjava3.core.Observable;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class DeviceUserManager {
    private final Map<String, User> currentUsers = new HashMap<String, User>();
    private int currentDeviceId = -1;
    private final InputStream inputStreamDevice;
    private final InputStream inputStreamPortal;
    private final FileOutputStream outputStream;
    public Observable<String> task;

    public DeviceUserManager(InputStream deviceStream,
                             InputStream portalStream,
                             FileOutputStream outputStream) {
        this.inputStreamDevice = deviceStream;
        this.inputStreamPortal = portalStream;
        this.outputStream = outputStream;
        task = Observable.fromCallable(this::runProcessorInBackground);
    }

   public String runProcessorInBackground()  {
        // Executed in background thread
       this.getDeviceUserListFromFile();
       this.updateFromPortalFromFile();
       this.outputResults();
       return Integer.toString(currentUsers.size());
   }

    private void getDeviceUserListFromFile() {
        try {
            BufferedReader bufferedReaderDevice = new BufferedReader(new InputStreamReader(inputStreamDevice));
            currentUsers.clear();
            /* Read the device user list and create the User objects first */
            String line = bufferedReaderDevice.readLine();
            while (line != null) {
                String[] thisLine = line.split("\t");
                // Each line of data is tab-limited to 3 columns
                if (thisLine.length == 3) {
                    String uuid = thisLine[0];
                    String deviceId = thisLine[1];
                    String statusString = thisLine[2];
                    User user = new User(uuid,
                            Integer.parseUnsignedInt(deviceId),
                            statusString);
                    if (currentDeviceId < 0) {
                        // copy the device Id from the first user. All the users on the
                        // deviceUserList have the same device.
                        currentDeviceId = user.getDeviceId();
                    }
                    // populate dictionary of currentUsers
                    currentUsers.put(uuid, user);
                }
                // read next line
                line = bufferedReaderDevice.readLine();
            }
            bufferedReaderDevice.close();
        } catch (Exception e) {
            System.out.printf("Unable to parse, reason: %s",e.getLocalizedMessage());
        }
    }

    private void updateFromPortalFromFile() {
        try {
            BufferedReader bufferedReaderPortal = new BufferedReader(new InputStreamReader(inputStreamPortal));
            // Read the portalUserList and only update the users that are the current device
            String line = bufferedReaderPortal.readLine();
            while (line != null) {
                String[] thisLine = line.split("\t");
                // Each line of data is tab-limited to 3 columns
                if (thisLine.length == 3) {
                    String portalUuid = thisLine[0];
                    String portalDeviceId = thisLine[1];
                    String statusString = thisLine[2];
                    // If the deviceId is the same as this system then we update the user data
                    int deviceIdUint = Integer.parseUnsignedInt(portalDeviceId);
                    if (deviceIdUint == currentDeviceId) {
                        User user = new User(portalUuid,
                                deviceIdUint,
                                statusString);
                        // check if the portal uuid is present in the current list of users
                        if (currentUsers.containsKey(portalUuid)) {
                            // update the user
                            currentUsers.put(portalUuid, user);
                        } else {
                            // New user
                            currentUsers.put(portalUuid, user);
                        }
                    }
                }
                // read next line
                line = bufferedReaderPortal.readLine();
            }
            bufferedReaderPortal.close();
        } catch (Exception e) {
            System.out.printf("Unable to parse, reason: %s",e.getLocalizedMessage());
        }

    }

    private void outputResults() {
        try {
            // Start producing the output file by looping through the users
            for (User user : currentUsers.values()) {
                outputStream.write(user.getOutPutString().getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            System.out.printf("Unable to parse, reason: %s",e.getLocalizedMessage());
        }
    }
}
