package ru.yandex.schedule.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasksID = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public Epic(String taskName, String description, int idNumber) {
        super(taskName, description);
        this.idNumber = idNumber;
    }

    public void clearEpicSubtasksID() {
        epicSubtasksID.clear();
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
                "описание = '" + description + '\'' +
                ", задача = '" + taskName + '\'' +
                ", статус = " + statusTask +
                ", id задачи = " + idNumber +
                '}';
    }

}
