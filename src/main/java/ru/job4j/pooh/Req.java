package ru.job4j.pooh;

import java.util.ArrayList;
import java.util.List;

/**
 * Req - класс служит для парсинга входящего запроса.
 */
public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String QUEUE = "queue";
    private static final String TOPIC = "topic";

    /**
     * httpRequestType - GET или POST. Он указывает на тип запроса.
     * poohMode - указывает на режим работы: queue или topic.
     * sourceName - имя очереди или топика.
     * param - содержимое запроса.
     */
    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    /**
     * Выполняет выделение имени очереди/топика или содержимого запроса.
     * @param array массив разделенный регексом.
     * @param pointer указатель на элемент списка, куда хотим добавить значение String.
     * @return Имя или содержимое запроса в виде строки.
     */
    private static String splitSplashExtractor(String[] array, int pointer) {
        return array[pointer].substring(0, array[pointer].indexOf(" "));
    }

    /** Выполняет парсинг строки входящего запроса.
     * @param content входящий запрос.
     * @return список параметров входящего запроса.
     */
    private static List<String> contentParser(String content) {
        List<String> rsl = new ArrayList<>();
        String[] whitespaceSplit = content.split(" ", 2);
        String[] slashSplit = content.split("/", 4);
        String[] lsSplit = content.split(System.lineSeparator());
        if (GET.matches(whitespaceSplit[0])) {
            rsl.add(0, GET);
        }
        if (POST.matches(whitespaceSplit[0])) {
            rsl.add(0, POST);
        }
        if (QUEUE.matches(slashSplit[1])) {
            rsl.add(1, QUEUE);
        }
        if (TOPIC.matches(slashSplit[1])) {
            rsl.add(1, TOPIC);
        }
        if (GET.equals(rsl.get(0)) && TOPIC.equals(rsl.get(1))) {
            rsl.add(2, slashSplit[2]);
            rsl.add(3, splitSplashExtractor(slashSplit, 3));
        } else if (POST.equals(rsl.get(0))) {
            rsl.add(2, splitSplashExtractor(slashSplit, 2));
            rsl.add(3, lsSplit[lsSplit.length - 1]);
        } else {
            rsl.add(2, splitSplashExtractor(slashSplit, 2));
            rsl.add(3, "");
        }
        return rsl;
    }

    /** Выполняет парсинг строки входящего запроса.
     * @param content входящий запрос.
     * @return объект Req, содержащий тип запроса, режим работы, имя очереди или топика, содержимое запроса.
     */
    public static Req of(String content) {
        var rsl = contentParser(content);
        return new Req(rsl.get(0), rsl.get(1), rsl.get(2), rsl.get(3));
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
