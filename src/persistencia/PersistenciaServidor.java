package persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import auxiliar.AuxiliarConstantes;
import entidade.EntidadeEscala;
import entidade.EntidadeServidor;

/**
 * Classe responsavel por fornecer as operacoes de banco de dados para informacoes do servidor.
 * @author Johanes Severo dos Santos
 */
public class PersistenciaServidor {
	
	private PersistenciaConexao con;
	
	public PersistenciaServidor() throws Exception {
		
		con = new PersistenciaConexao();
		con.conectar();
	
	}
	
	public void inserirServidor(EntidadeServidor servidores[]) throws Exception {
		
			for (int i = 0; i < servidores.length; ++i)
				inserirServidor(servidores[i]);
	}
	
	public void inserirServidor(EntidadeServidor servidor) throws Exception {
		
		try {
			
			if (pesquisarServidor(servidor.getMatricula(), servidor.getCargaHoraria()) == null) {
	
				PreparedStatement declaracao = con.getConexao().prepareStatement("insert into tb_servidor (NOME, MATRICULA, CARGAHORARIA, SETOR, CARGO, CARGAHORARIASETOR) values(" + 
																					"'" + servidor.getNome().trim() +
																					"', '" +   servidor.getMatricula().trim() 
																					+ "', '" +  servidor.getCargaHoraria() 
																					+ "', '" + servidor.getSetor().trim()
																					+ "', '" + servidor.getCargo().trim() 
																					+ "', '" +  servidor.getCargaHorariaSetor() 
																					+ "')");
				
				
				con.comandoDDL(declaracao);
			
			}
			else
				atualizarServidor(servidor);
			
		}
		catch(SQLException ex) {
			
			throw new Exception("Nao foi possivel inserir: " + ex.getMessage());
		}
		
	}
	
	public void atualizarServidor(EntidadeServidor servidor) throws Exception {
		
		try {
				
				Statement declaracao = con.getConexao().createStatement();
				con.comandoDDL(declaracao, "update tb_servidor set " +
						"nome = '" + servidor.getNome().trim() + "', " +
						"matricula = '" + servidor.getMatricula().trim() + "', " +
						"setor = '" + servidor.getSetor().trim() + "', " +
						"cargo = '" + servidor.getCargo().trim() + "', " +
						"cargaHoraria = " + servidor.getCargaHoraria() + ", " +
						"cargaHorariaSetor = " + servidor.getCargaHoraria() + "where matricula='" + servidor.getMatricula() + "'");
		
				
		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel atualizar: " + ex.getMessage());
		}
		
	}
	
	public EntidadeServidor pesquisarServidor(String matricula, int cargaHoraria) throws Exception {
		
		EntidadeServidor servidor = null;
		
		try {
			
			ResultSet rs = con.comandoDML(con.getConexao().createStatement(), "select * from tb_servidor where matricula = '" + matricula + "' AND cargaHoraria = " + cargaHoraria);
			
			while(rs.next()) {
				servidor = new EntidadeServidor();
				servidor.setMatricula(rs.getString("matricula"));
				servidor.setNome(rs.getString("nome"));
				servidor.setCargaHoraria(rs.getInt("cargaHoraria"));
				servidor.setSetor(rs.getString("setor"));
				servidor.setCargo(rs.getString("cargo"));
				servidor.setCargaHorariaSetor(rs.getInt("cargaHorariaSetor"));
				
			}
			
			if (matricula == null)
				servidor = null;
		
			
		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel pesquisar: " + ex.getMessage());
		}
			
		return servidor;
			
	}
	
	public EntidadeServidor[] pesquisarServidorEscala(String setor, int mes, int ano) throws Exception {
		
		EntidadeServidor servidor = null;
		ArrayList<EntidadeServidor> servidores = new ArrayList<EntidadeServidor>();
		EntidadeServidor s[] = null;
		
		try {
			
			ResultSet rs = con.comandoDML(con.getConexao().createStatement(), "select * from tb_servidor where setor = '" + setor + "'");
			
			while(rs.next()) {
				servidor = new EntidadeServidor();
				servidor.setMatricula(rs.getString("matricula"));
				servidor.setNome(rs.getString("nome"));
				servidor.setCargaHoraria(rs.getInt("cargaHoraria"));
				servidor.setSetor(rs.getString("setor"));
				servidor.setCargo(rs.getString("cargo"));
				servidor.setCargaHorariaSetor(rs.getInt("cargaHorariaSetor"));
				servidores.add(servidor);
			}
			
		
	
			if (pesquisarEscala(servidores.get(0).getMatricula(), mes, ano) != null) {
				s = new EntidadeServidor[servidores.size()];
				
				for (int i = 0; i < s.length; ++ i) {
					
					s[i] = servidores.get(i);
					s[i].setEscala(pesquisarEscala(s[i].getMatricula(), mes, ano));
					
				}
			}
			else
				s = null;

		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel pesquisar: " + ex.getMessage());
		}
			
		return s;
			
	}
	
