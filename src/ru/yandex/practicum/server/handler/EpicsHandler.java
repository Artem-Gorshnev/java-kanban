package ru.yandex.practicum.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.exception.ManagerTaskNotFoundException;
import ru.yandex.practicum.server.HttpTaskServer;
import ru.yandex.practicum.manager.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class EpicsHandler extends AbstractHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
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
                    // ecли путь "/epics"
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getAllEpic());
                        writeResponse(exchange, response);
                        break;
                    }

                    // ecли путь "/epics/{id}"
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.substring(7);
                        int id = parsePathId(pathId);
                        String response = gson.toJson(taskManager.getEpicById(id));
                        writeResponse(exchange, response);
                        break;
                    }

                    // ecли путь "/epics/{id}/subtasks"
                    if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String pathId = path.substring(7, (path.length() - 9));
                        int id = parsePathId(pathId);
                        String response = gson.toJson(taskManager.getAllEpicSubtasks(id));
                        writeResponse(exchange, response);
                        break;
                    }
                    break;
                }
                case "DELETE": {
                    // ecли путь "/epics?id=[id]"
                    if (Pattern.matches("^/epics$", path)) {
                        String query = exchange.getRequestURI().getQuery();

                        if (query != null) {
                            String pathId = query.substring(3);
                            int id = parsePathId(pathId);
                            taskManager.deleteEpic(id);
                            sendDeletedTaskContentResponseHeaders(exchange, id);
                        } else if (query == null) {
                            taskManager.removeAllEpics();
                            sendDeletedAllTasksContentResponseHeaders(exchange);
                        } else {
                            sendNotFoundIdInQueryStringResponseHeaders(exchange);
                        }
                    }
                    break;
                }
                case "POST": {
                    String request = readRequest(exchange);
                    Epic epic = gson.fromJson(request, Epic.class);

                    // ecли путь "/epics"
                    if (Pattern.matches("^/epics$", path)) {
                        if (epic.getIdNumber() != null) {
                            taskManager.updateEpic(epic);
                            sendUpdatedTaskContentResponseHeaders(exchange, epic.getIdNumber());
                        } else {
                            taskManager.createEpic(epic);
                            sendCreatedTaskContentResponseHeaders(exchange, epic.getIdNumber());
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
