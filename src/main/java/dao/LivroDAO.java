package dao;

import model.Livro;
import util.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // Método para cadastrar um novo livro no banco de dados
    public void cadastrarLivro(Livro livro) {
        // Comando SQL para inserir dados na tabela Livros
        String sql = "INSERT INTO Livros (titulo, autor, ano_publicacao, quantidade_estoque) VALUES (?, ?, ?, ?)";

        // Conexão e execução do comando com PreparedStatement para evitar SQL Injection
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Definindo os valores dos parâmetros SQL
            stmt.setString(1, livro.getTitulo());          // título do livro
            stmt.setString(2, livro.getAutor());           // autor do livro
            stmt.setInt(3, livro.getAnoPublicacao());      // ano de publicação
            stmt.setInt(4, livro.getQuantidadeEstoque());  // quantidade em estoque

            // Executa o comando e retorna quantas linhas foram afetadas
            int rowsAffected = stmt.executeUpdate();

            // Verifica se o cadastro foi bem-sucedido
            if (rowsAffected > 0) {
                System.out.println("Livro cadastrado com sucesso!");
            } else {
                System.out.println("Erro ao cadastrar livro.");
            }

        } catch (SQLException e) {
            // Tratamento de exceção e exibição da mensagem de erro
            System.err.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    // Método para listar todos os livros cadastrados
    public List<Livro> listarLivros() {
        List<Livro> lista = new ArrayList<>();

        // Comando SQL para selecionar todos os livros
        String sql = "SELECT id_livro, titulo, autor, ano_publicacao, quantidade_estoque FROM Livros";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {  // Executa a consulta

            // Enquanto houver resultados, cria objetos Livro e adiciona à lista
            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id_livro"),             // ID do livro
                        rs.getString("titulo"),            // título
                        rs.getString("autor"),             // autor
                        rs.getInt("ano_publicacao"),       // ano publicação
                        rs.getInt("quantidade_estoque")   // estoque
                );
                lista.add(livro);
            }

        } catch (SQLException e) {
            // Exibe erro caso ocorra problema ao listar
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }

        // Retorna a lista de livros obtida
        return lista;
    }

    // Método para buscar um livro específico pelo ID
    public Livro buscarLivroPorId(int idLivro) {
        // Comando SQL para buscar livro por ID
        String sql = "SELECT id_livro, titulo, autor, ano_publicacao, quantidade_estoque FROM Livros WHERE id_livro = ?";
        Livro livro = null;  // Inicializa com null caso não encontre

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro do ID do livro
            stmt.setInt(1, idLivro);

            try (ResultSet rs = stmt.executeQuery()) {
                // Se encontrar o livro, cria objeto Livro com os dados
                if (rs.next()) {
                    livro = new Livro(
                            rs.getInt("id_livro"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacao"),
                            rs.getInt("quantidade_estoque")
                    );
                }
            }

        } catch (SQLException e) {
            // Exibe erro caso ocorra problema na busca
            System.err.println("Erro ao buscar livro: " + e.getMessage());
        }

        // Retorna o livro encontrado ou null
        return livro;
    }

    // Método para atualizar os dados de um livro já cadastrado
    public void atualizarLivro(Livro livro) {
        // Comando SQL para atualizar livro, filtrando pelo id_livro
        String sql = "UPDATE Livros SET titulo = ?, autor = ?, ano_publicacao = ?, quantidade_estoque = ? WHERE id_livro = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros para atualização
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setInt(4, livro.getQuantidadeEstoque());
            stmt.setInt(5, livro.getId());

            // Executa a atualização e verifica sucesso
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro atualizado com sucesso!");
            } else {
                System.out.println("Erro: Nenhum livro encontrado com esse ID.");
            }

        } catch (SQLException e) {
            // Trata erro de atualização
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }

    // Método para deletar um livro pelo seu ID
    public void deletarLivro(int idLivro) {
        // Comando SQL para deletar o livro
        String sql = "DELETE FROM Livros WHERE id_livro = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o ID do livro a ser deletado
            stmt.setInt(1, idLivro);

            // Executa a deleção e verifica se teve sucesso
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro deletado com sucesso!");
            } else {
                System.out.println("Erro: Nenhum livro encontrado com esse ID.");
            }

        } catch (SQLException e) {
            // Trata erro ao deletar livro
            System.err.println("Erro ao deletar livro: " + e.getMessage());
        }
    }

    // Método para verificar se um livro existe no banco pelo ID
    public boolean existe(int idLivro) {
        // Comando SQL que retorna 1 se encontrar o livro
        String sql = "SELECT 1 FROM Livros WHERE id_livro = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro do ID
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                // Retorna true se encontrou algum resultado
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência do livro: " + e.getMessage());
        }
        // Caso ocorra erro ou não encontre, retorna false
        return false;
    }

    // Método estático para atualizar a quantidade no estoque do livro
    public static void atualizarEstoque(int idLivro, int quantidadeDelta) {
        // quantidadeDelta pode ser positivo (devolução) ou negativo (empréstimo)
        String sql = "UPDATE Livros SET quantidade_estoque = quantidade_estoque + ? WHERE id_livro = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros: quantidade a adicionar/subtrair e o ID do livro
            stmt.setInt(1, quantidadeDelta);
            stmt.setInt(2, idLivro);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Estoque atualizado com sucesso!");
            } else {
                System.out.println("Erro: Livro não encontrado para atualizar estoque.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
        }
    }
}
