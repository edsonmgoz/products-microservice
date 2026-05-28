package dev.edsonmm.products.domain.service;

import dev.edsonmm.products.data.repository.ProductRepository;
import dev.edsonmm.products.domain.entity.Product;
import dev.edsonmm.products.domain.service.interfaces.ProductService;
import dev.edsonmm.products.exception.ProductNotFoundException;
import dev.edsonmm.products.presentation.request.ProductRequest;
import dev.edsonmm.products.presentation.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        productRepository.save(buildProduct("Laptop", new BigDecimal("1500.00"), 5));
        productRepository.save(buildProduct("Mouse", new BigDecimal("25.00"), 100));

        List<ProductResponse> products = productService.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting(ProductResponse::name)
                .containsExactlyInAnyOrder("Laptop", "Mouse");
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoProducts() {
        assertThat(productService.findAll()).isEmpty();
    }

    @Test
    void findById_shouldReturnProductWhenExists() {
        Product saved = productRepository.save(buildProduct("Teclado", new BigDecimal("75.00"), 20));

        ProductResponse found = productService.findById(saved.getId());

        assertThat(found.name()).isEqualTo("Teclado");
        assertThat(found.price()).isEqualByComparingTo("75.00");
        assertThat(found.stock()).isEqualTo(20);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        assertThatThrownBy(() -> productService.findById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void create_shouldPersistAndReturnProduct() {
        ProductRequest request = buildRequest("Monitor", "Monitor 4K", new BigDecimal("800.00"), 3);

        ProductResponse created = productService.create(request);

        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo("Monitor");
        assertThat(created.description()).isEqualTo("Monitor 4K");
        assertThat(created.price()).isEqualByComparingTo("800.00");
        assertThat(created.stock()).isEqualTo(3);
        assertThat(created.active()).isTrue();
    }

    @Test
    void create_shouldSetTimestampsOnCreation() {
        ProductResponse created = productService.create(buildRequest("Audífonos", null, new BigDecimal("50.00"), 15));

        assertThat(created.createdAt()).isNotNull();
        assertThat(created.updatedAt()).isNotNull();
    }

    @Test
    void update_shouldModifyAndReturnProduct() {
        Product saved = productRepository.save(buildProduct("Viejo", new BigDecimal("100.00"), 1));
        ProductRequest request = buildRequest("Nuevo", "Actualizado", new BigDecimal("200.00"), 5);

        ProductResponse updated = productService.update(saved.getId(), request);

        assertThat(updated.name()).isEqualTo("Nuevo");
        assertThat(updated.description()).isEqualTo("Actualizado");
        assertThat(updated.price()).isEqualByComparingTo("200.00");
        assertThat(updated.stock()).isEqualTo(5);
    }

    @Test
    void update_shouldThrowWhenProductNotFound() {
        assertThatThrownBy(() -> productService.update(999L, buildRequest("X", null, new BigDecimal("10.00"), 1)))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void delete_shouldRemoveProduct() {
        Product saved = productRepository.save(buildProduct("A eliminar", new BigDecimal("30.00"), 2));
        Long id = saved.getId();

        productService.delete(id);

        assertThat(productRepository.findById(id)).isEmpty();
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        assertThatThrownBy(() -> productService.delete(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    private Product buildProduct(String name, BigDecimal price, int stock) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setStock(stock);
        p.setActive(true);
        return p;
    }

    private ProductRequest buildRequest(String name, String description, BigDecimal price, int stock) {
        ProductRequest r = new ProductRequest();
        r.setName(name);
        r.setDescription(description);
        r.setPrice(price);
        r.setStock(stock);
        r.setActive(true);
        return r;
    }
}
