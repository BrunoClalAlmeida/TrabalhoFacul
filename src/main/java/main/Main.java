package main;

import dao.AlunoDAO;
import dao.LivroDAO;
import dao.EmprestimoDAO;
import model.Aluno;
import model.Livro;
import model.Emprestimo;

import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Criação de instâncias dos DAOs e do Scanner
        Scanner scanner = new Scanner(System.in);
        AlunoDAO alunoDAO = new AlunoDAO();
        LivroDAO livroDAO = new LivroDAO();
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

        boolean continuar = true;

        while (continuar) {
            // Exibe o menu de opções
            System.out.println("\n📚 Sistema da Biblioteca 📚");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Cadastrar Livro");
            System.out.println("3. Cadastrar Empréstimo");
            System.out.println("4. Listar Alunos");
            System.out.println("5. Listar Livros");
            System.out.println("6. Listar Empréstimos");
            System.out.println("7. Atualizar Aluno");
            System.out.println("8. Atualizar Livro");
            System.out.println("9. Atualizar Empréstimo");
            System.out.println("10. Deletar Empréstimo");
            System.out.println("11. Deletar Aluno");
            System.out.println("12. Deletar Livro");
            System.out.println("13. Gerar Relatório");
            System.out.println("14. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());

                // Executa a ação conforme a opção escolhida
                switch (opcao) {
                    case 1:
                        cadastrarAluno(scanner, alunoDAO);
                        break;
                    case 2:
                        cadastrarLivro(scanner, livroDAO);
                        break;
                    case 3:
                        cadastrarEmprestimo(scanner, emprestimoDAO, alunoDAO, livroDAO);
                        break;
                    case 4:
                        listarAlunos(alunoDAO);
                        break;
                    case 5:
                        listarLivros(livroDAO);
                        break;
                    case 6:
                        listarEmprestimos(emprestimoDAO);
                        break;
                    case 7:
                        atualizarAluno(scanner, alunoDAO);
                        break;
                    case 8:
                        atualizarLivro(scanner, livroDAO);
                        break;
                    case 9:
                        atualizarEmprestimo(scanner, emprestimoDAO);
                        break;
                    case 10:
                        deletarEmprestimo(scanner, emprestimoDAO);
                        break;
                    case 11:
                        deletarAluno(scanner, alunoDAO);
                        break;
                    case 12:
                        deletarLivro(scanner, livroDAO);
                        break;
                    case 13:
                        gerarRelatorio(alunoDAO, livroDAO, emprestimoDAO);
                        break;
                    case 14:
                        System.out.println("Encerrando o sistema.");
                        continuar = false;
                        break;
                    default:
                        System.out.println("❌ Opção inválida. Escolha entre 1 e 14.");
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Digite apenas números.");
            }
        }
        scanner.close(); // Fecha o scanner ao final
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
    private static void cadastrarEmprestimo(Scanner scanner, EmprestimoDAO emprestimoDAO, AlunoDAO alunoDAO, LivroDAO livroDAO) {
        int idAluno = 0, idLivro = 0;
        LocalDate dataEmprestimo = null;
        LocalDate dataDevolucao = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Entrada e validação do ID do aluno
        while (true) {
            System.out.print("ID do aluno: ");
            try {
                idAluno = Integer.parseInt(scanner.nextLine());
                if (idAluno > 0 && alunoDAO.existe(idAluno)) {
                    break;
                } else {
                    System.out.println("❌ ID do aluno inválido ou não encontrado.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Insira um número válido para o ID do aluno.");
            }
        }

        // Entrada e validação do ID do livro
        while (true) {
            System.out.print("ID do livro: ");
            try {
                idLivro = Integer.parseInt(scanner.nextLine());
                if (idLivro > 0 && livroDAO.existe(idLivro)) {
                    break;
                } else {
                    System.out.println("❌ ID do livro inválido ou não encontrado.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Insira um número válido para o ID do livro.");
            }
        }

        // Entrada e validação da data de empréstimo
        while (true) {
            System.out.print("Data de empréstimo (ex: dd/MM/yyyy): ");
            String input = scanner.nextLine();
            try {
                dataEmprestimo = LocalDate.parse(input, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Data inválida. Use o formato dd/MM/yyyy.");
            }
        }

        // Entrada e validação da data de devolução
        while (true) {
            System.out.print("Data de devolução (ex: dd/MM/yyyy): ");
            String input = scanner.nextLine();
            try {
                dataDevolucao = LocalDate.parse(input, formatter);
                if (!dataDevolucao.isBefore(dataEmprestimo)) {
                    break;
                } else {
                    System.out.println("❌ A data de devolução não pode ser antes da data de empréstimo.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Data inválida. Use o formato dd/MM/yyyy.");
            }
        }

        // Cria objeto Emprestimo e envia para o DAO realizar o cadastro
        Emprestimo emprestimo = new Emprestimo(0, idAluno, idLivro, dataEmprestimo, dataDevolucao);

        try {
            emprestimoDAO.cadastrarEmprestimo(emprestimo);
            System.out.println("✅ Empréstimo cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar empréstimo: " + e.getMessage());
        }
    }


    private static void listarAlunos(AlunoDAO alunoDAO) {
        System.out.println("\n📋 Lista de Alunos:");
        for (Aluno aluno : alunoDAO.listarAlunos()) {
            System.out.printf("ID: %d | Nome: %s | Matrícula: %s | Nascimento: %s%n",
                    aluno.getId(), aluno.getNome(), aluno.getMatricula(), aluno.getDataNascimento());
        }
    }

    private static void listarLivros(LivroDAO livroDAO) {
        System.out.println("\n📋 Lista de Livros:");
        for (Livro livro : livroDAO.listarLivros()) {
            System.out.printf("ID: %d | Título: %s | Autor: %s | Ano: %d | Estoque: %d%n",
                    livro.getId(), livro.getTitulo(), livro.getAutor(),
                    livro.getAnoPublicacao(), livro.getQuantidadeEstoque());
        }
    }

    private static void listarEmprestimos(EmprestimoDAO emprestimoDAO) {
        System.out.println("\n📋 Lista de Empréstimos:");
        for (Emprestimo emp : emprestimoDAO.listarEmprestimos()) {
            // Exibe os dados de cada empréstimo
            System.out.printf("ID: %d | ID Aluno: %d | ID Livro: %d | Empréstimo: %s | Devolução: %s%n",
                    emp.getIdEmprestimo(),
                    emp.getIdAluno(),
                    emp.getIdLivro(),
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucao() != null ? emp.getDataDevolucao() : "Não devolvido");
        }
    }


    private static void atualizarAluno(Scanner scanner, AlunoDAO alunoDAO) {
        int idAluno;
        String nome = "", matricula = "", dataNascimento = "";

        // Solicita o ID do aluno a ser atualizado com repetição em caso de erro
        while (true) {
            System.out.println("Digite o ID do aluno que deseja atualizar: ");
            try {
                idAluno = Integer.parseInt(scanner.nextLine());

                Aluno aluno = alunoDAO.buscarAlunoPorId(idAluno);
                if (aluno == null) {
                    System.out.println("❌ Aluno não encontrado com o ID informado. Tente novamente.");
                    continue; // Continua pedindo o ID até encontrar o aluno
                }

                // Atualiza o nome
                System.out.println("Nome atual: " + aluno.getNome());
                System.out.println("Digite o novo nome do aluno: ");
                nome = scanner.nextLine();
                if (nome.trim().isEmpty()) {
                    nome = aluno.getNome(); // Mantém o nome atual se não fornecer um novo
                }

                // Atualiza a matrícula
                System.out.println("Matrícula atual: " + aluno.getMatricula());
                System.out.println("Digite a nova matrícula: ");
                matricula = scanner.nextLine();
                if (matricula.trim().isEmpty()) {
                    matricula = aluno.getMatricula(); // Mantém a matrícula atual
                }

                // Atualiza a data de nascimento
                DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                System.out.println("Data de nascimento atual: " + aluno.getDataNascimento());
                System.out.println("Digite a nova data de nascimento (ex: dd/MM/yyyy): ");
                dataNascimento = scanner.nextLine();
                if (dataNascimento.trim().isEmpty()) {
                    dataNascimento = aluno.getDataNascimento(); // Mantém a data atual
                } else {
                    try {
                        LocalDate data = LocalDate.parse(dataNascimento, formatterInput);
                        dataNascimento = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (DateTimeParseException e) {
                        System.out.println("❌ Data inválida.");
                        return;
                    }
                }

                // Atualiza o aluno no banco de dados
                aluno.setNome(nome);
                aluno.setMatricula(matricula);
                aluno.setDataNascimento(dataNascimento);

                alunoDAO.atualizarAluno(aluno);
                System.out.println("Aluno atualizado com sucesso! ✅");
                break; // Sai do loop quando o aluno for atualizado com sucesso

            } catch (NumberFormatException e) {
                System.out.println("❌ ID inválido. Insira um número.");
            }
        }
    }

    private static void atualizarLivro(Scanner scanner, LivroDAO livroDAO) {
        int idLivro, anoPublicacao, quantidadeEstoque;
        String titulo, autor;

        // Solicita o ID do livro a ser atualizado com repetição em caso de erro
        while (true) {
            System.out.println("Digite o ID do livro que deseja atualizar: ");
            try {
                idLivro = Integer.parseInt(scanner.nextLine());

                Livro livro = livroDAO.buscarLivroPorId(idLivro);
                if (livro == null) {
                    System.out.println("❌ Livro não encontrado com o ID informado. Tente novamente.");
                    continue; // Continua pedindo o ID até encontrar o livro
                }

                // Atualiza o título
                System.out.println("Título atual: " + livro.getTitulo());
                System.out.println("Digite o novo título: ");
                titulo = scanner.nextLine();
                if (titulo.trim().isEmpty()) {
                    titulo = livro.getTitulo(); // Mantém o título atual
                }

                // Atualiza o autor
                System.out.println("Autor atual: " + livro.getAutor());
                System.out.println("Digite o novo autor: ");
                autor = scanner.nextLine();
                if (autor.trim().isEmpty()) {
                    autor = livro.getAutor(); // Mantém o autor atual
                }

                // Atualiza o ano de publicação
                System.out.println("Ano de publicação atual: " + livro.getAnoPublicacao());
                System.out.println("Digite o novo ano de publicação: ");
                anoPublicacao = Integer.parseInt(scanner.nextLine());

                // Atualiza a quantidade em estoque
                System.out.println("Quantidade em estoque atual: " + livro.getQuantidadeEstoque());
                System.out.println("Digite a nova quantidade em estoque: ");
                quantidadeEstoque = Integer.parseInt(scanner.nextLine());

                // Atualiza o livro no banco de dados
                livro.setTitulo(titulo);
                livro.setAutor(autor);
                livro.setAnoPublicacao(anoPublicacao);
                livro.setQuantidadeEstoque(quantidadeEstoque);

                livroDAO.atualizarLivro(livro);
                System.out.println("Livro atualizado com sucesso! ✅");
                break; // Sai do loop quando o livro for atualizado com sucesso

            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Digite um número.");
            }
        }
    }

    private static void atualizarEmprestimo(Scanner scanner, EmprestimoDAO emprestimoDAO) {
        int idEmprestimo;
        String dataDevolucao;

        // Solicita o ID do empréstimo a ser atualizado com repetição em caso de erro
        while (true) {
            System.out.println("Digite o ID do empréstimo que deseja atualizar: ");
            try {
                idEmprestimo = Integer.parseInt(scanner.nextLine());

                Emprestimo emprestimo = emprestimoDAO.buscarEmprestimoPorId(idEmprestimo);
                if (emprestimo == null) {
                    System.out.println("❌ Empréstimo não encontrado com o ID informado. Tente novamente.");
                    continue; // Continua pedindo o ID até encontrar o empréstimo
                }

                // Atualiza a data de devolução
                DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                System.out.println("Data de devolução atual: " + emprestimo.getDataDevolucao());
                System.out.println("Digite a nova data de devolução (ex: dd/MM/yyyy): ");
                dataDevolucao = scanner.nextLine();
                try {
                    LocalDate data = LocalDate.parse(dataDevolucao, formatterInput);
                    dataDevolucao = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Data inválida.");
                    return;
                }

                // Atualiza o empréstimo no banco de dados
                emprestimo.setDataDevolucao(LocalDate.parse(dataDevolucao));

                emprestimoDAO.atualizarEmprestimo(emprestimo);
                System.out.println("Empréstimo atualizado com sucesso! ✅");
                break; // Sai do loop quando o empréstimo for atualizado com sucesso

            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Digite um número.");
            }
        }
    }

    private static void deletarAluno(Scanner scanner, AlunoDAO alunoDAO) {
        while (true) {
            System.out.println("Digite o ID do aluno que deseja deletar: ");
            try {
                int idAluno = Integer.parseInt(scanner.nextLine());
                Aluno aluno = alunoDAO.buscarAlunoPorId(idAluno);

                if (aluno == null) {
                    System.out.println("❌ Aluno não encontrado com o ID informado. Tente novamente.");
                } else {
                    alunoDAO.deletarAluno(idAluno);
                    System.out.println("✅ Aluno deletado com sucesso!");
                    break; // sai do loop após deletar
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ ID inválido. Insira um número.");
            }
        }
    }


    /**
     * Método responsável por deletar um livro do sistema.
     */
    private static void deletarLivro(Scanner scanner, LivroDAO livroDAO) {
        while (true) {
            System.out.println("Digite o ID do livro que deseja deletar: ");
            try {
                int idLivro = Integer.parseInt(scanner.nextLine());
                Livro livro = livroDAO.buscarLivroPorId(idLivro);

                if (livro == null) {
                    System.out.println("❌ Livro não encontrado com o ID informado. Tente novamente.");
                } else {
                    livroDAO.deletarLivro(idLivro);
                    System.out.println("✅ Livro deletado com sucesso!");
                    break; // sai do loop após sucesso
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ ID inválido. Insira um número.");
            }
        }
    }

    /**
     * Método responsável por deletar um empréstimo do sistema.
     */
    private static void deletarEmprestimo(Scanner scanner, EmprestimoDAO emprestimoDAO) {
        while (true) {
            System.out.println("Digite o ID do empréstimo que deseja deletar: ");
            try {
                int idEmprestimo = Integer.parseInt(scanner.nextLine());
                Emprestimo emprestimo = emprestimoDAO.buscarEmprestimoPorId(idEmprestimo);

                if (emprestimo == null) {
                    System.out.println("❌ Empréstimo não encontrado com o ID informado. Tente novamente.");
                } else {
                    emprestimoDAO.deletarEmprestimo(idEmprestimo);
                    System.out.println("✅ Empréstimo deletado com sucesso!");
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ ID inválido. Insira um número.");
            }
        }
    }

    public static void gerarRelatorio(AlunoDAO alunoDAO, LivroDAO livroDAO, EmprestimoDAO emprestimoDAO) {
        try {
            // Define o diretório onde o relatório será salvo
            File diretorio = new File("relatorio");
            if (!diretorio.exists()) {
                diretorio.mkdir(); // cria a pasta relatorio se não existir
            }

            // Cria o arquivo dentro da pasta relatorio
            File arquivo = new File(diretorio, "relatorio.txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {

                // Alunos
                writer.write("--- Alunos Cadastrados ---\n");
                List<Aluno> alunos = alunoDAO.listarAlunos();
                for (Aluno aluno : alunos) {
                    writer.write("ID: " + aluno.getId() +
                            ", Nome: " + aluno.getNome() +
                            ", Matrícula: " + aluno.getMatricula() +
                            ", Data de Nascimento: " + aluno.getDataNascimento() + "\n");
                }
                writer.write("\n");

                // Livros
                writer.write("--- Livros Cadastrados ---\n");
                List<Livro> livros = livroDAO.listarLivros();
                for (Livro livro : livros) {
                    writer.write("ID: " + livro.getId() +
                            ", Título: " + livro.getTitulo() +
                            ", Autor: " + livro.getAutor() +
                            ", Ano: " + livro.getAnoPublicacao() +
                            ", Estoque: " + livro.getQuantidadeEstoque() + "\n");
                }
                writer.write("\n");

                // Empréstimos
                writer.write("--- Empréstimos Cadastrados ---\n");
                List<Emprestimo> emprestimos = emprestimoDAO.listarEmprestimos();
                for (Emprestimo emp : emprestimos) {
                    writer.write("ID: " + emp.getIdEmprestimo() +
                            ", Aluno ID: " + emp.getIdAluno() +
                            ", Livro ID: " + emp.getIdLivro() +
                            ", Data Empréstimo: " + emp.getDataEmprestimo() +
                            ", Data Devolução: " + emp.getDataDevolucao() + "\n");
                }

                System.out.println("Relatório gerado com sucesso em 'relatorio/relatorio.txt'!");

            }
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}

