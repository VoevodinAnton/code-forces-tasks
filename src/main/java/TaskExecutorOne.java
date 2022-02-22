import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskExecutorOne {


    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Tasks tasks = TaskService.readTaskFromStream(inputStreamReader);

        Tasks tasksBacklog = new Tasks();
        Tasks completedTasks = new Tasks();
        Tasks remainingTasks = new Tasks(tasks);

        Task currentTask = tasks.iterator().next();
        for (int momentOfTime = 0; momentOfTime <= getMaxCount(tasks); momentOfTime++) {
            if (remainingTasks.isEmpty()){
                break;
            }
            //System.out.println(momentOfTime);
            if (TaskService.newTaskAppeared(momentOfTime, tasks)) {
                Task newTask = tasks.getTaskByIssuingTime(momentOfTime);
                if (currentTask == null) {
                    currentTask = newTask;
                    setStartTime(currentTask, momentOfTime);
                }
                if (newTask.getLeadTime() < newTask.getElapsedTime()) {
                    tasksBacklog.add(newTask);
                } else if ((currentTask.getElapsedTime() + newTask.getElapsedTime())
                        <= newTask.getLeadTime()) {
                    tasksBacklog.add(newTask);
                } else if (newTask.getElapsedTime() <= newTask.getLeadTime()) {
                    tasksBacklog.add(currentTask);
                    currentTask = newTask;
                    setStartTime(currentTask, momentOfTime);
                }
            }
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() == 0) {
                tasksBacklog.remove(currentTask);
                remainingTasks.remove(currentTask);
                currentTask.setEndTime(momentOfTime);
                completedTasks.add(currentTask);

                currentTask = TaskService.getActualFreshTask(momentOfTime, tasksBacklog);
                if (currentTask == null) {
                    currentTask = TaskService.getShortTask(tasksBacklog);
                }
                if (currentTask != null) {
                    if (currentTask.getStartTime() == 0){
                        currentTask.setStartTime(momentOfTime + 1);
                    }
                } else {
                    continue;
                }
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1);
            //System.out.println(currentTask);
        }

        List<Task> completedTaskList = new ArrayList<>(completedTasks);
        completedTaskList.sort(Comparator.comparing(Task::getId));

        System.out.println(completedTaskList.stream().filter(task ->
                task.getEndTime() <= task.getIssuingTime() + task.getLeadTime()).count());
        for (Task task: completedTaskList){
            System.out.println(task.getStartTime() + " " + task.getEndTime());
        }
    }

    public static int getMaxCount(Tasks tasks){
        return tasks.stream().map(task -> task.getIssuingTime() + task.getLeadTime()).reduce(0, Integer::sum);
    }

    public static void setStartTime(Task currentTask, int momentOfTime){
        if (currentTask.getStartTime() == 0){
            currentTask.setStartTime(momentOfTime + 1);
        }
    }
}
