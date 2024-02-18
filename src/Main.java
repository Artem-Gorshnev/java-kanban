public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("task1", "Собрать вещи");
        Task task2 = new Task("task2", "Помидор");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        SubTask subTask1 = new SubTask("subTask1", "Собрать вещи");
        SubTask subTask2 = new SubTask("subTask2", "Помидор");
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        Epic epic1 = new Epic("epic1", "Собрать вещи");
        Epic epic2 = new Epic("epic2", "Собрать вещи");
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);
        epic1.addSubTaskToEpic(subTask1);
        epic1.addSubTaskToEpic(subTask2);

        SubTask subTask3 = new SubTask("subTask3", "Помидор");
        taskManager.createTask(subTask3);
        epic2.addSubTaskToEpic(subTask3);

        System.out.println(taskManager.getAllTasks());

        task1.updateStatus(StatusTask.IN_PROGRESS);
        task2.updateStatus(StatusTask.DONE);

        System.out.println(taskManager.getAllTasks());

        subTask2.updateStatus(StatusTask.IN_PROGRESS);
        taskManager.updateTask(subTask2);

        System.out.println(taskManager.getAllTasks());

        subTask1.updateStatus(StatusTask.DONE);
        subTask2.updateStatus(StatusTask.DONE);
        taskManager.updateTask(subTask1);
        taskManager.updateTask(subTask2);
        System.out.println(taskManager.getAllTasks());

        taskManager.deleteTask(epic1.getIdNumber());

        System.out.println(taskManager.getAllTasks());
    }
}
