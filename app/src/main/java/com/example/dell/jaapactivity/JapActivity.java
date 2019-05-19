package com.example.dell.jaapactivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.dell.jaapactivity.Jap.JapData;
import com.example.dell.jaapactivity.Jap.JapDatabaseHandler;
import com.example.dell.jaapactivity.ReportManager.ReportData;
import com.example.dell.jaapactivity.ReportManager.ReportDataBaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JapActivity extends Activity{
    Spinner options_spinner;
    MyCountdownTimer myCountdownTimer;
    TextView timer_text;
    Button startJap;
    Long time_in_minutes = 0l;
    Long time_in_milli = 0l;
    Button stopJap;
    TextView time_textView_store;
    TextView display_time_selected;
    SharedPreferences sharedPreferences;
    SharedPreferences optionSelectedPreference;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor optionEditor;
    String selectedItemFromOptions;
    String videoUrl = null;
    String item = null;
    Button meditationActivity;
    Button swadhyayActivity;
    Button reportsActivity;
    Button yagyaActivity;
   ;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String OPTION_PREFERENCE = "OptionPref";
    public static final String selected_item = "item_selected";
    public static final String Time_in_minutes = "timeKey";
    private static final String TAG = "JapActivity";
    EditText userInput; int flag =0;

  //  LinearTimerView linearTimerView = (LinearTimerView)
  //          findViewById(R.id.linearTimer);

    // Instance Variables from video activity
    TextView timerTextView;
    VideoView videoView;
    TextView timeInMilliTextView;
     private  MediaPlayer mediaPlayer;
    int dr = 0;
    int primaryKey = 0;
    private ProgressDialog progressDialog;
    private int position = 0;
    public static Long time_in_milli_to_store = 0l;
    EditText JapTime;
    float actualTime = 0f;
    //Jap Database
       JapDatabaseHandler db = new JapDatabaseHandler(this);
       LinearLayout videoLayout;
       LinearLayout buttonLayout;
    //Report Manager Database
    ReportDataBaseHandler rDb = new ReportDataBaseHandler(this);


    private static final int TIME_REMINDER_NOTIFICATION_ID = 1138;
    private static final String TIME_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";



    // Bottom nav
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




    BottomNavigationView navigation;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jaap_activity_newui);
        time_textView_store = findViewById(R.id.time_text);

        timer_text = findViewById(R.id.timer_textView);
        startJap = findViewById(R.id.startJap);
        stopJap = findViewById(R.id.stopJap);
        display_time_selected = findViewById(R.id.display_selected_time);
        options_spinner = findViewById(R.id.options);

        userInput = new EditText(this);
        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        startJap.setEnabled(true);

        // Bottom nav
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setCheckable(false);  // to uncheck first item for preselecting

        //Current date and time
        Date currentTime = Calendar.getInstance().getTime();






        SimpleDateFormat df = new SimpleDateFormat("MMM");
        final String formattedDate = df.format(currentTime);

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd");
        final String formattedTime = timeFormat.format(currentTime);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        final String formattedDay = dayFormat.format(currentTime);
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        Log.d(TAG, "onCreate: Current Week :" + calender.get(Calendar.WEEK_OF_YEAR));


        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.prompts, null);

        videoLayout = (LinearLayout) findViewById(R.id.lltoshiftup);
       // final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoLayout.getLayoutParams();
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoLayout.getLayoutParams();

        buttonLayout = findViewById(R.id.linearLayoutToShift);
        final RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();

      //  LinearTimerView linearTimerView= findViewById(R.id.linearTimer);
        //LinearTimer linearTimer = new LinearTimer
        //     .duration(millisUntilFinished * 1000)
        //          .build();
        //Adding dummy data in Reports


        JapTime = promptsView.findViewById(R.id.editTextDialogUserInput);
        //Meditation activity intent
        
        //Variables initialisation from video Activity
        // timerTextView = findViewById(R.id.Jtimer);
        videoView = findViewById(R.id.videoViewV);

        timeInMilliTextView = findViewById(R.id.time_in_milli);

      /*  final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);*/

