import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static int idNumber = 0;

    private int generateId() {
        return idNumber++;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Task> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public Task getTaskById(int idNumber) {
        if (tasks.get(idNumber) != null) {
            return tasks.get(idNumber);
        }
        if (epics.get(idNumber) != null) {
            return epics.get(idNumber);
        }
        if (subTasks.get(idNumber) != null) {
            return subTasks.get(idNumber);
        }
        return null;
    }

    public void createTask(Task task) {
        int id = generateId();
        task.setIdNumber(id);
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        int id = generateId();
        epic.setIdNumber(id);
        epics.put(id, epic);
    }

    public void createSubTask(SubTask subTask) {
        int id = generateId();
        subTask.setIdNumber(id);
        subTasks.put(id, subTask);
    }

    public void updateTask(Task task) {
        tasks.put(task.getIdNumber(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getIdNumber(), subTask);
    }

    public void updateEpic(Epic epic) {
        tasks.put(epic.getIdNumber(), epic);

        ArrayList<SubTask> result = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTaskIds()) {
            result.add(subTasks.get(subTaskId));
        }
        if (epic.getSubTaskIds().isEmpty()) {
            epic.updateStatus(StatusTask.NEW);
        } else {
            boolean isNew = true;
            boolean isDone = true;
            for (SubTask subTask : result) {
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

    public void deleteTask(Integer idNumber) {
        tasks.remove(idNumber);
    }

    public void deleteEpic(Integer idNumber) {
        for (Integer subTaskId : epics.get(idNumber).getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(idNumber);
    }

    public void deleteSubTasks(Integer idNumber) {
        subTasks.remove(idNumber);
    }

    public ArrayList<SubTask> getAllEpicSubtasks(Integer epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer subTaskId : epics.get(epicId).getSubTaskIds()) {
            subTasks.add((SubTask) tasks.get(subTaskId));
        }
        return subTasks;
    }
}
