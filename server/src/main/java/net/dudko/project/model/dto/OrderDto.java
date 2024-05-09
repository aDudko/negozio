package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(
        name = "OrderDto",
        description = "Order DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @Schema(
            description = "ID of order in database"
    )
    private Long id;

    @Schema(
            description = "ID of user in database"
    )
    private Long userId;

    @Schema(
            description = "List of products"
    )
    private List<OrderListDto> products;

}
