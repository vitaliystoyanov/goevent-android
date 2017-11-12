package com.stoyanov.developer.goevent.ui.getstarted;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.ui.signin.SignInActivity;
import com.stoyanov.developer.goevent.ui.signup.SignUpActivity;

import butterknife.ButterKnife;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        ButterKnife.bind(this);
    }
}
