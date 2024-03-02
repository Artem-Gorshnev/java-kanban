package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>(); // храним задачи
    protected HashMap<Integer, Epic> epics = new HashMap<>(); // храним эпики
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>(); // храним подзадачи
    private final HistoryManager historyManager = Managers.getDefaultHistory(); // храним историю
    private int idNumber = 0; // идентификатор

    private int generateId() {
        return idNumber++;
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() { // получение списка всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getAllEpic() { // получение списка всех эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllSubTask() { // получение списка всех подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllTasks() { //удаление всех задач, подзадач, эпиков
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public SubTask getSubTaskById(int idNumber) { // Получение Подзадач по идентификатору
        SubTask subTask = subTasks.get(idNumber);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicById(int idNumber) { // Получение Эпика по идентификатору
        Epic epic = epics.get(idNumber);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Task getTaskById(int idNumber) { // Получение Задач по идентификатору
        Task task = tasks.get(idNumber);
        historyManager.add(task);
        return task;
    }

    @Override
    public Task createTask(Task task) { // создание новой задачи
        int id = generateId();
        task.setIdNumber(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) { // создание нового эпика
        int id = generateId();
        epic.setIdNumber(id);
        epics.put(id, epic);
        return epic;
    }


    @Override
    public SubTask createSubTask(SubTask subTask) { // создание новой подзадачи
        if (epics.get(subTask.getEpicId()) == null) {
            return null;
        }
        int id = generateId();
        subTask.setIdNumber(id);
        subTasks.put(id, subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
        return subTask;
    }


    @Override
    public void updateTask(Task task) { // обновление задачи
        if (tasks.containsKey(task.getIdNumber())) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) { // обновление подзадачи
        if (subTasks.containsKey(subTask.getIdNumber())) {
            subTasks.put(subTask.getIdNumber(), subTask);
    //        updateEpic(epics.get(subTask.getEpicId()));
            updateEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
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
            epic.setStatusTask(StatusTask.NEW);
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
                epic.setStatusTask(StatusTask.NEW);
            } else if (isDone) {
                epic.setStatusTask(StatusTask.DONE);
            } else {
                epic.setStatusTask(StatusTask.IN_PROGRESS);
            }
        }
    }

    @Override
    public void deleteTask(int idNumber) {
        tasks.remove(idNumber);
    }

    @Override
    public void deleteEpic(Integer idNumber) {
        for (Integer subTaskId : epics.get(idNumber).getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(idNumber);
    }

    @Override
    public void deleteSubTasks(Integer idNumber) {
        SubTask subTask = subTasks.get(idNumber);
        Integer epicId = subTask.getEpicId();

        // Удаление subtaskId из списка задач эпика
        Epic epic = epics.get(epicId);
        epic.removeEpicSubtasksID(idNumber);

        // Удаление подзадачи
        subTasks.remove(idNumber);

        // Проверка и обновление статуса эпика
        updateEpicStatus(epic);
    }



@Override
    public List<SubTask> getAllEpicSubtasks(Integer epicId) {
        List<SubTask> result = new ArrayList<>();
        for (int subTaskId : epics.get(epicId).getSubTaskIds()) {
            result.add((subTasks.get(subTaskId)));
        }
        return result;
    }
}
