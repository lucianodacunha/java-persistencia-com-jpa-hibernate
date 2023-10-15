package io.github.lucianodacunha.model;

public enum Categorias {
    CELULARES("Celulares"),
    IMPORTADOS("Importados"),
    LIVROS("Livros"),
    CASA("Casa"),
    JARDIM("Jardim"),
    BRINQUEDOS("Brinquedos");

    private final String descricao;

    Categorias(String descricao){
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return this.descricao;
    }
}
