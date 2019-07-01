package com.example.spotifyjoacir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Spotify " + LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1337;
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String CLIENT_ID = "5de6930c8a744270851a5064c7ff6333";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private String AUTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mLoginButton = (Button)findViewById(R.id.buttonLogin);
        mLoginButton.setOnClickListener(mListener);
    }

    private void openLoginWindow() {

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN,REDIRECT_URI);

        builder.setScopes(new String[]{"user-top-read", "user-read-recently-played", });
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    View.OnClickListener mListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.buttonLogin:
                    openLoginWindow();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("AUTH_TOKEN", AUTH);
        setResult(1,intent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE)        {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            switch (response.getType()) {
                case TOKEN:
                    AUTH = response.getAccessToken();
                    onBackPressed();
                    break;
                case ERROR:
                    AUTH = "ERROR";
                    Log.e(TAG,"Auth error: " + response.getError());
                    break;
                default:
                    AUTH = "ELSE";
                    Log.d(TAG,"Auth result: " + response.getType());
            }
        }
    }
}
