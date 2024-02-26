package ru.yandex.schedule.manager;

import ru.yandex.schedule.tasks.Epic;
import ru.yandex.schedule.tasks.StatusTask;
import ru.yandex.schedule.tasks.SubTask;
import ru.yandex.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int idNumber = 0;

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

    public Task getSubTaskById(int idNumber) {
        return subTasks.get(idNumber);
    }

    public Task getEpicById(int idNumber) {
        return epics.get(idNumber);
    }

    public Task getTaskById(int idNumber) {
        return tasks.get(idNumber);
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
        updateEpicStatus(epics.get(subTask.getEpicId()));
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdNumber())) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getIdNumber())) {
            subTasks.put(subTask.getIdNumber(), subTask);
            updateEpic(epics.get(subTask.getEpicId()));
            updateEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getIdNumber())) {
            Epic updatedEpic = epics.get(epic.getIdNumber());
            updatedEpic.setTaskName(epic.getTaskName());
            updatedEpic.setDescription(epic.getDescription());
            epics.put(updatedEpic.getIdNumber(), updatedEpic);
        }
    }

    private void updateEpicStatus(Epic epic) {
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
        Integer epicId = subTasks.get(idNumber).getEpicId();
        subTasks.remove(idNumber);
        updateEpicStatus(epics.get(epicId));
    }

    public ArrayList<SubTask> getAllEpicSubtasks(Integer epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTaskId : epics.get(epicId).getSubTaskIds()) {
            subTasks.add((subTasks.get(subTaskId)));
        }
        return subTasks;
    }
}
