package ru.yandex.practicum.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.exception.ManagerTaskNotFoundException;
import ru.yandex.practicum.server.HttpTaskServer;
import ru.yandex.practicum.manager.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class TasksHandler extends AbstractHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try (exchange) {
            switch (method) {
                case "GET": {
                    // ecли путь "/tasks"
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getAllTasks());
                        writeResponse(exchange, response);
                        break;
                    }

                    // ecли путь "/tasks/{id}"
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.substring(7);
                        int id = parsePathId(pathId);
                        String response = gson.toJson(taskManager.getTaskById(id));
                        writeResponse(exchange, response);
                        break;
                    }
                    break;
                }
                case "DELETE": {
                    // ecли путь "/tasks?id=[id]"
                    if (Pattern.matches("^/tasks$", path)) {
                        String query = exchange.getRequestURI().getQuery();

                        if (query != null) {
                            String pathId = query.substring(3);
                            int id = parsePathId(pathId);
                            taskManager.deleteTask(id);
                            sendDeletedTaskContentResponseHeaders(exchange, id);
                        } else if (query == null) {
                            taskManager.removeAllTasks();
                            sendDeletedAllTasksContentResponseHeaders(exchange);
                        } else {
                            sendNotFoundIdInQueryStringResponseHeaders(exchange);
                        }
                    }
                    break;
                }
                case "POST": {
                    String request = readRequest(exchange);
                    Task task = gson.fromJson(request, Task.class);

                    // проверка задачи на пересечение с остальными задачами в sortedList
                    if (taskManager.isCrossingTasks(task)) {
                        sendIsCrossingTasksResponseHeaders(exchange);
                        break;
                    }

                    // ecли путь "/tasks"
                    if (Pattern.matches("^/tasks$", path)) {
                        if (task.getIdNumber() != null) {
                            taskManager.updateTask(task);
                            sendUpdatedTaskContentResponseHeaders(exchange, task.getIdNumber());
                        } else {
                            taskManager.createTask(task);
                            sendCreatedTaskContentResponseHeaders(exchange, task.getIdNumber());
                        }
                    }
                    break;
                }
                default: {
                    sendNotFoundEndpointResponseHeaders(exchange, method);
                }
            }
        } catch (NumberFormatException exception) {
            sendErrorRequestResponseHeaders(exchange);
        } catch (ManagerTaskNotFoundException exception) {
            sendNotFoundRequestResponseHeaders(exchange);
        } catch (Exception exception) {
            exception.printStackTrace();
        } catch (Throwable exception) {
            exception.printStackTrace();
            sendInternalServerErrorResponseHeaders(exchange);
        }
    }
}