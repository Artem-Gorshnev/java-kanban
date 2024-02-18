import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private static int idNumber = 0;

    public static int generateId() {
        return idNumber++;
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int idNumber) {
        return tasks.get(idNumber);
    }

    public void createTask(Task task) {
        int id = generateId();
        task.setIdNumber(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {

        tasks.put(task.getIdNumber(), task);

        for (Task taskValue : tasks.values()) {
            if (taskValue instanceof Epic) {
                Epic epic = (Epic) taskValue;
                ArrayList<SubTask> subTasks = new ArrayList<>();
                for (Integer subTaskId : epic.getSubTasksId()) {
                    subTasks.add((SubTask) tasks.get(subTaskId));
                }
                if (subTasks.isEmpty()) {
                    epic.updateStatus(StatusTask.NEW);
                } else {
                    boolean isNew = true;
                    boolean isDone = true;
                    for (SubTask subTask : subTasks) {
                        if (!subTask.getStatusTask().equals(StatusTask.NEW)) {
                            isNew = false;
                        } else if (!subTask.getStatusTask().equals(StatusTask.DONE)) {
                            isDone = false;
                        }
                    }
                    if (isNew) {
                        epic.updateStatus(StatusTask.NEW);
                    } else if (isDone) {
                        epic.updateStatus(StatusTask.DONE);
                    } else {
                        epic.updateStatus(StatusTask.IN_PROGRESS);
                    }
                }
            }
        }
    }

    public void deleteTask(Integer idNumber) {
        tasks.remove(idNumber);
    }

    public ArrayList<SubTask> getTasksInEpic(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasksId()) {
            subTasks.add((SubTask) tasks.get(subTaskId));
        }
        return subTasks;
    }
}
