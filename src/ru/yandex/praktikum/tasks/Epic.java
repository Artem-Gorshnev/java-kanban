package ru.yandex.praktikum.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasksID = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, StatusTask.NEW);
    }

    public void removeEpicSubtasksID() {
        if (epicSubtasksID.contains(idNumber)) {
            epicSubtasksID.remove(Integer.valueOf(idNumber));
        }
    }

    public void removeEpicSubtasksID(int idNumber) {
        epicSubtasksID.remove(idNumber);
    }

    public void addEpicSubtasksID(Integer idNumber) {
        epicSubtasksID.add(idNumber);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return epicSubtasksID;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "описание = '" + getDescription() + '\'' +
                ", задача = '" + taskName + '\'' +
                ", статус = " + statusTask +
                ", id задачи = " + idNumber +
                '}';
    }

}
