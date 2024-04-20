package ru.yandex.praktikum.manager;

import java.io.File;

public class Managers {
    public static final File file = new File("fileCSV/File.csv");
    public static TaskManager getDefault() { // возвращаем объект InMemoryTaskManager
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() { // возвращаем объект InMemoryHistoryManager
        return new InMemoryHistoryManager();

    }
}
