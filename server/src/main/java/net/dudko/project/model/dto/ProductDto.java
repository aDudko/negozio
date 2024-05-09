package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "ProductDto",
        description = "Product DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @Schema(
            description = "ID of product in database"
    )
    private Long id;

    @Schema(
            description = "Name of product"
    )
    private String name;

    @Schema(
            description = "Description of product"
    )
    private String description;

    @Schema(
            description = "URL of image for product"
    )
    private String image;

    @Schema(
            description = "Price of product"
    )
    private double price;

    @Schema(
            description = "Rating of product"
    )
    private double rating;

}
