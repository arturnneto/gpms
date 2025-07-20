package com.artur.gpms.events;

import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.repositories.SaleOrderRepository;
import com.artur.gpms.utils.TestUtils;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static com.artur.gpms.config.mq.RabbitMqConfig.SALES_ORDER_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SaleOrderEventIntegrationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.12-management");

    private RabbitTemplate rabbitTemplate;
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    public SaleOrderEventIntegrationTests(RabbitTemplate rabbitTemplate, SaleOrderRepository saleOrderRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.saleOrderRepository = saleOrderRepository;
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        rabbitMQContainer.start();

        registry.add("spring.data.mongodb.uri", () ->
                "mongodb://localhost:" + mongoDBContainer.getMappedPort(27017) + "/test");

        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getAmqpPort());

        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
    }

    @Test
    void shouldConsumeMessageAndSaveToMongoDB() {
        SaleOrderEvent event = TestUtils.createSaleOrderEvent(123L);
        rabbitTemplate.convertAndSend(SALES_ORDER_QUEUE, event);

        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var resultList = saleOrderRepository.findByCustomerId(123L);
                    assertThat(resultList).isNotEmpty();

                    assertThat(resultList.get(0).getItemList()).isNotEmpty();
                });
    }
}
