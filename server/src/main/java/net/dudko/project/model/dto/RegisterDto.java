package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dudko.project.model.validation.UniqueEmail;

@Schema(
        description = "Register DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    @Schema(
            description = "First and Last name of user"
    )
    @NotNull(message = "{negozio.constraints.name.NotNull.message}")
    @Size(min = 4, max = 255)
    private String name;

    @Schema(
            description = "Email of user"
    )
    @NotNull(message = "{negozio.constraints.email.NotNull.message}")
    @Pattern(regexp = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*",
             message = "{negozio.constraints.email.Pattern.message}")
    @UniqueEmail
    private String email;

    @Schema(
            description = "Phone of user"
    )
    @NotNull(message = "{negozio.constraints.phone.NotNull.message}")
    private String phone;

    @Schema(
            description = "Address of user"
    )
    @NotNull(message = "{negozio.constraints.address.NotNull.message}")
    private String address;

    @Schema(
            description = "Password of user"
    )
    @NotNull(message = "{negozio.constraints.password.NotNull.message}")
    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
             message = "{negozio.constraints.password.Pattern.message}")
    private String password;

}
