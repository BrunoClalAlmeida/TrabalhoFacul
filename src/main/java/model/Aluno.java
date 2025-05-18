package model;

// Classe que representa um aluno do sistema
public class Aluno {
    // Atributos privados da classe
    private int id;                 // Identificador único do aluno
    private String nome;           // Nome completo do aluno
    private String matricula;      // Número de matrícula do aluno
    private String dataNascimento; // Data de nascimento no formato String (ex: "2000-05-18")

    // Construtor que recebe todos os atributos
    public Aluno(int id, String nome, String matricula, String dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
    }

    // Métodos getters e setters para acessar e modificar os atributos privados

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
