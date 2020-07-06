package com.bossondesign.christmascountdown;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.jvm.internal.Intrinsics;

import static android.media.MediaPlayer.*;

public class MainActivity extends AppCompatActivity {

    private TextView txtTimerDay;
    private TextView tvEvent;
    private Handler handler;
    private Runnable runnable;
    MediaPlayer mediaPlayer;
    MediaPlayer mp;
    int length;
    private ImageView bellImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTimerDay = findViewById(R.id.txtTimerDay);
        countDownStart();

        new AppRater(this).show();

        //super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mediaPlayer = create(getApplicationContext(), R.raw.song);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);


        //bell rocking animation
        bellImage = findViewById(R.id.imageView);
        final Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View arg0) {
                bellImage.startAnimation(myRotation);
                MediaPlayer mp = create(MainActivity.this,R.raw.bell);
                mp.start();

            }
        });

    }


    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    //set event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse(getString(R.string.date));
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtTimerDay.setText("" + String.format("%02d", days));

                    } else {
                        tvEvent.setVisibility(View.VISIBLE);
                        tvEvent.setText("The event started!");
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        handler.postDelayed(runnable, 1 * 1000);
    }

    public void textViewGone() {
        findViewById(R.id.textView1).setVisibility(View.GONE);
    }

    //prevent reload on orientation change - retain state on shift
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        length = mediaPlayer.getCurrentPosition();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer.start();
        mediaPlayer.seekTo(length);
    }

}