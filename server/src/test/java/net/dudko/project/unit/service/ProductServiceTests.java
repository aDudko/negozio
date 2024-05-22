package net.dudko.project.unit.service;

import net.dudko.project.TestUtil;
import net.dudko.project.domain.mapper.ProductMapper;
import net.dudko.project.domain.repository.ProductRepository;
import net.dudko.project.model.dto.ProductDto;
import net.dudko.project.model.exception.ResourceNotFoundException;
import net.dudko.project.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto;

    private final long productId = 1L;

    @BeforeEach
    public void setup() {
        productDto = TestUtil.getValidProductDto();
    }

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Unit positive test for get product when product exist")
    public void getProductById_whenProductExist_thenReturnProduct() {
        productDto.setId(productId);
        given(productRepository.findById(productId)).willReturn(Optional.of(ProductMapper.mapToProduct(productDto)));
        ProductDto inDb = productService.getProduct(productDto.getId());
        assertThat(inDb).isNotNull();
    }

    @Test
    @DisplayName("Unit negative test for get product when product not exist")
    public void getProductById_whenProductNotExist_thenReturnException() {
        given(productRepository.findById(productId)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(productId));
    }

    @Test
    @DisplayName("Unit positive test for get all products")
    public void getListOfProducts_whenGetAllProducts_thenReturnProductsList() {
        given(productRepository.findAll()).willReturn(List.of(ProductMapper.mapToProduct(productDto)));
        List<ProductDto> products = productService.getProducts();
        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();
    }

    @Test
    @DisplayName("Unit negative test for get all products method when we pull empty list")
    public void givenEmptyEmployeeList_whenGetAllEmployee_thenReturnEmptyEmployeesList() {
        given(productRepository.findAll()).willReturn(Collections.emptyList());
        List<ProductDto> products = productService.getProducts();
        assertThat(products).isNotNull();
        assertThat(products).isEmpty();
        assertThat(products.size()).isEqualTo(0);
    }

}
