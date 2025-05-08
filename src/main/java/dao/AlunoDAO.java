package dao;
import model.Aluno;
import util.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    public void cadastrarAluno(Aluno aluno) {
        String sql = "INSERT INTO Alunos (nome_aluno, matricula, data_nascimento) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            stmt.setString(3, aluno.getDataNascimento());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public List<Aluno> listarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM Alunos";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id_aluno"));
                aluno.setNome(rs.getString("nome_aluno"));
                aluno.setMatricula(rs.getString("matricula"));
                aluno.setDataNascimento(rs.getString("data_nascimento"));
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar alunos: " + e.getMessage());
        }
        return alunos;
    }
}