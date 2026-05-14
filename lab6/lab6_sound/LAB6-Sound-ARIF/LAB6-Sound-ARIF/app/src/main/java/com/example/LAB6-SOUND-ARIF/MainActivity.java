package com.example.lab6b_talib;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView alarmPrompt;
    Button startSetDialog, button;
    EditText message;

    private static final int ALARM_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmPrompt = findViewById(R.id.alarmprompt);
        startSetDialog = findViewById(R.id.startSetDialog);
        button = findViewById(R.id.button);
        message = findViewById(R.id.editText);

        requestNotificationPermission();

        startSetDialog.setOnClickListener(v -> openTimePickerDialog());

        button.setOnClickListener(v -> showCloseDialog());
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(
                        MainActivity.this,
                        (TimePicker view, int hourOfDay, int minute) -> {

                            Calendar targetCal = Calendar.getInstance();
                            targetCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            targetCal.set(Calendar.MINUTE, minute);
                            targetCal.set(Calendar.SECOND, 0);

                            if (targetCal.before(Calendar.getInstance())) {
                                targetCal.add(Calendar.DATE, 1);
                            }

                            alarmPrompt.setText("Alarm set at: " + hourOfDay + ":" + minute);

                            setAlarm(targetCal);
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                );

        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    private void setAlarm(Calendar targetCal) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg", message.getText().toString());

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this,
                        ALARM_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(settingsIntent);
                return;
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                targetCal.getTimeInMillis(),
                pendingIntent
        );
    }

    private void showCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit Application?");
        builder.setMessage("Do you want to close the app?");

        builder.setPositiveButton("Yes", (dialog, which) -> finish());

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}