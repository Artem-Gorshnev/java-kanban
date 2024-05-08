package ru.yandex.practicum.exception;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message) { //Конструктор с коротким сообщением
        super(message);
    }
}
