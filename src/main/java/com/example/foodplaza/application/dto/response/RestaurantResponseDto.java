package com.example.foodplaza.application.dto.response;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponseDto {

    @NotBlank(message = "El nombre es requerido")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[0-9a-zA-Z ]+$", message = "El nombre puede contener números con letras pero no solo números")
    private String nameRestaurant;
    @NotBlank(message = "El nit es requerido")
    @Pattern(regexp = "\\d+", message = "El nit debe ser númerico")
    private String nit;
    @NotBlank(message = "La direccion es requerida")
    private String address;
    @NotBlank(message = "El telefono es requerido")
    @Pattern(regexp = "^\\+?\\d{1,12}$", message = "El telefono debe contener máximo 13 caracteres y puede contener el símbolo '+' al inicio")
    private String phoneNumber;
    @NotBlank(message = "La urlLogo es requerida")
    private String urlLogo;
    @NotNull(message = "El id_propietario no puede ser nulo")
    @Min(value = 1, message = "El id_propietario debe ser mayor a cero")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Long ownerId;
}