	public EntidadeServidor[] pesquisarServidorEscalaNaoLancada(String setor) throws Exception {
		
		EntidadeServidor servidor = null;
		ArrayList<EntidadeServidor> servidores = new ArrayList<EntidadeServidor>();
		EntidadeServidor s[];
		
		try {
			
			ResultSet rs = con.comandoDML(con.getConexao().createStatement(), "select * from tb_servidor where setor = '" + setor + "'");
			
			while(rs.next()) {
				servidor = new EntidadeServidor();
				servidor.setMatricula(rs.getString("matricula"));
				servidor.setNome(rs.getString("nome"));
				servidor.setCargaHoraria(rs.getInt("cargaHoraria"));
				servidor.setSetor(rs.getString("setor"));
				servidor.setCargo(rs.getString("cargo"));
				servidor.setCargaHorariaSetor(rs.getInt("cargaHorariaSetor"));
				servidores.add(servidor);
			}
			
	
			
			if (pesquisarEscalaNaoLancada(servidores.get(0).getMatricula()) == null) {
				s = null;
			}
			else {
			
				s = new EntidadeServidor[servidores.size()];
			
				for (int i = 0; i < s.length; ++ i) {
				
					s[i] = servidores.get(i);
					s[i].setEscala(pesquisarEscalaNaoLancada(s[i].getMatricula()));
				
				}
			}
		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel pesquisar: " + ex.getMessage());
		}
			
		return s;
			
	}
	
   public void inserirEscala(EntidadeServidor servidores[]) throws Exception {
		
		for (int i = 0; i < servidores.length; ++i)
			inserirEscala(servidores[i].getEscala());
	}
	
	public void inserirEscala(EntidadeEscala escala) throws Exception {
		
		try {
			
			if (pesquisarEscala(escala.getMatricula(), escala.getMes(), escala.getAno()) == null) {
				
				String DNC = "";
				String DNE = "";
				String escalaContratual = "";
				String escalaExtra = "";
				
				for (int i = 0; i < AuxiliarConstantes.getUltimoDiaMes(escala.getMes(), escala.getAno()); ++i) {
					
					DNC += "D" + (i+1) + "C, ";
					DNE += "D" + (i+1) + "E, ";
					escalaContratual += "'" + escala.getEscalaHoraContratual()[i] + "', ";
					escalaExtra += "'" + escala.getEscalaHoraExtra()[i] + "', ";
				}
				
				PreparedStatement declaracao = con.getConexao().prepareStatement("insert into tb_escala (MATRICULA, MES, ANO," + DNC + DNE + "OBSERVACAO, HORASSEMANA1, HORASSEMANA2, HORASSEMANA3, HORASSEMANA4, HORASSEMANA5, HORASSEMANA6, PASSAGEMMESANTERIOR, PASSAGEMPROXIMOMES, COMPENSACAO, TOTALHORASEXTRAS, QUANTIDADEREFEICOESDIURNO, QUANTIDADEREFEICOESNOTURNO, ESCALALANCADA, CONTEMHORAEXTRA) values(" + 
																				"'" + escala.getMatricula() + "', " 
																				+  escala.getMes() + ", " 
																				+  escala.getAno() + "," 
																				+ escalaContratual
																				+ escalaExtra 
																				+ "'" +  escala.getObservacao() + "',"
																				+  escala.getHorasSemana1() + ","
																				+  escala.getHorasSemana2() + ","
																				+  escala.getHorasSemana3() + ","
																				+  escala.getHorasSemana4() + ","
																				+  escala.getHorasSemana5() + ","
																				+  escala.getHorasSemana6() + ","
																				+  escala.getHorasUltimaSemanaMesAnterior() + ","
																				+  escala.getHorasUltimaSemana() + ","
																				+  escala.getBancoHorasDestaEscala() + ","
																				+  escala.getTotalHorasExtras() + ","
																				+  escala.getQuantidadeRefeicoesDiurno() + ","
																				+  escala.getQuantidadeRefeicoesNoturno() + ","
																				+  escala.isEscalaLancada() + ","
																				+  escala.isContemHoraExtra()
																				+ ")");

				con.comandoDDL(declaracao);
		
			}
			else {
				atualizarEscala(escala);
			}
		}
		catch(SQLException ex) {
			
			throw new Exception("Nao foi possivel inserir: " + ex.getMessage());
		}
		
	}
	
