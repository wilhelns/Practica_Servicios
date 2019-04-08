package com.example.emisoraradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnReproducir;
    MediaPlayer mediaPlayer;
    String streaming="http://streaming.radionomy.com/Radio-Retro-Rock--Pop";
    boolean prepared = false;
    boolean started = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReproducir=(Button)findViewById(R.id.btn_Reproductor);
        btnReproducir.setEnabled(false);
        btnReproducir.setText("Cargando");



        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(streaming);


        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started)
                {
                    started = false;
                    mediaPlayer.pause();
                    btnReproducir.setText("PLAY");
                }
                else
                {
                    started = true;
                    mediaPlayer.start();
                    btnReproducir.setText("PAUSE");
                }
            }
        });

    }

    class PlayerTask extends AsyncTask<String,Void,Boolean>{
            @Override
            protected Boolean doInBackground(String ... string){
                try{
                    mediaPlayer.setDataSource(string[0]);
                    mediaPlayer.prepare();
                    prepared = true;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                return prepared;
            }

            @Override
        protected void onPostExecute(Boolean aBoolean){
                super.onPostExecute(aBoolean);
                btnReproducir.setEnabled(true);
                btnReproducir.setText("PLAY");

            }

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(started){
            mediaPlayer.release();
        }
    }

}