//https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4        //   videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"));
        videoView.requestFocus();
        videoView.canPause();
        progressDialog = new ProgressDialog(JapActivity.this);

        //Spinner items array
        final ArrayAdapter<CharSequence> optionsAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.japOptions, android.R.layout.simple_spinner_item);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        options_spinner.setAdapter(optionsAdapter);


        //shared preferences for time
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //shared preferences for option selected
        optionSelectedPreference = getSharedPreferences(OPTION_PREFERENCE, Context.MODE_PRIVATE);

        //item click listener on option spinner
        options_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: item selected " + item);
                optionEditor = optionSelectedPreference.edit();
                optionEditor.putString(selected_item, item);
                optionEditor.apply();
                //   Toast.makeText(JapActivity.this,"You selected "+item,Toast.LENGTH_SHORT).show();
                //   final String[] time = {"5","10","15","20","25","30"};

                //item click if statements
                // if statement if user clicks option by Time
                if (item.equalsIgnoreCase("by Time")) {
                    videoUrl = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(JapActivity.this);
                    // builder.setView(promptsView);

                    //freeing the parent view that already had a parent
                    if (promptsView.getParent() != null) {
                        ((ViewGroup) promptsView.getParent()).removeView(promptsView);
                    }
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            primaryKey++;

                            time_in_minutes = Long.parseLong(String.valueOf(JapTime.getText()));
                            // time_in_minutes = Long.parseLong(time[which]);
                            display_time_selected.setVisibility(View.VISIBLE);
                            display_time_selected.setText((time_in_minutes).toString() + " Minutes ");
                            time_in_milli = time_in_minutes * 60000;
                            actualTime = time_in_milli;
                            Log.d(TAG, "onClick: Time selected :" + time_in_milli + " milliseconds");
                            editor = sharedPreferences.edit();
                            editor.putLong(Time_in_minutes, time_in_milli);
                            editor.apply();


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setView(promptsView);
                    alertDialog.show();

                    params2.topMargin =350;
                    buttonLayout.setLayoutParams(params2);

                } else if (item.equalsIgnoreCase("by Mala")) {


                } else if (item.equalsIgnoreCase("with Pujya Gurudev")) {
                       params.topMargin = 1030;
                       videoLayout.setLayoutParams(params);

                       params2.topMargin =750;
                       buttonLayout.setLayoutParams(params2);

                    }





                 else if (item.equalsIgnoreCase("with Pujya Mataji")) {

                } else {
                    display_time_selected.setVisibility(View.INVISIBLE);

                }
                //    db.addJapData(new JapData(0,item,time_in_minutes,0,""));


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        startJap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                startJap.setEnabled(false);
                optionSelectedPreference = getSharedPreferences(OPTION_PREFERENCE, Context.MODE_PRIVATE);
                selectedItemFromOptions = optionSelectedPreference.getString(selected_item, "Choose Options");
                Log.d(TAG, "onClick: Selected Item " + selectedItemFromOptions);


                if (selectedItemFromOptions.equalsIgnoreCase("by Time")) {
                    timer_text.setVisibility(View.VISIBLE);
                    sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    Long timer_time = sharedPreferences.getLong(Time_in_minutes, 10);
                    myCountdownTimer = new MyCountdownTimer(timer_time, 100);
                    myCountdownTimer.start();
                    //adding data in the Jap database
                    //   long inserted = db.addJapData(new JapData(0, primaryKey, time_in_minutes, item, "null"));
                    //displaying rows inserted
                    long inserted = db.addJapData(new JapData(time_in_minutes, item));
                    Log.d(TAG, "onClick: Row inserted " + inserted);
                    // Displaying all data in a list view
                    List<JapData> japDataList = db.getAllJapData();
                    Log.d(TAG, "onClick: jap " + japDataList);
                    for (JapData jp : japDataList) {
                        String log = "Id: " + jp.getId() + " ,Type : " + jp.getType() + " ,Time: " +
                                jp.getTime() + ", Has Video :" + jp.isHasVideo() + ", Video Url :" + jp.getVideoURl();
                        // Writing Contacts to log
                        Log.d("Name: ", log);

                    }
                    //adding data in the Reports DataBase
                    long reportInserted = rDb.addUserReportData(new ReportData("Jap", time_in_minutes, time_in_minutes, formattedDate, formattedTime, formattedDay, item, String.valueOf(year)));
                    Log.d(TAG, "onClick: User Report : " + reportInserted);

                    //Displaying all data
                    List<ReportData> reportDataList = rDb.getAllUserReportData();
                    Log.d(TAG, "onClick: " + reportDataList);
                    for (ReportData rp : reportDataList) {
                        Log.d(TAG, "onClick: For loop");
                        String reportLog = "Id: " + rp.getId() //0
                                + ", Mode: " + rp.getMode()   //1
                                + ", User Time: " + rp.getUserTime() //2
                                + ", Actual Time: " + rp.getActualTime() //3
                                + ", Date : " + rp.getDate() //4
                                + ", Time : " + rp.getTime()  //5
                                + ", Day: " + rp.getDay()  //6
                                + ", Type: " + rp.getType() //7
                                + ", Audio Name: " + rp.getAudioName();//8
                        Log.d("Report: ", reportLog);
                    }

                } else if (selectedItemFromOptions.equalsIgnoreCase("with Pujya Gurudev")) {
                    display_time_selected.setVisibility(View.INVISIBLE);
                    timer_text.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.VISIBLE);

                    progressDialog.setTitle("Loading Video");
                    progressDialog.setMessage("Please Hold on");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    long inserted = db.addJapData(new JapData(time_in_minutes, item));
                    Log.d(TAG, "onClick: Row inserted " + inserted);
                    // Intent intent = new Intent(JapActivity.this,VideoActivity.class);
                    // startActivity(intent);
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();

                        }
                    });

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            progressDialog.dismiss();
                            dr = mp.getDuration();
                            int duration = mp.getDuration() / 1000;
                            int hours = duration / 3600;
                            int minutes = (duration / 60) - (hours * 60);
                            int seconds = duration - (hours * 3600) - (minutes * 60);
                            int videoWidth = videoView.getLayoutParams().width;
                            int videoHeight = videoView.getLayoutParams().height;
                            Log.d(TAG, "onPrepared: video Width " + videoWidth);
                            Log.d(TAG, "onPrepared: video Height " + videoHeight);

                            String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
                            Toast.makeText(getApplicationContext(), "duration is " + formatted, Toast.LENGTH_LONG).show();
                            if (position == 0) {
                                videoView.start();
                                myCountdownTimer = new MyCountdownTimer(dr, 100);
                                myCountdownTimer.start();

                            }
                            long inserted = rDb.addUserReportData(new ReportData("Jap", Long.parseLong(String.valueOf(minutes)), Long.parseLong(String.valueOf(minutes)), formattedDate, formattedTime, formattedDay, item, String.valueOf(year)));
                            Log.d(TAG, "onClick: User Report : " + inserted);

                            //Displaying all data
                            List<ReportData> reportDataList = rDb.getAllUserReportData();
                            Log.d(TAG, "onClick: " + reportDataList);
                            for (ReportData rp : reportDataList) {
                                Log.d(TAG, "onClick: For loop");
                                String reportLog = "Id: " + rp.getId() //0
                                        + ", Mode: " + rp.getMode()   //1
                                        + ", User Time: " + rp.getUserTime() //2
                                        + ", Actual Time: " + rp.getActualTime() //3
                                        + ", Date : " + rp.getDate() //4
                                        + ", Time : " + rp.getTime()  //5
                                        + ", Day: " + rp.getDay()  //6
                                        + ", Type: " + rp.getType() //7
                                        + ", Audio Name: " + rp.getAudioName();//8
                                Log.d("Report: ", reportLog);
                            }
                        }
                    });

                    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Log.d(TAG, "onError: \"API123\", \"What \" + what + \" extra \" + extra");
                            return false;
                        }
                    });
                    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                          /* switch (what) {
                               case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                   Log.d(TAG, "onInfo: "+timerTextView.getText());
                                   Log.d(TAG, "onInfo: "+timeInMilliTextView.getText());
                                   //time_in_milli_to_store = Long.parseLong((String) timeInMilliTextView.getText());
                                  // myCountdownTimer.cancel();
                                   //   case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                   //       myCountdownTimer.start();
                               case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                  // myCountdownTimer.cancel();
                                 //  myNewCountdownTimer = new MyCountdownTimer(time_in_milli_to_store,100);
                                  // myNewCountdownTimer.onTick(time_in_milli_to_store);
                                  // myNewCountdownTimer.start();

                           }*/

                            return false;
                        }

                    });


                } else if (selectedItemFromOptions.equalsIgnoreCase("with Pujya Mataji")) {
                    display_time_selected.setVisibility(View.INVISIBLE);
                    timer_text.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    //Intent intent = new Intent(JapActivity.this,VideoActivity.class);
                    //startActivity(intent);
                    long inserted = db.addJapData(new JapData(time_in_minutes, item));
                    Log.d(TAG, "onClick: Row inserted " + inserted);

                    progressDialog.setTitle("Loading Video");
                    progressDialog.setMessage("Please Hold on");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Intent intent = new Intent(JapActivity.this,VideoActivity.class);
                    // startActivity(intent);
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();

                        }
                    });

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            progressDialog.dismiss();
                            dr = mp.getDuration();
                            int duration = mp.getDuration() / 1000;
                            int hours = duration / 3600;
                            int minutes = (duration / 60) - (hours * 60);
                            int seconds = duration - (hours * 3600) - (minutes * 60);
                            int videoWidth = videoView.getLayoutParams().width;
                            int videoHeight = videoView.getLayoutParams().height;
                            Log.d(TAG, "onPrepared: video Width " + videoWidth);
                            Log.d(TAG, "onPrepared: video Height " + videoHeight);

                            String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
                            Toast.makeText(getApplicationContext(), "duration is " + formatted, Toast.LENGTH_LONG).show();
                            if (position == 0) {
                                videoView.start();
                                myCountdownTimer = new MyCountdownTimer(dr, 100);
                                myCountdownTimer.start();

                            }
                            long inserted = rDb.addUserReportData(new ReportData("Jap", Long.parseLong(String.valueOf(minutes)), Long.parseLong(String.valueOf(minutes)), formattedDate, formattedTime, formattedDay, item, String.valueOf(year)));
                            Log.d(TAG, "onClick: User Report : " + inserted);

                            //Displaying all data
                            List<ReportData> reportDataList = rDb.getAllUserReportData();
                            Log.d(TAG, "onClick: " + reportDataList);
                            for (ReportData rp : reportDataList) {
                                Log.d(TAG, "onClick: For loop");
                                String reportLog = "Id: " + rp.getId() //0
                                        + ", Mode: " + rp.getMode()   //1
                                        + ", User Time: " + rp.getUserTime() //2
                                        + ", Actual Time: " + rp.getActualTime() //3
                                        + ", Date : " + rp.getDate() //4
                                        + ", Time : " + rp.getTime()  //5
                                        + ", Day: " + rp.getDay()  //6
                                        + ", Type: " + rp.getType() //7
                                        + ", Audio Name: " + rp.getAudioName();//8
                                Log.d("Report: ", reportLog);
                            }
                        }
                    });

                    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Log.d(TAG, "onError: \"API123\", \"What \" + what + \" extra \" + extra");
                            return false;
                        }
                    });
                    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                          /* switch (what) {
                               case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                   Log.d(TAG, "onInfo: "+timerTextView.getText());
                                   Log.d(TAG, "onInfo: "+timeInMilliTextView.getText());
                                   //time_in_milli_to_store = Long.parseLong((String) timeInMilliTextView.getText());
                                  // myCountdownTimer.cancel();
                                   //   case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                   //       myCountdownTimer.start();
                               case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                  // myCountdownTimer.cancel();
                                 //  myNewCountdownTimer = new MyCountdownTimer(time_in_milli_to_store,100);
                                  // myNewCountdownTimer.onTick(time_in_milli_to_store);
                                  // myNewCountdownTimer.start();

                           }*/

                            return false;
                        }

                    });

                } else if (selectedItemFromOptions.equalsIgnoreCase("by Mala")) {
                    long inserted = db.addJapData(new JapData(time_in_minutes, item));
                    Log.d(TAG, "onClick: Row inserted " + inserted);
                }

            }
        });
        stopJap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_text.setTextColor(getResources().getColor(R.color.black));
                display_time_selected.setVisibility(View.INVISIBLE);
                startJap.setEnabled(true);
                if (selectedItemFromOptions.equalsIgnoreCase("with Pujya Gurudev") || selectedItemFromOptions.equalsIgnoreCase("with Pujya Mataji")) {
                    videoView.stopPlayback();
                    videoView.setVisibility(View.INVISIBLE);
                    actualTime = (float) dr - Long.parseLong(String.valueOf(time_textView_store.getText()));
                    Log.d(TAG, "onClick: " + actualTime);

                    timer_text.setText("00:00:00");
                    myCountdownTimer.cancel();

                } else if (selectedItemFromOptions.equalsIgnoreCase("by Time")) {
                    actualTime = (float) time_in_milli - Long.parseLong(String.valueOf(time_textView_store.getText()));
                    Log.d(TAG, "onClick: " + actualTime);

                    timer_text.setText("00:00:00");
                    myCountdownTimer.cancel();
                }


                rDb.getLastId();
                Log.d(TAG, "onClick: last Id " + rDb.getLastId());

                //update actual time
                rDb.updateData(String.valueOf(rDb.getLastId()), actualTime / 60000);
                List<ReportData> reportDataList = rDb.getAllUserReportData();
                Log.d(TAG, "onClick: " + reportDataList);
                for (ReportData rp : reportDataList) {
                    Log.d(TAG, "onClick: For loop");
                    String reportLog = "Id: " + rp.getId() //0
                            + ", Mode: " + rp.getMode()   //1
                            + ", User Time: " + rp.getUserTime() //2
                            + ", Actual Time: " + rp.getActualTime() //3
                            + ", Date : " + rp.getDate() //4
                            + ", Time : " + rp.getTime()  //5
                            + ", Day: " + rp.getDay()  //6
                            + ", Type: " + rp.getType() //7
                            + ", Audio Name: " + rp.getAudioName();//8
                    Log.d("Report: ", reportLog);
                }

            }
        });
    }

    public void withPujyaGurudev(){

    }

    public class MyCountdownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisInFuture),
                    TimeUnit.MILLISECONDS.toMinutes(millisInFuture) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisInFuture)),
                    TimeUnit.MILLISECONDS.toSeconds(millisInFuture) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisInFuture)));

            sendNotification(hms);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            time_textView_store.setText(String.valueOf(millisUntilFinished));
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            timer_text.setVisibility(View.VISIBLE);
            timer_text.setText(hms);
            Log.d(TAG, "onTick: "+ millisUntilFinished);

            if(millisUntilFinished<10000){
                if(flag>1){

                }
                else
                {
                    flag =1;
                    if(millisUntilFinished<10000 && flag ==1){
                        Log.d(TAG, "onTick: inside this ");
                        startBlinking(millisUntilFinished);

                    }
                    flag++;

                }

            }




         //   long percentProgress = (millisUntilFinished/time_in_milli)*100;
          //  donutProgress.setProgress(percentProgress);


        }

        @Override
        public void onFinish() {
            Log.d(TAG, "onFinish: onFinish called ");
            timer_text.setTextColor(getResources().getColor(R.color.black));
            stopAlarm();

        }
    }

    public void startBlinking(Long mili){
        sendNotification("Almost Done ");
        playAlarm();
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(mili/10); //You can manage the time of the blink with this parameter
       // anim.setStartOffset(80);
      //  anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(9);

        timer_text.startAnimation(anim);
        timer_text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        if(mili<1000){
            timer_text.clearAnimation();
        }
    }

    private void playAlarm() {
         mediaPlayer = MediaPlayer.create(this,R.raw.beep_two);
         mediaPlayer.start();

         mediaPlayer.setLooping(true);
         Long dur = (long) mediaPlayer.getDuration();
        Log.d(TAG, "playAlarm: duration "+ dur);
    }
    private void stopAlarm(){
        mediaPlayer.stop();
    }

    public void sendNotification(String timer) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "sendNotification: Build version > O ");
            NotificationChannel mChannel = new NotificationChannel(
                    TIME_REMINDER_NOTIFICATION_CHANNEL_ID,
                    this.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mNotification = new NotificationCompat.Builder(JapActivity.this,TIME_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(" Timer On")
                .setContentText(timer)
                .setDefaults(Notification.DEFAULT_VIBRATE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mNotification.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(TIME_REMINDER_NOTIFICATION_ID, mNotification.build());

    }
}