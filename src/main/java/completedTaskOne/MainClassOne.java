package completedTaskOne;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClassOne {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Tasks tasks = TaskService.readTaskFromStream(inputStreamReader);

        if (tasks.isEmpty()){
            return;
        }

        Tasks tasksBacklog = new Tasks();
        Tasks completedTasks = new Tasks();
        Tasks remainingTasks = new Tasks(tasks);

        Task currentTask = null;
        for (int momentOfTime = 0; momentOfTime <= getMaxCount(tasks); momentOfTime++) {
            if (remainingTasks.isEmpty()) {
                break;
            }
            //System.out.println(momentOfTime);
            if (TaskService.newTaskAppeared(momentOfTime, tasks)) {
                currentTask = getTaskWhenNewTaskAppears(tasks, tasksBacklog, momentOfTime, currentTask);
            }
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() == 0) {
                tasksBacklog.remove(currentTask);
                remainingTasks.remove(currentTask);
                setEndTime(currentTask, momentOfTime);
                completedTasks.add(currentTask);

                currentTask = getTaskWhenCurrentTaskIsCompleted(momentOfTime, tasksBacklog);
            }
            if (currentTask == null) {
                continue;
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1);
            //System.out.println(currentTask);
        }
        printResult(completedTasks);
    }

    public static Task getTaskWhenNewTaskAppears(Tasks tasks, Tasks tasksBacklog, int momentOfTime, Task currentTask) {
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
        return currentTask;
    }

    public static Task getTaskWhenCurrentTaskIsCompleted(int momentOfTime, Tasks tasksBacklog) {
        Task currentTask = TaskService.getActualFreshTask(momentOfTime, tasksBacklog);
        if (currentTask == null) {
            currentTask = TaskService.getShortTask(tasksBacklog);
        }
        if (currentTask != null) {
            setStartTime(currentTask, momentOfTime);
        }
        return currentTask;
    }

    public static int getMaxCount(Tasks tasks) {
        return tasks.stream().map(task -> task.getIssuingTime() + task.getLeadTime()).reduce(0, Integer::sum);
    }

    public static void setStartTime(Task currentTask, int momentOfTime) {
        if (currentTask.getStartTime() == 0) {
            currentTask.setStartTime(momentOfTime + 1);
        }
    }

    public static void setEndTime(Task currentTask, int momentOfTime) {
        if (currentTask.getStartTime() > momentOfTime) {
            currentTask.setEndTime(momentOfTime + 1);
        } else {
            currentTask.setEndTime(momentOfTime);
        }
    }

    public static void printResult(Tasks completedTasks){
        List<Task> completedTaskList = new ArrayList<>(completedTasks);
        completedTaskList.sort(Comparator.comparing(Task::getId));

        System.out.println(completedTaskList.stream().filter(task ->
                task.getEndTime() <= task.getIssuingTime() + task.getLeadTime()).count());
        for (Task task : completedTaskList) {
            System.out.println(task.getStartTime() + " " + task.getEndTime());
        }
    }
}

class Task implements Comparable<Task> {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private int id;
    private int issuingTime;
    private int leadTime;
    private int elapsedTime;
    private int startTime = 0;
    private int endTime = 0;

    public Task(int issuingTime, int leadTime, int elapsedTime) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.issuingTime = issuingTime;
        this.leadTime = leadTime;
        this.elapsedTime = elapsedTime;
    }

    public int getId() {
        return id;
    }

    public int getIssuingTime() {
        return issuingTime;
    }

    public void setIssuingTime(int issuingTime) {
        this.issuingTime = issuingTime;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", issuingTime=" + issuingTime +
                ", leadTime=" + leadTime +
                ", elapsedTime=" + elapsedTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return this.getIssuingTime() - o.getIssuingTime();
    }
}

class TaskService {

    public static Tasks readTaskFromStream(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> lines = new ArrayList<>();
        try {
            int numbersOfTasks = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < numbersOfTasks; i++) {
                lines.add(bufferedReader.readLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Tasks tasks = new Tasks();
        for (String line : lines) {
            String[] time = line.split(" ");
            tasks.add(new Task(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2])));
        }

        return tasks == null ? (Tasks) Collections.EMPTY_LIST : tasks;
    }

    public static boolean newTaskAppeared(int momentOfTime, Tasks tasks) {
        return tasks.stream().anyMatch(task -> momentOfTime == task.getIssuingTime());
    }

    public static Task getShortTask(Tasks tasks) {
        return tasks.stream().sorted(Comparator.reverseOrder()).min(Comparator.comparing(Task::getElapsedTime)).orElse(null);
    }

    public static Task getActualFreshTask(int momentOfTime, Tasks tasks) {
        return tasks.stream().sorted(Comparator.reverseOrder()).
                filter(task -> task.getIssuingTime() + task.getLeadTime() >= momentOfTime + task.getElapsedTime())
                .findFirst().orElse(null);
    }
}


class Tasks extends HashSet<Task> {

    public Tasks(HashSet<Task> tasks) {
        super(tasks);
    }

    public Tasks() {
    }

    public Task getTaskByIssuingTime(int momentOfTime) {
        for (Task task : this) {
            if (task.getIssuingTime() == momentOfTime) {
                return task;
            }
        }
        return null;
    }
}