package dao;

import model.Emprestimo;
import util.ConexaoBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    // Método para cadastrar um novo empréstimo no banco de dados
    public void cadastrarEmprestimo(Emprestimo emprestimo) {
        // Comando SQL para inserir um novo registro na tabela Emprestimos
        String sql = "INSERT INTO Emprestimos (id_aluno, id_livro, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Setando os parâmetros do PreparedStatement com os valores do objeto emprestimo
            stmt.setInt(1, emprestimo.getIdAluno());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));

            // Executa o comando INSERT
            stmt.executeUpdate();

            // Atualiza o estoque do livro, reduzindo 1 unidade
            LivroDAO.atualizarEstoque(emprestimo.getIdLivro(), -1);

        } catch (SQLException e) {
            // Caso ocorra algum erro na execução do SQL, imprime mensagem de erro
            System.err.println("Erro ao cadastrar empréstimo: " + e.getMessage());
        }
    }

    // Método para registrar a devolução de um livro pelo ID do empréstimo
    public void registrarDevolucao(int idEmprestimo) {
        // SQL para buscar o ID do livro relacionado ao empréstimo
        String sqlBusca = "SELECT id_livro FROM Emprestimos WHERE id_emprestimo = ?";
        // SQL para atualizar a data de devolução no empréstimo
        String sqlAtualiza = "UPDATE Emprestimos SET data_devolucao = ? WHERE id_emprestimo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)) {

            // Define o ID do empréstimo para a consulta
            stmtBusca.setInt(1, idEmprestimo);
            ResultSet rs = stmtBusca.executeQuery();

            // Verifica se encontrou o empréstimo
            if (rs.next()) {
                int idLivro = rs.getInt("id_livro");

                // Atualiza a data de devolução para a data atual
                try (PreparedStatement stmtAtualiza = conn.prepareStatement(sqlAtualiza)) {
                    stmtAtualiza.setDate(1, Date.valueOf(LocalDate.now()));
                    stmtAtualiza.setInt(2, idEmprestimo);
                    stmtAtualiza.executeUpdate();
                }

                // Atualiza o estoque do livro, aumentando 1 unidade
                LivroDAO.atualizarEstoque(idLivro, 1);

                System.out.println("✅ Devolução registrada com sucesso.");
            } else {
                // Caso o empréstimo não seja encontrado, informa o usuário
                System.out.println("❌ Empréstimo não encontrado.");
            }

        } catch (SQLException e) {
            // Em caso de erro, imprime mensagem detalhada
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
        }
    }

    // Método para listar todos os empréstimos cadastrados no banco
    public List<Emprestimo> listarEmprestimos() {
        List<Emprestimo> lista = new ArrayList<>();
        // Comando SQL para selecionar todos os dados da tabela Emprestimos
        String sql = "SELECT id_emprestimo, id_aluno, id_livro, data_emprestimo, data_devolucao FROM Emprestimos";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Percorre o resultado da consulta, criando objetos Emprestimo e adicionando à lista
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(
                        rs.getInt("id_emprestimo"),
                        rs.getInt("id_aluno"),
                        rs.getInt("id_livro"),
                        rs.getDate("data_emprestimo").toLocalDate(),
                        // Verifica se a data de devolução não é nula antes de converter
                        rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null
                );
                lista.add(emprestimo);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos: " + e.getMessage());
        }

        return lista; // Retorna a lista de empréstimos
    }

    // Método para buscar um empréstimo específico pelo seu ID
    public Emprestimo buscarEmprestimoPorId(int idEmprestimo) {
        String sql = "SELECT id_emprestimo, id_aluno, id_livro, data_emprestimo, data_devolucao FROM Emprestimos WHERE id_emprestimo = ?";
        Emprestimo emprestimo = null;

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmprestimo);

            try (ResultSet rs = stmt.executeQuery()) {
                // Se encontrar o empréstimo, cria o objeto Emprestimo
                if (rs.next()) {
                    emprestimo = new Emprestimo(
                            rs.getInt("id_emprestimo"),
                            rs.getInt("id_aluno"),
                            rs.getInt("id_livro"),
                            rs.getDate("data_emprestimo").toLocalDate(),
                            rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimo: " + e.getMessage());
        }

        return emprestimo; // Retorna o empréstimo encontrado ou null
    }

    // Método para atualizar os dados de um empréstimo existente
    public void atualizarEmprestimo(Emprestimo emprestimo) {
        // SQL para atualizar os campos do empréstimo pelo seu ID
        String sql = "UPDATE Emprestimos SET id_aluno = ?, id_livro = ?, data_emprestimo = ?, data_devolucao = ? WHERE id_emprestimo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Setar os novos valores para o empréstimo
            stmt.setInt(1, emprestimo.getIdAluno());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.setDate(4, Date.valueOf(emprestimo.getDataDevolucao()));
            stmt.setInt(5, emprestimo.getIdEmprestimo());

            // Executa o UPDATE
            stmt.executeUpdate();
            System.out.println("Empréstimo atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar empréstimo: " + e.getMessage());
        }
    }

    // Método para deletar um empréstimo pelo seu ID
    public void deletarEmprestimo(int idEmprestimo) {
        // SQL para deletar o registro da tabela Emprestimos
        String sql = "DELETE FROM Emprestimos WHERE id_emprestimo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o ID do empréstimo que será deletado
            stmt.setInt(1, idEmprestimo);

            // Executa o DELETE e retorna quantas linhas foram afetadas
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Empréstimo deletado com sucesso!");
            } else {
                System.out.println("Nenhum empréstimo encontrado com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar empréstimo: " + e.getMessage());
        }
    }
}
