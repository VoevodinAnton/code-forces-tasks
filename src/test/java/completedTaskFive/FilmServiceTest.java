package completedTaskFive;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.testng.Assert.*;

public class FilmServiceTest {
    public static final String FILEPATH = "src/main/resources/task5/filmsExample1";
    Films films;

    @BeforeMethod
    public void setUp() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        films = FilmService.readFilmsFromStream(inputStreamReader);
    }

    @Test
    public void testReadFilmsFromStream() {
        System.out.println(films);
    }

    @Test
    public void testIsInterestingFilm() {
        assertEquals(FilmService.isInterestingFilm(films.get(3), films), false);
    }

    @Test
    public void testGetInterestingFilm() {
        assertEquals(FilmService.getInterestingFilm(films.get(3), films), films.get(4));
    }

    @Test
    public void testGetNextFilm() {
        assertEquals(FilmService.getNextFilm(films.get(3), films), films.get(4));
    }
}