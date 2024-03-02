package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>(); // храним историю

    @Override
    public void add(Task task) { // Метод для добавления задачи в историю просмотров
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return new ArrayList<>(history);
    }
}
