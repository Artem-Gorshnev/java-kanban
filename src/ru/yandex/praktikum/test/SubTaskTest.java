package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void subtasksShouldBeEqualIfTheirIdIsEqual() {
        Task epic = new Epic("Эпик 1", "Описание 1");
        Task task1 = new SubTask("Подзадача 1", "Описание 1", StatusTask.NEW, 1);
        Task task2 = new SubTask("Подзадача 2", "Описание 2", StatusTask.DONE, 1);
        assertEquals(task1, task2);
    }

    // *проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    public void testAddEpicToItself() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        epic.setIdNumber(1);
        TaskManager taskManager = Managers.getDefault();
        assertNull(taskManager.createEpic(epic));
    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком;
    @Test
    public void testSubTaskCannotBeItsOwnEpic() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1",StatusTask.NEW, 1);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1",StatusTask.DONE, 2);
        SubTask actual = manager.createSubTask(subTask1);
        assertNull(actual);
    }


}