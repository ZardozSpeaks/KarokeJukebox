import com.davidremington.KaraokeMachine;
import com.davidremington.model.SongBook;

public class Karaoke {

    public static void main(String[] args){
        SongBook songBook = new SongBook();
        songBook.importFrom("songs.txt");
        KaraokeMachine machine = new KaraokeMachine(songBook);
        machine.run();
        System.out.println("Saving book...");
        songBook.exportTo("songs.txt");
    }
}
