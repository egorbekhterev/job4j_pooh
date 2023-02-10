package ru.job4j.pooh;

/** Resp - ответ от сервиса.
 * text - текст ответа.
 * status  - HTTP response status code.*/
public class Resp {
    private final String text;
    private final String status;

    public Resp(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String text() {
        return text;
    }

    public String status() {
        return status;
    }
}
