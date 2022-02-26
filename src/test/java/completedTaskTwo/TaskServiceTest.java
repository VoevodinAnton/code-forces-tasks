package completedTaskTwo;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import static org.testng.Assert.*;

public class TaskServiceTest {
    public static final String FILEPATH = "src/main/resources/task2/tasksExample1.txt";
    Tasks tasks;

    @BeforeTest
    public void setUp() throws FileNotFoundException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(FILEPATH));
        tasks = TaskService.readTaskFromStream(inputStreamReader);
    }

    @Test
    public void testGetActualShortTask() {
    }

    @Test
    public void testGetFreshTask() {
        System.out.println(TaskService.getFreshTask(tasks));
    }
}