package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    // *проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void testSubTaskEqualityById() {
        Task task1 = new Epic("Эпик 1", "Описание 1");
        Task task2 = new Epic("Эпик 2", "Описание 2");
        assertEquals(task1, task2);
    }

}