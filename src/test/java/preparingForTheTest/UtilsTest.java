package preparingForTheTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.testng.Assert.*;

public class UtilsTest {
    public static final String FILEPATH = "src/main/resources/preparing-for-the-test/Example1.txt";

    Student student;
    Textbook textbook;
    Tasks tasks;

    @BeforeMethod
    public void setUp() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        Trio<Student, Textbook, Tasks> object = Utils.readTextbookAndTasksFromStream(inputStreamReader);
        student = object.first;
        textbook = object.second;
        tasks = object.third;
    }

    @Test
    public void testReadTextbookAndTasksFromStream() {
        System.out.println(student);
        System.out.println(textbook);
        System.out.println(tasks);
    }
}