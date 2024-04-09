package ru.yandex.praktikum.test;

import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void testTaskEqualityById() { // *проверьте, что экземпляры класса Task равны друг другу, если равен их id;
        Task task1 = new Task("Задача 1", "Описание 1", StatusTask.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", StatusTask.DONE);
        assertEquals(task1, task2);
    }
}