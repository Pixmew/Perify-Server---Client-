package com.yash.perify.Tasks;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.rtp.AudioStream;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.*;

public class AudioProcesser {
    private MediaRecorder recorder;
    private MediaPlayer player;
    private MediaStore store;
    private Context micContext;
    private boolean streaming = false;

    private Button startrecord , stoprecord , playrecoded;


    public AudioProcesser(Context asset , Button start , Button stop , Button play) {
        micContext = asset;

        startrecord = start;
        stoprecord = stop;
        playrecoded = play;



        startrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setOutputFile( CreateFileSavePath() );
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    recorder.prepare();
                    recorder.start();
                    Log.println(Log.DEBUG, "Yash", "Recording Started ");
                } catch (Exception e) {

                }
            }
        });

        stoprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                Log.println(Log.DEBUG, "Yash", "Recording Stoped");
                recorder = null;
            }
        });

        playrecoded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            player = new MediaPlayer();
                            if(getFileSavePath() == null) {
                                Log.println(Log.DEBUG, "Yash", "Playing : " + getFileSavePath() );
                            }
                            else {
                                Log.println(Log.DEBUG, "Yash", "Playing : " + getFileSavePath() );
                                player.setDataSource(getFileSavePath());
                            }
                            player.prepare();
                            player.start();
                            Log.println(Log.DEBUG, "Yash", "Playing : " + getFileSavePath() );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                t.start();
            }
        });
    }

    public String getFileSavePath(){
        ContextWrapper contextwrapper = new ContextWrapper( micContext );
        File dir = contextwrapper.getExternalFilesDir( Environment.DIRECTORY_DOCUMENTS );
        File AudioSavePath = new File( dir , "audio.mp3" );
        if(AudioSavePath.exists()){
            return AudioSavePath.getAbsolutePath();
        }
       return null;
    }
    public String CreateFileSavePath(){
        File dir = micContext.getExternalFilesDir( Environment.DIRECTORY_DOCUMENTS );
        File AudioSavePath = new File( dir , "audio.mp3" );

        try {
            AudioSavePath.delete();
            AudioSavePath.createNewFile();
            return AudioSavePath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //defaults to android mic (if any)
    public void recordMic() {

    }
}