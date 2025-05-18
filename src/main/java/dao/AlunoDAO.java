package dao;

import model.Aluno;
import util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // Método para cadastrar um novo aluno no banco de dados
    public void cadastrarAluno(Aluno aluno) {
        // Comando SQL para inserir dados na tabela Alunos
        String sql = "INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros do PreparedStatement
            stmt.setString(1, aluno.getNome());  // nome do aluno
            stmt.setString(2, aluno.getMatricula());  // matrícula do aluno
            // Converte LocalDate para java.sql.Date para salvar no banco
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));

            // Executa o comando INSERT
            stmt.executeUpdate();
            System.out.println("Aluno cadastrado com sucesso!");

        } catch (SQLException e) {
            // Em caso de erro exibe mensagem
            System.err.println("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    // Método para listar todos os alunos cadastrados
    public List<Aluno> listarAlunos() {
        List<Aluno> lista = new ArrayList<>();
        // Consulta SQL para selecionar todos os alunos
        String sql = "SELECT id_aluno, nome_aluno, matricula, data_nascimento FROM Alunos";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itera sobre os resultados para criar objetos Aluno
            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getInt("id_aluno"),             // ID do aluno
                        rs.getString("nome_aluno"),        // nome
                        rs.getString("matricula"),         // matrícula
                        rs.getString("data_nascimento")    // data de nascimento (string)
                );
                lista.add(aluno);
            }

        } catch (SQLException e) {
            // Em caso de erro ao listar
            System.err.println("Erro ao listar alunos: " + e.getMessage());
        }

        return lista;  // retorna a lista com os alunos
    }

    // Método para buscar um aluno pelo ID
    public Aluno buscarAlunoPorId(int idAluno) {
        String sql = "SELECT id_aluno, nome_aluno, matricula, data_nascimento FROM Alunos WHERE id_aluno = ?";
        Aluno aluno = null; // Inicializa null caso não encontre

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno); // Define parâmetro do ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    aluno = new Aluno(
                            rs.getInt("id_aluno"),
                            rs.getString("nome_aluno"),
                            rs.getString("matricula"),
                            rs.getString("data_nascimento")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluno: " + e.getMessage());
        }

        return aluno;  // Retorna o aluno encontrado ou null
    }

    // Método para atualizar os dados de um aluno existente
    public void atualizarAluno(Aluno aluno) {
        String sql = "UPDATE Alunos SET nome_aluno = ?, matricula = ?, data_nascimento = ? WHERE id_aluno = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros para o UPDATE
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            stmt.setInt(4, aluno.getId());

            // Executa a atualização e verifica sucesso
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluno atualizado com sucesso!");
            } else {
                System.out.println("Erro: Nenhum aluno encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    // Método para deletar aluno pelo ID
    public void deletarAluno(int idAluno) {
        String sql = "DELETE FROM Alunos WHERE id_aluno = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);  // Define ID do aluno para deletar

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aluno deletado com sucesso!");
            } else {
                System.out.println("Erro: Nenhum aluno encontrado com esse ID.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar aluno: " + e.getMessage());
        }
    }

    // Método para verificar se um aluno existe pelo ID
    public boolean existe(int idAluno) {
        String sql = "SELECT 1 FROM Alunos WHERE id_aluno = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);  // Define parâmetro do ID
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // Retorna true se existir, false caso contrário
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência do aluno: " + e.getMessage());
        }
        return false;
    }
}
