package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.SubTask;
import ru.yandex.praktikum.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>(); // храним задачи
    protected HashMap<Integer, Epic> epics = new HashMap<>(); // храним эпики
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>(); // храним подзадачи
    protected final HistoryManager historyManager = Managers.getDefaultHistory(); // храним историю
    protected int idNumber = 0; // идентификатор

    private final Comparator<Task> comparator = (task1, task2) -> {
        if (task1.getStartTime() == null) {
            return 1;
        } else if (task2.getStartTime() == null) {
            return -1;
        }
        return task1.getStartTime().compareTo(task2.getStartTime());
    };
    protected TreeSet<Task> sortedTasks = new TreeSet<>(comparator);

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    @Override
    public boolean isCrossingTasks(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            return !sortedTasks
                    .stream()
                    .filter(t -> t.getStartTime() != null && t.getDuration() != null)
                    .allMatch(t -> (
                            (task.getStartTime().isBefore(t.getStartTime()) &&
                                    task.getEndTime().isBefore(t.getStartTime())) ||
                                    (task.getStartTime().isAfter(t.getEndTime()) &&
                                            task.getEndTime().isAfter(t.getEndTime()))));
        }
        return true;
    }

    protected void updateEpicTime(Epic epic) {
        Duration epicDuration;
        LocalDateTime earlyStartTime = LocalDateTime.MAX;
        LocalDateTime lateEndTime = LocalDateTime.MIN;

        if (!epic.getSubTaskIds().isEmpty()) {
            for (Integer id : epic.getSubTaskIds()) {
                SubTask subTask = subTasks.get(id);

                if (subTask.getStartTime() != null && subTask.getDuration() != null) {
                    if (earlyStartTime.isAfter(subTask.getStartTime())) {
                        earlyStartTime = subTask.getStartTime();
                    }

                    if (lateEndTime.isBefore(subTask.getEndTime())) {
                        lateEndTime = subTask.getEndTime();
                    }
                    epicDuration = Duration.between(earlyStartTime, lateEndTime);

                    epic.setStartTime(earlyStartTime);
                    epic.setEndTime(lateEndTime);
                    epic.setDuration(epicDuration);
                }
            }
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        }
    }

    @Override
    public void createTask(Task task) { // создание новой задачи
        int id = generateId();
        task.setIdNumber(id);
        // если по времени задачи пересекаются - обнулить время добавляемой задачи
        if (isCrossingTasks(task)) {
            task.setDuration(null);
            task.setStartTime(null);
            sortedTasks.add(task);
        } else {
            sortedTasks.add(task);
        }
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
        // если по времени задачи пересекаются - обнулить время добавляемой задачи
        if (isCrossingTasks(newSubTask)) {
            newSubTask.setDuration(null);
            newSubTask.setStartTime(null);
            sortedTasks.add(newSubTask);
        } else {
            sortedTasks.add(newSubTask);
        }
        subTasks.put(newSubTask.getIdNumber(), newSubTask);

        if (epics.get(newSubTask.getEpicId()) != null) {
            int epicId = newSubTask.getEpicId();
            ArrayList<Integer> subTaskIdList = epics.get(epicId).getSubTaskIds();
            subTaskIdList.add(newSubTask.getIdNumber());
            updateEpicStatus(epicId);
            updateEpicTime(epics.get(newSubTask.getEpicId()));
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() { // получение списка всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() { // получение списка всех эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() { // получение списка всех подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public Task getTaskById(int idNumber) { // Получение Задач по идентификатору
        if (tasks.get(idNumber) != null) {
            historyManager.add(tasks.get(idNumber));
        }
        return tasks.get(idNumber);
    }

    @Override
    public Epic getEpicById(int idNumber) { // Получение Эпика по идентификатору
        if (epics.get(idNumber) != null) {
            historyManager.add(epics.get(idNumber));
        }
        return epics.get(idNumber);
    }

    @Override
    public SubTask getSubTaskById(int idNumber) { // Получение Подзадач по идентификатору
        if (subTasks.get(idNumber) != null) {
            historyManager.add(subTasks.get(idNumber));
        }
        return subTasks.get(idNumber);
    }

    @Override
    public List<SubTask> getAllEpicSubtasks(Integer epicId) {
        List<Integer> subTasksId = epics.get(epicId).getSubTaskIds();
        List<SubTask> subtasksByEpic = new ArrayList<>();
        // преобразовать for-each в stream
        subTasksId.stream().forEach(i -> subtasksByEpic.add(subTasks.get(i)));
        return subtasksByEpic;
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        if (tasks.containsKey(task.getIdNumber())) {
            if (isCrossingTasks(task)) {
                task.setDuration(null);
                task.setStartTime(null);
            }
            tasks.put(task.getIdNumber(), task);

            // создать новый sortedList без старой Task и добавить newTask
            sortedTasks = sortedTasks.stream()
                    .filter(t -> t.getIdNumber() != task.getIdNumber())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
            sortedTasks.add(task);
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) { // обновление подзадачи
        if (subTasks.containsKey(newSubTask.getIdNumber())) {
            if (isCrossingTasks(newSubTask)) {
                newSubTask.setDuration(null);
                newSubTask.setStartTime(null);
            }

            subTasks.put(newSubTask.getIdNumber(), newSubTask);

            if (epics.get(newSubTask.getEpicId()) != null) {
                updateEpicStatus(newSubTask.getEpicId());
                updateEpicTime(epics.get(newSubTask.getEpicId()));
            }

            // создать новый sortedList без старой SubTask и добавить newSubTask
            sortedTasks = sortedTasks.stream()
                    .filter(t -> t.getIdNumber() != newSubTask.getIdNumber())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
            sortedTasks.add(newSubTask);
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

    @Override
    public void updateEpicStatus(int id) {
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
    public void removeAllTasks() { //удаление всех задач, подзадач, эпиков
        // преобразовать for-each в stream
        tasks.values().stream().forEach(task -> {
            historyManager.remove(task.getIdNumber());
            sortedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        // преобразовать for-each в stream
        epics.values()
                .stream()
                .forEach(epic -> {
                    epics.get(epic.getIdNumber()).getSubTaskIds()
                            .stream()
                            .forEach(id -> {
                                historyManager.remove(id);
                                sortedTasks.remove(subTasks.get(id));
                                subTasks.remove(id);
                            });
                    historyManager.remove(epic.getIdNumber());

                });
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() { //удаление всех подзадач
        // преобразовать for-each в stream
        epics.values()
                .stream()
                .forEach(epic -> {
                    epic.getSubTaskIds()
                            .forEach(id -> {
                                historyManager.remove(id);
                                sortedTasks.remove(subTasks.get(id));
                            });
                    epic.getSubTaskIds().clear();
                    updateEpicStatus(epic.getIdNumber());
                });
        subTasks.clear();

        // обновить время Epic'ов
        epics.values().stream().forEach(epic -> updateEpicTime(epic));
    }

    @Override
    public void deleteTask(int idNumber) { // удаление задачи
        // удалить Task из SortedList
        sortedTasks.remove(tasks.get(idNumber));
        historyManager.remove(idNumber);
        tasks.remove(idNumber);
    }

    @Override
    public void deleteEpic(Integer idNumber) {  //удаление эпика
        ArrayList<Integer> subTasksId = epics.get(idNumber).getSubTaskIds();
        // преобразовать for-each в stream
        subTasksId
                .stream()
                .filter(i -> subTasks.containsKey(i))
                .forEach(i -> {
                    // удалить SubTask Epic'а из sortedList
                    sortedTasks.remove(subTasks.get(i));
                    historyManager.remove(i);
                    subTasks.remove(i);
                });
        historyManager.remove(idNumber);
        epics.remove(idNumber);
    }

    @Override
    public void deleteSubTasks(Integer idNumber) { // удаление подзадачи
        // получить идентификатор эпика, чтобы после удаления подзадач обновить его статус
        int epicId = subTasks.get(idNumber).getEpicId();
        // удалить идентификаторы подзадач из эпика
        epics.get(epicId).getSubTaskIds().remove((Integer) idNumber);
        // создать новый sortedList без удаленной SubTask
        sortedTasks.remove(subTasks.get(idNumber));
        // удалить subTask из истории просмотров
        historyManager.remove(idNumber);
        // удалить subTask из subTaskHashMap
        subTasks.remove(idNumber);
        // проверить статус эпика
        updateEpicStatus(epicId);
        // обновить время Epic'а
        updateEpicTime(epics.get(epicId));
    }

    @Override
    public int generateId() {
        return idNumber++;
    }

    @Override
    public Integer getId() {
        return idNumber;
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return historyManager.getHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}
