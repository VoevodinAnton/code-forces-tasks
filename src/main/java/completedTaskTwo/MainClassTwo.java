package completedTaskTwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClassTwo {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Tasks tasks = TaskService.readTaskFromStream(inputStreamReader);

        if (tasks.isEmpty()) {
            printResult(Collections.EMPTY_LIST);
            return;
        }

        Tasks tasksBacklog = new Tasks();
        Tasks remainingTasks = new Tasks(tasks);
        List<Integer> countOfIssuedTasksAtEachPointOfTime = new ArrayList<>();

        Task currentTask = null;
        int initialMoment = tasks.iterator().next().getIssuingTime();
        int maxCount = getMaxCount(tasks);
        for (int momentOfTime = initialMoment; momentOfTime <= maxCount; momentOfTime++) {
            if (remainingTasks.isEmpty()) {
                break;
            }
            //System.out.println(momentOfTime);
            Task newTask = tasks.getTaskByIssuingTime(momentOfTime);
            if (newTask != null) {
                if (currentTask == null) {
                    currentTask = newTask;
                }
                tasksBacklog.add(newTask);
            }
            countOfIssuedTasksAtEachPointOfTime.add(tasksBacklog.size());
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() == 0) {
                tasksBacklog.remove(currentTask);
                remainingTasks.remove(currentTask);
                currentTask = getTaskWhenCurrentTaskIsCompleted(momentOfTime, tasksBacklog);
            }
            if (currentTask == null) {
                continue;
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1);
        }
        printResult(countOfIssuedTasksAtEachPointOfTime);
    }

    public static int getMaxCount(Tasks tasks) {
        return tasks.stream().map(task -> task.getIssuingTime() + task.getLeadTime() + task.getElapsedTime()).reduce(0, Integer::sum);
    }


    public static Task getTaskWhenCurrentTaskIsCompleted(int momentOfTime, Tasks tasksBacklog) {

        Task currentTask = TaskService.getActualShortTask(momentOfTime, tasksBacklog);
        if (currentTask == null) {
            currentTask = TaskService.getFreshTask(tasksBacklog);
        }
        return currentTask;
    }

    public static void printResult(List<Integer> countOfIssuedTasksAtEachPointOfTime) {
        if (countOfIssuedTasksAtEachPointOfTime.isEmpty()) {
            System.out.println(0);
            return;
        }
        int maxCountIssuedTasks = Collections.max(countOfIssuedTasksAtEachPointOfTime);
        long time = countOfIssuedTasksAtEachPointOfTime.stream().filter(count -> count == maxCountIssuedTasks).count();

        System.out.println(maxCountIssuedTasks);
        System.out.println(time);
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

    public int getLeadTime() {
        return leadTime;
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
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Tasks tasks = new Tasks();
        for (String line : lines) {
            String[] time = line.split(" ");
            tasks.add(new Task(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2])));
        }

        return tasks;
    }

    public static Task getActualShortTask(int momentOfTime, Tasks tasks) {
        Task actualShortTask = tasks.stream()
                .filter(task -> task.getIssuingTime() + task.getLeadTime() >= momentOfTime + task.getElapsedTime())
                .min(Comparator.comparing(Task::getElapsedTime)).orElse(null);

        if (actualShortTask != null) {
            Task actualShortTask1 = tasks.stream()
                    .filter(task -> task.getIssuingTime() + task.getLeadTime() >= momentOfTime + task.getElapsedTime())
                    .filter(task -> task.getElapsedTime() == actualShortTask.getElapsedTime())
                    .min(Comparator.comparingInt(task -> task.getIssuingTime() + task.getLeadTime())).orElse(null);

            if (actualShortTask1 != null) {
                return tasks.stream()
                        .filter(task -> task.getIssuingTime() + task.getLeadTime() >= momentOfTime + task.getElapsedTime())
                        .filter(task -> task.getElapsedTime() == actualShortTask.getElapsedTime())
                        .filter(task -> task.getIssuingTime() + task.getLeadTime() == actualShortTask1.getIssuingTime() + actualShortTask1.getLeadTime())
                        .min(Comparator.comparingInt(task -> task.getIssuingTime() + task.getElapsedTime())).orElse(null);
            }
        }
        return null;
    }

    public static Task getFreshTask(Tasks tasks) {
        return tasks.stream().max(Comparator.naturalOrder()).orElse(null);
    }
}


class Tasks extends HashSet<Task> {
    public Tasks(HashSet<Task> tasks) {
        super(tasks);
    }

    public Tasks() {
    }

    public Task getTaskByIssuingTime(int momentOfTime) {
        return this.stream().filter(task -> task.getIssuingTime() == momentOfTime).findFirst().orElse(null);
    }
}
