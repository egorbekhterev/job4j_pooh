package ru.job4j.pooh.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CASMap {
    public static void main(String[] args) {
        Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
        String name = "weather";

        /* add if empty */
        queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());

        /* put */
        queue.get(name).add("value");

        /* extract */
        var text = queue.getOrDefault(name, new ConcurrentLinkedQueue<>()).poll();

        System.out.println(text);
    }
}
