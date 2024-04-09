import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.HistoryManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void testUtilityClassAlwaysReturnsInitializedManagers() {
        // *убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Задача 1", "Описание 1", StatusTask.NEW);
        taskManager.createTask(task);
        int taskId = task.getIdNumber();
        Task getTask = taskManager.getTaskById(taskId);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(historyManager);
        assertNotNull(taskManager);
        assertNotNull(history);
        assertEquals(1, history.size());
        assertNotNull(getTask);

    }
}