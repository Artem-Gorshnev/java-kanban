package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    // *проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void testInMemoryTaskManagerAddsAndFindsTasks() {
        Task task = manager.createTask(new Task("Задача 1", "Описание 1"));
        Epic epic = manager.createEpic(new Epic("Эпик 1", "Описание 1"));
        SubTask subtask = manager.createSubTask(new SubTask("Подзадача 1", "Описание 1", 2));
        Task getTask = manager.getTaskById(0);
        Epic getEpic = manager.getEpicById(1);
        SubTask getSubtask = manager.getSubTaskById(2);
        assertEquals(task, getTask);
        assertEquals(epic, getEpic);
        assertEquals(subtask, getSubtask);
    }

    // *проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void testTaskIdsDoNotConflict() {
        Task task = manager.createTask(new Task("Задача 1", "Описание 1"));
        Task taskWithId = manager.createTask(new Task("Задача", "Описание", StatusTask.NEW, 1));
        assertNotEquals(task.getIdNumber(), taskWithId.getIdNumber());
    }

    // *создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void testTaskIsImmutableWhenAddedToManager() {
        Task task = new Task("Задача 1", "Описание 1");
        Task task1 = manager.createTask(task);
        assertEquals(task.getTaskName(), task1.getTaskName());
        assertEquals(task.getStatusTask(), task1.getStatusTask());
        assertEquals(task.getDescription(), task1.getDescription());
    }
}