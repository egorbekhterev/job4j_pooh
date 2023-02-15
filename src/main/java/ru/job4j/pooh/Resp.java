package ru.job4j.pooh;

/**
 * Resp - ответ от сервиса.
 * text - текст ответа.
 * status  - HTTP response status code.
 */
public class Resp {

    public static final String OK = "200";
    public static final String NO_CONTENT = "204";
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
