package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryHistoryManager;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    // *убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void testHistoryManagerStoresPreviousTaskVersions() {
        Task task = new Task("Задача 1", "Описание 1");
        Task task1 = new Task("Задача 2", "Описание 2");
        manager.createTask(task);
        manager.createTask(task1);
        manager.getTaskById(task.getIdNumber());
        manager.getTaskById(task1.getIdNumber());
        assertEquals(List.of(task, task1), manager.getHistory());
    }

}