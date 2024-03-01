package ru.yandex.schedule.manager;

import ru.yandex.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Task> history = new ArrayList<>(); // храним историю
    @Override
    public void add(Task task) { // Метод для добавления задачи в историю просмотров
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return history;
    }
}
