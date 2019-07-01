package com.example.spotifyjoacir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements ConnectionStateCallback
{
    public TextView txtA1, txtA2, txtA3, txtA4, txtA5;
    public ImageView imgA1, imgA2, imgA3, imgA4, imgA5;

    private static final String TAG = "Spotify MainActivity";
    private static final String CLIENT_ID = "1d7edefde74f45beb393a66257a0ba49";     //This should be generate by spotify dashboard
    private static final String REDIRECT_URI = "http://localhost:8888/callback";    //you should register it on spotify dashboard
    private SpotifyAppRemote mSpotifyAppRemote;                                     //Besides that, register the SHA-1 key on spotify dashboard
    public static SpotifyPlayer mPlayer;

    private String AUTH_TOKEN;
    public static SpotifyService spotifyService;
    private SpotifyApi api = new SpotifyApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doInitialize();

        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent,1);
        onAuthenticationComplete(AUTH_TOKEN);

        api.setAccessToken(AUTH_TOKEN);
    }

    public void getMyTopArtist(){

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 5);
        SpotifyError spotifyError;
        //api.setAccessToken("BQBbWy2YcvbaHuob7iKkmeJrLsj7dYmJPCQ-djtihWEJx2nAW0BHAnx1uQWO4ELJFrn92tSHTvUMt4L9F9CxQnTdwdgIjVD87z3LsooiWRpPF3rFcELCf9LpGwayQnZUN1rOkz_thN-OILk1ENYqqhRtsbX8yJbM7LRn8A");
        //api.setAccessToken("BQAYfasnpuCmnsEIf4FFAtUmZJUJImOFocH7JvlCW7y74HpmwRlSbofi0Bk9zJ81Mtqy9DfIVPgcPxkNRcYQfJWs3UUswnV_GG38vrrdhnh1buCKWDa0Exr9JlcZbewBO6N0G99L35JCR_peXszxkPtkUSES_n0pZpl8Y4T_ALcB00gZLA");
        api.setAccessToken(AUTH_TOKEN);
        spotifyService = api.getService();
        spotifyService.getTopArtists(options, new SpotifyCallback<Pager<Artist>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.d("SearchPager", spotifyError.toString());
            }

            @Override
            public void success(Pager<Artist> artistPager, Response response) {
                int i = 0;
                List<Artist> mList = artistPager.items;

                for(Artist art : mList){
                    if (i == 0) {
                        txtA1.setText("1. " + art.name);
                        new DownloadImageTask(imgA1).execute(art.images.get(1).url);
                    } else if (i == 1) {
                        txtA2.setText("2. " + art.name);
                        new DownloadImageTask(imgA2).execute(art.images.get(1).url);
                    } else if (i == 2) {
                        txtA3.setText("3. " + art.name);
                        new DownloadImageTask(imgA3).execute(art.images.get(1).url);
                    } else if (i == 3) {
                        txtA4.setText("4. " + art.name);
                        new DownloadImageTask(imgA4).execute(art.images.get(1).url);
                    } else if (i == 4) {
                        txtA5.setText("5. " + art.name);
                        new DownloadImageTask(imgA5).execute(art.images.get(1).url);
                    }
                    i++;
                }
            }
        });
    }


    private void onAuthenticationComplete(final String auth_token) {

        Log.d(TAG,"Got authentication token");

        if(mPlayer == null)
        {
            Config playerConfig = new Config(this, auth_token, LoginActivity.CLIENT_ID);

            Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer spotifyPlayer) {
                    Log.d(TAG,"-- Player initialized --");
                    mPlayer = spotifyPlayer;
                    mPlayer.addConnectionStateCallback(MainActivity.this);

                    Log.d(TAG, "AccessToken: " + auth_token);
                    setServiceAPI();
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
                }
            });
        } else {
            mPlayer.login(auth_token);
        }

    }

    private void setServiceAPI(){
        Log.d(TAG, "Setting Spotify API Service");
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(AUTH_TOKEN);

        spotifyService = api.getService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                AUTH_TOKEN = intent.getExtras().getString("AUTH_TOKEN");
                getMyTopArtist();
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d(TAG, "Received connection message: " + message);
    }

    private void doInitialize() {
        txtA1 = findViewById(R.id.txtArtist1);
        txtA2 = findViewById(R.id.txtArtist2);
        txtA3 = findViewById(R.id.txtArtist3);
        txtA4 = findViewById(R.id.txtArtist4);
        txtA5 = findViewById(R.id.txtArtist5);

        imgA1 = findViewById(R.id.imgArtist1);
        imgA2 = findViewById(R.id.imgArtist2);
        imgA3 = findViewById(R.id.imgArtist3);
        imgA4 = findViewById(R.id.imgArtist4);
        imgA5 = findViewById(R.id.imgArtist5);
    }

}
