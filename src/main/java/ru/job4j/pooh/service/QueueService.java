package ru.job4j.pooh.service;

import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

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
                status = "204";
            }
        }
        return new Resp(text, status);
    }
}
