package net.dudko.project.service;

import net.dudko.project.model.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto getProduct(Long id);

    List<ProductDto> getProducts();

}
