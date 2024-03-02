package ru.yandex.praktikum.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasksID = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, StatusTask.NEW);
    }

    public void removeEpicSubtasksID(int idNumber) {
        int index = epicSubtasksID.indexOf(idNumber);
        if (index != -1) {
            epicSubtasksID.remove(index);
        }
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
                ", задача = '" + getTaskName() + '\'' +
                ", статус = " + getStatusTask() +
                ", id задачи = " + getIdNumber() +
                '}';
    }

}
