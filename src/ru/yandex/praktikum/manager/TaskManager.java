package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.List;

public interface TaskManager {
    //получение списка всех задач
    List<Task> getAllTasks();

    //получение списка всех эпиков
    List<Task> getAllEpic();

    //получение списка всех подзадач
    List<Task> getAllSubTask();

    // Получение истории
    List<Task> getHistory();

    // удаление всех задач
    void removeAllTasks();

    // получение подзадачи по Id
    Task getSubTaskById(int idNumber);

    // получение задачи по Id
    Task getEpicById(int idNumber);

    // получение задачи по Id
    Task getTaskById(int idNumber);

    // создание задачи
    Task createTask(Task task);

    // создание подзадачи
    Epic createEpic(Epic epic);

    // создание эпика
    SubTask createSubTask(SubTask subTask);

    // обновление задачи
    void updateTask(Task task);

    // обновление подзадачи
    void updateSubTask(SubTask subTask);

    // обновление эпика
    void updateEpic(Epic epic);

    // удаление задачи
    void deleteTask(int idNumber);

    // удаление эпика
    void deleteEpic(Integer idNumber);

    // удаление подзадачи
    void deleteSubTasks(Integer idNumber);

    // получение подзадач эпика
    List<SubTask> getAllEpicSubtasks(Integer epicId);
}
