package ru.yandex.schedule;

import ru.yandex.schedule.manager.TaskManager;
import ru.yandex.schedule.tasks.Epic;
import ru.yandex.schedule.tasks.StatusTask;
import ru.yandex.schedule.tasks.SubTask;
import ru.yandex.schedule.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("task1", "Собрать вещи");
        Task task2 = new Task("task2", "Помидор");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("epic1", "Собрать вещи");
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("subTask1", "Собрать вещи", epic1.getIdNumber(), StatusTask.NEW);
        SubTask subTask2 = new SubTask("subTask2", "Помидор", epic1.getIdNumber(), StatusTask.NEW);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        epic1.addSubTaskId(subTask1.getIdNumber());
        epic1.addSubTaskId(subTask2.getIdNumber());

        Epic epic2 = new Epic("epic2", "Собрать вещи");
        taskManager.createEpic(epic2);

        SubTask subTask3 = new SubTask("subTask3", "Помидор", epic2.getIdNumber(), StatusTask.NEW);
        taskManager.createSubTask(subTask3);
        epic2.addSubTaskId(subTask3.getIdNumber());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        task1.updateStatus(StatusTask.IN_PROGRESS);
        task2.updateStatus(StatusTask.DONE);

        taskManager.updateTask(task1);
        taskManager.updateTask(task2);

        System.out.println(taskManager.getAllTasks());

        subTask2.updateStatus(StatusTask.IN_PROGRESS);
        taskManager.updateSubTask(subTask2);
        taskManager.updateEpic(epic1);

        System.out.println(taskManager.getAllEpic());

        subTask1.updateStatus(StatusTask.DONE);
        subTask2.updateStatus(StatusTask.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        taskManager.updateEpic(epic1);

        System.out.println(taskManager.getAllEpic());

        taskManager.deleteEpic(epic1.getIdNumber());

        System.out.println(taskManager.getAllSubTask());
    }
}
