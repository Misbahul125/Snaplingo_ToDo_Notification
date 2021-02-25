package com.example.snaplingotodonotification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText etText;
    Button btnSelectDate , btnSelectTime , btnSetReminder;

    String timeTonotify;
    String text , textDate , textTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.text);
        btnSelectDate = findViewById(R.id.select_date);
        btnSelectTime = findViewById(R.id.select_time);
        btnSetReminder = findViewById(R.id.set_reminder);
    }

    public void selectDate(View view) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnSelectDate.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void selectTime(View view) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                btnSelectTime.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    public void setReminder(View view) {

        text = etText.getText().toString().trim();
        textDate = btnSelectDate.getText().toString().trim();
        textTime = btnSelectTime.getText().toString().trim();

        if ((TextUtils.isEmpty(text)) || (TextUtils.isEmpty(textDate)) || (TextUtils.isEmpty(textTime))) {
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(this, "Enter text", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(textDate)) {
                Toast.makeText(this, "Select date", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(textTime)) {
                Toast.makeText(this, "Select time", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
            intent.putExtra("TEXT", text);
            intent.putExtra("TIME", textDate);
            intent.putExtra("DATE", textTime);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String dateandtime = textDate + " " + timeTonotify;
            DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
            try {
                Date date1 = formatter.parse(dateandtime);
                am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

                Toast.makeText(this, "Reminder set successfully", Toast.LENGTH_LONG).show();

            } catch (ParseException e) {
                e.printStackTrace();

                Toast.makeText(this, "Unable to set reminder!", Toast.LENGTH_LONG).show();
            }
        }
    }
}