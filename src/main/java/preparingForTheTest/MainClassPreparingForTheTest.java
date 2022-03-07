package preparingForTheTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainClassPreparingForTheTest {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Trio<Student, Textbook, Tasks> object = Utils.readTextbookAndTasksFromStream(inputStreamReader);



        Student student = object.first;
        Textbook textbook = object.second;
        Tasks tasks = object.third;

        int numberOfTextbookHits = 0;
        List<Integer> completedTasks = new ArrayList<>();
        List<String> startThemes = new ArrayList<>();

        //long start = System.currentTimeMillis();
        for (Task task : tasks) {
            int initialCursor = textbook.getCursor();

            student.markThemes(task);

            student.leaveUnstudiedThemesInTask(textbook, task);

            if (task.isComplete()) {
                completedTasks.add(task.getId());
                continue;
            }
            student.readThemesRequiredForTask(textbook, task);
            if (task.isComplete()) {
                completedTasks.add(task.getId());
            }

            int finalCursor = textbook.getCursor();
            if (finalCursor > initialCursor) {
                numberOfTextbookHits++;
                startThemes.add(textbook.getThemes().get(initialCursor));
            }
        }

        printResult(textbook, completedTasks, startThemes, numberOfTextbookHits);
        /*
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("\n" + "Прошло времени, нс: " + elapsed);
         */
    }

    public static void printResult(Textbook textbook, List<Integer> completedTasks, List<String> startThemes, int numberOfTextbookHits){
        StringBuilder result = new StringBuilder();
        result.append(textbook.getCursor()).append(" ").append(numberOfTextbookHits).append("\n");
        for(String startTheme: startThemes){
            result.append(startTheme).append("\n");
        }
        result.append(completedTasks.size()).append("\n");
        for (Integer completedTask: completedTasks){
            result.append(completedTask).append(" ");
        }
        System.out.print(result);
    }
}

class Textbook {
    private int cursor = 0;
    private final int size;
    private final List<String> themes;

    public Textbook(List<String> themes) {
        this.themes = new ArrayList<>(themes);
        this.size = themes.size();
    }

    public int getCursor() {
        return cursor;
    }

    public int getSize() {
        return size;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void increaseCursor() {
        cursor++;
    }

    @Override
    public String toString() {
        return "Textbook{" +
                "themes=" + themes +
                '}';
    }
}

class Task {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private final int id;
    private final List<String> themesToStudy;

    public Task(List<String> themesToStudy) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.themesToStudy = new ArrayList<>(themesToStudy);
    }

    public int getId() {
        return id;
    }

    public List<String> getThemesToStudy() {
        return themesToStudy;
    }

    public boolean isComplete() {
        return themesToStudy.isEmpty();
    }

    @Override
    public String toString() {
        return "Task{" +
                "themesToStudy=" + themesToStudy +
                '}';
    }
}

class Tasks extends ArrayList<Task> { }

class Student {
    private final int k;
    private final int m;
    private final Map<String, Integer> markedThemes;

    public Student(int k, int m) {
        this.k = k;
        this.m = m;
        this.markedThemes = new HashMap<>();
    }

    public Map<String, Integer> getMarkedThemes() {
        return markedThemes;
    }

    public void markThemes(Task task) {
        List<String> themesToStudy = task.getThemesToStudy();
        for (String s : themesToStudy) {
            if (markedThemes.containsKey(s)) {
                markedThemes.put(s, markedThemes.get(s) + 1);
            } else {
                markedThemes.put(s, 1);
            }
        }
    }

    public void readThemesRequiredForTask(Textbook textbook, Task task) {
        int initialCursor = textbook.getCursor();
        List<String> themesToStudy = task.getThemesToStudy().stream().filter(theme -> markedThemes.get(theme) >= k).collect(Collectors.toList());
        if (!themesToStudy.isEmpty()) {
            loop:
            for (int i = initialCursor; i < initialCursor + m; i++) {
                if (i >= textbook.getSize()){
                    break;
                }
                textbook.increaseCursor();
                String themeInTextbook = textbook.getThemes().get(i);
                for (String theme : themesToStudy) {
                    if (themeInTextbook.equals(theme)) {
                        task.getThemesToStudy().remove(theme);
                        break loop;
                    }
                }
            }
        }
    }

    public void leaveUnstudiedThemesInTask(Textbook textbook, Task task) {
        task.getThemesToStudy().removeAll(textbook.getThemes().subList(0, textbook.getCursor()));
    }

    @Override
    public String toString() {
        return "Student{" +
                "k=" + k +
                ", m=" + m +
                '}';
    }
}

class Utils {
    public static Trio<Student, Textbook, Tasks> readTextbookAndTasksFromStream(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Student student = null;
        int countOfTasks = 0;
        try {
            String[] lines = bufferedReader.readLine().split(" ");
            countOfTasks = Integer.parseInt(lines[0]);
            student = new Student(Integer.parseInt(lines[2]), Integer.parseInt(lines[3]));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Textbook textbook = null;
        try {
            String[] lines = bufferedReader.readLine().split(" ");
            textbook = new Textbook(Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Tasks tasks = new Tasks();
        try {
            for (int i = 0; i < countOfTasks; i++) {
                String[] lines = bufferedReader.readLine().substring(2).split(" ");
                tasks.add(new Task(Arrays.asList(lines)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new Trio<>(student, textbook, tasks);
    }
}

class Trio<T1, T2, T3> {
    public T1 first;
    public T2 second;
    public T3 third;

    public Trio(T1 _first, T2 _second, T3 _third) {
        first = _first;
        second = _second;
        third = _third;
    }
}

