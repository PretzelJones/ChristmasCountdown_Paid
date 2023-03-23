package com.bossondesign.christmascountdown;

import static android.media.MediaPlayer.create;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    int length;
    private ImageView bellImage;

    private TextView countdownTextView;

    private TextView daystillTextView;

    private CountDownTimer countDownTimer;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AppRater(this).show();

        countdownTextView = findViewById(R.id.txtTimerDay);
        daystillTextView = findViewById(R.id.dayTillText);

        // Get current date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Set next Christmas date
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 26);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date nextChristmasDate = calendar.getTime();

        // Check if it's already Christmas
        if (currentDate.after(nextChristmasDate)) {
            // Set next Christmas date to next year
            calendar.add(Calendar.YEAR, 1);
            nextChristmasDate = calendar.getTime();
        }

        // Calculate remaining time until next Christmas
        long diffInMillis = nextChristmasDate.getTime() - currentDate.getTime();

        startCountdownTimer(diffInMillis);
    }

    private void startCountdownTimer(long millisUntilFinished) {
        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                //String remainingTime = String.format(Locale.getDefault(), "%02d days %02d:%02d:%02d",
                //        days, hours, minutes, seconds);

                String remainingTime = String.format(Locale.getDefault(), "%2d",
                        days);

                countdownTextView.setText(remainingTime);

                // Check if it's Christmas Eve
                if (days == 1) {
                    daystillTextView.setText(R.string.christmasEve);
                    findViewById(R.id.txtTimerDay).setVisibility(View.INVISIBLE);
                }

                if (days == 0) {
                    daystillTextView.setText(R.string.christmas);
                    findViewById(R.id.txtTimerDay).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFinish() {
                // Start countdown for next Christmas
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, 1);
                calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                calendar.set(Calendar.DAY_OF_MONTH, 26);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Date nextChristmasDate = calendar.getTime();
                long diffInMillis = nextChristmasDate.getTime() - System.currentTimeMillis();

                startCountdownTimer(diffInMillis);
            }
        };

        countDownTimer.start();

        //privacy policy text link
        textView = findViewById(R.id.textPrivacy);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        //super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mediaPlayer = create(getApplicationContext(), R.raw.song);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        //bell rocking animation
        bellImage = findViewById(R.id.imageView);
        final Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
        findViewById(R.id.imageView).setOnClickListener((View arg0) -> {
            bellImage.startAnimation(myRotation);
            MediaPlayer mp = create(MainActivity.this,R.raw.bell);
            mp.start();
        });
    }
    //prevent reload on orientation change - retain state on shift
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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