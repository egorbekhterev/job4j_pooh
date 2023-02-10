package ru.job4j.pooh.service;

import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private final Map<String, ConcurrentMap<String, ConcurrentLinkedQueue<String>>> topic = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        return null;
    }
}