	public void inserirNovaEscala(EntidadeServidor servidores[]) throws Exception {
			
		for (int i = 0; i < servidores.length; ++i)
			inserirNovaEscala(servidores[i].getEscala());
	}
	
	public void inserirNovaEscala(EntidadeEscala escala) throws Exception {
		
		try {
			
			if (pesquisarEscala(escala.getMatricula(), (escala.getMes() == 12 ? 1 : escala.getMes() + 1 ), escala.getAno()) == null) {
				
				PreparedStatement declaracao = con.getConexao().prepareStatement("insert into tb_escala (MATRICULA, MES, ANO, OBSERVACAO, HORASSEMANA1, HORASSEMANA2, HORASSEMANA3, HORASSEMANA4, HORASSEMANA5, HORASSEMANA6, PASSAGEMMESANTERIOR, PASSAGEMPROXIMOMES, COMPENSACAO, TOTALHORASEXTRAS, QUANTIDADEREFEICOESDIURNO, QUANTIDADEREFEICOESNOTURNO, ESCALALANCADA, CONTEMHORAEXTRA) values(" + 
						"'" + escala.getMatricula() + "', " 
						+  (escala.getMes() == 12 ? 1 : escala.getMes() + 1 ) + ", " 
						+  escala.getAno() + "," 
						+ "'',"
						+ 0 + ","
						+  0 + ","
						+ 0 + ","
						+  0 + ","
						+ 0 + ","
						+ 0 + ","
						+  escala.getHorasUltimaSemana() + ","
						+ 0 + ","
						+  0 + ","
						+ 0 + ","
						+ 0 + ","
						+ false + ","
						+ false
						+ ")");

				con.comandoDDL(declaracao);
			
			}
		}
		catch(SQLException ex) {
			
			throw new Exception("Nao foi possivel inserir: " + ex.getMessage());
		}
		
	}
	
	public EntidadeEscala pesquisarEscalaNaoLancada(String matricula) throws Exception {
		
		EntidadeEscala escala = null;
		
		try {
			
			con = new PersistenciaConexao();
			
			ResultSet rs = con.comandoDML(con.getConexao().createStatement(), "select * from tb_escala where matricula = '" + matricula + "' AND escalaLancada = " + false);
			
			while(rs.next()) {
				escala = new EntidadeEscala();
				escala.setMatricula(rs.getString("matricula")); 
				escala.setMes(rs.getInt("mes")); 
				escala.setAno(rs.getInt("ano")); 
				escala.setObservacao(rs.getString("observacao"));
				escala.setHorasSemana1(rs.getFloat("horasSemana1"));
				escala.setHorasSemana2(rs.getFloat("horasSemana2"));
				escala.setHorasSemana3(rs.getFloat("horasSemana3"));
				escala.setHorasSemana4(rs.getFloat("horasSemana4"));
				escala.setHorasSemana5(rs.getFloat("horasSemana5"));
				escala.setHorasSemana6(rs.getFloat("horasSemana6"));
				escala.setHorasUltimaSemanaMesAnterior(rs.getFloat("passagemMesAnterior"));
				escala.setHorasUltimaSemana(rs.getFloat("passagemProximoMes"));
				escala.setBancoHorasDestaEscala(rs.getFloat("compensacao"));
				escala.setTotalHorasExtras(rs.getInt("totalHorasExtras"));
				escala.setQuantidadeRefeicoesDiurno(rs.getInt("quantidadeRefeicoesDiurno"));
				escala.setQuantidadeRefeicoesDiurno(rs.getInt("quantidadeRefeicoesNoturno"));
				escala.setEscalaLancada(rs.getBoolean("escalaLancada"));
				escala.setContemHoraExtra(rs.getBoolean("contemHoraExtra"));
			
				
			}
			
			
		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel pesquisar: " + ex.getMessage());
		}
			
		return escala;
			
	}
	
