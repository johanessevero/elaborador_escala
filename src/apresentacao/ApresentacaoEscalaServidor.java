package apresentacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import persistencia.PersistenciaServidor;
import processamento.ProcessConferidor;
import auxiliar.AuxiliarConstantes;
import auxiliar.AuxiliarGeral;
import entidade.EntidadeEscala;
import entidade.EntidadeLegenda;
import entidade.EntidadeMensagem;
import entidade.EntidadeServidor;

/**
 * 
 * @author Johanes Severo dos Santos
 * Classe que implementa a interface de apresentacao da escala de um servidor.
 */
public class ApresentacaoEscalaServidor extends JPanel implements ActionListener, KeyListener {
	

	private JLabel lbDiasSemana[];
	private JLabel lbRotuloPassagemProximoMes, lbPassagemProximoMes;
	private JLabel lbRotuloSemana1, lbSemana1;
	private JLabel lbRotuloSemana2, lbSemana2;
	private JLabel lbRotuloSemana3, lbSemana3; 
	private JLabel lbRotuloSemana4, lbSemana4;
	private JLabel lbRotuloSemana5, lbSemana5; 
	private JLabel lbRotuloSemana6, lbSemana6;
	private JLabel lbRotuloErro, lbErro;
	private JLabel lbRotuloMes, lbMes;
	private JLabel lbRotuloAno, lbAno;
	private JLabel lbRotuloQuantidadeRefeicoes, lbQuantidadeRefeicoesDiurno, lbQuantidadeRefeicoesNoturno;
	private JLabel lbRotuloTotalHorasExtras, lbTotalHorasExtras;
	private JLabel lbRotuloCompensacao, lbCompensacao, lbRotuloCompensacaoMesAnterior, lbCompensacaoMesAnterior;
	
	private JTextField txMatricula, 
						txNome,  
						txCargo, 
						txCargaHoraria, 
						txPassagemMesAnterior;  
	private JTextField txEscalaContratual[];
	private JTextField txEscalaExtra[];
	private JTextField txObservacao;
	
	private JButton btValidar;
	private JButton btLimpar;
	private JButton btQuantitativo;
	
	private EntidadeServidor servidor, servidorMesAnterior;
	
	private EntidadeMensagem mensagem;
	
	private int mes;
	private int ano;
	private int numDiasResto;
	
	private String diasSemanaResto[];
	
	private ProcessConferidor processConferidor;

	private PersistenciaServidor persistenciaServidor;
	
	public ApresentacaoEscalaServidor(EntidadeServidor servidor) throws Exception {
		
		setLayout(new BorderLayout());
		
		processConferidor = new ProcessConferidor();
		persistenciaServidor = new PersistenciaServidor();
		this.servidorMesAnterior = servidor;
		this.mes = servidorMesAnterior.getEscala().getMes()+1;
		this.ano = servidorMesAnterior.getEscala().getAno();
		this.servidor = new EntidadeServidor();
		this.servidor.setNome(new String(servidorMesAnterior.getNome()));
		this.servidor.setMatricula(new String(servidorMesAnterior.getMatricula()));
		this.servidor.setCargaHoraria(servidorMesAnterior.getCargaHoraria());
		this.servidor.setCargaHorariaSetor(servidorMesAnterior.getCargaHorariaSetor());
		this.servidor.setCargo(new String(servidorMesAnterior.getCargo()));
		this.servidor.setSetor(new String(servidorMesAnterior.getSetor().trim()));
		this.servidor.setEscala(new EntidadeEscala());
		this.servidor.getEscala().setMes(mes);
		this.servidor.getEscala().setAno(ano);
		this.servidor.getEscala().setHorasUltimaSemanaMesAnterior(servidorMesAnterior.getEscala().getHorasUltimaSemana());
		this.servidor.getEscala().setMatricula(servidorMesAnterior.getMatricula());
		persistenciaServidor.inserirServidor(servidor);
		persistenciaServidor.inserirEscala(servidor.getEscala());
		montaLayout(true);
		validar();
	}
	
	public ApresentacaoEscalaServidor(EntidadeServidor servidorMesAnterior, EntidadeServidor servidorProximoMes) throws Exception {
		
		setLayout(new BorderLayout());
		processConferidor = new ProcessConferidor();
		this.servidorMesAnterior = servidorMesAnterior;
		this.mes = servidorProximoMes.getEscala().getMes();
		this.ano = servidorMesAnterior.getEscala().getAno();
		this.servidor = servidorProximoMes;
		montaLayoutProximoMes(true);
		validar();
	}
	
