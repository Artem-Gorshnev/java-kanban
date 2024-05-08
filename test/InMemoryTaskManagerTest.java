import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import ru.yandex.practicum.manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }
}