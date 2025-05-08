package main;

import dao.AlunoDAO;
import dao.LivroDAO;
import dao.EmprestimoDAO;
import model.Aluno;
import model.Livro;
import model.Emprestimo;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        // Criação de instâncias dos DAOs e do Scanner
        Scanner scanner = new Scanner(System.in);
        AlunoDAO alunoDAO = new AlunoDAO();
        LivroDAO livroDAO = new LivroDAO();
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

        boolean continuar = true;

        while (continuar) {
            System.out.println("\n📚 Sistema da Biblioteca 📚");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Cadastrar Livro");
            System.out.println("3. Cadastrar Empréstimo");
            System.out.println("4. Listar Alunos");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cadastrarAluno(scanner, alunoDAO);
                        break;
                    case 2:
                        cadastrarLivro(scanner, livroDAO);
                        break;
                    case 3:
                        cadastrarEmprestimo(scanner, emprestimoDAO);
                        break;
                    case 4:
                        listarAlunos(alunoDAO); // <-- chamada do método
                        break;
                    case 5:
                        System.out.println("Encerrando o sistema.");
                        continuar = false;
                        break;
                    default:
                        System.out.println("❌ Opção inválida. Escolha entre 1 e 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Digite apenas números (1 a 5).");
            }
        }

        scanner.close();
    }

    /**
     * Método responsável por cadastrar um aluno no sistema.
     * Valida as entradas de nome, matrícula e data de nascimento.
     */
    private static void cadastrarAluno(Scanner scanner, AlunoDAO alunoDAO) {
        String nome = "", matricula = "", dataNascimento = "";
        boolean nomeValido = false, matriculaValida = false, dataValida = false;

        // Entrada e validação do nome
        while (!nomeValido) {
            System.out.println("Nome do aluno: ");
            nome = scanner.nextLine();
            if (!nome.trim().isEmpty()) {
                nomeValido = true;
            } else {
                System.out.println("❌ Nome inválido. Tente novamente.");
            }
        }

        // Entrada e validação da matrícula
        while (!matriculaValida) {
            System.out.println("Matrícula: ");
            matricula = scanner.nextLine();
            if (!matricula.trim().isEmpty()) {
                matriculaValida = true;
            } else {
                System.out.println("❌ Matrícula inválida. Tente novamente.");
            }
        }

        // Entrada e validação da data de nascimento no formato dd/MM/yyyy
        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!dataValida) {
            System.out.println("Data de nascimento (ex: dd/MM/yyyy): ");
            String input = scanner.nextLine();
            try {
                LocalDate data = LocalDate.parse(input, formatterInput);
                dataNascimento = data.format(formatterOutput); // Conversão para formato SQL
                dataValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Data inválida. Use o formato dd/MM/yyyy.");
            }
        }

        // Cria objeto Aluno e envia para o DAO realizar o cadastro
        Aluno aluno = new Aluno(0, nome, matricula, dataNascimento);

        try {
            alunoDAO.cadastrarAluno(aluno);
            System.out.println("Aluno cadastrado com sucesso! ✅");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    /**
     * Método responsável por cadastrar um livro no sistema.
     * Valida as entradas de título, autor, ano de publicação e estoque.
     */
    private static void cadastrarLivro(Scanner scanner, LivroDAO livroDAO) {
        String titulo = "", autor = "";
        int anoPublicacao = 0, quantidadeEstoque = 0;

        // Entrada e validação do título
        while (titulo.isEmpty()) {
            System.out.println("Título do livro: ");
            titulo = scanner.nextLine();
            if (titulo.trim().isEmpty()) {
                System.out.println("❌ Título inválido. Tente novamente.");
            }
        }

        // Entrada e validação do autor
        while (autor.isEmpty()) {
            System.out.println("Autor do livro: ");
            autor = scanner.nextLine();
            if (autor.trim().isEmpty()) {
                System.out.println("❌ Autor inválido. Tente novamente.");
            }
        }

        // Entrada e validação do ano de publicação
        boolean anoValido = false;
        while (!anoValido) {
            System.out.println("Ano de publicação: ");
            try {
                anoPublicacao = Integer.parseInt(scanner.nextLine());
                if (anoPublicacao > 0) {
                    anoValido = true;
                } else {
                    System.out.println("❌ Ano inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Insira um número válido para o ano.");
            }
        }

        // Entrada e validação da quantidade em estoque
        boolean quantidadeValida = false;
        while (!quantidadeValida) {
            System.out.println("Quantidade em estoque: ");
            try {
                quantidadeEstoque = Integer.parseInt(scanner.nextLine());
                if (quantidadeEstoque >= 0) {
                    quantidadeValida = true;
                } else {
                    System.out.println("❌ Quantidade não pode ser negativa.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Insira um número válido para a quantidade.");
            }
        }

        // Cria objeto Livro e envia para o DAO realizar o cadastro
        Livro livro = new Livro(0, titulo, autor, anoPublicacao, quantidadeEstoque);

        try {
            livroDAO.cadastrarLivro(livro);
            System.out.println("Livro cadastrado com sucesso! ✅");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    /**
     * Método responsável por cadastrar um empréstimo no sistema.
     * Valida as entradas de aluno, livro e data de devolução.
     */
    private static void cadastrarEmprestimo(Scanner scanner, EmprestimoDAO emprestimoDAO) {
        int idAluno = 0, idLivro = 0;
        String dataDevolucao = "";

        // Entrada e validação do id do aluno
        boolean alunoValido = false;
        while (!alunoValido) {
            System.out.println("ID do aluno: ");
            try {
                idAluno = Integer.parseInt(scanner.nextLine());
                if (idAluno > 0) {
                    alunoValido = true;
                } else {
                    System.out.println("❌ ID do aluno inválido. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Insira um número válido para o ID do aluno.");
            }
        }

        // Entrada e validação do id do livro
        boolean livroValido = false;
        while (!livroValido) {
            System.out.println("ID do livro: ");
            try {
                idLivro = Integer.parseInt(scanner.nextLine());
                if (idLivro > 0) {
                    livroValido = true;
                } else {
                    System.out.println("❌ ID do livro inválido. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Insira um número válido para o ID do livro.");
            }
        }

        // Entrada e validação da data de devolução
        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        boolean dataValida = false;
        while (!dataValida) {
            System.out.println("Data de devolução (ex: dd/MM/yyyy): ");
            String input = scanner.nextLine();
            try {
                LocalDate data = LocalDate.parse(input, formatterInput);
                dataDevolucao = data.format(formatterOutput);
                dataValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Data inválida. Use o formato dd/MM/yyyy.");
            }
        }

        // Cria objeto Emprestimo e envia para o DAO realizar o cadastro
        Emprestimo emprestimo = new Emprestimo(0, idAluno, idLivro, LocalDate.now().toString(), dataDevolucao);

        try {
            emprestimoDAO.cadastrarEmprestimo(emprestimo);
            System.out.println("Empréstimo cadastrado com sucesso! ✅");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar empréstimo: " + e.getMessage());
        }
    }

    private static void listarAlunos(AlunoDAO alunoDAO) {
        System.out.println("\n📋 Lista de Alunos:");
        List<Aluno> alunos = alunoDAO.listarAlunos();
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
        } else {
            for (Aluno aluno : alunos) {
                System.out.println("ID: " + aluno.getId());
                System.out.println("Nome: " + aluno.getNome());
                System.out.println("Matrícula: " + aluno.getMatricula());
                System.out.println("Data de Nascimento: " + aluno.getDataNascimento());
                System.out.println("-----------------------------");
            }
        }
    }

}
