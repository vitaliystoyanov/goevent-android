package com.stoyanov.developer.goevent.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.stoyanov.developer.goevent.R;
import com.stoyanov.developer.goevent.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        findViewById(R.id.login_button).setOnClickListener(view -> finish());

        findViewById(R.id.login_register_button).setOnClickListener(view ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
    }

    @OnClick(R.id.ib_back)
    public void onClickBack() {
        finish();
    }
}
