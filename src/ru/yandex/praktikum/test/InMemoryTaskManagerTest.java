package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Test
    public void testInMemoryTaskManagerAddsAndFindsTasks() {
        // *проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        Task task = new Task("Задча 1", "Описание 1", StatusTask.NEW);
        Task task1 = manager.createTask(task);
        Task task2 = manager.getTaskById(task1.getIdNumber());
        assertEquals(task1, task2);
    }

    @Test
    public void testTaskIsImmutableWhenAddedToManager() {
        // *создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
        Task task = new Task("Задача 1", "Описание 1", StatusTask.NEW);
        Task task1 = manager.createTask(task);
        assertEquals(task.getTaskName(), task1.getTaskName());
        assertEquals(task.getStatusTask(), task1.getStatusTask());
        assertEquals(task.getDescription(), task1.getDescription());
    }
}