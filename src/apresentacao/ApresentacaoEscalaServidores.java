package apresentacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import persistencia.PersistenciaServidor;
import processamento.ProcessConferidor;
import processamento.ProcessGeracaoArquivoDados;
import auxiliar.AuxiliarConstantes;
import auxiliar.RelatorioException;
import entidade.EntidadeServidor;

/**
 * 
 * @author Johanes Severo dos Santos
 * Classe que implementa a interface de edicao e visualizacao das escalas dos servidores.
 */
public class ApresentacaoEscalaServidores extends JFrame implements ActionListener {

	private boolean habilitaCamposEscala;
	
	private int mes;
	private int ano;
	
	private String localArquivo;
	
	private JPanel pDadosEscalaSetor;
	private JPanel pEscalaServidores;
	private JPanel pQuantitativo;
	
	private EntidadeServidor servidores[];
	
	private PersistenciaServidor persistenciaServidor;
	
	private JButton btGerarRascunho;
	private JButton btLancarEscala;
	
	private JLabel lbDias[];
	private JLabel lbManha[];
	private JLabel lbTarde[];
	private JLabel lbNoite[];
	
	private static JLabel lbQuantitativo[][];

	private ProcessGeracaoArquivoDados geracaoArquivoDados;
	private ProcessConferidor conferidor;
	
	private boolean contemErro;
	
	private static HashMap<String, EntidadeServidor> listaServidoresMap = new HashMap<String, EntidadeServidor>();   
	private static ArrayList<EntidadeServidor> listaServidores = new ArrayList<EntidadeServidor>();
	
	public ApresentacaoEscalaServidores() {}
	
