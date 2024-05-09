import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.InMemoryHistoryManager;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    @Test
    public void addToHistory() {
        Task task1 = new Task("Task", "Task description");
        Epic epic2 = new Epic("Epic", "Epic description");
        SubTask subTask3 = new SubTask("SubTask", "SubTask description", 2);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getEpicById(2);
        manager.getSubTaskById(3);
        manager.getSubTaskById(3);

        // проверить размер списка истории после повторных просмотров задач
        assertEquals(3, manager.getHistory().size(), "Список истории сохраняет повторные просмотры задач.");
    }

    @Test
    public void removeToHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");
        Task task4 = new Task("Task 4", "Description 4");
        Task task5 = new Task("Task 5", "Description 5");
        Task task6 = new Task("Task 6", "Description 6");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        manager.createTask(task5);
        manager.createTask(task6);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task6);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(6, history.size());
        inMemoryHistoryManager.add(task1);
        assertEquals(6, history.size());
        inMemoryHistoryManager.remove(1);
        List<Task> history2 = inMemoryHistoryManager.getHistory();
        assertEquals(5, history2.size());
    }

    @Test
    public void removeTest() {
        Task task1 = new Task("Task", "Task description");
        Epic epic2 = new Epic("Epic", "Epic description");
        SubTask subTask3 = new SubTask("SubTask", "SubTask description", 2);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        manager.getTaskById(task1.getIdNumber());
        manager.getEpicById(epic2.getIdNumber());
        manager.getSubTaskById(subTask3.getIdNumber());

        manager.getHistoryManager().remove(task1.getIdNumber());
        manager.getHistoryManager().remove(epic2.getIdNumber());
        manager.getHistoryManager().remove(subTask3.getIdNumber());

        // проверить, что список истории пуст
        assertEquals(0, manager.getHistoryManager().getHistory().size(), "Список истории не пуст.");
    }

    @Test
    public void getHistoryTest() {
        Task task1 = new Task("Task", "Task description");
        Epic epic2 = new Epic("Epic", "Epic description");
        SubTask subTask3 = new SubTask("SubTask", "SubTask description", 2);

        manager.createTask(task1);
        manager.createEpic(epic2);
        manager.createSubTask(subTask3);

        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubTaskById(3);

        // проверить, что список истории не пуст
        assertFalse(manager.getHistoryManager().getHistory().isEmpty(), "Список истории пуст.");

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();

        // проверить, что список истории пуст
        assertTrue(manager.getHistoryManager().getHistory().isEmpty(), "Список истории не пуст.");
    }

    @Test
    public void shouldReturnEmptyHistoryTest() {
        List<Task> history = new ArrayList<>();

        assertEquals(history, manager.getHistoryManager().getHistory(), "Возвращается непустая история.");
    }

    @Test
    public void duplicateTasksViewsTest() {
        Task task = new Task("Task", "Task description");

        manager.createTask(task);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);

        assertEquals(1, manager.getHistoryManager().getHistory().size(), "Дублирующиеся просмотры задач не исключаются из истории.");
    }

    @Test
    public void removeFromBeginningOfHistoryTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");
        Task task3 = new Task("Task3", "Task description");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.getTaskById(task1.getIdNumber());
        manager.getTaskById(task2.getIdNumber());
        manager.getTaskById(task3.getIdNumber());

        manager.getHistoryManager().remove(task1.getIdNumber());

        assertEquals(new ArrayList<>(List.of(task2, task3)), manager.getHistoryManager().getHistory(),
                "Истории просмотров не соответствуют.");
    }

    @Test
    public void removeFromMiddleOfHistoryTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");
        Task task3 = new Task("Task3", "Task description");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.getTaskById(task1.getIdNumber());
        manager.getTaskById(task2.getIdNumber());
        manager.getTaskById(task3.getIdNumber());

        manager.getHistoryManager().remove(task2.getIdNumber());

        assertEquals(new ArrayList<>(List.of(task1, task3)), manager.getHistoryManager().getHistory(), "Задача не удалена из истории просмотров.");
        assertFalse(manager.getHistoryManager().getHistory().contains(task2), "Задача не удалена из истории просмотров.");
    }

    @Test
    public void removeFromEndOfHistoryTest() {
        Task task1 = new Task("Task1", "Task description");
        Task task2 = new Task("Task2", "Task description");
        Task task3 = new Task("Task3", "Task description");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        manager.getTaskById(task1.getIdNumber());
        manager.getTaskById(task2.getIdNumber());
        manager.getTaskById(task3.getIdNumber());

        manager.getHistoryManager().remove(task3.getIdNumber());

        assertEquals(new ArrayList<>(List.of(task1, task2)), manager.getHistoryManager().getHistory(),
                "Истории просмотров не соответствуют.");
    }

    @Test
    public void testAddTaskToHistory() { // Тест на добавление задачи в историю в InMemoryHistoryManager:
        Task task = new Task("Задача 1", "Описание 1");

        inMemoryHistoryManager.add(task);

        List<Task> history = inMemoryHistoryManager.getHistory();
        assertTrue(history.contains(task));
    }
}