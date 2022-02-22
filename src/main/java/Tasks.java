import java.util.HashSet;

public class Tasks extends HashSet<Task> {

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
