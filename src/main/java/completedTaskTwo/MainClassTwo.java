package completedTaskTwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*Евлампий и третий сон о сессии*/
public class MainClassTwo {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Tasks tasks = TaskService.readTaskFromStream(inputStreamReader);

        if (tasks.isEmpty()) {
            printResult(Collections.EMPTY_LIST);
            return;
        }

        Tasks tasksBacklog = new Tasks(); //таски, которые Евлампий отложил
        Tasks remainingTasks = new Tasks(tasks); //оставшиеся задания; в начальный момент времени это все таски
        List<Integer> countOfIssuedTasksAtEachPointOfTime = new ArrayList<>(); //количество выданных, но еще не
                                                                                // выполненных заданий в каждый момент времени
        Task currentTask = null;
        int initialMoment = tasks.iterator().next().getIssuingTime(); //начальный момент времени; соответствует времени выдачи первой таски
        int maxCount = getMaxCount(tasks);
        for (int momentOfTime = initialMoment; momentOfTime <= maxCount; momentOfTime++) {
            if (remainingTasks.isEmpty()) { //условие выхода из цикла; выполнится, когда закончатся таски к выполнению
                break;
            }

            Task newTask = tasks.getTaskByIssuingTime(momentOfTime); //новая таска, соответствующая определенному моменту времени; по условию гаранттируется, что в один момент времмени  может появиться одна таска
            if (newTask != null) {
                if (currentTask == null) {
                    currentTask = newTask;
                }
                tasksBacklog.add(newTask); //по условию, при появлении таски Евлампий ее откладывает, так как хочет закончить текущую таску
            }
            countOfIssuedTasksAtEachPointOfTime.add(tasksBacklog.size()); //записываем количество отложенных заданий в каждый момет времени
            if (currentTask == null) {
                continue;
            }
            if (currentTask.getElapsedTime() == 0) {  //условие выполнения таски
                tasksBacklog.remove(currentTask); //задача выполнена, поэтому удаляем из списка отложенных
                remainingTasks.remove(currentTask); //также удаляем из списка оставшихся
                currentTask = getTaskWhenCurrentTaskIsCompleted(momentOfTime, tasksBacklog); //теперь текущей таской будет та, которая соответствует заданию
            }
            if (currentTask == null) {
                continue;
            }
            currentTask.setElapsedTime(currentTask.getElapsedTime() - 1); //в каждый момент времени, уменьшаем на единицу время выполнения таски
        }
        printResult(countOfIssuedTasksAtEachPointOfTime);
    }

    //маскимальное количество итераций цикла
    public static int getMaxCount(Tasks tasks) {
        return tasks.stream().map(task -> task.getIssuingTime() + task.getLeadTime() + task.getElapsedTime()).reduce(0, Integer::sum);
    }


    public static Task getTaskWhenCurrentTaskIsCompleted(int momentOfTime, Tasks tasksBacklog) {

        return TaskService.getActualShortTask(momentOfTime, tasksBacklog);
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
    private int id; //номер таски
    private int issuingTime; //момент времени, в которое было выдано задание
    private int leadTime; //время, которое давалось на его выполнение
    private int elapsedTime; //время, за которое это задание может выполнить Евлампий

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
    public int compareTo(Task o) { //таски сравниваются по времени выдачи
        return this.getIssuingTime() - o.getIssuingTime();
    }
}

class TaskService {
    /*чтение из консоли и заполние объектов данными*/
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

        /*
        Если среди заданий с наименьшим временем выполнения окажется такое, которое можно успеть сдать в срок, Евлампий выберет это задание.
        Если таких заданий окажется более одного, Евлампий будет делать то задание, срок сдачи которого наступит раньше.
        Если и в этом случае найдётся более одного задания, из которых можно выбрать, он выберет то, которое было выдано раньше.
         */
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
