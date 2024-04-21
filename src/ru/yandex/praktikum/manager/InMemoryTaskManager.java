package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>(); // храним задачи
    protected HashMap<Integer, Epic> epics = new HashMap<>(); // храним эпики
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>(); // храним подзадачи
    protected final HistoryManager historyManager = Managers.getDefaultHistory(); // храним историю
    protected int idNumber = 0; // идентификатор

    private int generateId() {
        return idNumber++;
    }

    private Integer getId() {
        return idNumber;
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
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        epics.clear();
        for (Integer subtaskId : subTasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subTasks.clear();
    }

    @Override
    public void removeAllSubtasks() { //удаление всех подзадач
        for (Integer subtaskId : subTasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatusTask(StatusTask.NEW);
            epic.clearSubtaskIdList();
        }
    }

    @Override
    public SubTask getSubTaskById(int idNumber) { // Получение Подзадач по идентификатору
        if (subTasks.get(idNumber) != null) {
            historyManager.add(subTasks.get(idNumber));
        }
        return subTasks.get(idNumber);
    }

    @Override
    public Epic getEpicById(int idNumber) { // Получение Эпика по идентификатору
        if (epics.get(idNumber) != null) {
            historyManager.add(epics.get(idNumber));
        }
        return epics.get(idNumber);
    }

    @Override
    public Task getTaskById(int idNumber) { // Получение Задач по идентификатору
        if (tasks.get(idNumber) != null) {
            historyManager.add(tasks.get(idNumber));
        }
        return tasks.get(idNumber);
    }

    @Override
    public List<SubTask> getAllEpicSubtasks(Integer epicId) {
        List<SubTask> result = new ArrayList<>();
        for (int subTaskId : epics.get(epicId).getSubTaskIds()) {
            result.add((subTasks.get(subTaskId)));
        }
        return result;
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
    public void createSubTask(SubTask newSubTask) { // создание новой подзадачи
        int id = generateId();
        newSubTask.setIdNumber(id);
        subTasks.put(newSubTask.getIdNumber(), newSubTask);
        int epicId = newSubTask.getEpicId();
        ArrayList<Integer> subTaskIdList = epics.get(epicId).getSubTaskIds();
        subTaskIdList.add(newSubTask.getIdNumber());
        updateEpicStatus(epicId);
    }


    @Override
    public void updateTask(Task task) { // обновление задачи
        if (tasks.containsKey(task.getIdNumber())) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) { // обновление подзадачи
        if (subTasks.containsKey(newSubTask.getIdNumber())) {
            subTasks.put(newSubTask.getIdNumber(), newSubTask);
            int epicId = subTasks.get(newSubTask.getIdNumber()).getEpicId();
            updateEpicStatus(epicId);
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

    protected void updateEpicStatus(int id) {
        int countNewTasks = 0;
        int countDoneTasks = 0;

        ArrayList<Integer> subTasksId = epics.get(id).getSubTaskIds();

        for (Integer subTaskId : subTasksId) {
            if (subTasks.get(subTaskId).getStatusTask().equals(StatusTask.NEW)) {
                countNewTasks++;
            } else if (subTasks.get(subTaskId).getStatusTask().equals(StatusTask.DONE)) {
                countDoneTasks++;
            }
        }

        if (subTasksId.size() == countDoneTasks) {
            epics.get(id).setStatusTask(StatusTask.DONE);
        } else if (subTasksId.size() == countNewTasks || subTasksId.isEmpty()) {
            epics.get(id).setStatusTask(StatusTask.NEW);
        } else {
            epics.get(id).setStatusTask(StatusTask.IN_PROGRESS);
        }

    }

    @Override
    public void deleteTask(int idNumber) { // удаление задачи
        historyManager.remove(idNumber);
        tasks.remove(idNumber);
    }

    @Override
    public void deleteEpic(Integer idNumber) {  //удаление эпика
        Epic deletedEpic = epics.remove(idNumber);
        if (deletedEpic == null) { //если объект пустой - выходим из метода
            return;
        }
        historyManager.remove(idNumber);
        //удаление связанных с эпиком подзадач
        for (Integer subtaskId : deletedEpic.getSubTaskIds()) {
            subTasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubTasks(Integer idNumber) { // удаление подзадачи
        // получить идентификатор эпика, чтобы после удаления подзадач обновить его статус
        int epicId = subTasks.get(idNumber).getEpicId();
        // удалить идентификаторы подзадач из эпика
        epics.get(epicId).getSubTaskIds().remove((Integer) idNumber);
        // удалить subTask из истории просмотров
        historyManager.remove(idNumber);
        // удалить subTask из subTaskHashMap
        subTasks.remove(idNumber);
        // проверить статус эпика
        updateEpicStatus(epicId);
    }
}
