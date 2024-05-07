package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "LoginDto",
        description = "Login DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @Schema(
            description = "Username or Email of user"
    )
    private String usernameOrEmail;

    @Schema(
            description = "Password of user"
    )
    private String password;

}
