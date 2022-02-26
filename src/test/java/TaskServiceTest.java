import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskServiceTest {
    public static final String FILEPATH = "src/main/resources/task1/tasksExample1.txt";
    Tasks tasks;

    @BeforeTest
    public void setUp() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        tasks = TaskService.readTaskFromStream(inputStreamReader);
    }

    @org.testng.annotations.Test
    public void testReadTaskFromFile() {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    @Test
    public void testNewTaskAppeared() {
        Assert.assertEquals(true, TaskService.newTaskAppeared(0, tasks));
        Assert.assertEquals(false, TaskService.newTaskAppeared(1, tasks));
        Assert.assertEquals(true, TaskService.newTaskAppeared(5, tasks));
        Assert.assertEquals(true, TaskService.newTaskAppeared(72, tasks));
        Assert.assertEquals(false, TaskService.newTaskAppeared(40, tasks));
    }

    @Test
    public void testGetShortTask() {
        Assert.assertEquals(TaskService.getShortTask(tasks), tasks.stream().toArray()[3]);
    }

    @Test
    public void testGetActualFreshTask() {
        Assert.assertEquals(TaskService.getActualFreshTask(40, tasks), tasks.stream().toArray()[9]);
    }

    @Test
    public void testGetActualShortTask() {
        Assert.assertEquals(TaskService.getActualShortTask(50, tasks), tasks.stream().toArray()[7]);
    }

    @Test
    public void testGetFreshTask() {
        Assert.assertEquals(TaskService.getFreshTask(tasks), tasks.stream().toArray()[9]);
    }
}