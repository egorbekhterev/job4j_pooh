package ru.job4j.pooh.service;

import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

public interface Service {
    /**
     * Логика работы сервиса.
     * @param req запрос после парсинга.
     * @return Ответ от сервиса.
     */
    Resp process(Req req);
}
