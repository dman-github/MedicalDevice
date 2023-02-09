package com.example.meddevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<String, UserStatus> currentUsers = new HashMap<String, UserStatus>();
    private int currentDeviceId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Resources res = getResources();
            // Read the text files from R.raw resources
            InputStream inputStreamDevice = res.openRawResource(R.raw.deviceuserlist);
            InputStream inputStreamPortal = res.openRawResource(R.raw.portaluserlist);
            BufferedReader bufferedReaderDevice = new BufferedReader(new InputStreamReader(inputStreamDevice));
            BufferedReader bufferedReaderPortal = new BufferedReader(new InputStreamReader(inputStreamPortal));
            String outPutFileName = "OutputFile.txt";
            FileOutputStream outputStream = openFileOutput(outPutFileName, Context.MODE_PRIVATE);

            currentUsers.clear();

            /* Read the device user list and create the User objects first */
            String line = bufferedReaderDevice.readLine();
            while (line != null) {
                String[] thisLine = line.split("\t");
                // Each line of data is tab-limited to 3 columns
                if (thisLine.length == 3)
                {
                    String uuid = thisLine[0];
                    String deviceId = thisLine[1];
                    String statusString = thisLine[2];
                    System.out.println(uuid);
                    User user = new User(uuid,
                            Integer.parseUnsignedInt(deviceId),
                            statusString);
                    if (currentDeviceId < 0) {
                        // copy the device Id from the first user. All the users on the
                        // deviceUserList have the same device.
                        currentDeviceId = user.getDeviceId();
                    }
                    if (currentUsers.containsKey(uuid)) {
                        String textToWrite = String.format("Duplicate uuid %s%n", uuid);
                        System.out.printf(textToWrite);
                        outputStream.write(textToWrite.getBytes());
                    }
                    // populate dictionary of currentUsers
                    currentUsers.put(uuid, user.getUserStatus());
                }
                // read next line
                line = bufferedReaderDevice.readLine();
            }
            bufferedReaderDevice.close();

            // Read the portalUserList and only update the users that
            line = bufferedReaderPortal.readLine();
            while (line != null) {
                String[] thisLine = line.split("\t");
                // Each line of data is tab-limited to 3 columns
                if (thisLine.length == 3)
                {
                    String portalUuid = thisLine[0];
                    String portalDeviceId = thisLine[1];
                    String statusString = thisLine[2];
                    // If the deviceId is the same as this system then we update the user data
                    int deviceIdUnsignedint = Integer.parseUnsignedInt(portalDeviceId);
                    if (deviceIdUnsignedint == currentDeviceId) {
                        User user = new User(portalUuid,
                                deviceIdUnsignedint,
                                statusString);
                        // check if the portal uuid is present in the current list of users
                        if (currentUsers.containsKey(portalUuid)) {
                            // update the user
                            System.out.printf(">>>>>>Update portal user %s   %s%n", portalUuid,statusString);
                            currentUsers.put(portalUuid, user.getUserStatus());
                        } else {
                            // New user
                            System.out.printf("Portal has new user %s%n", portalUuid);
                        }
                    }
                }
                // read next line
                line = bufferedReaderPortal.readLine();
            }
            bufferedReaderPortal.close();

            outputStream.close();
            System.out.printf("Dictionary size %d%n", currentUsers.size());
            UserStatus s = currentUsers.get("7be519b2-96f6-4b4c-b47b-39c555186ffc");
            System.out.printf("         userIsAuthorised:%b, userIsTrainedOnDevice:%b, userIsAdmin:%b%n",
                    s.getUserIsAuthorised(),
                    s.getUserIsTrainedOnDevice(),
                    s.getUserIsAdmin());
        } catch (Exception e) {
            System.out.printf("Unable to parse, reason: %s",e.getMessage());
        }
    }
}