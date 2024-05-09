package net.dudko.project.domain.mapper;

import net.dudko.project.domain.entity.Product;
import net.dudko.project.model.dto.ProductDto;

public class ProductMapper {

    public static ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .rating(product.getRating())
                .build();
    }

    public static Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .image(productDto.getImage())
                .price(productDto.getPrice())
                .rating(productDto.getRating())
                .build();
    }

}
