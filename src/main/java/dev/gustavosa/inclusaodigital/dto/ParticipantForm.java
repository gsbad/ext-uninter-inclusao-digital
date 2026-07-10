package dev.gustavosa.inclusaodigital.dto;

import dev.gustavosa.inclusaodigital.entity.AgeRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Objeto de binding do formulário de cadastro (Thymeleaf th:object).
 * Diferente dos DTOs de saída do projeto, não é um record: o binding
 * bidirecional de formulário (th:field + BindingResult) depende de um
 * bean mutável com getters/setters convencionais.
 */
public class ParticipantForm {

    @NotBlank(message = "Por favor, informe seu nome.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    private String fullName;

    @NotNull(message = "Por favor, selecione sua faixa de idade.")
    private AgeRange ageRange;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    private String phone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
