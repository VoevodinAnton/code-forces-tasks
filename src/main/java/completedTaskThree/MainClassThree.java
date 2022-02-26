package completedTaskThree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClassThree {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Tasks tasks = TaskService.readTaskFromStream(inputStreamReader);

        if (tasks.isEmpty()) {
            printResult(null);
            return;
        }

        Tasks tasksBacklog = new Tasks();
        Tasks completedTasks = new Tasks();
        Tasks remainingTasks = new Tasks(tasks);
        Task currentTask = null;
        int initialMoment = tasks.iterator().next().getIssuingTime();
        int maxCount = getMaxCount(tasks);

        for (int momentOfTime = initialMoment; momentOfTime < maxCount; momentOfTime++) {
            if (remainingTasks.isEmpty()) {
                break;
            }
            if (TaskService.newTaskAppeared(momentOfTime, tasks)) {
                Task newTask = tasks.getTaskByIssuingTime(momentOfTime);
                if (shouldExecute(newTask, tasksBacklog)) {
                    if (currentTask == null) {
                        currentTask = newTask;
                    } else {
                        tasksBacklog.add(newTask);
                    }
                } else {
                    remainingTasks.remove(newTask);
                }
            }
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() <= 0) {
                completedTasks.add(currentTask);
                tasksBacklog.remove(currentTask);
                remainingTasks.remove(currentTask);
                currentTask = getTaskWhenCurrentTaskIsCompleted(tasksBacklog);
            }
            if (currentTask == null) {
                continue;
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1);
        }
        printResult(completedTasks);
    }

    public static int getMaxCount(Tasks tasks) {
        return tasks.stream().map(task -> task.getIssuingTime() + task.getLeadTime() + task.getElapsedTime()).reduce(0, Integer::sum);
    }

    public static boolean shouldExecute(Task newTask, Tasks backlog) {
        int sumElapsedTime = backlog.stream().mapToInt(Task::getElapsedTime).sum() + newTask.getElapsedTime();
        return newTask.getLeadTime() >= sumElapsedTime;
    }

    public static Task getTaskWhenCurrentTaskIsCompleted(Tasks backlog) {
        if (backlog == null || backlog.isEmpty()) {
            return null;
        }
        return TaskService.getEarlyTask(backlog);
    }

    public static void printResult(Tasks completedTasks) {
        if (completedTasks == null || completedTasks.isEmpty()) {
            System.out.println(0);
            return;
        }
        System.out.println(completedTasks.size());
        for (Task task : completedTasks) {
            System.out.print(task.getId() + " ");
        }
    }
}

class Task implements Comparable<Task> {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private int id;
    private int issuingTime;
    private int leadTime;
    private int elapsedTime;

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
            System.out.println(0);
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

    public static Task getEarlyTask(Tasks tasks) {
        return tasks.stream().min(Comparator.naturalOrder()).orElse(null);
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