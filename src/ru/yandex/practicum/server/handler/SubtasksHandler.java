package ru.yandex.practicum.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.exception.ManagerTaskNotFoundException;
import ru.yandex.practicum.server.HttpTaskServer;
import ru.yandex.practicum.manager.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class SubtasksHandler extends AbstractHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                    // ecли путь "/subtasks"
                    if (Pattern.matches("^/subtasks$", path)) {
                        String response = gson.toJson(taskManager.getAllSubTask());
                        writeResponse(exchange, response);
                        break;
                    }

                    // ecли путь "/subtasks/{id}"
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.substring(10);
                        int id = parsePathId(pathId);
                        String response = gson.toJson(taskManager.getSubTaskById(id));
                        writeResponse(exchange, response);
                        break;
                    }
                    break;
                }
                case "DELETE": {
                    // ecли путь "/subtasks?id=[id]"
                    if (Pattern.matches("^/subtasks$", path)) {
                        String query = exchange.getRequestURI().getQuery();

                        if (query != null) {
                            String pathId = query.substring(3);
                            int id = parsePathId(pathId);
                            taskManager.deleteSubTasks(id);
                            sendDeletedTaskContentResponseHeaders(exchange, id);
                        } else if (query == null) {
                            taskManager.removeAllSubtasks();
                            sendDeletedAllTasksContentResponseHeaders(exchange);
                        } else {
                            sendNotFoundIdInQueryStringResponseHeaders(exchange);
                        }
                    }
                    break;
                }
                case "POST": {
                    String request = readRequest(exchange);
                    SubTask subTask = gson.fromJson(request, SubTask.class);

                    // проверка задачи на пересечение с остальными задачами в sortedList
                    if (taskManager.isCrossingTasks(subTask)) {
                        sendIsCrossingTasksResponseHeaders(exchange);
                        break;
                    }

                    // ecли путь "/subtasks?id=[id]"
                    if (Pattern.matches("^/subtasks$", path)) {
                        if (subTask.getIdNumber() != null) {
                            taskManager.updateSubTask(subTask);
                            sendUpdatedTaskContentResponseHeaders(exchange, subTask.getIdNumber());
                        } else {
                            taskManager.createSubTask(subTask);
                            sendCreatedTaskContentResponseHeaders(exchange, subTask.getIdNumber());
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
        } catch (Throwable exception) {
            exception.printStackTrace();
            sendInternalServerErrorResponseHeaders(exchange);
        }
    }
}
