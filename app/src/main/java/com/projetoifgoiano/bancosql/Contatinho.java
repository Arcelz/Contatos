package com.projetoifgoiano.bancosql;
/**
 * Created by Usuario on 09/11/2017.
 */

public class Contatinho {
    private Integer id;
    private String nome;
    private String telefone;
    private String infos;

    public Contatinho(){}

    public Contatinho(Integer id, String nome, String telefone, String infos) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.infos = infos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }
}
