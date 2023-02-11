package ru.job4j.pooh.service;

import org.junit.jupiter.api.Test;
import ru.job4j.pooh.Req;
import ru.job4j.pooh.Resp;

import static org.assertj.core.api.Assertions.*;

class TopicServiceTest {

     @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Пытаемся забрать данные из индивидуальной очереди в топике weather. Очередь client6565.
        Эта очередь отсутствует, т.к. client6565 еще не был подписан, поэтому он получит пустую строку.
        Будет создана индивидуальная очередь для client6565. */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text()).isEqualTo("temperature=18");
        assertThat(result1.status()).isEqualTo("200");
        assertThat(result2.text()).isEqualTo("");
        assertThat(result2.status()).isEqualTo("204");
    }

    @Test
    public void whenTopicSequentialPost() {
        TopicService topicService = new TopicService();
        String paramForPublisher1 = "temperature=18";
        String paramForPublisher2 = "temperature=22";
        String paramForSubscriber1 = "client407";
        topicService.process(new Req(
                "GET", "topic", "weather", paramForSubscriber1));
        topicService.process(new Req(
                "POST", "topic", "weather", paramForPublisher1)
        );
        topicService.process(new Req(
                "POST", "topic", "weather", paramForPublisher2)
        );
        Resp rsl1 = topicService.process(new Req(
                "GET", "topic", "weather", paramForSubscriber1));
        Resp rsl2 = topicService.process(new Req(
                "GET", "topic", "weather", paramForSubscriber1));
        assertThat(rsl1.text()).isEqualTo("temperature=18");
        assertThat(rsl1.status()).isEqualTo("200");
        assertThat(rsl2.text()).isEqualTo("temperature=22");
        assertThat(rsl2.status()).isEqualTo("200");
    }

    @Test
    public void whenTopicHasNoData() {
        TopicService topicService = new TopicService();
        String paramForSubscriber1 = "client407";
        Resp rsl = topicService.process(new Req(
                "GET", "topic", "weather", paramForSubscriber1));
        assertThat(rsl.text()).isEqualTo("");
        assertThat(rsl.status()).isEqualTo("204");
    }
}
