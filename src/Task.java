import java.util.Objects;

public class Task {

    private String description; // переменная для хранния описания
    private String taskName; // переменная для храниения задач
    private StatusTask statusTask = StatusTask.NEW;
    private int idNumber;// переменная для создания уникального номера задачи

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void updateStatus(StatusTask newStatusTask) {
        this.statusTask = newStatusTask;
    }

    public Task(String taskName, String description, StatusTask statusTask) {
        this(taskName, description);
        this.statusTask = statusTask;
    }

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", taskName='" + taskName + '\'' +
                ", statusTask=" + statusTask +
                ", idNumber=" + idNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idNumber == task.idNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }
}
