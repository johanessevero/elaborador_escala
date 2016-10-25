package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import auxiliar.JDBCUtil;

/**
 * 
 * @author Johanes Severo dos Santos
 * Classe responsavel por gerenciar a conexao com o banco de dados, bem como executar declara��es no SGBD.
 */
public class PersistenciaConexao {
	
	private Connection conexao;
	
	public void conectar() throws Exception {
		
		conexao = JDBCUtil.getInstance().getConexao();
		
	}
	
	public Connection getConexao() {
		
		return conexao;
	}
	
	public ResultSet comandoDML(PreparedStatement declaracao) throws Exception {
		
		ResultSet rs = null;
		
		try {
		
			rs = declaracao.executeQuery();
		
		}
		catch(SQLException ex) {
			
			throw new Exception("Problema na execucao no banco de dados. (manipula��o, comandos DML): " + ex.getMessage());
			
		}
		
		return rs;
	}
	
	public ResultSet comandoDML(Statement declaracao, String sql) throws Exception {
		
		ResultSet rs = null;
		
		try {
		
			rs = declaracao.executeQuery(sql);
		
		}
		catch(SQLException ex) {
			
			throw new Exception("Problema na execucao no banco de dados. (manipula��o, comandos DML): " + ex.getMessage());
			
		}
		
		return rs;
	}
	
	public void comandoDDL(PreparedStatement declaracao) throws Exception {
	
		try {
			
			declaracao.executeUpdate();
			
		}
		catch(SQLException ex) {
			
			throw new Exception("Problema na execucao no banco de dados. (definicao, comandos DDL): " + ex.getMessage());
		}
		
	}
	
	public void comandoDDL(Statement declaracao, String sql) throws Exception {
		
		try {
			
			declaracao.execute(sql);
			
		}
		catch(SQLException ex) {
			
			throw new Exception("Problema na execucao no banco de dados. (definicao, comandos DDL): " + ex.getMessage());
		}
		
	}

}
