package model;

// Classe que representa um livro com seus atributos principais
public class Livro {
    private int id;                 // Identificador único do livro
    private String titulo;          // Título do livro
    private String autor;           // Autor do livro
    private int anoPublicacao;      // Ano de publicação do livro
    private int quantidadeEstoque;  // Quantidade disponível em estoque

    // Construtor que inicializa todos os atributos da classe
    public Livro(int id, String titulo, String autor, int anoPublicacao, int quantidadeEstoque) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters e Setters para acessar e modificar os atributos privados

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}
