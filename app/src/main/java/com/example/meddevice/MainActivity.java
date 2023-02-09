package com.example.meddevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<String, UserStatus> currentUsers = new HashMap<String, UserStatus>();
    private int deviceId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Resources res = getResources();
            // Read the text file from R.raw resources
            InputStream inputStream = res.openRawResource(R.raw.deviceuserlist);
            currentUsers.clear();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
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
                    if (currentUsers.containsKey(uuid)) {
                        System.out.printf("Duplicate uuid %s%n", uuid);
                    }
                    // populate dictionary
                    currentUsers.put(uuid, user.getUserStatus());
                }
                // read next line
                line = bufferedReader.readLine();

            }
            bufferedReader.close();
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