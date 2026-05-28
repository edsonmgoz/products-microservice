package dev.edsonmm.products.domain.mapper;

import dev.edsonmm.products.domain.entity.Product;
import dev.edsonmm.products.presentation.request.ProductRequest;
import dev.edsonmm.products.presentation.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
    }

    @Test
    void toEntity_shouldMapAllFields() {
        ProductRequest request = buildRequest("Laptop", "Laptop gamer", new BigDecimal("1500.00"), 10, true);

        Product product = mapper.toEntity(request);

        assertThat(product.getName()).isEqualTo("Laptop");
        assertThat(product.getDescription()).isEqualTo("Laptop gamer");
        assertThat(product.getPrice()).isEqualByComparingTo("1500.00");
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.isActive()).isTrue();
    }

    @Test
    void toEntity_shouldMapWithNullDescription() {
        ProductRequest request = buildRequest("Mouse", null, new BigDecimal("25.00"), 50, true);

        Product product = mapper.toEntity(request);

        assertThat(product.getDescription()).isNull();
    }

    @Test
    void toEntity_shouldMapActiveAsFalse() {
        ProductRequest request = buildRequest("Teclado", null, new BigDecimal("75.00"), 5, false);

        Product product = mapper.toEntity(request);

        assertThat(product.isActive()).isFalse();
    }

    @Test
    void updateEntity_shouldOverwriteAllFields() {
        Product existing = new Product();
        existing.setName("Viejo");
        existing.setPrice(new BigDecimal("100.00"));
        existing.setStock(1);
        existing.setActive(true);

        ProductRequest request = buildRequest("Nuevo", "Desc nueva", new BigDecimal("200.00"), 20, false);
        mapper.updateEntity(request, existing);

        assertThat(existing.getName()).isEqualTo("Nuevo");
        assertThat(existing.getDescription()).isEqualTo("Desc nueva");
        assertThat(existing.getPrice()).isEqualByComparingTo("200.00");
        assertThat(existing.getStock()).isEqualTo(20);
        assertThat(existing.isActive()).isFalse();
    }

    @Test
    void updateEntity_shouldOverwriteDescriptionWithNull() {
        Product existing = new Product();
        existing.setDescription("Descripción anterior");

        ProductRequest request = buildRequest("Producto", null, new BigDecimal("10.00"), 1, true);
        mapper.updateEntity(request, existing);

        assertThat(existing.getDescription()).isNull();
    }

    @Test
    void toResponse_shouldMapAllFields() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Monitor");
        product.setDescription("Monitor 4K");
        product.setPrice(new BigDecimal("800.00"));
        product.setStock(5);
        product.setActive(true);
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        ProductResponse response = mapper.toResponse(product);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Monitor");
        assertThat(response.description()).isEqualTo("Monitor 4K");
        assertThat(response.price()).isEqualByComparingTo("800.00");
        assertThat(response.stock()).isEqualTo(5);
        assertThat(response.active()).isTrue();
        assertThat(response.createdAt()).isEqualTo(now);
        assertThat(response.updatedAt()).isEqualTo(now);
    }

    @Test
    void toResponse_shouldMapWithNullDescriptionAndTimestamps() {
        Product product = new Product();
        product.setId(2L);
        product.setName("Audífonos");
        product.setDescription(null);
        product.setPrice(new BigDecimal("50.00"));
        product.setStock(15);
        product.setActive(false);

        ProductResponse response = mapper.toResponse(product);

        assertThat(response.description()).isNull();
        assertThat(response.createdAt()).isNull();
        assertThat(response.updatedAt()).isNull();
        assertThat(response.active()).isFalse();
    }

    private ProductRequest buildRequest(String name, String description, BigDecimal price, int stock, boolean active) {
        ProductRequest r = new ProductRequest();
        r.setName(name);
        r.setDescription(description);
        r.setPrice(price);
        r.setStock(stock);
        r.setActive(active);
        return r;
    }
}
