package com.doordash.base.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doordash.DoorDashApplication;

/**
 *
 */
public abstract class DoorDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoorDashApplication.getApplication().setCurrentActivity(this);
    }
}
