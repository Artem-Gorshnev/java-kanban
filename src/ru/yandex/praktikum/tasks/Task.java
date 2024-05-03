package ru.yandex.praktikum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String taskName; // переменная для хранения задач
    protected String description; // переменная для хранения описания
    protected int idNumber;// переменная для создания уникального номера задачи
    protected StatusTask statusTask; // статус задачи
    protected TaskType type; // тип задачи
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.statusTask = StatusTask.NEW;
        this.type = TaskType.TASK;
    }

    public Task(String taskName, String description, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.statusTask = StatusTask.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public TaskType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration.toMinutes());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "задача='" + taskName + '\'' +
                ", описание='" + description + '\'' +
                ", id='" + idNumber + '\'' +
                ", статус='" + statusTask + '\'' +
                ", начало времени='" + startTime + '\'' +
                ", duration='" + duration + '}' + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(description, task.description) &&
                Objects.equals(idNumber, task.idNumber) && statusTask == task.statusTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, taskName, statusTask, idNumber);
    }
}
