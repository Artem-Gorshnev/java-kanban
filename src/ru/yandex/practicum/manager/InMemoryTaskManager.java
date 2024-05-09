package ru.yandex.practicum.manager;

import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.exception.ManagerTaskNotFoundException;
import ru.yandex.practicum.tasks.*;

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
                    .filter(t -> t.getEndTime() != null && !t.equals(task) && (t.getIdNumber() != task.getIdNumber()))
                    .allMatch(t -> (
                            (task.getStartTime().isBefore(t.getStartTime())
                                    && task.getEndTime().isBefore(t.getStartTime())) ||
                                    (task.getStartTime().isAfter(t.getEndTime())
                                            && task.getEndTime().isAfter(t.getEndTime())) ||
                                    (task.getEndTime().equals(t.getStartTime()) || task.getStartTime().equals(t.getEndTime()))));
        } else if (task.getStartTime() == null && task.getDuration() == null) {
            return false;
        }
        return true;
    }

    protected void updateEpicTime(Epic epic) {
        Duration epicDuration;
        LocalDateTime earlyStartTime = LocalDateTime.MAX;
        LocalDateTime lateEndTime = LocalDateTime.MIN;

        if (epic.getSubTaskIds() != null) {
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
    public void createTask(Task newTask) { // создание новой задачи
        newTask.setIdNumber(++idNumber);
        // если по времени задачи пересекаются - обнулить время добавляемой задачи
        if (isCrossingTasks(newTask)) {
            throw new ManagerSaveException("Задача не сохранена. Время выполнения задачи пересекается" +
                    " со временем существующих задач");
        } else {
            sortedTasks.add(newTask);
        }
        tasks.put(newTask.getIdNumber(), newTask);
    }

    @Override
    public void createEpic(Epic newEpic) { // создание нового эпика
        newEpic.setIdNumber(++idNumber);
        updateEpicTime(newEpic);
        epics.put(newEpic.getIdNumber(), newEpic);
    }


    @Override
    public void createSubTask(SubTask newSubTask) { // создание новой подзадачи
        // если по времени задачи пересекаются - обнулить время добавляемой задачи
        newSubTask.setIdNumber(++idNumber);
        if (isCrossingTasks(newSubTask)) {
            throw new ManagerSaveException("Подзадача не сохранена. Время выполнения подзадачи пересекается" +
                    " со временем существующих задач");
        } else {
            sortedTasks.add(newSubTask);
        }

        if (epics.get(newSubTask.getEpicId()) != null && epics.containsKey(newSubTask.getEpicId())) {
            subTasks.put(newSubTask.getIdNumber(), newSubTask);
            ArrayList<Integer> subTaskIdList = epics.get(newSubTask.getEpicId()).getSubTaskIds();
            subTaskIdList.add(newSubTask.getIdNumber());
            updateEpicStatus(newSubTask.getEpicId());
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
        if (tasks.get(idNumber) != null && tasks.containsKey(idNumber)) {
            historyManager.add(tasks.get(idNumber));
            return tasks.get(idNumber);
        } else {
            throw new ManagerTaskNotFoundException(TaskType.TASK);
        }
    }

    @Override
    public Epic getEpicById(int idNumber) { // Получение Эпика по идентификатору
        if (epics.get(idNumber) != null && epics.containsKey(idNumber)) {
            historyManager.add(epics.get(idNumber));
            return epics.get(idNumber);
        } else {
            throw new ManagerTaskNotFoundException(TaskType.EPIC);
        }
    }

    @Override
    public SubTask getSubTaskById(int idNumber) { // Получение Подзадач по идентификатору
        if (subTasks.get(idNumber) != null && subTasks.containsKey(idNumber)) {
            historyManager.add(subTasks.get(idNumber));
            return subTasks.get(idNumber);
        } else {
            throw new ManagerTaskNotFoundException(TaskType.SUBTASK);
        }
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
                throw new ManagerSaveException("Задача не сохранена. Время выполнения задачи пересекается" +
                        " со временем существующих задач");
            }

            tasks.put(task.getIdNumber(), task);

            // создать новый sortedList без старой Task и добавить newTask
            sortedTasks = sortedTasks.stream()
                    .filter(t -> t.getIdNumber() != task.getIdNumber())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));
            sortedTasks.add(task);
        } else {
            throw new ManagerTaskNotFoundException(task.getType());
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) { // обновление подзадачи
        if (subTasks.containsKey(newSubTask.getIdNumber())) {
            if (isCrossingTasks(newSubTask)) {
                throw new ManagerSaveException("Подзадача не сохранена. Время выполнения подзадачи пересекается" +
                        " со временем существующих задач");
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
        } else {
            throw new ManagerTaskNotFoundException(newSubTask.getType());
        }
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
        if (epics.containsKey(epic.getIdNumber())) {
            updateEpicTime(epic);
            epics.put(epic.getIdNumber(), epic);
        } else {
            throw new ManagerTaskNotFoundException(epic.getType());
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
        if (tasks.get(idNumber) != null && tasks.containsKey(idNumber)) {
            // удалить Task из SortedList
            sortedTasks.remove(tasks.get(idNumber));
            historyManager.remove(idNumber);
            tasks.remove(idNumber);
        } else {
            throw new ManagerTaskNotFoundException(TaskType.TASK);
        }
    }

    @Override
    public void deleteEpic(Integer idNumber) {  //удаление эпика
        if (epics.get(idNumber) != null && epics.containsKey(idNumber)) {
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
        } else {
            throw new ManagerTaskNotFoundException(TaskType.EPIC);
        }
    }

    @Override
    public void deleteSubTasks(Integer idNumber) { // удаление подзадачи
        if (subTasks.get(idNumber) != null && subTasks.containsKey(idNumber)) {
            int epicId = subTasks.get(idNumber).getEpicId();
            epics.get(epicId).getSubTaskIds().remove((Integer) idNumber);
            // создать новый sortedList без удаленной SubTask
            sortedTasks.remove(subTasks.get(idNumber));
            historyManager.remove(idNumber);
            subTasks.remove(idNumber);
            updateEpicStatus(epicId);
            // обновить время Epic'а
            updateEpicTime(epics.get(epicId));
        } else {
            throw new ManagerTaskNotFoundException(TaskType.SUBTASK);
        }
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
