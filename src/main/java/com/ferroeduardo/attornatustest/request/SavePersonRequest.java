package com.ferroeduardo.attornatustest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SavePersonRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // yyyy-MM-dd
    @NotNull
    private LocalDate birthDate;

    public SavePersonRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
