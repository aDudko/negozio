package net.dudko.project.unit.controller;

import net.dudko.project.TestUtil;
import net.dudko.project.controller.AuthController;
import net.dudko.project.controller.OrderController;
import net.dudko.project.model.dto.ProductDto;
import net.dudko.project.security.JwtAuthenticationFilter;
import net.dudko.project.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthController authController;

    @MockBean
    private OrderController orderController;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ProductDto productDto;

    private final long productId = 1L;

    @BeforeEach
    public void setup() {
        productDto = TestUtil.getValidProductDto();
    }

    @Test
    @DisplayName("Unit test for get product by id")
    public void givenProductId_whenGetProductById_thenReturnProductDto() throws Exception {
        productDto.setId(productId);
        given(productService.getProduct(productId)).willReturn(productDto);
        ResultActions responce = mockMvc.perform(get("/product/{id}", productId));
        responce.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice())))
                .andExpect(jsonPath("$.rating", is(productDto.getRating())));
    }

    @Test
    @DisplayName("Unit test for get all products method")
    public void givenListOfProducts_whenGetAllProducts_thenReturnProductsList() throws Exception {
        List<ProductDto> listOfProducts = Stream.of(productDto, productDto).toList();
        given(productService.getProducts()).willReturn(listOfProducts);
        ResultActions responce = mockMvc.perform(get("/product"));
        responce.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.size()", is(listOfProducts.size())));
    }
}
