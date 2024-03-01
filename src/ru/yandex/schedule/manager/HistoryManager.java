package ru.yandex.schedule.manager;

import ru.yandex.schedule.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}

