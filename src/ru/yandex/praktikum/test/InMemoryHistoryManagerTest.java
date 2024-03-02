package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryHistoryManager;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    // *убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void testHistoryManagerStoresPreviousTaskVersions() {
        Task task1 = new Task("Task 1", "Description 1", StatusTask.NEW);
        Task task2 = new Task("Task 2", "Description 2", StatusTask.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", StatusTask.DONE);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(2);

        List<Task> history = manager.getHistory();

        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
        assertEquals(3, history.size());
    }

}