package com.ferroeduardo.attornatustest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SavePersonAddressRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    private String logradouro;

    @NotBlank
    @Size(min = 8, max = 8)
    private String cep;

    @NotBlank
    @Size(min = 1, max = 20)
    private String number;

    @NotBlank
    @Size(min = 1, max = 50)
    private String city;

    public SavePersonAddressRequest() {
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
