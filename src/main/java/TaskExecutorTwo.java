import java.io.*;

public class TaskExecutorTwo {
    public static final String FILEPATH = "src/main/resources/task2/tasksExample1.txt";

    public static void main(String[] args) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        Tasks tasks = null;
        try {
            tasks = TaskService.readTaskFromStream(inputStreamReader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Tasks tasksBacklog = new Tasks();

        Task currentTask = tasks.iterator().next();
        for (int momentOfTime = 0; momentOfTime <= 134; momentOfTime++) {
            System.out.println(momentOfTime);
            if (TaskService.newTaskAppeared(momentOfTime, tasks)) {
                Task newTask = tasks.getTaskByIssuingTime(momentOfTime);
                if (currentTask == null) {
                    currentTask = newTask;
                }
                tasksBacklog.add(newTask);
            }
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() == 0) {
                tasksBacklog.remove(currentTask);
                currentTask = TaskService.getActualShortTask(momentOfTime, tasksBacklog);
                if (currentTask == null) {
                    currentTask = TaskService.getFreshTask(tasksBacklog);
                }
            }
            if (currentTask == null) {
                continue;
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1);
            System.out.println(currentTask);
        }

    }
}
