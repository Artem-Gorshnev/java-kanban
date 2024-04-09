package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private final Map<Integer, Node> history = new HashMap<>(); // храним историю

    @Override
    public void add(Task task) { // Метод для добавления задачи в историю просмотров
        if (task != null) {
            Integer taskId = task.getIdNumber();
            remove(taskId);
            linkLast(task);
            history.put(taskId, tail);
        }
    }

    private void linkLast(Task task) {
        final Node l = tail;
        final Node newNode = new Node(l, task, null);
        tail = newNode;
        if (l == null)
            head = newNode;
        else
            l.setNext(newNode);
    }

    @Override
    public void remove(int id) { // Метод для удаления задачи из просмотра
        removeNode(history.get(id));
    }

    public void removeNode(Node node) {
        if (node != null) {
            final Task element = node.getObject();
            final Node next = node.getNext();
            final Node prev = node.getPrevious();
            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
                node.setPrevious(null);
            }
            if (next == null) {
                tail = prev;
            } else {
                next.setPrevious(prev);
                node.setNext(null);
            }
            node.setObject(null);
        }
    }

    @Override
    public List<Task> getHistory() { // История просмотра задач
        return getTasks();
    }

    private List<Task> getTasks() { // собираем все задачи в обычный список
        List<Task> task = new ArrayList<>();
        Node node = head;

        while (node != null) {
            task.add(node.getObject());
            node = node.getNext();
        }
        return task;
    }
}
