package ru.yandex.schedule.manager;

public class Managers {
    public static TaskManager getDefault() { // возвращаем объект InMemoryTaskManager
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){ // возвращаем объект InMemoryHistoryManager
        return new InMemoryHistoryManager();

    }
}
