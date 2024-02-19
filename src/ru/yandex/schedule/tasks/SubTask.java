package ru.yandex.schedule.tasks;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description, StatusTask.NEW);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
