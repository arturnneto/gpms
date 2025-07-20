package com.artur.gpms.repository;

import com.artur.gpms.data.entities.SaleOrderEntity;
import com.artur.gpms.data.repositories.SaleOrderRepository;
import com.artur.gpms.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@Testcontainers
public class SaleOrderRepositoryIntegrationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    private SaleOrderRepository saleOrderRepository;

    @Autowired
    public SaleOrderRepositoryIntegrationTests(SaleOrderRepository saleOrderRepository) {
        this.saleOrderRepository = saleOrderRepository;
    }

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        String uri = "mongodb://localhost:" + mongoDBContainer.getMappedPort(27017) + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    @Test
    public void testThatFindAllReturnsAllSaleOrders() {
        SaleOrderEntity saleOrderEntity = TestUtils.createSaleOrder("ID1", 1L, BigDecimal.valueOf(10));
        SaleOrderEntity saleOrderEntityTwo = TestUtils.createSaleOrder("ID2", 2L, BigDecimal.valueOf(20));
        SaleOrderEntity saleOrderEntityThree = TestUtils.createSaleOrder("ID3", 3L, BigDecimal.valueOf(30));

        saleOrderRepository.save(saleOrderEntity);
        saleOrderRepository.save(saleOrderEntityTwo);
        saleOrderRepository.save(saleOrderEntityThree);

        Iterable<SaleOrderEntity> result = saleOrderRepository.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(SaleOrderEntity::getOrderId)
                .containsExactlyInAnyOrder("ID1", "ID2", "ID3");
    }

    @Test
    public void testThatFindByIdReturnsCorrectSaleOrder() {
        SaleOrderEntity saleOrderEntity = TestUtils.createSaleOrder("ID1", 1L, BigDecimal.valueOf(10));
        saleOrderRepository.save(saleOrderEntity);

        Optional<SaleOrderEntity> saleOrderFromRepository = saleOrderRepository.findByOrderId("ID1");

        assertThat(saleOrderFromRepository).isPresent();
        assertThat(saleOrderFromRepository.get().getOrderId()).isEqualTo(saleOrderEntity.getOrderId());
        assertThat(saleOrderFromRepository.get().getCustomerId()).isEqualTo(saleOrderEntity.getCustomerId());

    }
}
