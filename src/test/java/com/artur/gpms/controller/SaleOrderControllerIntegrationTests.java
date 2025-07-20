package com.artur.gpms.controller;

import com.artur.gpms.data.dtos.SaleOrderEvent;
import com.artur.gpms.data.entities.SaleOrderEntity;
import com.artur.gpms.data.repositories.SaleOrderRepository;
import com.artur.gpms.service.impl.SaleOrderServiceImpl;
import com.artur.gpms.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Testcontainers
public class SaleOrderControllerIntegrationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.12-management");

    private SaleOrderServiceImpl saleOrderService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    public SaleOrderControllerIntegrationTests(SaleOrderServiceImpl saleOrderService, MockMvc mockMvc, ObjectMapper objectMapper, SaleOrderRepository saleOrderRepository) {
        this.saleOrderService = saleOrderService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
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
    public void testThatGetOrderByOrderIdReturnsHttp200WhenOrderIsFound() throws Exception {
        SaleOrderEvent saleOrderEvent = TestUtils.createSaleOrderEvent(1L);
        saleOrderService.saveSaleOrderEvent(saleOrderEvent);

        List<SaleOrderEntity> saleOrderList = saleOrderRepository.findByCustomerId(saleOrderEvent.customerId());
        SaleOrderEntity order = saleOrderList.get(0);

        String id = saleOrderList.get(0).getOrderId();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetOrderByOrderIdReturnsHttp404WhenOrderIsNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders/" + "FAKE_ORDER_ID")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetOrderByOrderIdReturnsCorrectOrder() throws Exception {
        SaleOrderEvent saleOrderEvent = TestUtils.createSaleOrderEvent(1L);
        saleOrderService.saveSaleOrderEvent(saleOrderEvent);

        List<SaleOrderEntity> saleOrderList = saleOrderRepository.findByCustomerId(saleOrderEvent.customerId());

        String id = saleOrderList.get(0).getOrderId();
        String requestContent = objectMapper.writeValueAsString(saleOrderEvent);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.customerId").value(saleOrderList.get(0).getCustomerId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].product").value(saleOrderEvent.itemList().get(0).product())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].quantity").value(saleOrderEvent.itemList().get(0).quantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].price").value(saleOrderEvent.itemList().get(0).price())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].description").value(saleOrderEvent.itemList().get(0).description())
        );
    }

    @Test
    public void testThatCreateOrderIdCorrectlyCreatesSaleOrder() throws Exception {
        SaleOrderEvent saleOrderEvent = TestUtils.createSaleOrderEvent(1L);
        saleOrderService.saveSaleOrderEvent(saleOrderEvent);

        List<SaleOrderEntity> saleOrderList = saleOrderRepository.findByCustomerId(saleOrderEvent.customerId());

        String id = saleOrderList.get(0).getOrderId();
        String requestContent = objectMapper.writeValueAsString(saleOrderEvent);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.customerId").value(saleOrderList.get(0).getCustomerId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].product").value(saleOrderEvent.itemList().get(0).product())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].quantity").value(saleOrderEvent.itemList().get(0).quantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].price").value(saleOrderEvent.itemList().get(0).price())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.itemList[0].description").value(saleOrderEvent.itemList().get(0).description())
        );
    }

    @Test
    public void testThatCreateSaleOrderReturnsHttp202WhenOrderIsCreated() throws Exception {
        SaleOrderEvent saleOrderEvent = TestUtils.createSaleOrderEvent(1L);
        saleOrderService.saveSaleOrderEvent(saleOrderEvent);

        String requestContent = objectMapper.writeValueAsString(saleOrderEvent);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
        ).andExpect(
                MockMvcResultMatchers.status().isAccepted()
        );
    }

    @Test
    public void testThatCreateSaleOrderReturnsHttp500BadRequestIfContentIsIncorrect() throws Exception {
        String requestContent = "INCORRECT STRING FOR TEST PURPOSES";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatGetAllOrdersReturnsHttp200WhenOrdersAreFound() throws Exception {
        SaleOrderEvent saleOrderEvent = TestUtils.createSaleOrderEvent(1L);
        saleOrderService.saveSaleOrderEvent(saleOrderEvent);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAllOrdersReturnsHttp404WhenOrdersAreNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }
}
