package ru.yandex.schedule;

import ru.yandex.schedule.manager.InMemoryTaskManager;
import ru.yandex.schedule.manager.Managers;
import ru.yandex.schedule.manager.TaskManager;
import ru.yandex.schedule.tasks.Epic;
import ru.yandex.schedule.tasks.StatusTask;
import ru.yandex.schedule.tasks.SubTask;
import ru.yandex.schedule.tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Трекер задач");

        TaskManager taskManager = Managers.getDefault();

        // содание двух задач
        Task task1 = new Task("Задача №1", "Собрать игрушки", StatusTask.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Задача №2", "Выбросить ненужные игрушки", StatusTask.NEW);
        taskManager.createTask(task2);

        // создание эпика с двумя подзадачами
        Epic epic1 = new Epic("Эпик №1", "Собрать все вещи в квартире");
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача №1", "Собрать вещи белого цвета", StatusTask.NEW, epic1.getIdNumber());
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Подзадача №2", "Собрать вещи чёрного цвета", StatusTask.NEW, epic1.getIdNumber());
        taskManager.createSubTask(subTask2);
        epic1.addEpicSubtasksID(subTask1.getIdNumber());
        epic1.addEpicSubtasksID(subTask2.getIdNumber());

        //создание эпика с одной подзадачей
        Epic epic2 = new Epic("Эпик №2", "Собрать копьютер");
        taskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("Подзадача №3", "Купить видеокарту", StatusTask.NEW, epic2.getIdNumber());
        taskManager.createSubTask(subTask3);
        epic2.addEpicSubtasksID(subTask3.getIdNumber());

        // печатаем списки задач, эпиков и подзадач
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("***");
        //изменим статус объектов
        task1.updateStatus(StatusTask.DONE);
        task2.updateStatus(StatusTask.DONE);
        subTask1.updateStatus(StatusTask.DONE);
        subTask2.updateStatus(StatusTask.DONE);
        subTask3.updateStatus(StatusTask.DONE);

        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        taskManager.updateSubTask(subTask3);

        // печатаем изменённые списки задач, эпиков и подзадач
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("***");
        // удаляем одну задачу и один эпик
        taskManager.deleteTask(task1.getIdNumber());
        taskManager.deleteEpic(epic1.getIdNumber());
        // печатаем обновлённый трекер задач
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println("***");

        printAllTasks(taskManager);

        System.out.println("***"); // заполняем историю
        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getEpicById(5);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6);
        // перегружаем для проверки, что в массиве хранится не более 10 элементов
        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getEpicById(5);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6);
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Task task : manager.getAllEpicSubtasks(epic.getIdNumber())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTask()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
