package ru.yandex.praktikum.test;

import ru.yandex.praktikum.tasks.StatusTask;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test // *проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    public void testTaskEqualityById() {
        Task task1 = new Task("Задача 1", "Описание 1", StatusTask.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", StatusTask.DONE);
        assertEquals(task1, task2);
    }
}