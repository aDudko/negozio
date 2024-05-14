package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "LoginDto",
        description = "Login DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @Schema(
            description = "Email of user"
    )
    @NotNull(message = "{negozio.constraints.email.NotNull.message}")
    private String email;

    @Schema(
            description = "Password of user"
    )
    @NotNull(message = "{negozio.constraints.password.NotNull.message}")
    private String password;

}
