package com.example.dell.jaapactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell.jaapactivity.Meditation.MeditationData;
import com.example.dell.jaapactivity.Meditation.MeditationDataBaseHandler;
import com.example.dell.jaapactivity.ReportManager.ReportData;
import com.example.dell.jaapactivity.ReportManager.ReportDataBaseHandler;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeditationActivity extends AppCompatActivity {

    Button playButton;
    Button pauseButton;
    Button stopButton;
    TextView soundTrackName;
    int playButtonPressCount = 0;
    int buttonCountOnDestroy = 0;
    int pausePosition = 0;
    int id = 0;
    String soundTrack;
    boolean pauseIsPressed = false;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private static final String TAG = "MeditationActivity";
    SharedPreferences medSharedPreferences;
    SharedPreferences.Editor medEditor;
    public static final String BUTTON_COUNT_PREFERENCE = "CountPref" ;
    public static final String PLAY_BUTTON_CLICKS = "0";
    //Mediatation database
    MeditationDataBaseHandler mDb= new MeditationDataBaseHandler(this);

    ImageView ImageViewName;
    WaveVisualizer mVisulaizer ;
    //Report data base
    URL url ;
    ReportDataBaseHandler rDb = new ReportDataBaseHandler(this);
   private  ProgressDialog pd ;


    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        int id = item.getItemId();
        item.setCheckable(true);  // to select the tapped item in bottom nav

        if (id == R.id.all_reports) {
            // Handle the camera action
            Intent reportsIntet = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(reportsIntet);
        }

        if(id == R.id.chat){
            Intent intent = new Intent(this,ChatActivity.class);
            startActivity(intent);
        }


        if(id == R.id.nav_share){

        }

        if(id == R.id.nav_send){

            Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
            startActivity(intent);

        }


        return true;
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);



        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setCheckable(false);  // to uncheck first item for preselecting




        progressDialog = new ProgressDialog(MeditationActivity.this);
      //  progressDialog.setMessage("Loading");
      pd = new ProgressDialog(this);
      pd.setCancelable(false);
      pd.setMessage("Loading Audio");




     //   mediaPlayer = MediaPlayer.create(this,R.raw.day_one);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        soundTrackName = findViewById(R.id.soundTrackName);
        medSharedPreferences = getSharedPreferences(BUTTON_COUNT_PREFERENCE, Context.MODE_PRIVATE);
        medSharedPreferences = getSharedPreferences(BUTTON_COUNT_PREFERENCE, Context.MODE_PRIVATE);
        buttonCountOnDestroy = medSharedPreferences.getInt(PLAY_BUTTON_CLICKS,0);
        ImageViewName = findViewById(R.id.cardImage);
        mVisulaizer = findViewById(R.id.blast);
        if(buttonCountOnDestroy==7){
            buttonCountOnDestroy=0;
        }

        playButtonPressCount=buttonCountOnDestroy;
        Log.d(TAG, "onCreate: button Clicks : "+buttonCountOnDestroy);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MMM");
        final String formattedDate = df.format(currentTime);

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd");
        final String formattedTime = timeFormat.format(currentTime);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        final String formattedDay = dayFormat.format(currentTime);

        Calendar calender = Calendar.getInstance();
        final int year  = calender.get(Calendar.YEAR);

        //adding dummy data


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                id++;
                playButton.setEnabled(false);
                playButtonPressCount++;
                medSharedPreferences = getSharedPreferences(BUTTON_COUNT_PREFERENCE, Context.MODE_PRIVATE);
                medEditor = medSharedPreferences.edit();
                medEditor.putInt(PLAY_BUTTON_CLICKS,playButtonPressCount);
                medEditor.apply();
                Log.d(TAG,"onClick: "+playButtonPressCount);
                if(playButtonPressCount==1){
                    pd.show();

                    final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer = new MediaPlayer();
                                    pd.show();

                                    try {
                                        mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        mediaPlayer.prepare();
                                        Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            pd.dismiss();
                                        }
                                    });
                                    Log.d(TAG, "run: "+ mediaPlayer);
                                    if(mediaPlayer !=null){
                                        mediaPlayer.start();
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        Log.d(TAG, "run: media player not null ");
                                        if(pauseIsPressed){
                                            mediaPlayer.seekTo(pausePosition+100);
                                        }
                                        mediaPlayer.setLooping(true);
                                        soundTrackName.setText(R.string.song_one);
                                        ImageViewName.setImageResource(R.drawable.bgmed);
                                        YoYo.with(Techniques.Swing).duration(2000).playOn(ImageViewName);
                                        YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);
                                        int audioSessionId  = mediaPlayer.getAudioSessionId();
                                        if(audioSessionId != -1){
                                            mVisulaizer.setVisibility(View.VISIBLE);

                                            mVisulaizer.setAudioSessionId(audioSessionId);
                                        }

                                    }


                                    else{
                                        Log.d(TAG, "run: media Player not prepared ");
                                    }

                                }
                            });
                        }
                    });
                    thread.start();



                    //meditation database
                    if(mediaPlayer!=null){
                        long inserted=  mDb.addMeditationData(new MeditationData("No 1 Atam Bodh Dhyaan",mediaPlayer.getDuration()));
                        Log.d(TAG, "onClick: inserted "+ inserted);
                        List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                        for (MeditationData mp : meditationDataList) {
                            String log = "Id: " + mp.getId() + " ,Audio Nsme : " + mp.getAudioName() + " ,Duration Time: " +
                                    mp.getDuration();
                            // Writing Contacts to log
                            Log.d("Name: ", log);



                        }
                        long reportinserted = rDb.addUserReportData(new ReportData("Meditation",formattedDate,formattedTime,formattedDay,
                                "Atam Bodh Dhyaan "
                                ,Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000))
                                ,Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000))
                                ,String.valueOf(year)));
                        Log.d(TAG, "onClick: report inserted : "+reportinserted);
                        List<ReportData> reportDataList = rDb.getAllUserReportData();
                        Log.d(TAG, "onClick: "+reportDataList);
                        for (ReportData rp : reportDataList) {
                            Log.d(TAG, "onClick: For loop");
                            String reportLog = "Id: "+rp.getId() //0
                                    + ", Mode: "+ rp.getMode()   //1
                                    + ", User Time: "+ rp.getUserTime() //2
                                    + ", Actual Time: "+ rp.getActualTime() //3
                                    + ", Date : "+rp.getDate() //4
                                    + ", Time : "+rp.getTime()  //5
                                    + ", Day: "+rp.getDay()  //6
                                    + ", Type: "+rp.getType() //7
                                    + ", Audio Name : "+rp.getAudioName(); //8
                            Log.d("Report: ",reportLog);
                        }

                    }

                }

                else if(playButtonPressCount==2){
                    pd.show();

                    final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3";

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer = new MediaPlayer();
                                    pd.show();

                                    try {
                                        mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        mediaPlayer.prepare();
                                        Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            pd.dismiss();
                                        }
                                    });
                                    Log.d(TAG, "run: "+ mediaPlayer);
                                    if(mediaPlayer !=null){
                                        mediaPlayer.start();
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        Log.d(TAG, "run: media player not null ");
                                        if(pauseIsPressed){
                                            mediaPlayer.seekTo(pausePosition+100);
                                        }
                                        mediaPlayer.setLooping(true);
                                        soundTrackName.setText(R.string.song_two);
                                        ImageViewName.setImageResource(R.drawable.bgmed2);
                                        YoYo.with(Techniques.RotateInUpLeft).duration(2000).playOn(ImageViewName);
                                        YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);
                                        int audioSessionId  = mediaPlayer.getAudioSessionId();
                                        if(audioSessionId != -1){
                                            mVisulaizer.setVisibility(View.VISIBLE);

                                            mVisulaizer.setAudioSessionId(audioSessionId);
                                        }
                                    }


                                    else{
                                        Log.d(TAG, "run: media Player not prepared ");
                                    }

                                }
                            });
                        }
                    });
                    thread.start();

                 if(mediaPlayer!=null){
                     Log.d(TAG, "onClick: Position " + mediaPlayer.getCurrentPosition());
                     long inserted =     mDb.addMeditationData(new MeditationData("No 2 Panchkosh Dhyaan",mediaPlayer.getDuration()));
                     Log.d(TAG, "onClick: inserted : "+ inserted);
                     List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                     for (MeditationData mp : meditationDataList) {
                         String log = "Id: " + mp.getId() + " ,Audiio Nsme : " + mp.getAudioName() + " ,Duration Time: " +
                                 mp.getDuration();
                         // Writing Contacts to log
                         Log.d("Name: ", log);


                     }
                     long reportinserted = rDb.addUserReportData(new ReportData("Meditation",formattedDate,
                             formattedTime,formattedDay,"Panchkosh Dhyaan ",Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),String.valueOf(year)));

                     Log.d(TAG, "onClick: report inserted : "+reportinserted);
                     List<ReportData> reportDataList = rDb.getAllUserReportData();
                     Log.d(TAG, "onClick: "+reportDataList);
                     for (ReportData rp : reportDataList) {
                         Log.d(TAG, "onClick: For loop");
                         String reportLog = "Id: "+rp.getId() //0
                                 + ", Mode: "+ rp.getMode()   //1
                                 + ", User Time: "+ rp.getUserTime() //2
                                 + ", Actual Time: "+ rp.getActualTime() //3
                                 + ", Date : "+rp.getDate() //4
                                 + ", Time : "+rp.getTime()  //5
                                 + ", Day: "+rp.getDay()  //6
                                 + ", Type: "+rp.getType() //7
                                 + ", Audio Name : "+rp.getAudioName(); //8
                         Log.d("Report: ",reportLog);
                     }


                 }


                }
                else if(playButtonPressCount==3){
                    pd.show();

                    final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3";

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer = new MediaPlayer();
                                    pd.show();

                                    try {
                                        mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        mediaPlayer.prepare();
                                        Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "run: "+ mediaPlayer);
                                    if(mediaPlayer !=null){
                                        mediaPlayer.start();
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        Log.d(TAG, "run: media player not null ");
                                        if(pauseIsPressed){
                                            mediaPlayer.seekTo(pausePosition+100);
                                        }

                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                pd.dismiss();
                                            }
                                        });
                                        mediaPlayer.setLooping(true);
                                        soundTrackName.setText(R.string.song_three);
                                        ImageViewName.setImageResource(R.drawable.bgmed11);
                                        YoYo.with(Techniques.SlideInLeft).duration(2000).playOn(ImageViewName);
                                        YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);
                                        int audioSessionId  = mediaPlayer.getAudioSessionId();
                                        if(audioSessionId != -1){
                                            mVisulaizer.setVisibility(View.VISIBLE);

                                            mVisulaizer.setAudioSessionId(audioSessionId);
                                        }
                                    }


                                    else{
                                        Log.d(TAG, "run: media Player not prepared ");
                                    }

                                }
                            });
                        }
                    });
                    thread.start();

                    if(mediaPlayer != null){
                        Log.d(TAG, "onClick: Position " + mediaPlayer.getCurrentPosition());
                        long inserted =   mDb.addMeditationData(new MeditationData("No 3 Sharir Dhyaan",mediaPlayer.getDuration()));
                        Log.d(TAG, "onClick: inserted "+ inserted);
                        List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                        for (MeditationData mp : meditationDataList) {
                            String log = "Id: " + mp.getId() + " ,Audiio Nsme : " + mp.getAudioName() + " ,Duration Time: " +
                                    mp.getDuration();
                            // Writing Contacts to log
                            Log.d("Name: ", log);

                        }
                        long reportinserted = rDb.addUserReportData(new ReportData("Meditation",formattedDate,formattedTime,
                                formattedDay,"Sharir Dhyaan ",
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                                String.valueOf(year)));
                        Log.d(TAG, "onClick: report inserted : "+reportinserted);
                        List<ReportData> reportDataList = rDb.getAllUserReportData();
                        Log.d(TAG, "onClick: "+reportDataList);
                        for (ReportData rp : reportDataList) {
                            Log.d(TAG, "onClick: For loop");
                            String reportLog = "Id: "+rp.getId() //0
                                    + ", Mode: "+ rp.getMode()   //1
                                    + ", User Time: "+ rp.getUserTime() //2
                                    + ", Actual Time: "+ rp.getActualTime() //3
                                    + ", Date : "+rp.getDate() //4
                                    + ", Time : "+rp.getTime()  //5
                                    + ", Day: "+rp.getDay()  //6
                                    + ", Type: "+rp.getType() //7
                                    + ", Audio Name : "+rp.getAudioName(); //8
                            Log.d("Report: ",reportLog);
                        }
                    }



                }
                else if(playButtonPressCount==4){
                    pd.show();

                    pd.show();

                    final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3";

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer = new MediaPlayer();
                                    pd.show();

                                    try {
                                        mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        mediaPlayer.prepare();
                                        Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "run: "+ mediaPlayer);
                                    if(mediaPlayer !=null){
                                        mediaPlayer.start();
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        Log.d(TAG, "run: media player not null ");
                                        if(pauseIsPressed){
                                            mediaPlayer.seekTo(pausePosition+100);
                                        }
                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                pd.dismiss();
                                            }
                                        });
                                        mediaPlayer.setLooping(true);
                                        soundTrackName.setText(R.string.song_four);
                                        ImageViewName.setImageResource(R.drawable.bgmed3);
                                        YoYo.with(Techniques.SlideInRight).duration(2000).playOn(ImageViewName);
                                        YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);
                                        int audioSessionId  = mediaPlayer.getAudioSessionId();
                                        if(audioSessionId != -1){
                                            mVisulaizer.setVisibility(View.VISIBLE);

                                            mVisulaizer.setAudioSessionId(audioSessionId);
                                        }
                                    }


                                    else{
                                        Log.d(TAG, "run: media Player not prepared ");
                                    }

                                }
                            });
                        }
                    });
                    thread.start();

                    if(mediaPlayer != null){
                        Log.d(TAG, "onClick: Position " + mediaPlayer.getCurrentPosition());
                        long inserted =  mDb.addMeditationData(new MeditationData("No 4 Amrit Varsha Dhyaan",mediaPlayer.getDuration()));
                        Log.d(TAG, "onClick: inserted: "+ inserted);
                        List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                        for (MeditationData mp : meditationDataList) {
                            String log = "Id: " + mp.getId() + " ,Audiio Nsme : " + mp.getAudioName() + " ,Duration Time: " +
                                    mp.getDuration();
                            // Writing Contacts to log
                            Log.d("Name: ", log);

                        }
                        long reportinserted = rDb.addUserReportData(new ReportData("Meditation",formattedDate,
                                formattedTime,formattedDay,
                                "Amrit Varsha Dhyaan ",
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000))
                                ,String.valueOf(year)));
                        Log.d(TAG, "onClick: report inserted : "+reportinserted);
                        List<ReportData> reportDataList = rDb.getAllUserReportData();
                        Log.d(TAG, "onClick: "+reportDataList);
                        for (ReportData rp : reportDataList) {
                            Log.d(TAG, "onClick: For loop");
                            String reportLog = "Id: "+rp.getId() //0
                                    + ", Mode: "+ rp.getMode()   //1
                                    + ", User Time: "+ rp.getUserTime() //2
                                    + ", Actual Time: "+ rp.getActualTime() //3
                                    + ", Date : "+rp.getDate() //4
                                    + ", Time : "+rp.getTime()  //5
                                    + ", Day: "+rp.getDay()  //6
                                    + ", Type: "+rp.getType() //7
                                    + ", Audio Name : "+rp.getAudioName(); //8
                            Log.d("Report: ",reportLog);
                        }

                    }

                }
                else if(playButtonPressCount==5){
                    pd.show();

                    final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3";

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer = new MediaPlayer();
                                    pd.show();

                                    try {
                                        mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        mediaPlayer.prepare();
                                        Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "run: "+ mediaPlayer);
                                    if(mediaPlayer !=null){
                                        mediaPlayer.start();
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        Log.d(TAG, "run: media player not null ");
                                        if(pauseIsPressed){
                                            mediaPlayer.seekTo(pausePosition+100);
                                        }
                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                pd.dismiss();
                                            }
                                        });
                                        mediaPlayer.setLooping(true);
                                        soundTrackName.setText(R.string.song_five);
                                        ImageViewName.setImageResource(R.drawable.bgmed5);
                                        YoYo.with(Techniques.ZoomInLeft).duration(2000).playOn(ImageViewName);
                                        YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);
                                        int audioSessionId  = mediaPlayer.getAudioSessionId();
                                        if(audioSessionId != -1){
                                            mVisulaizer.setVisibility(View.VISIBLE);

                                            mVisulaizer.setAudioSessionId(audioSessionId);
                                        }
                                    }


                                    else{
                                        Log.d(TAG, "run: media Player not prepared ");
                                    }

                                }
                            });
                        }
                    });
                    thread.start();

                    if(mediaPlayer != null){
                        Log.d(TAG, "onClick: Position " + mediaPlayer.getCurrentPosition());
                        long inserted = mDb.addMeditationData(new MeditationData("No 5 Jyoti Avdhrnam Dhyaan",mediaPlayer.getDuration()));
                        Log.d(TAG, "onClick: inserted" + inserted);
                        List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                        for (MeditationData mp : meditationDataList) {
                            String log = "Id: " + mp.getId() + " ,Audiio Nsme : " + mp.getAudioName() + " ,Duration Time: " +
                                    mp.getDuration();
                            // Writing Contacts to log
                            Log.d("Name: ", log);

                        }
                        long reportinserted = rDb.addUserReportData(new ReportData("Meditation",
                                formattedDate,formattedTime,formattedDay,
                                "Jyoti Avardhanam Dhyaan ",
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                                String.valueOf(year)));
                        Log.d(TAG, "onClick: report inserted : "+reportinserted);
                        List<ReportData> reportDataList = rDb.getAllUserReportData();
                        Log.d(TAG, "onClick: "+reportDataList);
                        for (ReportData rp : reportDataList) {
                            Log.d(TAG, "onClick: For loop");
                            String reportLog = "Id: "+rp.getId() //0
                                    + ", Mode: "+ rp.getMode()   //1
                                    + ", User Time: "+ rp.getUserTime() //2
                                    + ", Actual Time: "+ rp.getActualTime() //3
                                    + ", Date : "+rp.getDate() //4
                                    + ", Time : "+rp.getTime()  //5
                                    + ", Day: "+rp.getDay()  //6
                                    + ", Type: "+rp.getType() //7
                                    + ", Audio Name : "+rp.getAudioName(); //8
                            Log.d("Report: ",reportLog);
                        }


                    }

                }
                else if(playButtonPressCount==6){
                    pd.show();

                    final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3";

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaPlayer = new MediaPlayer();
                                    pd.show();

                                    try {
                                        mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        mediaPlayer.prepare();
                                        Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "run: "+ mediaPlayer);
                                    if(mediaPlayer !=null){

                                        mediaPlayer.start();
                                        Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                        Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                        Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        Log.d(TAG, "run: media player not null ");
                                        if(pauseIsPressed){
                                            mediaPlayer.seekTo(pausePosition+100);
                                        }
                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                pd.dismiss();
                                            }
                                        });
                                        mediaPlayer.setLooping(true);
                                        soundTrackName.setText(R.string.song_six);
                                        ImageViewName.setImageResource(R.drawable.bgmed6);
                                        YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(ImageViewName);
                                        YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);  int audioSessionId  = mediaPlayer.getAudioSessionId();

                                        if(audioSessionId != -1){
                                            mVisulaizer.setVisibility(View.VISIBLE);

                                            mVisulaizer.setAudioSessionId(audioSessionId);
                                        }
                                    }


                                    else{
                                        Log.d(TAG, "run: media Player not prepared ");
                                    }

                                }
                            });
                        }
                    });
                    thread.start();

                   if(mediaPlayer != null){
                       Log.d(TAG, "onClick: Position " + mediaPlayer.getCurrentPosition());
                       long inserted =     mDb.addMeditationData(new MeditationData("No 6 Naad yog Dhyaan",mediaPlayer.getDuration()));
                       Log.d(TAG, "onClick: inserted "+ inserted);
                       List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                       for (MeditationData mp : meditationDataList) {
                           String log = "Id: " + mp.getId() + " ,Audiio Nsme : " + mp.getAudioName() + " ,Duration Time: " +
                                   mp.getDuration();
                           // Writing Contacts to log
                           Log.d("Name: ", log);

                       }
                       long reportinserted = rDb.addUserReportData(new ReportData("Meditation",formattedDate,formattedTime,formattedDay,
                               "Naad Yog Dhyaan ",
                               Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                               Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),String.valueOf(year)));
                       Log.d(TAG, "onClick: report inserted : "+reportinserted);
                       List<ReportData> reportDataList = rDb.getAllUserReportData();
                       Log.d(TAG, "onClick: "+reportDataList);
                       for (ReportData rp : reportDataList) {
                           Log.d(TAG, "onClick: For loop");
                           String reportLog = "Id: "+rp.getId() //0
                                   + ", Mode: "+ rp.getMode()   //1
                                   + ", User Time: "+ rp.getUserTime() //2
                                   + ", Actual Time: "+ rp.getActualTime() //3
                                   + ", Date : "+rp.getDate() //4
                                   + ", Time : "+rp.getTime()  //5
                                   + ", Day: "+rp.getDay()  //6
                                   + ", Type: "+rp.getType() //7
                                   + ", Audio Name : "+rp.getAudioName(); //8
                           Log.d("Report: ",reportLog);
                       }

                   }

                }
                else if(playButtonPressCount==7){


                       final String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3";

                       Thread thread = new Thread(new Runnable() {
                           @Override
                           public void run() {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       pd.show();

                                       mediaPlayer = new MediaPlayer();
                                       try {
                                           mediaPlayer.setDataSource(MeditationActivity.this, Uri.parse(url));
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }

                                       try {
                                           mediaPlayer.prepare();
                                           Log.d(TAG, "run: media player prepare  "+ mediaPlayer);
                                           Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                           Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                           Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       Log.d(TAG, "run: "+ mediaPlayer);
                                       if(mediaPlayer !=null){

                                           mediaPlayer.start();
                                           Log.d(TAG, "run: audio session id "+ mediaPlayer.getAudioSessionId());
                                           Log.d(TAG, "run:  duration "+ mediaPlayer.getDuration());
                                           Log.d(TAG, "run:  current position "+ mediaPlayer.getCurrentPosition());

                                           //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                           Log.d(TAG, "run: media player not null ");
                                           if(pauseIsPressed){
                                               mediaPlayer.seekTo(pausePosition+100);
                                           }
                                           mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                               @Override
                                               public void onPrepared(MediaPlayer mp) {
                                                   pd.dismiss();
                                               }
                                           });
                                           mediaPlayer.setLooping(true);
                                           soundTrackName.setText(R.string.song_seven);
                                           ImageViewName.setImageResource(R.drawable.bgmed7);
                                           YoYo.with(Techniques.RotateInUpLeft).duration(2000).playOn(ImageViewName);
                                           YoYo.with(Techniques.FadeIn).duration(1000).delay(1000).playOn(mVisulaizer);
                                           int audioSessionId  = mediaPlayer.getAudioSessionId();

                                           if(audioSessionId != -1){
                                               mVisulaizer.setVisibility(View.VISIBLE);
                                               mVisulaizer.setAudioSessionId(audioSessionId);
                                           }
                                       }

                                       
                                       else{
                                           Log.d(TAG, "run: media Player not prepared ");
                                       }
                                      
                                   }
                               });
                           }
                       });
                       thread.start();

                    playButtonPressCount=0;



                    if(mediaPlayer != null){
                        Log.d(TAG, "onClick: Position " + mediaPlayer.getCurrentPosition());
                        long inserted = mDb.addMeditationData(new MeditationData("No 7 Tatv Bodh Dhyaan",mediaPlayer.getDuration()));
                        Log.d(TAG, "onClick: inserted : "+ inserted);
                        List<MeditationData> meditationDataList = mDb.getAllMeditationData();
                        for (MeditationData mp : meditationDataList) {
                            String log = "Id: " + mp.getId() + " ,Audio Name : " + mp.getAudioName() + " ,Duration Time: " +
                                    mp.getDuration();
                            // Writing Contacts to log
                            Log.d("Name: ", log);

                        }
                        long reportinserted = rDb.addUserReportData(new ReportData("Meditation",
                                formattedDate,formattedTime,formattedDay,
                                "Tatv Bodh Dhyaan ",
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),
                                Long.parseLong(String.valueOf(mediaPlayer.getDuration()/60000)),String.valueOf(year)));
                        Log.d(TAG, "onClick: report inserted : "+reportinserted);
                        List<ReportData> reportDataList = rDb.getAllUserReportData();
                        Log.d(TAG, "onClick: "+reportDataList);
                        for (ReportData rp : reportDataList) {
                            Log.d(TAG, "onClick: For loop");
                            String reportLog = "Id: "+rp.getId() //0
                                    + ", Mode: "+ rp.getMode()   //1
                                    + ", User Time: "+ rp.getUserTime() //2
                                    + ", Actual Time: "+ rp.getActualTime() //3
                                    + ", Date : "+rp.getDate() //4
                                    + ", Time : "+rp.getTime()  //5
                                    + ", Day: "+rp.getDay()  //6
                                    + ", Type: "+rp.getType() //7
                                    + ", Audio Name : "+rp.getAudioName(); //8
                            Log.d("Report: ",reportLog);
                        }
                    }


                }

            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setEnabled(true);
                Log.d(TAG, "onClick: Pause Button Clicked");
                playButtonPressCount--;
                Log.d(TAG, "onClick: "+  mediaPlayer.getCurrentPosition());
                pausePosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                pauseIsPressed = true;
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setEnabled(true);
                pauseIsPressed = false;
                pausePosition = 0;
              Log.d(TAG, "onClick: stop button clicked "+mediaPlayer.getCurrentPosition()/1000 +" seconds" );
                int lastid = rDb.getLastId();
               rDb.updateData(String.valueOf(lastid),(float)mediaPlayer.getCurrentPosition()/60000);
                mediaPlayer.stop();
                progressDialog.dismiss();

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Method called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Method Called");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Method called");

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Method called");
        if (mVisulaizer != null)
            mVisulaizer.release();
        medSharedPreferences = getSharedPreferences(BUTTON_COUNT_PREFERENCE, Context.MODE_PRIVATE);
        buttonCountOnDestroy = medSharedPreferences.getInt(PLAY_BUTTON_CLICKS,0);
        Log.d(TAG, "onDestroy: "+buttonCountOnDestroy);
        super.onDestroy();
    }



}
