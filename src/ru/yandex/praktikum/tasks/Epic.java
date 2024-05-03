package ru.yandex.praktikum.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasksID = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return epicSubtasksID;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addEpicSubtasksID(Integer idNumber) {
        epicSubtasksID.add(idNumber);
    }

    public void removeEpicSubtasksID(int idNumber) {
        int index = epicSubtasksID.indexOf(idNumber);
        if (index != -1) {
            epicSubtasksID.remove(index);
        }
    }

    public void clearSubtaskIdList() {
        epicSubtasksID.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(epicSubtasksID, epic.epicSubtasksID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicSubtasksID);
    }

/*    @Override
    public String toString() {
        return "Epic{" +
                "описание = '" + getDescription() + '\'' +
                ", задача = '" + getTaskName() + '\'' +
                ", статус = " + getStatusTask() +
                ", id задачи = " + getIdNumber() +
                '}';
    }*/

    @Override
    public String toString() {
        return "Epic{" +
                "задача='" + taskName + '\'' +
                ", описание='" + description + '\'' +
                ", id='" + idNumber + '\'' +
                ", статус='" + statusTask + '\'' +
                ", подзадача' ID='" + epicSubtasksID + '\'' +
                ", начало времени='" + startTime + '\'' +
                ", конец времени='" + endTime + '\'' +
                ", duration='" + duration + '}' + '\'';
    }

}
