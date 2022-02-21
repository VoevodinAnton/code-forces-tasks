import java.io.*;

public class TaskExecutorOne {
    public static final String FILEPATH = "src/main/resources/task1/tasksExample1.txt";


    public static void main(String[] args) throws FileNotFoundException {
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
        Tasks completedTasks = new Tasks();

        Task currentTask = tasks.iterator().next();
        for (int momentOfTime = 0; momentOfTime <= 134; momentOfTime++) {
            //System.out.println(momentOfTime);
            if (TaskService.newTaskAppeared(momentOfTime, tasks)) {
                Task newTask = tasks.getTaskByIssuingTime(momentOfTime);
                if (currentTask == null) {
                    currentTask = newTask;
                }
                if (newTask.getLeadTime() < newTask.getElapsedTime()) {
                    tasksBacklog.add(newTask);
                } else if ((currentTask.getElapsedTime() + newTask.getElapsedTime())
                        <= newTask.getLeadTime()) {
                    tasksBacklog.add(newTask);
                } else if (newTask.getElapsedTime() <= newTask.getLeadTime()) {
                    tasksBacklog.add(currentTask);
                    currentTask = newTask;
                }
            }
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() == 0) {
                tasksBacklog.remove(currentTask);
                currentTask.setEndTime(momentOfTime);
                //if (currentTask.getIssuingTime() + currentTask.getLeadTime() >= momentOfTime) {
                    completedTasks.add(currentTask);
                    System.out.println("In " + momentOfTime + " moment completed " + currentTask);
                //}
                currentTask = TaskService.getActualFreshTask(momentOfTime, tasksBacklog);
                if (currentTask == null) {
                    currentTask = TaskService.getShortTask(tasksBacklog);
                }
            }
            if (currentTask == null) {
                continue;
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1);
            //System.out.println(currentTask);
        }
    }
}
