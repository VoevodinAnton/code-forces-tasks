import java.io.*;
import java.util.*;

public class TaskService {

    public static Tasks readTaskFromStream(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> lines = new ArrayList<>();
        try{
            int numbersOfTasks = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < numbersOfTasks; i++) {
                lines.add(bufferedReader.readLine());
            }
        } catch (IOException ex){
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
        /*
        for (Task task: tasks){
            System.out.println(task);
        }
         */
        return tasks.stream().sorted(Comparator.reverseOrder()).min(Comparator.comparing(Task::getElapsedTime)).orElse(null);
    }

    public static Task getActualFreshTask(int momentOfTime, Tasks tasks) {
        /*
        for (Task task: tasks){
            System.out.println(task);
        }
         */
        return tasks.stream().sorted(Comparator.reverseOrder()).
                filter(task -> task.getIssuingTime() + task.getLeadTime() >= momentOfTime + task.getElapsedTime())
                .findFirst().orElse(null);
    }

    public static Task getActualShortTask(int momentOfTime, Tasks tasks) {
        return tasks.stream()
                .filter(task -> task.getIssuingTime() + task.getLeadTime() >= momentOfTime + task.getElapsedTime())
                .min(Comparator.comparing(Task::getElapsedTime)).orElse(null);
    }

    public static Task getFreshTask(Tasks tasks) {
        return tasks.stream().max(Comparator.naturalOrder()).orElse(null);
    }
}
