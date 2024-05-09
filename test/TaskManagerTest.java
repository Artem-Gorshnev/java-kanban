import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ManagerTaskNotFoundException;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.StatusTask;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void createTaskTest() {
        Task task = new Task("Task", "Task description");
        taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskById(task.getIdNumber());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        // проверить запись в список и сравнить генерируемый id
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Количество задач в списке неверное.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpicTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        final Epic savedEpic = taskManager.getEpicById(epic.getIdNumber());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpic();

        // проверить запись в список и сравнить генерируемый id
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Количество эпиков в списке неверное.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void createSubTaskTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", 1);
        taskManager.createSubTask(subTask);

        final SubTask savedSubTask = taskManager.getSubTaskById(subTask.getIdNumber());

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTask();

        // проверить запись в список и сравнить генерируемый id
        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Количество подзадач в списке неверное.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getAllTasksTest() {
        Task task = new Task("Task", "Task description");
        taskManager.createTask(task);

        assertNotNull(taskManager.getAllTasks(), "Список задач пуст.");
        assertEquals(task, taskManager.getAllTasks().get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllEpicsTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        assertNotNull(taskManager.getAllEpic(), "Список эпиков пуст.");
        assertEquals(epic, taskManager.getAllEpic().get(0), "Эпики не совпадают.");
    }

    @Test
    void getAllSubTasksTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", 1);
        taskManager.createSubTask(subTask);

        assertNotNull(taskManager.getAllSubTask(), "Список подзадач пуст.");
        assertEquals(subTask, taskManager.getAllSubTask().get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void getTaskByIdTest() {
        Task task = new Task("Task", "Task description");
        taskManager.createTask(task);

        ManagerTaskNotFoundException thrown = Assertions.assertThrows(ManagerTaskNotFoundException.class, () -> {
            taskManager.getTaskById(2);
        }, "Ожидалось получение исключения");

        assertEquals("Задача типа TASK не найдена в менеджере", thrown.getMessage());

        assertEquals(task, taskManager.getTaskById(task.getIdNumber()), "Задача не соответствует.");
    }

    @Test
    public void getEpicByIdTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        ManagerTaskNotFoundException thrown = Assertions.assertThrows(ManagerTaskNotFoundException.class, () -> {
            taskManager.getEpicById(2);
        }, "Ожидалось получение исключения");

        assertEquals("Задача типа EPIC не найдена в менеджере", thrown.getMessage());

        assertEquals(epic, taskManager.getEpicById(epic.getIdNumber()), "Задача не соответствует.");
    }

    @Test
    public void getSubTaskByIdAndGetSubTaskByEpicIdTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask", "Subtask description", 1);
        taskManager.createSubTask(subTask);

        ManagerTaskNotFoundException thrown = Assertions.assertThrows(ManagerTaskNotFoundException.class, () -> {
            taskManager.getSubTaskById(3);
        }, "Ожидалось получение исключения");

        assertEquals("Задача типа SUBTASK не найдена в менеджере", thrown.getMessage());

        assertEquals(subTask, taskManager.getSubTaskById(subTask.getIdNumber()), "Задача не соответствует.");

        assertEquals(epic.getSubTaskIds().getFirst(), subTask.getIdNumber(),
                "id подзадачи в эпике не соответствует.");
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("Task", "Task description");
        taskManager.createTask(task);

        Task newTask = new Task("Task", "Updated task");
        newTask.setIdNumber(1);
        taskManager.updateTask(newTask);

        final Task updatedTask = taskManager.getTaskById(task.getIdNumber());

        assertNotNull(updatedTask, "Задача не найдена.");
        assertNotEquals(task, updatedTask, "Задачи соответствуют.");
    }

    @Test
    public void updateEpicTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        Epic newEpic = new Epic("Epic", "Updated epic");
        newEpic.setIdNumber(1);
        taskManager.updateEpic(newEpic);

        final Epic updatedEpic = taskManager.getEpicById(epic.getIdNumber());

        assertNotNull(updatedEpic, "Эпик не найден.");
        assertNotEquals(epic, updatedEpic, "Эпики соответствуют.");
    }

    @Test
    public void updateSubTaskTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask", "SubTask description", 1);
        taskManager.createSubTask(subTask);

        SubTask newSubTask = new SubTask("SubTask", "Updated subtask", 1);
        newSubTask.setIdNumber(2);
        taskManager.updateSubTask(newSubTask);

        final SubTask updatedSubTask = taskManager.getSubTaskById(subTask.getIdNumber());

        assertNotNull(updatedSubTask, "Подзадача не найдена.");
        assertNotEquals(subTask, updatedSubTask, "Подзадачи не соответствуют.");
    }

    @Test
    public void checkEpicStatusTest() {
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("SubTask1", "NEW", 1);
        SubTask subTask2 = new SubTask("SubTask2", "IN_PROGRESS", 1);
        SubTask subTask3 = new SubTask("SubTask3", "DONE", 1);

        subTask1.setStatusTask(StatusTask.NEW);
        subTask2.setStatusTask(StatusTask.IN_PROGRESS);
        subTask3.setStatusTask(StatusTask.DONE);

        taskManager.createSubTask(subTask1);
        assertEquals(StatusTask.NEW, epic.getStatusTask(), "Статус эпика не соответствует.");

        taskManager.createSubTask(subTask2);
        assertEquals(StatusTask.IN_PROGRESS, epic.getStatusTask(), "Статус эпика не соответствует.");

        taskManager.createSubTask(subTask3);
        assertNotEquals(StatusTask.DONE, epic.getStatusTask(), "Статус эпика не соответствует.");

        subTask1.setStatusTask(StatusTask.DONE);
        subTask2.setStatusTask(StatusTask.DONE);
        taskManager.updateEpicStatus(epic.getIdNumber());
        assertEquals(StatusTask.DONE, epic.getStatusTask(), "Статус эпика не соответствует.");
    }

    @Test
    public void removeAllTasksTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertEquals(2, taskManager.getAllTasks().size(), "Размер списка не соответствует.");

        taskManager.removeAllTasks();

        assertEquals(0, taskManager.getAllTasks().size(), "Список не очищен.");
    }

    @Test
    public void removeAllEpicsTest() {
        Epic epic1 = new Epic("Epic1", "Epic description");
        Epic epic2 = new Epic("Epic2", "Epic description");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        assertEquals(2, taskManager.getAllEpic().size(), "Размер списка не соответствует.");

        taskManager.removeAllEpics();

        assertEquals(0, taskManager.getAllEpic().size(), "Список не очищен.");
    }

    @Test
    public void removeAllSubtasksAndCheckEpicContentTest() {
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask1 = new SubTask("SubTask1", "SubTask description", 1);
        SubTask subTask2 = new SubTask("SubTask2", "SubTask description", 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(2, taskManager.getAllSubTask().size(), "Размер списка не соответствует.");

        taskManager.removeAllSubtasks();

        assertEquals(0, taskManager.getAllSubTask().size(), "Список подзадач не очищен.");
        assertEquals(0, epic.getSubTaskIds().size(), "Список id подзадач не очищен.");
    }

    @Test
    public void deleteTaskTest() {
        Task task = new Task("Task", "Task description");
        taskManager.createTask(task);

        taskManager.deleteTask(task.getIdNumber());
        assertEquals(0, taskManager.getAllTasks().size(), "Задача с заданным id не удалена.");
    }

    @Test
    public void deleteEpicTest() {
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask1 = new SubTask("SubTask1", "SubTask description", 1);
        SubTask subTask2 = new SubTask("SubTask2", "SubTask description", 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        taskManager.deleteEpic(epic.getIdNumber());
        assertEquals(0, taskManager.getAllTasks().size(), "Эпик с заданным id не удалена.");
        assertEquals(0, taskManager.getAllSubTask().size(), "Идентификаторы подзадач эпика не удалены.");
    }

    @Test
    public void removeSubTasksByIdTest() {
        Epic epic = new Epic("Epic", "Epic description");
        SubTask subTask = new SubTask("SubTask", "SubTask description", 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        taskManager.deleteSubTasks(subTask.getIdNumber());
        assertEquals(0, taskManager.getAllSubTask().size(), "Подзадача с заданным id не удалена.");
        assertEquals(0, epic.getSubTaskIds().size(), "Идентификаторы подзадач эпика не удалены.");
    }

    @Test
    public void isCrossingTasksCheckTasks() {
        Task task = new Task("Task", "Task description", LocalDateTime.of(2024, 01, 01, 00, 00), Duration.ofMinutes(15));
        Epic epic = new Epic("Epic", "Epic description");

        SubTask subTask1 = new SubTask("Subtask1", "Subtask1 description", 2, LocalDateTime.of(2024, 01, 02, 00, 00),
                Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("Subtask2", "Subtask1 description", 2, LocalDateTime.of(2024, 01, 02, 01, 00),
                Duration.ofMinutes(15));

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(task.getStartTime(), taskManager.getTaskById(task.getIdNumber()).getStartTime(),
                "Время начала задачи не соответствует.");
        assertEquals(subTask1.getStartTime(), taskManager.getSubTaskById(subTask1.getIdNumber()).getStartTime(),
                "Время начала подзадачи не соответствует.");
        assertEquals(subTask2.getStartTime(), taskManager.getSubTaskById(subTask2.getIdNumber()).getStartTime(),
                "Время начала подзадачи не соответствует.");

        assertEquals(task, taskManager.getPrioritizedTasks().first(),
                "Задача с самым ранним временем начала не первая в списке.");
        assertEquals(subTask1.getStartTime(), epic.getStartTime(),
                "Время начала эпика не соответствует времени начала самой ранней подзадачи.");
        assertEquals(subTask2.getEndTime(), epic.getEndTime(),
                "Время завершения эпика не соответствует времени начала самой поздней подзадачи.");

        taskManager.deleteSubTasks(subTask2.getIdNumber());

        assertEquals(subTask1.getEndTime(), epic.getEndTime(), "Время завершения эпика не обновлено.");
    }
}