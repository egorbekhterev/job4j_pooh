package ru.job4j.pooh.service;

import org.junit.jupiter.api.Test;
import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import static org.assertj.core.api.Assertions.*;

class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text()).isEqualTo("temperature=18");
        assertThat(result.status()).isEqualTo(Resp.OK);
    }

    @Test
    public void whenGetQueueWithNoData() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text()).isEqualTo("");
        assertThat(result.status()).isEqualTo(Resp.NO_CONTENT);
    }
}
