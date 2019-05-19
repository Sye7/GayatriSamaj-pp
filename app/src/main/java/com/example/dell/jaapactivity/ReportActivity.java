package com.example.dell.jaapactivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.jaapactivity.Jap.JapDatabaseHandler;
import com.example.dell.jaapactivity.Meditation.MeditationDataBaseHandler;
import com.example.dell.jaapactivity.ReportManager.ReportData;
import com.example.dell.jaapactivity.ReportManager.ReportDataBaseHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "ReportActivity";
    ReportDataBaseHandler rDb = new ReportDataBaseHandler(this);
    JapDatabaseHandler jDb = new JapDatabaseHandler(this);
    MeditationDataBaseHandler mDb = new MeditationDataBaseHandler(this);

    Button startDate;
    Button endDate;
    Integer selectedStartDate=1;
    Integer selectedStartMonth=0;
    Integer selectedStartYear=2019;
    Integer selectedEndDate=20;
    Integer selectedEndMonth=0;
    Integer selectedEndYear=2019;
    public PieDataSet rangeDataSet;
    DatePickerDialog datePickerDialog;
    public int selected_date;
    public int selected_month;
    public int selected_year;

    boolean startClicked = false;
    boolean endClicked = true;

    public TextView c1;
    public TextView c2;
    public TextView c3;
    public TextView c4;

    public TextView h1;
    public TextView h2;
    public TextView h3;
    public TextView h4;
    public PieChart rangeDataPieChart;

    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);




        final BarChart lineChart = findViewById(R.id.totalBar);





        c1 = findViewById(R.id.Tmedi);
        c2 = findViewById(R.id.Tjaps);
        c3 = findViewById(R.id.Tswadhya);
        c4 = findViewById(R.id.Tyagya);

        h1 = findViewById(R.id.h1);
        h2 = findViewById(R.id.h2);
        h3 = findViewById(R.id.h3);
        h4 = findViewById(R.id.h4);

        card1 = findViewById(R.id.fc);
        card2 = findViewById(R.id.sc);
        card3 = findViewById(R.id.tc);
        card4 = findViewById(R.id.fourthc);

        setValues();


        //Day Wise Bar Chart

        ArrayList<BarEntry> overAllBar = new ArrayList<BarEntry>();
        overAllBar.add(new BarEntry(1,rDb.totalJaps()));
        overAllBar.add(new BarEntry(2,rDb.totalSwadhyay()));
        overAllBar.add(new BarEntry(3, rDb.totalMeditations()));
        overAllBar.add(new BarEntry(4, rDb.totalYagya()));




        BarDataSet overAllBarSet = new BarDataSet(overAllBar,"Overall DataSet");
        BarData barData = new BarData(overAllBarSet);
        lineChart.setData(barData);
        overAllBarSet.setLabel("Japs, Swadhyay, Meditation, Yagya");

        int[] c = {Color.parseColor("#5C6BC0"),Color.parseColor("#33691E"),Color.parseColor("#424242"),Color.parseColor("#00E676")};
        overAllBarSet.setColors(c);
        // overAllBarSet.setStackLabels(labels);
        //lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        overAllBarSet.setColors(ColorTemplate.MATERIAL_COLORS);
        lineChart.animateXY(3000,3000);
        overAllBarSet.setBarBorderColor(Color.BLACK);
        overAllBarSet.setBarBorderWidth(0.7f);
        overAllBarSet.setHighLightAlpha(3);
        lineChart.setFitBars(true);

        // change width
        overAllBarSet.setBarBorderWidth(0.00075f);








    }

    private void setValues() {
        c1.setText(rDb.totalMeditations() + "");
        c2.setText(rDb.totalJaps() + "");
        c3.setText(rDb.totalSwadhyay() + "");
        c4.setText(rDb.totalYagya() + "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.datePicker:
                LayoutInflater li = LayoutInflater.from(ReportActivity.this);
                View datePickView = li.inflate(R.layout.datedialog, null);
                startDate =  datePickView.findViewById(R.id.setStartDate);
                endDate  = datePickView.findViewById(R.id.setEndDate);
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setView(datePickView);


                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startClicked = true;
                        datePickerDialog.show();
                    }
                });
                endDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endClicked = true;
                        datePickerDialog.show();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
  /*  public void refreshPieChart(){

        //Pie Chart for displaying data in a date range set by user
        ReportDataBaseHandler rDb2 = new ReportDataBaseHandler(getParent());
        ArrayList<PieEntry> rangeDataList = new ArrayList<>();
        rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate,selectedEndDate,selectedStartMonth,selectedEndMonth,0),"Japs"));
        rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate,selectedEndDate,selectedStartMonth,selectedEndMonth,1),"Meditataion"));
        rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate,selectedEndDate,selectedStartMonth,selectedEndMonth,2),"Swadhyay"));
        rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate,selectedEndDate,selectedStartMonth,selectedEndMonth,3),"Yagya"));
        PieDataSet rangeDataSet = new PieDataSet(rangeDataList,"Selected Range View");
        PieData rangeData = new PieData(rangeDataSet);
        rangeDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        rangeDataSet.setSliceSpace(1f);
        rangeDataPieChart.setEntryLabelColor(Color.BLACK);
        rangeDataPieChart.animateXY(2000,2000);
        rangeDataPieChart.setData(rangeData);
        rangeData.notifyDataChanged();
        rangeDataPieChart.invalidate();


    }*/

   /* public void  showDialogBox(Activity activity, String title, final CharSequence message) {
        LayoutInflater li = LayoutInflater.from(activity);
        View datePickView = li.inflate(R.layout.datedialog, null);
        startDate =  datePickView.findViewById(R.id.setStartDate);
        endDate  = datePickView.findViewById(R.id.setEndDate);
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setView(datePickView);


       startDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showDatePicker();
           }
       });
       endDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showDatePicker();

           }
       });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }*/

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selected_date = view.getDayOfMonth();
        selected_month = view.getMonth();
        selected_year = view.getYear();


        if(startClicked){

            selectedStartDate = selected_date;
            selectedStartMonth = selected_month;
            selectedStartYear = selected_year;
            startClicked=false;

        }
        else if(endClicked){
            selectedEndDate = selected_date;
            selectedEndMonth = selected_month;
            selectedEndYear = selected_year;
            endClicked=false;

        }
        if(!startClicked&&!endClicked) {


            ReportDataBaseHandler rDb2 = new ReportDataBaseHandler(ReportActivity.this);
            ArrayList<PieEntry> rangeDataList = new ArrayList<>();
            rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate, selectedEndDate, selectedStartMonth, selectedEndMonth, 0), "Japs"));
            rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate, selectedEndDate, selectedStartMonth, selectedEndMonth, 1), "Meditations"));
            rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate, selectedEndDate, selectedStartMonth, selectedEndMonth, 2), "Swadhyay"));
            rangeDataList.add(new PieEntry(rDb2.timeBetweenRange(selectedStartDate, selectedEndDate, selectedStartMonth, selectedEndMonth, 3), "Yagya"));
            PieDataSet rangeDataSet = new PieDataSet(rangeDataList, "Selected Range View");
            PieData rangeData = new PieData(rangeDataSet);
            rangeDataPieChart.setData(rangeData);
            rangeDataPieChart.setCenterText("Time in your selected range");
            rangeDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
            rangeDataSet.setSliceSpace(1f);
            rangeDataPieChart.setEntryLabelColor(Color.BLACK);
            rangeDataPieChart.animateXY(2000, 2000);
            rangeData.notifyDataChanged();
            rangeDataPieChart.invalidate();

        }


        Log.d(TAG, "onCreate: selected date from date picker "+ selectedStartDate);
        Log.d(TAG, "onCreate: selected month from date picker "+ selectedStartMonth);
        Log.d(TAG, "onCreate: selected year from date picker "+ selectedStartYear);


    }


}