import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Task implements Comparable<Task> {
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
