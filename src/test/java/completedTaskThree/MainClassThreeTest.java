package completedTaskThree;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class MainClassThreeTest {
    public static final String FILEPATH = "src/main/resources/task3/tasksExample1.txt";
    Tasks tasks;

    @BeforeTest
    public void setUp() throws FileNotFoundException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(FILEPATH));
        tasks = TaskService.readTaskFromStream(inputStreamReader);
    }

    @Test
    public void testShouldExecute() {

    }
}