package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>(); // храним историю
    private Node first;
    private Node last;

    private void linkLast(Task task) { //Добавление элемента в конец списка
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.setNext(newNode);
        }
    }

    private List<Task> getTasks() { // собираем все задачи в обычный список
        List<Task> task = new ArrayList<>();
        Node node = first;

        while (node != null) {
            task.add(node.getObject());
            node = node.getNext();
        }
        return task;
    }

    public void removeNode(Node node) { // Метод удаления узла из связного списка
        if (node != null) {
            final Node next = node.getNext();
            final Node prev = node.getPrevious();
            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
            }
            if (next == null) {
                last = prev;
            } else {
                next.setPrevious(prev);
            }
        }
    }

    @Override
    public void add(Task task) { // Метод для добавления задачи в историю просмотров
        if (history.isEmpty()) {
            linkLast(task);
            history.put(task.getIdNumber(), last);
        } else {
            if (history.containsKey(task.getIdNumber())) {
                remove(task.getIdNumber());
                linkLast(task);
                history.put(task.getIdNumber(), last);
            } else {
                linkLast(task);
                history.put(task.getIdNumber(), last);
            }
        }
    }

    @Override
    public void remove(int id) { // Метод для удаления задачи из просмотра
        removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return getTasks();
    }
}
