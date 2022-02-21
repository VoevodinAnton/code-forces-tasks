import java.util.HashSet;

public class Tasks extends HashSet<Task> {

    /*
    public Task getNext(Task task) {
        Iterator<Task> tasks = this.iterator();
        while (tasks.hasNext()) {
            Task target = tasks.next();
            if (target.equals(task)) {
                return tasks.hasNext() ? tasks.next() : null;
            }
        }
        return null;
    }

     */

    public Task getTaskByIssuingTime(int momentOfTime) {
        for (Task task : this) {
            if (task.getIssuingTime() == momentOfTime) {
                return task;
            }
        }
        return null;
    }
}
