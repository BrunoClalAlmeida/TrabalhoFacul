package model;

import java.time.LocalDate;

// Classe que representa o empréstimo de um livro para um aluno
public class Emprestimo {
    private int idEmprestimo;      // Identificador único do empréstimo
    private int idAluno;           // ID do aluno que fez o empréstimo
    private int idLivro;           // ID do livro emprestado
    private LocalDate dataEmprestimo;   // Data em que o livro foi emprestado
    private LocalDate dataDevolucao;    // Data prevista ou efetiva da devolução

    // Construtor completo: inicializa todos os atributos
    public Emprestimo(int idEmprestimo, int idAluno, int idLivro, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        this.idEmprestimo = idEmprestimo;
        this.idAluno = idAluno;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    // Construtor vazio: útil para criação de objeto e posterior configuração via setters
    public Emprestimo() {
    }

    // Getters e Setters para acessar e modificar os atributos privados

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    // Método toString sobrescrito para facilitar a exibição das informações do empréstimo
    @Override
    public String toString() {
        return "Empréstimo [ID: " + idEmprestimo +
                ", Aluno ID: " + idAluno +
                ", Livro ID: " + idLivro +
                ", Data Empréstimo: " + dataEmprestimo +
                ", Data Devolução: " + dataDevolucao + "]";
    }
}
