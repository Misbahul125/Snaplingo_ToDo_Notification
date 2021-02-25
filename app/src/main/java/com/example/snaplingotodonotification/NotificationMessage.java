package com.example.snaplingotodonotification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationMessage extends AppCompatActivity {

    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);

        tvMessage = findViewById(R.id.tv_message);
        Bundle bundle = getIntent().getExtras();
        tvMessage.setText(bundle.getString("MESSAGE"));
    }
}