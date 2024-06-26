package ru.yandex.praktikum.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(taskName, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "задача='" + taskName + '\'' +
                ", описание='" + description + '\'' +
                ", id='" + idNumber + '\'' +
                ", статус='" + statusTask + '\'' +
                ", эаик ID='" + epicId + '\'' +
                ", начало времени='" + startTime + '\'' +
                ", duration='" + duration + '}' + '\'';
    }
}
