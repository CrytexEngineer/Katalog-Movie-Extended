


package com.example.aqil.katalogfilmuiux;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.aqil.katalogfilmuiux.pref.SettingPreference;
import com.example.aqil.katalogfilmuiux.scheduler.AlarmScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.checkbox_release)
    CheckBox chckRelease;
    @BindView(R.id.checkbox_reminder)
    CheckBox checkBoxReminder;
    @BindView(R.id.change_leanguae)
    TextView tvLanguage;
    final Calendar repeatTime = Calendar.getInstance();
    final AlarmScheduler alarmScheduler = new AlarmScheduler();
    private SettingPreference settingPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingPreference=new SettingPreference(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        checkBoxReminder.setChecked(settingPreference.getHasChcked(R.id.checkbox_reminder));
        chckRelease.setChecked(settingPreference.getHasChcked(R.id.checkbox_release));
        settingPreference.getHasChcked(R.id.checkbox_release);
        checkBoxReminder.setOnCheckedChangeListener(this);
        chckRelease.setOnCheckedChangeListener(this);
        tvLanguage.setOnClickListener(new onClick());


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        if (isChecked) {
            setAlarm(buttonView.getId());
        } else {
            cancelAlarm(buttonView.getId());
        }

    }

    public void setAlarm(final int checkBoxId) {
        final Calendar currentDate = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                repeatTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                repeatTime.set(Calendar.MINUTE, minute);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String repeatTimeTime = timeFormat.format(repeatTime.getTime());

                if (checkBoxId == R.id.checkbox_reminder) {
                    alarmScheduler.setRepeatTimeAlarm(SettingActivity.this, AlarmScheduler.TYPE_REMINDER, repeatTimeTime);
                } else {
                    alarmScheduler.setRepeatTimeAlarm(SettingActivity.this, AlarmScheduler.TYPE_RELEASE_DATE, repeatTimeTime);
                }
                settingPreference.setHasChecked(true, checkBoxId);

            }

        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);

        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    if (checkBoxId == R.id.checkbox_reminder) {
                        checkBoxReminder.setChecked(false);
                    } else {
                        chckRelease.setChecked(false);
                    }
                    settingPreference.setHasChecked(false, checkBoxId);
                }
            }
        });
        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.show();
    }

    public void cancelAlarm(int checkBoxId) {
        if (checkBoxId == R.id.checkbox_reminder) {
            alarmScheduler.cancelAlarm(SettingActivity.this, AlarmScheduler.TYPE_REMINDER);
        } else {
            alarmScheduler.cancelAlarm(SettingActivity.this, AlarmScheduler.TYPE_RELEASE_DATE);
        }

        settingPreference.setHasChecked(false, checkBoxId);
    }

    private class onClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
            setTitle(getResources().getText(R.string.change_leanguae));

        }
    }
}


