package ru.yandex.praktikum.tasks;

import java.util.Objects;

public class Task {

    private String description; // переменная для хранения описания
    private String taskName; // переменная для хранения задач
    private StatusTask statusTask; // статус задачи
    // protected StatusTask statusTask = StatusTask.NEW; // статус задачи
    private int idNumber;// переменная для создания уникального номера задачи

    public Task(String taskName, String description, StatusTask statusTask) {
        this.taskName = taskName;
        this.description = description;
        this.statusTask = statusTask;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
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
                "описание = '" + description + '\'' +
                ", задача = '" + taskName + '\'' +
                ", статус = " + statusTask +
                ", id задачи = " + idNumber +
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
        return Objects.hash(description, taskName, statusTask, idNumber);
    }
}
