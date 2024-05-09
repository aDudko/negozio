package net.dudko.project.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.project.domain.mapper.ProductMapper;
import net.dudko.project.domain.repository.ProductRepository;
import net.dudko.project.model.dto.ProductDto;
import net.dudko.project.model.exception.ResourceNotFoundException;
import net.dudko.project.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDto getProduct(Long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream().map(ProductMapper::mapToProductDto).toList();
    }

}
