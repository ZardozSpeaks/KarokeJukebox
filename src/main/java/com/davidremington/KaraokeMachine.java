package com.davidremington;

import com.davidremington.model.Song;
import com.davidremington.model.SongBook;
import com.davidremington.model.SongRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KaraokeMachine {
    private SongBook mSongBook;
    private BufferedReader mReader;
    private Queue<SongRequest> mSongQueue;
    private Map<String, String> mMenu;


    public KaraokeMachine(SongBook songBook) {
        this.mSongBook = songBook;
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mSongQueue = new ArrayDeque<>();
        mMenu = new HashMap<>();
        mMenu.put("add", "Add a new song to the song book");
        mMenu.put("play", "Add a new song to the song book");
        mMenu.put("choose", "Chose a song to sing");
        mMenu.put("quit", "Give up. Exit the program");
    }

    private String promptAction() throws IOException{
        System.out.printf("There are %d songs available and %d in the queue. Your options are: %n",
                            mSongBook.getSongCount(),
                            mSongQueue.size());
        for (Map.Entry<String, String> option : mMenu.entrySet()) {
            System.out.printf("%s - %s %n",
                                option.getKey(),
                                option.getValue());
        }
        System.out.print("What do you want to do: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    public void run() {
        String choice = "";
        do {
            try {
                choice = promptAction();
                switch(choice) {
                    case "add":
                        Song song = promptNewSong();
                        mSongBook.addSong(song);
                        System.out.printf("%s added!  %n%n", song);
                        break;
                    case "choose":
                        String singerName = promptForSingerName();
                        String artist = promptForArtist();
                        Song artistSong = promptSongForArtist(artist);
                        SongRequest songRequest = new SongRequest(singerName, artistSong);
                        if (mSongQueue.contains(songRequest)) {
                            System.out.printf("%n%n Whoops %s already requested %s!%n",
                                                singerName,
                                                artistSong);
                                                break;
                        }
                        mSongQueue.add(songRequest);
                        System.out.printf("You chose: %s %n", artistSong);
                        break;
                    case "play":
                        playNext();
                        break;
                    case "quit":
                        System.out.println("Thanks for playing!");
                        break;
                    default:
                        System.out.printf("Unknown choice: '%s'. Try again. %n%n%n", choice);
                }
            } catch(IOException ioe){
                System.out.println("Problem with input");
                ioe.printStackTrace();
            }
        } while(!choice.equals("quit"));
    }

    private String promptForSingerName() throws IOException {
        System.out.println("Enter the singer's name");
        return mReader.readLine();
    }

    private Song promptNewSong() throws IOException {
        System.out.print("Enter the artist's name: ");
        String artist = mReader.readLine();
        System.out.print("Enter the title: ");
        String title = mReader.readLine();
        System.out.print("Enter the video URL: ");
        String videoUrl = mReader.readLine();
        return new Song(artist, title, videoUrl);
    }

    private String promptForArtist() throws IOException {
        System.out.println("Available artists:");
        List<String> artists = new ArrayList<>(mSongBook.getArtists());
        int index = promptForIndex(artists);
        return artists.get(index);
    }

    private Song promptSongForArtist(String artist) throws IOException {
        List<Song> songs = mSongBook.getSongsForArtist(artist);
        List<String> songTitles = new ArrayList<>();
        for(Song song : songs) {
            songTitles.add(song.getTitle());
        }
        System.out.printf("Available songs for %s: %n", artist);
        int index = promptForIndex(songTitles);
        return songs.get(index);
    }

    private int promptForIndex(List<String> options) throws IOException {
        int counter =1;
        for (String option : options) {
            System.out.printf("%d.)  %s %n", counter, option );
            counter ++;
        }
        String optionAsString = mReader.readLine();
        System.out.println("Your choice:  ");
        int choice = Integer.parseInt(optionAsString.trim());
        return choice -1;
    }

    public void playNext() {
        SongRequest songRequest = mSongQueue.poll();
        if (songRequest == null) {
            System.out.println("Sorry there are no songs in the queue. Use choose" +
                                "from the menu to add some");
        } else {
            Song song = songRequest.getSong();
            System.out.printf("%n%n%n Ready %s? Open %s to hear %s by %s %n%n%n",
                                songRequest.getSingerName(),
                                song.getVideoUrl(),
                                song.getTitle(),
                                song.getArtist());
        }
    }
}
