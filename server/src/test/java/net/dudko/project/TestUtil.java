package net.dudko.project;

import net.dudko.project.domain.entity.User;
import net.dudko.project.model.dto.LoginDto;
import net.dudko.project.model.dto.ProductDto;
import net.dudko.project.model.dto.RegisterDto;

public class TestUtil {

    public static ProductDto getValidProductDto() {
        return ProductDto.builder()
                .name("Product Name")
                .description("Description")
                .image(null)
                .price(12)
                .rating(4.9)
                .build();
    }

    public static User getValidUser() {
        return User.builder()
                .name("test-user")
                .email("test@mail.com")
                .phone("+491234567890")
                .address("TestAddress st. 69/7")
                .password("P4ssword")
                .build();
    }

    public static RegisterDto getValidRegisterDto() {
        return RegisterDto.builder()
                .name("test-user")
                .email("test@mail.com")
                .phone("+491234567890")
                .address("TestAddress st. 69/7")
                .password("P4ssword")
                .build();
    }

    public static LoginDto getValidLoginDto() {
        return LoginDto.builder()
                .email("test@mail.com")
                .password("P4ssword")
                .build();
    }

}