	private void montaLayoutProximoMes(boolean novo) {
		
		try {

			getRestoUtimaSemanaMesAnterior();
			int ultimoDiaMes = AuxiliarConstantes.getUltimoDiaMes(servidor.getEscala().getMes(), servidor.getEscala().getAno());
			
			JPanel pDadosPessoais = new JPanel(new GridLayout(6, 1));
			JPanel pInformacoes = new JPanel(new FlowLayout(FlowLayout.LEFT));	
			
			JPanel pDadosEscala = new JPanel(new GridLayout(3, numDiasResto + ultimoDiaMes));
			JPanel pFuncionalidades = new JPanel();
			
			txMatricula = new JTextField(servidor.getMatricula());
			txMatricula.setForeground(Color.BLUE);
			txMatricula.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txNome = new JTextField(servidor.getNome());
			txNome.setForeground(Color.BLUE);
			txNome.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txCargo = new JTextField(servidor.getCargo());
			txCargo.setForeground(Color.BLUE);
			txCargo.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txCargaHoraria = new JTextField(Integer.toString(servidor.getCargaHoraria()));
			txCargaHoraria.setForeground(Color.BLUE);
			txCargaHoraria.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txObservacao = new JTextField();
			txObservacao.addKeyListener(this);
			txObservacao.setText(servidor.getEscala().getObservacao());
			txObservacao.setEditable(true);
			txObservacao.setColumns(25);
			txPassagemMesAnterior = new JTextField(Float.toString(servidor.getEscala().getHorasUltimaSemana()));
			txPassagemMesAnterior.setForeground(Color.BLUE);
			txPassagemMesAnterior.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbDiasSemana = new JLabel[numDiasResto + ultimoDiaMes];
			txEscalaContratual = new JTextField[numDiasResto + ultimoDiaMes];
			txEscalaExtra = new JTextField[numDiasResto + ultimoDiaMes];
			lbRotuloErro = new JLabel(" STATUS: ");
			lbErro = new JLabel();
			lbErro.getPreferredSize();
			lbRotuloMes = new JLabel(" MES: ");
			lbMes = new JLabel((servidor.getEscala().getMes()) + "");
			lbMes.setForeground(Color.BLUE);
			lbMes.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloAno = new JLabel(" ANO: ");
			lbAno = new JLabel(servidor.getEscala().getAno() + "");
			lbAno.setForeground(Color.BLUE);
			lbAno.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloQuantidadeRefeicoes= new JLabel(" REFEICOES D & N: ");
			lbQuantidadeRefeicoesDiurno = new JLabel(servidor.getEscala().getQuantidadeRefeicoesDiurno() + "");
			lbQuantidadeRefeicoesDiurno.setForeground(Color.BLUE);
			lbQuantidadeRefeicoesDiurno.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbQuantidadeRefeicoesNoturno = new JLabel(servidor.getEscala().getQuantidadeRefeicoesNoturno() + "");
			
			lbQuantidadeRefeicoesNoturno.setForeground(Color.BLUE);
			lbQuantidadeRefeicoesNoturno.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloTotalHorasExtras = new JLabel(" TOTAL DE HORAS EXTRAS: ");
			lbTotalHorasExtras = new JLabel(servidor.getEscala().getTotalHorasExtras() + "");
			
			lbTotalHorasExtras.setForeground(Color.BLUE);
			lbTotalHorasExtras.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			lbRotuloCompensacaoMesAnterior = new JLabel(" SALDO MES ANTERIOR: ");
			lbCompensacaoMesAnterior = new JLabel((servidorMesAnterior.getEscala().getBancoHorasDestaEscala())  + "");
			lbCompensacaoMesAnterior.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloCompensacao = new JLabel(" SALDO DESTE MES: ");
			lbCompensacao = new JLabel((servidorMesAnterior.getEscala().getBancoHorasDestaEscala() + servidor.getEscala().getBancoHorasDestaEscala())  + "");
			lbCompensacao.setForeground(Color.BLUE);
			lbCompensacao.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloPassagemProximoMes = new JLabel(" PASSAGEM PARA O PROXIMO MES: ");
			lbPassagemProximoMes = new JLabel(servidor.getEscala().getHorasUltimaSemana() + "");
			lbPassagemProximoMes.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			lbPassagemProximoMes.setForeground(Color.BLUE);
			txPassagemMesAnterior.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana1 = new JLabel(" SEMANA 1: ");
		
			lbSemana1 = new JLabel(servidor.getEscala().getHorasSemana1() + "");
			
			lbSemana1.setForeground(Color.BLUE);
			lbSemana1.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana2 = new JLabel(" SEMANA 2: ");
			
			lbSemana2 = new JLabel(servidor.getEscala().getHorasSemana2() + "");
			
			lbSemana2.setForeground(Color.BLUE);
			lbSemana2.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana3 = new JLabel(" SEMANA 3: ");
		
			lbSemana3 = new JLabel(servidor.getEscala().getHorasSemana3() + "");
			lbSemana3.setForeground(Color.BLUE);
			lbSemana3.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
		
			lbRotuloSemana4 = new JLabel(" SEMANA 4: ");
			lbSemana4 = new JLabel(servidor.getEscala().getHorasSemana4() + "");
			lbSemana4.setForeground(Color.BLUE);
			lbSemana4.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana5 = new JLabel(" SEMANA 5: ");
			
			lbSemana5= new JLabel(servidor.getEscala().getHorasSemana5() + "");
			lbSemana5.setForeground(Color.BLUE);
			lbSemana5.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana6 = new JLabel(" SEMANA 6: ");
			
			lbSemana6 = new JLabel(servidor.getEscala().getHorasSemana6() + "");
			lbSemana6.setForeground(Color.BLUE);
			lbSemana6.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			txMatricula.setEditable(false);
			txNome.setEditable(false);  
			txCargo.setEditable(false); 
			txCargaHoraria.setEditable(false); 
			txPassagemMesAnterior.setEditable(false);
			
			btValidar = new JButton("VALIDAR");
			btValidar.setFont(new Font( "Monospaced", Font.BOLD, 15));
			btValidar.addActionListener(this);
			btValidar.setEnabled(true);
			
			btLimpar = new JButton("LIMPAR");
			btLimpar.setFont(new Font( "Monospaced", Font.BOLD, 15));
			btLimpar.addActionListener(this);
			btLimpar.setEnabled(true);
			
			btQuantitativo = new JButton("QUANTITATIVO");
			btQuantitativo.setFont(new Font( "Monospaced", Font.BOLD, 15));
			btQuantitativo.addActionListener(this);
			btQuantitativo.setEnabled(true);
			
			int diaMesAnterior = diasSemanaResto.length-numDiasResto;
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				
				
				if (i < numDiasResto) {
					
					txEscalaContratual[i] = new JTextField();
					txEscalaContratual[i].setEditable(false);
					txEscalaContratual[i].addKeyListener(this);
					txEscalaContratual[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaContratual[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
					
					txEscalaExtra[i] = new JTextField(JTextField.CENTER);
					txEscalaExtra[i].setEditable(false);
					txEscalaExtra[i].addKeyListener(this);
					txEscalaExtra[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaExtra[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
					
					lbDiasSemana[i] = new JLabel((diaMesAnterior+1) + ": " +  diasSemanaResto[diaMesAnterior++], JLabel.CENTER);
					lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
					lbDiasSemana[i].setFont(new Font( "Monospaced", Font.BOLD, 15));
				
					if (servidor.getEscala() != null)
						txEscalaContratual[i].setText(servidor.getEscala().getEscalaHoraContratual()[diaMesAnterior-1]);
					
					if (servidor.getEscala() != null)
						txEscalaExtra[i].setText(servidor.getEscala().getEscalaHoraExtra()[diaMesAnterior-1]);
					
				}
				else {
					lbDiasSemana[i] = new JLabel(((i-numDiasResto)+1) + ": " +  AuxiliarConstantes.getDiaSemanaData((i-numDiasResto)+1, servidor.getEscala().getMes(), servidor.getEscala().getAno()), JLabel.CENTER);
	
					
					lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
					lbDiasSemana[i].setFont(new Font( "Monospaced", Font.BOLD, 15));
				
					txEscalaContratual[i] = new JTextField("");
					txEscalaContratual[i].addKeyListener(this);
					txEscalaContratual[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaContratual[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
				
					
					if (servidor.getEscala() != null)
						txEscalaContratual[i].setText(servidor.getEscala().getEscalaHoraContratual()[i-numDiasResto]);
					
			
					txEscalaContratual[i].setEditable(true);
					
					txEscalaExtra[i] = new JTextField("");
					txEscalaExtra[i].addKeyListener(this);
					txEscalaExtra[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaExtra[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
				
					
					if (servidor.getEscala() != null)
						txEscalaExtra[i].setText(servidor.getEscala().getEscalaHoraExtra()[i-numDiasResto]);
					
					txEscalaExtra[i].setEditable(true);
				}
			}
			
			diaMesAnterior = diasSemanaResto.length-numDiasResto;
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				
				lbDiasSemana[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
				
				if (i < numDiasResto) {
					if (diasSemanaResto[diaMesAnterior].trim().equals("Dom")) {

						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
	
				
					}
					if (diasSemanaResto[diaMesAnterior].trim().equals("Sab")) {
				
						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
						
					}
					
					diaMesAnterior+=1;
					pDadosEscala.add(lbDiasSemana[i]);
					
				}
				else {
					if (lbDiasSemana[i].getText().equals(((i-numDiasResto)+1) + ": " + "DOM")) {

						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
					
				
					}
					if (lbDiasSemana[i].getText().equals(((i-numDiasResto)+1) + ": " + "SAB")) {
				
						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
						;
					}
					
					pDadosEscala.add(lbDiasSemana[i]);
					
				}
			}
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				pDadosEscala.add(txEscalaContratual[i]);
			}
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				pDadosEscala.add(txEscalaExtra[i]);
			}
			
			pDadosEscala.setBorder(BorderFactory.createLineBorder (Color.black, 1));
			
			if (!novo)
				txObservacao.setText(servidor.getEscala().getObservacao());
			
			pInformacoes.add(lbRotuloErro);
			pInformacoes.add(lbErro);
			pInformacoes.add(lbRotuloCompensacaoMesAnterior);
			pInformacoes.add(lbCompensacaoMesAnterior);
			pInformacoes.add(lbCompensacao);
			pInformacoes.add(lbRotuloCompensacao);
			pInformacoes.add(lbCompensacao);
			pInformacoes.add(lbRotuloPassagemProximoMes);
			pInformacoes.add(lbPassagemProximoMes);
			pInformacoes.add(lbRotuloSemana1);
			pInformacoes.add(lbSemana1);
			pInformacoes.add(lbRotuloSemana2);
			pInformacoes.add(lbSemana2);
			pInformacoes.add(lbRotuloSemana3);
			pInformacoes.add(lbSemana3);
			pInformacoes.add(lbRotuloSemana4);
			pInformacoes.add(lbSemana4);
			pInformacoes.add(lbRotuloSemana5);
			pInformacoes.add(lbSemana5);
			pInformacoes.add(lbRotuloSemana6);
			pInformacoes.add(lbSemana6);
			pInformacoes.add(lbRotuloMes);
			pInformacoes.add(lbMes);
			pInformacoes.add(lbRotuloAno);
			pInformacoes.add(lbAno);
			pInformacoes.add(lbRotuloQuantidadeRefeicoes);
			pInformacoes.add(lbQuantidadeRefeicoesDiurno);
			pInformacoes.add(lbQuantidadeRefeicoesNoturno);
			pInformacoes.add(lbRotuloTotalHorasExtras);
			pInformacoes.add(lbTotalHorasExtras);
			pDadosPessoais.add(txMatricula);
			pDadosPessoais.add(txNome);
			pDadosPessoais.add(txCargo);
			pDadosPessoais.add(txCargaHoraria);
			pDadosPessoais.add(txPassagemMesAnterior);
			pDadosPessoais.add(txObservacao);
			
			pFuncionalidades.add(btQuantitativo);
			pFuncionalidades.add(btValidar);
			pFuncionalidades.add(btLimpar);
			pFuncionalidades.setBorder(BorderFactory.createLineBorder (Color.black, 1));
			
			add(pInformacoes, BorderLayout.NORTH);
			add(pDadosPessoais, BorderLayout.WEST);
			add(pDadosEscala, BorderLayout.CENTER);
			add(pFuncionalidades, BorderLayout.SOUTH);
			setBorder(BorderFactory.createLineBorder (Color.blue, 2));
		
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void montaLayout(boolean novo) {
		
		try {

			getRestoUtimaSemanaMesAnterior();
			int ultimoDiaMes = AuxiliarConstantes.getUltimoDiaMes(servidorMesAnterior.getEscala().getMes()+1, servidorMesAnterior.getEscala().getAno());
			
			JPanel pDadosPessoais = new JPanel(new GridLayout(6, 1));
			JPanel pInformacoes = new JPanel(new FlowLayout(FlowLayout.LEFT));	
			
			JPanel pDadosEscala = new JPanel(new GridLayout(3, numDiasResto + ultimoDiaMes));
			JPanel pFuncionalidades = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
			txMatricula = new JTextField(servidorMesAnterior.getMatricula());
			txMatricula.setForeground(Color.BLUE);
			txMatricula.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txNome = new JTextField(servidorMesAnterior.getNome());
			txNome.setForeground(Color.BLUE);
			txNome.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txCargo = new JTextField(servidorMesAnterior.getCargo());
			txCargo.setForeground(Color.BLUE);
			txCargo.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txCargaHoraria = new JTextField(Integer.toString(servidorMesAnterior.getCargaHoraria()));
			txCargaHoraria.setForeground(Color.BLUE);
			txCargaHoraria.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txObservacao = new JTextField();
			if (!novo)
				txObservacao.setText(servidor.getEscala().getObservacao());
			txObservacao.setEditable(true);
			txObservacao.setColumns(25);
			txObservacao.addKeyListener(this);
			txPassagemMesAnterior = new JTextField(Float.toString(servidorMesAnterior.getEscala().getHorasUltimaSemana()));
			txPassagemMesAnterior.setForeground(Color.BLUE);
			txPassagemMesAnterior.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));

			lbDiasSemana = new JLabel[numDiasResto + ultimoDiaMes];
			txEscalaContratual = new JTextField[numDiasResto + ultimoDiaMes];
			txEscalaExtra = new JTextField[numDiasResto + ultimoDiaMes];
			
			lbRotuloErro = new JLabel(" STATUS: ");
			lbErro = new JLabel();
			lbErro.getPreferredSize();
	
			
			lbRotuloMes = new JLabel(" MES: ");
			lbMes = new JLabel((servidorMesAnterior.getEscala().getMes()+1) + "");
			lbMes.setForeground(Color.BLUE);
			lbMes.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloAno = new JLabel(" ANO: ");
			lbAno = new JLabel(servidorMesAnterior.getEscala().getAno() + "");
			lbAno.setForeground(Color.BLUE);
			lbAno.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloQuantidadeRefeicoes= new JLabel(" REFEICOES D & N: ");
			
			if (novo)
				lbQuantidadeRefeicoesDiurno = new JLabel();
			else
				lbQuantidadeRefeicoesDiurno = new JLabel(servidor.getEscala().getQuantidadeRefeicoesDiurno() + "");
			
			lbQuantidadeRefeicoesDiurno.setForeground(Color.BLUE);
			lbQuantidadeRefeicoesDiurno.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			if (novo)
				lbQuantidadeRefeicoesNoturno = new JLabel();
			else
				lbQuantidadeRefeicoesNoturno = new JLabel(servidor.getEscala().getQuantidadeRefeicoesNoturno() + "");
			
			lbQuantidadeRefeicoesNoturno.setForeground(Color.BLUE);
			lbQuantidadeRefeicoesNoturno.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloTotalHorasExtras = new JLabel(" TOTAL DE HORAS EXTRAS: ");
			if (novo)
				lbTotalHorasExtras = new JLabel();
			else
				lbTotalHorasExtras = new JLabel(servidor.getEscala().getTotalHorasExtras() + "");
			
			lbTotalHorasExtras.setForeground(Color.BLUE);
			lbTotalHorasExtras.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			lbRotuloCompensacaoMesAnterior = new JLabel(" SALDO MES ANTERIOR: ");
			
			if (novo)
				lbCompensacaoMesAnterior = new JLabel();
			else
				lbCompensacaoMesAnterior = new JLabel((servidorMesAnterior.getEscala().getBancoHorasDestaEscala())  + "");
			lbCompensacaoMesAnterior.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			lbRotuloCompensacao = new JLabel(" SALDO DESTE MES: ");

			if (novo)
				lbCompensacao = new JLabel();
			else
				lbCompensacao = new JLabel((servidor.getEscala().getBancoHorasDestaEscala() + servidorMesAnterior.getEscala().getBancoHorasDestaEscala())  + "");
			
			lbCompensacao.setForeground(Color.BLUE);
			lbCompensacao.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloPassagemProximoMes = new JLabel(" PASSAGEM PARA O PROXIMO MES: ");
			if (novo)
				lbPassagemProximoMes = new JLabel();
			else
				lbPassagemProximoMes = new JLabel(servidor.getEscala().getHorasUltimaSemana() + "");
			
			lbPassagemProximoMes.setForeground(Color.BLUE);
			lbPassagemProximoMes.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			txPassagemMesAnterior.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana1 = new JLabel(" SEMANA 1: ");
			if (novo) 
				lbSemana1 = new JLabel();
			else
				lbSemana1 = new JLabel(servidor.getEscala().getHorasSemana1() + "");
			
			lbSemana1.setForeground(Color.BLUE);
			lbSemana1.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana2 = new JLabel(" SEMANA 2: ");
			if (novo) 
				lbSemana2 = new JLabel();
			else
				lbSemana2 = new JLabel(servidor.getEscala().getHorasSemana2() + "");
			
			lbSemana2.setForeground(Color.BLUE);
			lbSemana2.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana3 = new JLabel(" SEMANA 3: ");
			if (novo)
			lbSemana3 = new JLabel();
			else
				lbSemana3 = new JLabel(servidor.getEscala().getHorasSemana3() + "");
			lbSemana3.setForeground(Color.BLUE);
			lbSemana3.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana4 = new JLabel(" SEMANA 4: ");
			if (novo)
				lbSemana4 = new JLabel();
			else
				lbSemana4 = new JLabel(servidor.getEscala().getHorasSemana4() + "");
			lbSemana4.setForeground(Color.BLUE);
			lbSemana4.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana5 = new JLabel(" SEMANA 5: ");
			if (novo)
				lbSemana5 = new JLabel();
			else
				lbSemana5= new JLabel(servidor.getEscala().getHorasSemana5() + "");
			lbSemana5.setForeground(Color.BLUE);
			lbSemana5.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			lbRotuloSemana6 = new JLabel(" SEMANA 6: ");
			if (novo)
				lbSemana6 = new JLabel();
			else 
				lbSemana6 = new JLabel(servidor.getEscala().getHorasSemana6() + "");
			lbSemana6.setForeground(Color.BLUE);
			lbSemana6.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 14));
			
			txMatricula.setEditable(false);
			txNome.setEditable(false);  
			txCargo.setEditable(false); 
			txCargaHoraria.setEditable(false); 
			txPassagemMesAnterior.setEditable(false);

			
			btValidar = new JButton("VALIDAR");
			btValidar.setFont(new Font( "Monospaced", Font.BOLD, 15));
			btValidar.addActionListener(this);
			btValidar.setEnabled(true);
			
			btLimpar = new JButton("LIMPAR");
			btLimpar.setFont(new Font( "Monospaced", Font.BOLD, 15));
			btLimpar.addActionListener(this);
			btLimpar.setEnabled(true);
			
			btQuantitativo = new JButton("QUANTITATIVO");
			btQuantitativo.setFont(new Font( "Monospaced", Font.BOLD, 15));
			btQuantitativo.addActionListener(this);
			btQuantitativo.setEnabled(true);
			
			int diaMesAnterior = diasSemanaResto.length-numDiasResto;
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
		
				
				if (i < numDiasResto) {
					
					txEscalaContratual[i] = new JTextField();
					txEscalaContratual[i].setEditable(false);
					txEscalaContratual[i].addKeyListener(this);
					txEscalaContratual[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaContratual[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
					
					txEscalaExtra[i] = new JTextField(JTextField.CENTER);
					txEscalaExtra[i].setEditable(false);
					txEscalaExtra[i].addKeyListener(this);
					txEscalaExtra[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaExtra[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
					
					lbDiasSemana[i] = new JLabel((diaMesAnterior+1) + ": " +  diasSemanaResto[diaMesAnterior++], JLabel.CENTER);
					lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
					lbDiasSemana[i].setFont(new Font( "Monospaced", Font.BOLD, 15));
				
					if (servidorMesAnterior.getEscala() != null)
						txEscalaContratual[i].setText(servidorMesAnterior.getEscala().getEscalaHoraContratual()[diaMesAnterior-1]);
					
					if (servidorMesAnterior.getEscala() != null)
						txEscalaExtra[i].setText(servidorMesAnterior.getEscala().getEscalaHoraExtra()[diaMesAnterior-1]);
					
				}
				else {
					lbDiasSemana[i] = new JLabel(((i-numDiasResto)+1) + ": " +  AuxiliarConstantes.getDiaSemanaData((i-numDiasResto)+1, mes, servidorMesAnterior.getEscala().getAno()), JLabel.CENTER);
	
					
					lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
					lbDiasSemana[i].setFont(new Font( "Monospaced", Font.BOLD, 15));
				
					txEscalaContratual[i] = new JTextField("");
					txEscalaContratual[i].addKeyListener(this);
					txEscalaContratual[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaContratual[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
				
					if (!novo) {
						if (servidor.getEscala() != null)
							txEscalaContratual[i].setText(servidor.getEscala().getEscalaHoraContratual()[i-numDiasResto]);
					}
			
					txEscalaContratual[i].setEditable(true);
					
					txEscalaExtra[i] = new JTextField("");
					txEscalaExtra[i].addKeyListener(this);
					txEscalaExtra[i].setHorizontalAlignment(JTextField.CENTER);
					txEscalaExtra[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
				
					if (!novo) {
						if (servidor.getEscala() != null)
							txEscalaExtra[i].setText(servidor.getEscala().getEscalaHoraExtra()[i-numDiasResto]);
					}
					txEscalaExtra[i].setEditable(true);
				}
			}
			
			diaMesAnterior = diasSemanaResto.length-numDiasResto;
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				
				lbDiasSemana[i].setBorder(BorderFactory.createLineBorder (Color.black, 1));
				
				if (i < numDiasResto) {
					if (diasSemanaResto[diaMesAnterior].trim().equals("Dom")) {

						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
	
				
					}
					if (diasSemanaResto[diaMesAnterior].trim().equals("Sab")) {
				
						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
						
					}
					
					diaMesAnterior+=1;
					pDadosEscala.add(lbDiasSemana[i]);
					
				}
				else {
					if (lbDiasSemana[i].getText().equals(((i-numDiasResto)+1) + ": " + "DOM")) {

						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
					
				
					}
					if (lbDiasSemana[i].getText().equals(((i-numDiasResto)+1) + ": " + "SAB")) {
				
						lbDiasSemana[i].setOpaque(true);
						lbDiasSemana[i].setBackground(Color.BLUE);
						lbDiasSemana[i].setForeground(Color.WHITE);
						lbDiasSemana[i].setText(lbDiasSemana[i].getText().toUpperCase());
				
					}
					
					pDadosEscala.add(lbDiasSemana[i]);
					
				}
			}
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				pDadosEscala.add(txEscalaContratual[i]);
			}
			
			for (int i = 0; i < lbDiasSemana.length; ++i) {
				pDadosEscala.add(txEscalaExtra[i]);
			}
			
			pDadosEscala.setBorder(BorderFactory.createLineBorder (Color.black, 1));
			
			if (!novo)
				txObservacao.setText(servidor.getEscala().getObservacao());
		
			pInformacoes.add(lbRotuloErro);
			pInformacoes.add(lbErro);
			pInformacoes.add(lbRotuloCompensacaoMesAnterior);
			pInformacoes.add(lbCompensacaoMesAnterior);
			pInformacoes.add(lbRotuloCompensacao);
			pInformacoes.add(lbCompensacao);
			pInformacoes.add(lbRotuloPassagemProximoMes);
			pInformacoes.add(lbPassagemProximoMes);
			pInformacoes.add(lbRotuloSemana1);
			pInformacoes.add(lbSemana1);
			pInformacoes.add(lbRotuloSemana2);
			pInformacoes.add(lbSemana2);
			pInformacoes.add(lbRotuloSemana3);
			pInformacoes.add(lbSemana3);
			pInformacoes.add(lbRotuloSemana4);
			pInformacoes.add(lbSemana4);
			pInformacoes.add(lbRotuloSemana5);
			pInformacoes.add(lbSemana5);
			pInformacoes.add(lbRotuloSemana6);
			pInformacoes.add(lbSemana6);
			pInformacoes.add(lbRotuloMes);
			pInformacoes.add(lbMes);
			pInformacoes.add(lbRotuloAno);
			pInformacoes.add(lbAno);
			pInformacoes.add(lbRotuloQuantidadeRefeicoes);
			pInformacoes.add(lbQuantidadeRefeicoesDiurno);
			pInformacoes.add(lbQuantidadeRefeicoesNoturno);
			pInformacoes.add(lbRotuloTotalHorasExtras);
			pInformacoes.add(lbTotalHorasExtras);
		
			pDadosPessoais.add(txMatricula);
			pDadosPessoais.add(txNome);
			pDadosPessoais.add(txCargo);
			pDadosPessoais.add(txCargaHoraria);
			pDadosPessoais.add(txPassagemMesAnterior);
			pDadosPessoais.add(txObservacao);
			
			pFuncionalidades.add(btQuantitativo);
			pFuncionalidades.add(btValidar);
			pFuncionalidades.add(btLimpar);
			pFuncionalidades.setBorder(BorderFactory.createLineBorder (Color.black, 1));

			add(pInformacoes, BorderLayout.NORTH);
			add(pDadosPessoais, BorderLayout.WEST);
			add(pDadosEscala, BorderLayout.CENTER);
			add(pFuncionalidades, BorderLayout.SOUTH);
			setBorder(BorderFactory.createLineBorder (Color.blue, 2));
		
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void getDados() {
		
		servidor.setMatricula(txMatricula.getText());
		servidor.setCargaHoraria(Integer.parseInt(txCargaHoraria.getText()));
		servidor.setNome(txNome.getText());
		servidor.setCargo(txCargo.getText());getRestoUtimaSemanaMesAnterior();
		
		EntidadeEscala escala = new EntidadeEscala();
	
		escala.setHorasUltimaSemanaMesAnterior(Float.parseFloat(txPassagemMesAnterior.getText()));
		escala.setHorasUltimaSemana(Float.parseFloat(lbPassagemProximoMes.getText()));
		escala.setBancoHorasDestaEscala(Float.parseFloat(lbCompensacao.getText()));
		escala.setHorasSemana1(Float.parseFloat(lbSemana1.getText()));
		escala.setHorasSemana1(Float.parseFloat(lbSemana2.getText()));
		escala.setHorasSemana1(Float.parseFloat(lbSemana3.getText()));
		escala.setHorasSemana1(Float.parseFloat(lbSemana4.getText()));
		escala.setHorasSemana1(Float.parseFloat(lbSemana5.getText()));
		escala.setHorasSemana1(Float.parseFloat(lbSemana6.getText()));
		escala.setAno(Integer.parseInt(lbAno.getText()));
		escala.setMes(Integer.parseInt(lbMes.getText()));
		escala.setTotalHorasExtras(Integer.parseInt(lbTotalHorasExtras.getText()));
		escala.setQuantidadeRefeicoesDiurno(Integer.parseInt(lbQuantidadeRefeicoesDiurno.getText()));
		escala.setQuantidadeRefeicoesDiurno(Integer.parseInt(lbQuantidadeRefeicoesNoturno.getText()));
		escala.setObservacao(txObservacao.getText());
		
		int ultimoDiaMes = AuxiliarConstantes.getUltimoDiaMes(escala.getMes(), escala.getAno());
		String escalaHoraContratual[] = new String[ultimoDiaMes];
		String escalaHoraExtra[] = new String[ultimoDiaMes];
		
		for (int i = 0; i < ultimoDiaMes; ++i) {
			
			escalaHoraContratual[i] = txEscalaContratual[i].getText();
			escalaHoraExtra[i] = txEscalaContratual[i].getText();
			
		}
		
		escala.setEscalaHoraContratual(escalaHoraContratual);
		escala.setEscalaHoraExtra(escalaHoraExtra);
		servidor.setEscala(escala);
		
	}
	
	public EntidadeMensagem validar() {
		
		String escalaContratual[] = new String[AuxiliarConstantes.getUltimoDiaMes(servidor.getEscala().getMes(), servidor.getEscala().getAno())];
		String escalaExtra[] = new String[AuxiliarConstantes.getUltimoDiaMes(servidor.getEscala().getMes(), servidor.getEscala().getAno())];
		
		
		for (int i = numDiasResto; i < AuxiliarConstantes.getUltimoDiaMes(mes, ano)+numDiasResto; ++i) {
			
			escalaContratual[i-numDiasResto] =  AuxiliarGeral.substituiEspacos(txEscalaContratual[i].getText()).trim().toUpperCase();
			escalaExtra[i-numDiasResto] = AuxiliarGeral.substituiEspacos(txEscalaExtra[i].getText()).trim().toUpperCase();
			
		}
		
		servidor.getEscala().setEscalaHoraContratual(escalaContratual);
		servidor.getEscala().setEscalaHoraExtra(escalaExtra);
		servidor.getEscala().setObservacao(txObservacao.getText());
		
		mensagem = processConferidor.conferirEscala(servidor);
		getRestoUtimaSemanaMesAnterior();
		servidor.getEscala().setEscalaLancada(mensagem.isOk());
		removeAll();
		montaLayout(false);
	
		if (!mensagem.isOk()) {
					
			lbErro.setText("ERRO");
			lbErro.setForeground(Color.RED);
			lbErro.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 25));
			servidor.getEscala().setEscalaLancada(false);
			
					
		}
		else {
					
			lbErro.setText("OK");
			lbErro.setForeground(Color.BLUE);
			lbErro.setFont(new Font( "Monospaced", Font.BOLD + Font.ITALIC, 25));
			servidor.getEscala().setEscalaLancada(true);
			
		}
		
		if (!ApresentacaoEscalaServidores.getListaServidoresMap().containsKey(servidor.getMatricula())) {
			
			ApresentacaoEscalaServidores.getListaServidoresMap().put(servidor.getMatricula(), servidor);
			ApresentacaoEscalaServidores.getListaServidores().add(servidor);
			ApresentacaoEscalaServidores.getListaServidores().trimToSize();
			
		}
		
		contar();
		
		updateUI();
		
		return mensagem;
	}
	
	private void contar() {
		
		processConferidor = new ProcessConferidor();
		String escalaContratual[] = new String[AuxiliarConstantes.getUltimoDiaMes(servidor.getEscala().getMes(), servidor.getEscala().getAno())];
		String escalaExtra[] = new String[AuxiliarConstantes.getUltimoDiaMes(servidor.getEscala().getMes(), servidor.getEscala().getAno())];
		
		
		for (int i = numDiasResto; i < AuxiliarConstantes.getUltimoDiaMes(mes, ano)+numDiasResto; ++i) {
			
			escalaContratual[i-numDiasResto] =  AuxiliarGeral.substituiEspacos(txEscalaContratual[i].getText()).trim().toUpperCase();
			escalaExtra[i-numDiasResto] = AuxiliarGeral.substituiEspacos(txEscalaExtra[i].getText()).trim().toUpperCase();
			
		}
		
		servidor.getEscala().setEscalaHoraContratual(escalaContratual);
		servidor.getEscala().setEscalaHoraExtra(escalaExtra);
		servidor.getEscala().setObservacao(txObservacao.getText());
		
		mensagem = processConferidor.conferirEscala(servidor);
		getRestoUtimaSemanaMesAnterior();
		servidor.getEscala().setEscalaLancada(mensagem.isOk());
		
		if (!ApresentacaoEscalaServidores.getListaServidoresMap().containsKey(servidor.getMatricula())) {
			
			ApresentacaoEscalaServidores.getListaServidoresMap().put(servidor.getMatricula(), servidor);
			ApresentacaoEscalaServidores.getListaServidores().add(servidor);
			ApresentacaoEscalaServidores.getListaServidores().trimToSize();
			
		}
		
		int numServidoresManha = 0;
		int numServidoresTarde = 0;
		int numServidoresNoite = 0;
			
		ApresentacaoEscalaServidores.getListaServidores().trimToSize();
		for (int j = 0; j < AuxiliarConstantes.getUltimoDiaMes(mes, ano);++j) {
				
				numServidoresManha = 0;
				numServidoresTarde = 0;
				numServidoresNoite = 0;
				
			for (int k = 0; k < ApresentacaoEscalaServidores.getListaServidores().size(); k++) {
				
				EntidadeLegenda legendaContratual = AuxiliarConstantes.getLegenda(ApresentacaoEscalaServidores.getListaServidores().get(k).getEscala().getEscalaHoraContratual()[j].trim());
				EntidadeLegenda legendaExtra = AuxiliarConstantes.getLegenda(ApresentacaoEscalaServidores.getListaServidores().get(k).getEscala().getEscalaHoraExtra()[j].trim());
				
				if (legendaContratual.getValorManha() > 0) {
						numServidoresManha += 1;
				}
				
				if (legendaExtra.getValorManha() > 0) {
					numServidoresManha += 1;
				}
					
				if (legendaContratual.getValorTarde() > 0) {
					numServidoresTarde += 1;
				}
				
				if (legendaExtra.getValorTarde() > 0) {
					numServidoresTarde += 1;
				}
					
				if (legendaContratual.getValorNoite() > 0) {
					numServidoresNoite += 1;
				}
				
				if (legendaExtra.getValorNoite() > 0) {
					numServidoresNoite += 1;
				}

			}
				
			ApresentacaoEscalaServidores.getLbQuantitativo()[1][(j)+1].setText("0");
			ApresentacaoEscalaServidores.getLbQuantitativo()[1][(j)+1].updateUI();
			ApresentacaoEscalaServidores.getLbQuantitativo()[1][(j)+1].setText(""+numServidoresManha);
			ApresentacaoEscalaServidores.getLbQuantitativo()[1][(j)+1].updateUI();
				
			ApresentacaoEscalaServidores.getLbQuantitativo()[2][(j)+1].setText("0");
			ApresentacaoEscalaServidores.getLbQuantitativo()[2][(j)+1].updateUI();
			ApresentacaoEscalaServidores.getLbQuantitativo()[2][(j)+1].setText(""+numServidoresTarde);
			ApresentacaoEscalaServidores.getLbQuantitativo()[2][(j)+1].updateUI();
			
			ApresentacaoEscalaServidores.getLbQuantitativo()[3][(j)+1].setText("0");
			ApresentacaoEscalaServidores.getLbQuantitativo()[3][(j)+1].updateUI();
			ApresentacaoEscalaServidores.getLbQuantitativo()[3][(j)+1].setText("" + numServidoresNoite);
			ApresentacaoEscalaServidores.getLbQuantitativo()[3][(j)+1].updateUI();
				
				
		}

		
	}
	
	private void limpar() {
		
		for (int i = 0; i < AuxiliarConstantes.getUltimoDiaMes(mes, ano); ++i) {
			
			if (!txEscalaContratual[i].getText().equals("")) {
				txEscalaContratual[i].setText("");
			}
			
			if (!txEscalaExtra[i].getText().equals("")) {
				txEscalaExtra[i].setText("");
			}
		}
		
		txObservacao.setText("");
		validar();
		
	}

	public EntidadeMensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(EntidadeMensagem mensagem) {
		this.mensagem = mensagem;
	}
	
	public void getRestoUtimaSemanaMesAnterior() {
		
		int ultimoDiaMes = AuxiliarConstantes.getUltimoDiaMes(mes-1, ano);
		numDiasResto = 0;
		
		diasSemanaResto = new String[ultimoDiaMes];
		 
		for (int i = 0; i < ultimoDiaMes; i++) {
			diasSemanaResto[i] = new String(AuxiliarConstantes.getDiaSemanaData(i+1, mes-1, ano));
		} 
		
		aqui: for (int i = ultimoDiaMes - 1; i >= 0; --i) {
			
			if (diasSemanaResto[i].trim().equalsIgnoreCase("Sab")) {
				break aqui;
			}
			else
			if (diasSemanaResto[i].trim().equalsIgnoreCase("Dom")) {
				if (numDiasResto == 0)
					numDiasResto = 1;
				else
					numDiasResto += 1;
				break aqui;
			}
			else
				numDiasResto += 1;
			
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btValidar) {
			
			String mensagem = validar().getMensagem().replace('$', '\n');
			JOptionPane.showMessageDialog(null, mensagem, "Relatorio", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else
		if (e.getSource() == btLimpar) {
			
			limpar();
		}
		else
		if (e.getSource() == btQuantitativo) {
			
			contar();
		}
	}

	public void keyPressed(KeyEvent ke) {}	
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {
			
		lbErro.setText("NAO VALIDADO");
		lbErro.setForeground(Color.RED);
		lbErro.setFont(new Font( "Monospaced", Font.ITALIC + Font.BOLD, 25));
		servidor.getEscala().setEscalaLancada(false);
		lbErro.updateUI();
			
		
	}

}
