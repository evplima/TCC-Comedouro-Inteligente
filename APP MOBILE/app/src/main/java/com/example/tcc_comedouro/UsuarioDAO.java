package com.example.tcc_comedouro;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {

    public boolean checkLogin(String login, String senha) {

        Connection con = ConexaoBD.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean check = false;

        senha =  Utils.criptografia(senha);

        try {

            stmt = con.prepareStatement("SELECT * FROM usuarios WHERE nm_login = ? and cd_senha = ?");
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();

            if (rs.next()) {
                check = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoBD.closeConnection(con, stmt, rs);
        }

        return check;

    }

    public boolean checaEmailExiste(String email) {

        Connection con = ConexaoBD.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean check = false;

        try {

            stmt = con.prepareStatement("SELECT * FROM usuarios WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {


                check = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoBD.closeConnection(con, stmt, rs);
        }

        return check;
    }

    public String cadastra(Usuario usuario) throws IOException{

        Connection con = ConexaoBD.getConnection();

        PreparedStatement stmt = null;

        ResultSet rs = null;
        String mensagem = "";
        try {

            stmt = con.prepareStatement("SELECT * FROM usuarios WHERE nm_login = ? ");
            stmt.setString(1, usuario.getLogin());

            rs = stmt.executeQuery();

            if (rs.next()) {
                mensagem += "Já existe um cadastro com este usuário\n";
            }

            stmt = con.prepareStatement("SELECT * FROM usuarios WHERE cd_cpf = ? ");
            stmt.setString(1, usuario.getCpf());

            rs = stmt.executeQuery();

            if (rs.next()) {
                mensagem += "Já existe um cadastro com este CPF\n";
            }

            stmt = con.prepareStatement("SELECT * FROM usuarios WHERE email = ? ");
            stmt.setString(1, usuario.getEmail());

            rs = stmt.executeQuery();

            if (rs.next()) {
                mensagem += "Já existe um cadastro com este e-mail\n";
            }


          if(mensagem.length() < 1) {
              try {
                  stmt = con.prepareStatement("INSERT INTO usuarios (nm_usuario, nm_login, email, cd_cpf, cd_senha) "
                          + "VALUES (?,?,?,?,?)");
                  stmt.setString(1, usuario.getNome());
                  stmt.setString(2, usuario.getLogin());
                  stmt.setString(3, usuario.getEmail());
                  stmt.setString(4, usuario.getCpf());
                  stmt.setString(5, Utils.criptografia(usuario.getSenha()));
                  stmt.executeUpdate();
                  mensagem += "Cadastrado com sucesso!\n";
                  // enviar email
                  // enviaEmailCadastro(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getLogin(), usuario.getSenha());

              } catch (SQLException ex) {
                  System.out.println(ex);
              } finally {
                  ConexaoBD.closeConnection(con, stmt);
              }
          }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoBD.closeConnection(con, stmt, rs);
        }

        return mensagem;
        //codigo do create


    }

    public Usuario busca(String email) {

        Connection con = ConexaoBD.getConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM usuarios WHERE email = ?");
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            while (rs.next()) {
                usuario = new Usuario(rs.getString("nm_usuario"), rs.getString("nm_login"), rs.getString("email"), rs.getString("cd_cpf"), Utils.descriptografar(rs.getString("cd_senha")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoBD.closeConnection(con, stmt, rs);
        }

        return usuario;

    }

}
