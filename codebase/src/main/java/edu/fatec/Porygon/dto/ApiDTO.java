
package edu.fatec.Porygon.dto;

import edu.fatec.Porygon.model.Api;

public class ApiDTO {
    private Integer id;
    private String nome;

    public ApiDTO(Api api) {
        this.id = api.getId();
        this.nome = api.getNome();
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}