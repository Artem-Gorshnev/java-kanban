package ru.yandex.praktikum;

import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Трекер задач");

        TaskManager taskManager = Managers.getDefault();

        // Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        Task task1 = new Task("Task 1", "Description 1", StatusTask.NEW);
        Task task2 = new Task("Task 2", "Description 2", StatusTask.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic Description 1");
        Epic epic2 = new Epic("Epic 2", "Epic Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        SubTask subTask1 = new SubTask("SubTask 1", "SubTask Description 1", StatusTask.IN_PROGRESS, epic1.getIdNumber());
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask Description 2", StatusTask.DONE, epic1.getIdNumber());
        SubTask subTask3 = new SubTask("SubTask 3", "SubTask Description 3", StatusTask.NEW, epic1.getIdNumber());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        System.out.println(taskManager.getAllTasks());

        System.out.println("***");
        // Запросите созданные задачи несколько раз в разном порядке.
        taskManager.getTaskById(task1.getIdNumber());
        taskManager.getTaskById(task1.getIdNumber());
        taskManager.getEpicById(epic2.getIdNumber());
        taskManager.getSubTaskById(subTask2.getIdNumber());

        // После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
        System.out.println(taskManager.getHistory());

        System.out.println("***");
        // Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        taskManager.deleteTask(task1.getIdNumber());
        System.out.println(taskManager.getAllTasks());

        System.out.println("***");
        // Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        taskManager.deleteEpic(epic1.getIdNumber());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
    }
}
