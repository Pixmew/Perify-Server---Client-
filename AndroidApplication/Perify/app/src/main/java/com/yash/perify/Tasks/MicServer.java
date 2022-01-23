package com.yash.perify.Tasks;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.spec.ECField;

public class MicServer {
    public static Context applicationContext;
    public static  String wifiServiceType;

    private ServerSocket micServerSocket;
    private static AudioProcesser audio;

    public static String SERVER_IP = "";
    public static int SERVER_PORT = 8080;


    public MicServer( TextView IPText ) {
        try {
            SERVER_IP = getLocalIpAddress();
            SERVER_PORT = 8080;
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    public static void setContext( Context context  , String service ){
        applicationContext = context;
        wifiServiceType = service;
    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService( wifiServiceType );;
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }

    public void UpdateServerInfo( TextView textfield ) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    textfield.setText(getLocalIpAddress() + ":8080");
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        });
        try {
            t.join();
        }catch( Exception e ) {
        }
        t.start();
    }

    public void RestartServer( TextView view )
    {
        audio = new AudioProcesser( applicationContext );
        //audio.recordMic();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.println(Log.DEBUG, "Yash", getLocalIpAddress());
                    UpdateServerInfo( view );
                    InetSocketAddress addr = new InetSocketAddress(SERVER_IP, SERVER_PORT);
                    micServerSocket = new ServerSocket(addr.getPort() , 100 , addr.getAddress());
                    //micServerSocket.bind(addr);
                    Socket client = micServerSocket.accept();
                    Log.println(Log.DEBUG, "Yash", "Connected");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

