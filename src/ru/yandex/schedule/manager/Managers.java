package ru.yandex.schedule.manager;

public class Managers {
    public static TaskManager getDefault() { // здесь возвращаем объект InMemoryTaskManager
        return new InMemoryTaskManager();
    }
}