	public ApresentacaoEscalaServidores(EntidadeServidor servidores[], boolean habilitaCamposEscala, int mes, int ano, String localArquivo) throws Exception {
		
		super(AuxiliarConstantes.TITULO);
		conferidor = new ProcessConferidor();
		conferidor.conferirEscala(servidores);
		this.persistenciaServidor = new PersistenciaServidor();
		
		this.contemErro = false;
		
		geracaoArquivoDados = new ProcessGeracaoArquivoDados();
		
		this.habilitaCamposEscala = habilitaCamposEscala;
		this.mes = mes+1;
		this.ano = ano;
		this.localArquivo = localArquivo;
		
		lbDias = new JLabel[AuxiliarConstantes.getUltimoDiaMes(mes+1, ano) + 1];
		lbManha = new JLabel[AuxiliarConstantes.getUltimoDiaMes(mes+1, ano) + 1];
		lbTarde= new JLabel[AuxiliarConstantes.getUltimoDiaMes(mes+1, ano) + 1];
		lbNoite = new JLabel[AuxiliarConstantes.getUltimoDiaMes(mes+1, ano) + 1];
		
		lbQuantitativo = new JLabel[4][AuxiliarConstantes.getUltimoDiaMes(mes, ano) + 1];
		
		lbDias[0] = new JLabel("", JLabel.CENTER);
		lbDias[0].setBorder(BorderFactory.createLineBorder (Color.black, 1));
		lbDias[0].setOpaque(true);
		lbDias[0].setForeground(Color.WHITE);
		lbDias[0].setBackground(Color.BLUE);
		lbManha[0] = new JLabel("M", JLabel.CENTER);
		lbManha[0].setBorder(BorderFactory.createLineBorder (Color.black, 1));
		lbTarde[0] = new JLabel("T", JLabel.CENTER);
		lbTarde[0].setBorder(BorderFactory.createLineBorder (Color.black, 1));
		lbNoite[0] = new JLabel("N", JLabel.CENTER);
		lbNoite[0].setBorder(BorderFactory.createLineBorder (Color.black, 1));
		for (int i = 1; i < AuxiliarConstantes.getUltimoDiaMes(mes+1, ano) + 1; ++i) {
			
			lbDias[i] = new JLabel("" + i, JLabel.CENTER);
			lbDias[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
			lbDias[i].setOpaque(true);
			lbDias[i].setForeground(Color.WHITE);
			lbDias[i].setBackground(Color.BLUE);
			lbManha[i] = new JLabel("0", JLabel.CENTER);
			lbManha[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
			lbTarde[i] = new JLabel("0", JLabel.CENTER);
			lbTarde[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
			lbNoite[i] = new JLabel("0", JLabel.CENTER);
			lbNoite[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
			
		}
		
		lbQuantitativo[0] = lbDias;
		lbQuantitativo[1] = lbManha;
		lbQuantitativo[2] = lbTarde;
		lbQuantitativo[3] = lbNoite;
		
		pQuantitativo = new JPanel(new GridLayout(4, AuxiliarConstantes.getUltimoDiaMes(mes+1, ano) + 1));
		pDadosEscalaSetor = new JPanel();
		
		for (int i = 0; i < 4; ++i) {
			for (int k = 0; k < lbQuantitativo[i].length; ++k) {
				pQuantitativo.add(lbQuantitativo[i][k]);
			}
		}
		
		
		btGerarRascunho = new JButton("GERAR RASCUNHO");
		btGerarRascunho.setFont(new Font( "Monospaced", Font.BOLD, 25));
		btGerarRascunho.addActionListener(this);
		pDadosEscalaSetor.add(btGerarRascunho);
		btLancarEscala = new JButton("ATUALIZAR");
		btLancarEscala.setFont(new Font( "Monospaced", Font.BOLD, 25));
		btLancarEscala.addActionListener(this);
		
		pDadosEscalaSetor.add(btLancarEscala);
		
		pEscalaServidores = new JPanel(new GridLayout(servidores.length, 1));
		setLayout(new BorderLayout());
		add(pQuantitativo, BorderLayout.NORTH);
		add(pDadosEscalaSetor, BorderLayout.SOUTH);
		mostrarEscalasServidores(servidores);
		
		addWindowListener(new ApresentacaoFechamentoJanela(this));
		setLocation(0, 0);
		setResizable(true);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	
	}
	
	private void mostrarFuncionalidades() {
		
		pDadosEscalaSetor.removeAll();
		add(pDadosEscalaSetor, BorderLayout.NORTH);
		pDadosEscalaSetor.updateUI();
		
	}
	
	private void mostrarEscalasServidores(EntidadeServidor servidores[]) {
		
		try {
			pEscalaServidores.removeAll();
			this.servidores = servidores;
			
			persistenciaServidor.inserirEscala(servidores);
			persistenciaServidor.inserirServidor(servidores);
			
			EntidadeServidor s[] = null;
			s = persistenciaServidor.pesquisarServidorEscala(servidores[0].getSetor().trim(), mes, ano);

			for (int i = 0; i < servidores.length; ++i) {
				
				
				if (s == null)
					pEscalaServidores.add(new ApresentacaoEscalaServidor(servidores[i]));
				else {
					if (!ApresentacaoEscalaServidores.getListaServidoresMap().containsKey(s[i].getMatricula())) {
						
						ApresentacaoEscalaServidores.getListaServidoresMap().put(s[i].getMatricula(), s[i]);
						ApresentacaoEscalaServidores.getListaServidores().add(s[i]);
						ApresentacaoEscalaServidores.getListaServidores().trimToSize();
						
					}
					pEscalaServidores.add(new ApresentacaoEscalaServidor(servidores[i], s[i]));
				}
				
				if (!servidores[i].getEscala().isEscalaLancada() & contemErro == false)
					contemErro = true;
			}
			
			listaServidores.trimToSize();
			add(new JScrollPane(pEscalaServidores), BorderLayout.CENTER);
			pEscalaServidores.updateUI();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} 
		
	}
	
	public static JLabel[][] getLbQuantitativo() {
		return lbQuantitativo;
	}

	public static void setLbQuantitativo(JLabel[][] lbQuantitativo) {
		ApresentacaoEscalaServidores.lbQuantitativo = lbQuantitativo;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		EntidadeServidor servidoresNovos[] = new EntidadeServidor[listaServidores.size()];
		
		listaServidores.trimToSize();
		for (int i = 0; i < listaServidores.size(); ++i) {
			servidoresNovos[i] = listaServidores.get(i);
		}

		try {
			if (e.getSource() == btGerarRascunho) {
				
				//geracaoArquivoDados.geraFolhaPonto(servidores, "", mes-1, ano);
				if (servidoresNovos.length > 0) {
					geracaoArquivoDados.geraRascunho(servidoresNovos, localArquivo, mes, ano);
					
					JOptionPane.showMessageDialog(null, "Rascunho gerado com sucesso na mesma pasta deste aplicativo.", "OK", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Nenhum servidor validado!", "ERRO", JOptionPane.ERROR_MESSAGE);

				}
				
			}
			else
			if (e.getSource() == btLancarEscala) {
				
				if (servidoresNovos.length > 0) {
					persistenciaServidor.inserirEscala(servidoresNovos);
					JOptionPane.showMessageDialog(null, "Escala armazenada com sucesso.", "OK", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Nenhum servidor validado!", "ERRO", JOptionPane.ERROR_MESSAGE);

				}
				
			}
			
		}
		catch(RelatorioException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

		}
		
	}
	
	public static HashMap<String, EntidadeServidor> getListaServidoresMap() {
		return listaServidoresMap;
	}

	public static ArrayList<EntidadeServidor> getListaServidores() {
		return listaServidores;
	}
	
}