	public EntidadeEscala pesquisarEscala(String matricula, int mes, int ano) throws Exception {
		
		EntidadeEscala escala = null;
		
		try {
			
			ResultSet rs = con.comandoDML(con.getConexao().createStatement(), "select * from tb_escala where matricula = '" + matricula + "' AND mes = " + mes + "AND ano = " + ano);
			
			while(rs.next()) {
				escala = new EntidadeEscala();
				escala.setMatricula(rs.getString("matricula")); 
				escala.setMes(rs.getInt("mes")); 
				escala.setAno(rs.getInt("ano")); 
				escala.setObservacao(rs.getString("observacao"));
				for (int i = 0; i < AuxiliarConstantes.getUltimoDiaMes(mes, ano); ++i) {
					
					escala.getEscalaHoraContratual()[i] = rs.getString("D" + (i+1) + "C");
					escala.getEscalaHoraExtra()[i] = rs.getString("D" + (i+1) + "E");
				}
				escala.setHorasSemana1(rs.getFloat("horasSemana1"));
				escala.setHorasSemana2(rs.getFloat("horasSemana2"));
				escala.setHorasSemana3(rs.getFloat("horasSemana3"));
				escala.setHorasSemana4(rs.getFloat("horasSemana4"));
				escala.setHorasSemana5(rs.getFloat("horasSemana5"));
				escala.setHorasSemana6(rs.getFloat("horasSemana6"));
				escala.setHorasUltimaSemanaMesAnterior(rs.getFloat("passagemMesAnterior"));
				escala.setHorasUltimaSemana(rs.getFloat("passagemProximoMes"));
				escala.setBancoHorasDestaEscala(rs.getFloat("compensacao"));
				escala.setTotalHorasExtras(rs.getInt("totalHorasExtras"));
				escala.setQuantidadeRefeicoesDiurno(rs.getInt("quantidadeRefeicoesDiurno"));
				escala.setQuantidadeRefeicoesNoturno(rs.getInt("quantidadeRefeicoesNoturno"));
				escala.setEscalaLancada(rs.getBoolean("escalaLancada"));
				escala.setContemHoraExtra(rs.getBoolean("contemHoraExtra"));
			
				
			}
			
			
		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel pesquisar: " + ex.getMessage());
		}
			
		return escala;
			
	}
	
	public EntidadeEscala excluirEscala(String matricula, int mes, int ano) throws Exception {
		
		EntidadeEscala escala = null;
		
		try {
			
			con.comandoDDL(con.getConexao().createStatement(), "delete from tb_escala where matricula = '" + matricula + "' AND mes = " + mes + "AND ano = " + ano);

		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel pesquisar: " + ex.getMessage());
		}
			
		return escala;
			
	}
	
	public void excluirEscala(EntidadeServidor servidores[]) throws Exception {
		
		for (int i = 0; i < servidores.length; ++i)
			excluirEscala(servidores[i].getMatricula(), servidores[i].getEscala().getAno(), servidores[i].getEscala().getMes());
	}
	
	public void atualizarEscala(EntidadeEscala escala) throws Exception {
		
		try {
				
				String DNC = "";
				String DNE = "";
				String escalaContratual = "";
				String escalaExtra = "";
				
				for (int i = 0; i < AuxiliarConstantes.getUltimoDiaMes(escala.getMes(), escala.getAno()); ++i) {
					escalaContratual += "D" + (i+1) + "C = '" + escala.getEscalaHoraContratual()[i] + "', ";
					escalaExtra += "D" + (i+1) + "E = '" + escala.getEscalaHoraExtra()[i] + "', ";
				}
				
				Statement declaracao = con.getConexao().createStatement();
				con.comandoDDL(declaracao, "update tb_escala set " +
																				"matricula = '" + escala.getMatricula() + "', " 
																				+ " mes = " +  escala.getMes() + ", " 
																				+ " ano = " +  escala.getAno() + "," 
																				+ escalaContratual
																				+ escalaExtra 
																				+ "observacao = '" +  escala.getObservacao() + "',"
																				+ "horasSemana1 = " +  escala.getHorasSemana1() + ","
																				+ "horasSemana2 = " +  escala.getHorasSemana2() + ","
																				+ "horasSemana3 = " +  escala.getHorasSemana3() + ","
																				+ "horasSemana4 = " +  escala.getHorasSemana4() + ","
																				+ "horasSemana5 = " +  escala.getHorasSemana5() + ","
																				+ "horasSemana6 = " +  escala.getHorasSemana6() + ","
																				+ "passagemMesAnterior = " +  escala.getHorasUltimaSemanaMesAnterior() + ","
																				+ "passagemProximoMes = " +  escala.getHorasUltimaSemana() + ","
																				+ "compensacao = " +  escala.getBancoHorasDestaEscala() + ","
																				+ "totalHorasExtras = " +  escala.getTotalHorasExtras() + ","
																				+ "quantidadeRefeicoesDiurno = "+  escala.getQuantidadeRefeicoesDiurno() + ","
																					+ "quantidadeRefeicoesNoturno = "+  escala.getQuantidadeRefeicoesNoturno() + ","
																				+ "escalaLancada = " +  escala.isEscalaLancada() + ","
																				+ "contemHoraExtra = " +  escala.isContemHoraExtra() + ","
																				+ "escalaAlterada = " +  escala.isEscalaAlterada()
																				+ " where matricula='" + escala.getMatricula() + "' and mes=" + escala.getMes() + " and ano=" + + escala.getAno());
		}
		catch(Exception ex) {
			
			throw new Exception("Nao foi possivel atualizar: " + ex.getMessage());
		}
		
	}
}
