package ru.yandex.schedule.tasks;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String taskName, String description, StatusTask statusTask, int epicId) {
        super(taskName, description, statusTask);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
    @Override
    public String toString() {
        return "SubTask{" +
                "описание = '" + description + '\'' +
                ", задача = '" + taskName + '\'' +
                ", статус = " + statusTask +
                ", id задачи = " + idNumber +
                '}';
    }
}
