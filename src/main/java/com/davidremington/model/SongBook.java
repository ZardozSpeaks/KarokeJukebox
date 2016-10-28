package com.davidremington.model;

import java.util.*;

public class SongBook {
    private List<Song> mSongs;

    public SongBook() {
        this.mSongs = new ArrayList<Song>();
    }

    public void addSong(Song song) {
        mSongs.add(song);
    }

    public int getSongCount() {
        return mSongs.size();
    }

    private Map<String, List<Song>> byArtist() {
        Map<String, List<Song>> byArtist = new HashMap<>();
        for (Song song : mSongs) {
            List<Song> artistSongs = byArtist.get(song.getArtist());
            if (artistSongs == null) {
                artistSongs = new ArrayList<>();
                byArtist.put(song.getArtist(), artistSongs);
            }
            artistSongs.add(song);
        }
        return byArtist;
    }

    public Set<String> getArtists() {
        return byArtist().keySet();
    }

    public List<Song> getSongsForArtist(String artistName) {
        return byArtist().get(artistName);
    }
}
