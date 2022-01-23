package com.yash.perify.Tasks;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.rtp.AudioStream;
import android.provider.MediaStore;
import android.util.Log;

import java.io.*;

public class AudioProcesser {
    private MediaRecorder recorder;
    private MediaStore store;
    private boolean streaming = false;


    public AudioProcesser(Context asset) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        File f = new File(asset.getFilesDir() + "audio.mp3");

        recorder.setOutputFile( f.getPath() );
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.println(Log.DEBUG, "Yash", "prepare() failed");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                recorder.start();
                synchronized (this) {
                    try {
                        Log.println(Log.DEBUG , "Yash" , "Done");
                        this.wait(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    recorder.stop();
                    recorder.release();
                    Log.println(Log.DEBUG , "Yash" , "Done");
                    MediaPlayer player = new MediaPlayer();
                    try {
                        player.setDataSource( asset.getFilesDir() + "audio.mp3" );

                        player.prepare();
                        player.start();
                            try {
                                Log.println(Log.DEBUG, "Yash", "Done " + player.isPlaying());
                                this.wait(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        player.stop();
                        Log.println(Log.DEBUG, "Yash", "Done");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    //defaults to android mic (if any)
    public void recordMic() {

    }
}