import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void subtasksShouldBeEqualIfTheirIdIsEqual() {
        Task task1 = new SubTask("Подзадача 1", "Описание 1", StatusTask.NEW, 1);
        Task task2 = new SubTask("Подзадача 2", "Описание 2", StatusTask.DONE, 1);
        assertEquals(task1, task2);
    }

    @Test
    public void testSubTaskCannotBeItsOwnEpic() { // проверьте, что объект Subtask нельзя сделать своим же эпиком;
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", StatusTask.NEW, 1);
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", StatusTask.DONE, 2);
        SubTask actual = manager.createSubTask(subTask1);
        assertNull(actual);
    }

    @Test
    public void testDeleteSubTaskFromEpic() { // Тест  удаление подзадач из эпика в InMemoryTaskManager:
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик 1", "Описание 1");
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", StatusTask.NEW, epic.getIdNumber());

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

        taskManager.deleteSubTasks(subTask.getIdNumber());

        Epic updatedEpic = (Epic) taskManager.getEpicById(epic.getIdNumber());
        assertFalse(updatedEpic.getSubTaskIds().contains(subTask.getIdNumber()));
    }

    @Test
    public void testUpdateTaskUsingSetters() { // Тест на обновление задачи, используя сеттеры в InMemoryTaskManager:
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Задача 1", "Описание 1", StatusTask.NEW);

        taskManager.createTask(task);

        task.setTaskName("Новая задача");
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTaskById(task.getIdNumber());
        assertEquals("Новая задача", updatedTask.getTaskName());
    }
}