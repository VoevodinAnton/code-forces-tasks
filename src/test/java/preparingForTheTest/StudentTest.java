package preparingForTheTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.testng.Assert.*;

public class StudentTest {
    public static final String FILEPATH = "src/main/resources/preparing-for-the-test/Example1.txt";

    Student student;
    Textbook textbook;
    Tasks tasks;

    @BeforeMethod
    public void setUp() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Trio<Student, Textbook, Tasks> object = Utils.readTextbookAndTasksFromStream(inputStreamReader);
        student = object.first;
        textbook = object.second;
        tasks = object.third;
    }


    @Test
    public void testMarkThemes() {
        for (Task task : tasks) {
            student.markThemes(task);
            System.out.println(student.getMarkedThemes() + "\n");
        }
    }

    @Test
    public void testLeaveUnstudiedThemesInTask() {
        for (int i = 0; i < 3; i++ ){
            textbook.increaseCursor();
        }
        System.out.println(textbook.getThemes().subList(0, textbook.getCursor()) + "\n");

        for (Task task : tasks) {
            student.leaveUnstudiedThemesInTask(textbook, task);
            System.out.println(task + "\n");
        }
    }

    @Test
    public void testReadThemesRequiredForTask() {
    }


}