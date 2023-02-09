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
    DeviceUserManager deviceUserManager = new DeviceUserManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Resources res = getResources();
            // Read the text files from R.raw resources
            InputStream inputStreamDevice = res.openRawResource(R.raw.deviceuserlist);
            InputStream inputStreamPortal = res.openRawResource(R.raw.portaluserlist);
            String outPutFileName = "DeviceUserListUpdated.txt";
            FileOutputStream outputStream = openFileOutput(outPutFileName, Context.MODE_PRIVATE);
            deviceUserManager.getDeviceUserListFromFile(inputStreamDevice);
            deviceUserManager.updateFromPortalFromFile(inputStreamPortal);
            deviceUserManager.outputResults(outputStream);
            System.out.println("********Processing DONE*********");
        } catch (Exception e) {
            System.out.printf("Unable to parse, reason: %s",e.getLocalizedMessage());
        }
    }
}