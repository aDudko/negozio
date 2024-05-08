package net.dudko.project.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "JwtAuthResponse to transfer the data between client and server"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    @Schema(
            description = "Secret Token"
    )
    private String access_token;

}
