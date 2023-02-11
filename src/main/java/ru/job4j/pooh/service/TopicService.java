package ru.job4j.pooh.service;

import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Режим topic. Для каждого потребителя своя уникальная очередь.
 */
public class TopicService implements Service {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private final Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic = new ConcurrentHashMap<>();

    /**
     * Отправитель посылает запрос на добавление данных. Сообщение помещается в конец
     * каждой индивидуальной очереди получателей. Если топика нет в сервисе, то данные игнорируются.
     * Получатель посылает запрос на получение данных с указанием топика. Если топик отсутствует, то создается новый.
     * Если топик присутствует, то сообщение забирается из начала индивидуальной очереди получателя и удаляется.
     * Когда получатель впервые получает данные из топика – для него создается индивидуальная пустая очередь.
     * Все последующие сообщения от отправителей с данными для этого топика так же помещаются в эту очередь.
     * @param req входящий запрос.
     * @return ответ от сервиса.
     */
    @Override
    public Resp process(Req req) {
        String name = req.getSourceName();
        String param = req.getParam();
        String text = "";
        String status = "200";
        if (POST.equals(req.httpRequestType())) {
            var innerMap = topic.getOrDefault(name,
                    new ConcurrentHashMap<>());
            for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry : innerMap.entrySet()) {
                entry.getValue().add(param);
            }
        } else if (GET.equals(req.httpRequestType())) {
            topic.putIfAbsent(name, new ConcurrentHashMap<>());
            topic.get(name).putIfAbsent(param, new ConcurrentLinkedQueue<>());
            var paramQueue = topic.get(name).get(param);
            text = paramQueue.poll();
            if (text == null) {
                text = "";
                status = "204";
            }
        }
        return new Resp(text, status);
    }
}
