package ru.yandex.praktikum.tasks;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String taskName, String description, StatusTask statusTask, int epicId) {
        super(taskName, description, statusTask);
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "описание = '" + getDescription() + '\'' +
                ", задача = '" + getTaskName() + '\'' +
                ", статус = " + getStatusTask() +
                ", id задачи = " + getIdNumber() +
                '}';
    }
}
