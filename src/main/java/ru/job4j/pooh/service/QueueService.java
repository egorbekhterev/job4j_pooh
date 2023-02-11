package ru.job4j.pooh.service;

import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Режим queue. Потребители получают данные из общей очереди.
 */
public class QueueService implements Service {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    /**
     * Отправитель посылает запрос на добавление данных, сообщение помещается
     * в конец очереди. Если очереди нет в сервисе, создается новая очередь.
     * Получатель посылает запрос на получение данных с указанием очереди.
     * Сообщение забирается из начала очереди и удаляется.
     * @param req входящий запрос.
     * @return ответ от сервиса.
     */
    @Override
    public Resp process(Req req) {
        String text = "";
        String status = "200";
        if (POST.equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(req.getParam());
        } else if (GET.equals(req.httpRequestType())) {
            text = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
            if (text == null) {
                text = "";
                status = "204";
            }
        }
        return new Resp(text, status);
    }
}
