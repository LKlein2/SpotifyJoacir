package com.example.spotifyjoacir;

import java.util.ArrayList;


public class ConsultaActivity {

    private static ConsultaActivity listManager;

    private ArrayList<TopArtist> topArtists;
    private ArrayList<TopTrack> topTracks;


    public static ConsultaActivity getInstance(){
        if(listManager == null){
            listManager = new ConsultaActivity();
        }

        return listManager;
    }

    private ConsultaActivity(){
        topArtists = new ArrayList<>();
        topTracks = new ArrayList<>();
    }

    public ArrayList<TopTrack> getTopTracks(){
        return topTracks;
    }

    public void addTopTrack(TopTrack track){

        topTracks.add(track);
    }

    public void addTopArtist(TopArtist artist){
        topArtists.add(artist);
    }

    public ArrayList<TopArtist> getTopArtists(){
        return topArtists;
    }



}
