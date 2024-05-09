package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "OrderListDto",
        description = "OrderList DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDto {

    @Schema(
            description = "ID of order_products in database"
    )
    private long id;

    @Schema(
            description = "ID of product in database"
    )
    private Long productId;

    @Schema(
            description = "Count products"
    )
    private int count;

}
