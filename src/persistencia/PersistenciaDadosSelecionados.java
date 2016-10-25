package persistencia;

import java.sql.ResultSet;
import java.util.ArrayList;

import entidade.EntidadeServidor;

public class PersistenciaDadosSelecionados {
	
	private PersistenciaConexao con;
	
	public PersistenciaDadosSelecionados() {
		
		con = new PersistenciaConexao();
		
	}
	
	public String[] getSetores() throws Exception {
		
		ArrayList<String> setores = new ArrayList<String>();
		String s[] = null;
		
		try {
			
			con = new PersistenciaConexao();
			con.conectar();
			
			ResultSet rs = con.comandoDML(con.getConexao().createStatement(), "select distinct setor from tb_servidor");
			
			while(rs.next()) {
				String setor = rs.getString("setor");
				setores.add(setor);
			}
			
			s = new String[setores.size()];
			for (int i = 0; i < s.length; ++i) {
				s[i] = setores.get(i);
			}
			
		}
		catch(Exception ex) {
			
				throw new Exception("Nao foi possivel obter os setores: " + ex.getMessage());
		}
		
		return s;
		
	}

}
