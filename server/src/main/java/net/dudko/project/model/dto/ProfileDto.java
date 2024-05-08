package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "UserData DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    @Schema(
            description = "User ID"
    )
    private Long id;

    @Schema(
            description = "First and Last name of user"
    )
    private String name;

    @Schema(
            description = "Email of user"
    )
    private String email;

    @Schema(
            description = "Phone of user"
    )
    private String phone;

    @Schema(
            description = "Address of user"
    )
    private String address;

    @Schema(
            description = "Hash of password for user"
    )
    private String passwordHash;

}
