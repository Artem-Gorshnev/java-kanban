package ru.yandex.schedule.manager;

import ru.yandex.schedule.tasks.Epic;
import ru.yandex.schedule.tasks.StatusTask;
import ru.yandex.schedule.tasks.SubTask;
import ru.yandex.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>(); // храним задачи
    protected HashMap<Integer, Epic> epics = new HashMap<>(); // храним эпики
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>(); // храним подзадачи
    protected List<Task> history = new ArrayList<>(); // храним историю
    protected int idNumber = 0; // идентификатор

    private int generateId() {
        return idNumber++;
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return history;
    }

    public void addToHistory(Task task) { // Метод для добавления задачи в историю просмотров
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
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

    /*
        @Override
        public SubTask getSubTaskById(int idNumber) { // Получение Подзадач по идентификатору
            SubTask subTask = subTasks.get(idNumber);
            if (subTask != null) {
                addToHistory(subTask);
            }
            return subTasks.get(idNumber);
        }

        @Override
        public Epic getEpicById(int idNumber) { // Получение Эпика по идентификатору
            Epic epic = epics.get(idNumber);
            if (epic != null) {
                addToHistory(epic);
            }
            return epics.get(idNumber);
        }

        @Override
        public Task getTaskById(int idNumber) { // Получение Задач по идентификатору
            Task task = subTasks.get(idNumber);
            if (task != null) {
                addToHistory(task);
            }
            return tasks.get(idNumber);
        }
    */
    @Override
    public SubTask getSubTaskById(int idNumber) { // Получение Подзадач по идентификатору
        addToHistory(subTasks.get(idNumber));
        return subTasks.get(idNumber);
    }

    @Override
    public Epic getEpicById(int idNumber) { // Получение Эпика по идентификатору
        addToHistory(epics.get(idNumber));
        return epics.get(idNumber);
    }

    @Override
    public Task getTaskById(int idNumber) { // Получение Задач по идентификатору
        addToHistory(tasks.get(idNumber));
        return tasks.get(idNumber);
    }

    @Override
    public void createTask(Task task) { // создание новой задачи
        int id = generateId();
        task.setIdNumber(id);
        tasks.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) { // создание нового эпика
        int id = generateId();
        epic.setIdNumber(id);
        epics.put(id, epic);
    }

    @Override
    public void createSubTask(SubTask subTask) { // создание новой подзадачи
        int id = generateId();
        subTask.setIdNumber(id);
        subTasks.put(id, subTask);
        updateEpicStatus(epics.get(subTask.getEpicId()));
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
            updateEpic(epics.get(subTask.getEpicId()));
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

    @Override
    public void deleteTask(Integer idNumber) {
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
        Integer epicId = subTasks.get(idNumber).getEpicId();
        subTasks.remove(idNumber);
        updateEpicStatus(epics.get(epicId));
    }

    @Override
    public ArrayList<SubTask> getAllEpicSubtasks(Integer epicId) {
        ArrayList<SubTask> result = new ArrayList<>();
        for (int subTaskId : epics.get(epicId).getSubTaskIds()) {
            result.add((subTasks.get(subTaskId)));
        }
        return result;
    }
}
