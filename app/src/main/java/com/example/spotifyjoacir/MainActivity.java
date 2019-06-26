package com.example.spotifyjoacir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "1d7edefde74f45beb393a66257a0ba49";     //This should be generate by spotify dashboard
    private static final String REDIRECT_URI = "http://localhost:8888/callback";    //you should register it on spotify dashboard
    private SpotifyAppRemote mSpotifyAppRemote;                                     //Besides that, register the SHA-1 key on spotify dashboard

    private String AUTH_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent,1);

        Toast.makeText(getApplicationContext(), AUTH_TOKEN, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                AUTH_TOKEN = intent.getExtras().getString("AUTH_TOKEN");
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
