package controller;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import model.LeitorADV;
import model.Multimidia;
import model.Speak;
import org.eclipse.swt.widgets.Shell;

import controller.I18N.Linguagem;
import de.humatic.dsj.DSJUtils;
import de.humatic.dsj.SwingMovieController;
import to.DatabaseADV;
import view.ADVeditor2;
import view.DialogoGeral;

/**
 * 
 * @author Adonias
 * Representa a camada de controle entre a camada de vis�o e de neg�cio.
 * 	- Nesta classe s�o controlados a leitura e manipula��o de v�deos e audiodescri��o
 * 	- controla a exibi��o de mensagens de informa��o, aten��o e erro.
 */ 
public class Controle implements java.beans.PropertyChangeListener{
	
	private Multimidia multimidia;
	private ADVeditor2 view;
	public static int CANAL_EM_USO = -1;
	private String[] configTTS;
	private I18N i18n;
	
	/**
	 * Construtor da classe ControleEdicao.
	 * @param view : representa a classe da camada de vis�o respons�vel em construir a tela principal
	 */
	public Controle(ADVeditor2 view){
		this.view = view;
		i18n = new I18N();
	}
	
	/*
	 * aciona a exibi��o uma mensagem de informa��o
	 */
	public void printInformacao(String mensagem, Shell shell){
		DialogoGeral.showMensagemInformacao(mensagem, shell);
	}
	
	/*
	 * aciona a exibi��o uma mensagem de aviso
	 */
	public void printAtencao(String mensagem, Shell shell){
		DialogoGeral.showMensagemAtencao(mensagem, shell);
	}
	
	/*
	 * aciona a exibi��o de uma mensagem de erro
	 */
	public void printErro(String mensagem, Shell shell){
		DialogoGeral.showMensagemErro(mensagem, shell);
	}
	
	//controla os eventos de pause e play acionados pela barra swing de v�deo.
	public void propertyChange(PropertyChangeEvent pe) {
				
		switch( DSJUtils.getEventValue_int(pe)) {
			case 0 : pauseADV();break;
			case 1 : playADV();break;
			case 14 : multimidia.resetNumeroADV();break;
		}
	}
	 
	//controla a leitura de um v�deo com seu respectivo arquivo ADV.
	//retorna true se o video foi aberto com sucesso.
	public boolean abrirMultimidia(Shell shell){ 
		String pathVideo = DialogoGeral.showFileDialog(shell);
		
		//se foi aberto algum video
		if( pathVideo != null){
			finalize(); //finaliza poss�veis v�deos em reprodu��o
	
			//determina o nome diret�rio onde est� o arquivo ADV
			File file = new File(pathVideo);
			StringTokenizer st = new StringTokenizer(file.getName(),".");
			String name = st.nextToken();
			String pathADV = file.getParent() + "/" + name + ".adv";
			
			try {
				//Carrega as descri��es do video.
				DatabaseADV database = LeitorADV.findAll( pathADV );
				CANAL_EM_USO = 0;
				
				//Se n�o foi o arquivo ADV do video
				if(database == null){
					multimidia = new Multimidia(pathVideo, pathADV, new DatabaseADV(), this);
					printAtencao(getText("notFindADV"), shell);
				}else{// se o arquivo ADV foi encontrado
					multimidia = new Multimidia(pathVideo, pathADV, database, this);
					
					if(database.size() == 0 )// se o arquivo estiver vazio.
						printAtencao(getText("emptyADV"), shell);
				}
				
				return true;
				
			} catch (NumberFormatException e) {//Erro de sintaxe nas descri��es
				printErro(getText("sintaxError"),shell);
				e.printStackTrace();
			} catch (IOException e) {//Erro de I/O na leitura do arquivo.
				printErro(e.getMessage() + "\n " + getText("contactAdmin"),shell);
				e.printStackTrace();
			}
		}
		return false;
	 }
	
	//retorna o conjunto de descri��es atrav�s do objeto multimidia.
	public DatabaseADV getDatabaseADV(){
		return multimidia.getDatabase();
	}
	
	//P�ra a reprodu��o de um v�deo juntamente com a audiodescri��o.
	 public void stop(){
		 if(multimidia != null)
			 multimidia.stop();
	 }
	 
	 //Pausa a reprodu��o de um v�deo juntamente com a audiodescri��o.
	 public void pause(){
		 if(multimidia != null)
			 multimidia.pause();
	 }
	 
	 //Executa o v�deo juntamente com a audiodescri��o.
	 public void play(){
		 if(multimidia != null)
			 multimidia.play();
	 }
	 
	//Executa o v�deo em um intervalo de tempo juntamente com a audiodescri��o.
	 public void playVideo(int tempoInicial){
		 if(multimidia != null)
			 multimidia.playVideo(tempoInicial);
	 }
	
	
	 //retorna o componente do video atrav�s do objeto multimidia.
	 public Component asComponent(){
	   	return multimidia.asComponent();
	 }
	 
	  // Pausa a execu��o da audiodescri��o
	  public void pauseADV(){
		  if(multimidia != null)
			  multimidia.pauseADV();
	  }
	    
	  // Executa a audiodescri��o
	  public void playADV(){
		  if(multimidia != null)
			  multimidia.playADV();
	  }
	  
	  // Atrav�s do objeto multimidia, verifica se a audiodescri��o est� pausada ou n�o.
	  // retorna true se estiver pausado.
	  public boolean isPausadoADV(){
		  return (multimidia != null ?  multimidia.isPausadoADV() : false);
	  }
		    
	  // Cria e retorna a barra swing de controle do v�deo.
	  public SwingMovieController getSwingMovieController(){
		    return ( new SwingMovieController(multimidia) );
	  }
		
	  //controla e ativa a atualiza��o do arquivo ADV
	  // retorna true caso seja atualizado com sucesso.
	  public boolean salvar(Shell shell, DatabaseADV database){
			
			if(database == null){
				DialogoGeral.showMensagemInformacao(getText("descriptionsEmpty"), shell);
				return false;
			}
			
			try {
				LeitorADV.salvar( database.getDescricoesADV(), multimidia.getPathADV() );
				return true;
			} catch (IOException e) {
				DialogoGeral.showMensagemErro( getText("errorUpdateADV") + "\n " + getText("contactAdmin"), shell);
				e.printStackTrace();
			}
			
			return false;
		}
		
	  // controla e aciona o teste do audio aplicado em uma descri��o.
	  // Executa a classe Speak para testa o �udio de uma descri��o em uma
	  // determinada velocidade e volume de �udio
		public synchronized void speak(String descricao, int velocidade, int audio, int tomVoz, Shell shell){
			
			if(velocidade < 0 || audio < 0){
				DialogoGeral.showMensagemInformacao(getText("valueSpeedAudioShould"), shell);
				return;
			}
			
			try {
				Speak speak = new Speak();
				speak.falar(descricao, velocidade, audio,tomVoz, configTTS[1] + "+" + configTTS[2]);
			} catch (IOException e) {
			 	e.printStackTrace();
				DialogoGeral.showMensagemErro( getText("errorRuneSpeak"), shell);
			}
		}
		
		//aciona o aumento do volume do v�deo
		  public void aumentarVolumeVideo(){
			  if(multimidia != null)
				  multimidia.aumentarVolumeVideo();
		  }
		  
		//aciona a diminui��o do volume da audiodescri��o
		public void diminuirVolumeVideo(){
		  if(multimidia != null)
			  multimidia.diminuirVolumeVideo();
		}
		  
		//avan�a uma posi��o no v�deo.
		public void avancarVideo(){
			 if(multimidia != null)
				 multimidia.avancarVideo();
		}
			 
		//retrocede uma posi��o de v�deo
		public void voltarVideo(){
			 if(multimidia != null)
				 multimidia.voltarVideo();
		}
			
		// Atrav�s do objeto multimidia, � retornado o milisegundos o tempo atual do video em execu��o
		public int getTempoEmMiliSegundos(){
			return (multimidia != null ?  multimidia.getTime() : 0);
		}
		
		// Atrav�s do objeto multimidia, retorna o tempo total do video em milisegundos
		public int getDuracao(){
			return (multimidia != null ?  multimidia.getDuration() : 0);
		}
		
		//Finaliza (fecha) a multimidia
		public void finalize(){
			if(multimidia != null)
				multimidia.finalize();
		}
		
		// Atrav�s do objeto multimidia, verifica se h� mais de um canal de �udio.
		// retorna true, se o video possui mais de um canal de �udio.
		public boolean isMultiCanais(){
			return (multimidia != null ?  multimidia.isMultiCanais() : false);
		}
		
		// Atrav�s do objeto multimidia, retorna a lista de canais de �udio detectados
		public String[] getNomeCanais(){
			return (multimidia != null ?  multimidia.getNomeCanais() : null);
		}
		
		// altera o canal de �udio a ser utilizado
		public void setCanalEmUso(int opcao){
			 if(multimidia != null){
				 
				 CANAL_EM_USO = opcao;
				 
				 multimidia.stop();
				 
				 multimidia = new Multimidia(
						 multimidia.getPathVideo(),
						 multimidia.getPathADV(),
						 multimidia.getDatabase(), 
						 this);
				 
				 multimidia.play();
				 view.reproduzir();
			 }
	    }
		
		public Hashtable<String, String> getIdiomas(Shell shell){
			try {
				return Speak.getIdiomas();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogoGeral.showMensagemErro(getText("findLanguage"), shell);
				return null;
			}
		}
		
		public  ArrayList<String> getVariantes (Shell shell){
			try {
				return Speak.getVariantes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogoGeral.showMensagemErro(getText("findVariants"), shell);
				return null;
			}
		}
		
		public void initConfigTTS(Shell shell){
			try {
				configTTS = LeitorADV.getSintetizador();
				
				if( configTTS[3].equals("ingles"))
					i18n.setLinguagem(Linguagem.INGLES);
				else if( configTTS[3].equals("espanhol") )
					i18n.setLinguagem(Linguagem.ESPANHOL);
				else
					i18n.setLinguagem(Linguagem.PORTUGUES);
			
			} catch (IOException e) {
				e.printStackTrace();
				DialogoGeral.showMensagemErro(getText("startSynthesizer"), shell);
			}
		}
		
		public boolean isIdiomaEscolhido(String idioma){
			return (idioma.equalsIgnoreCase(configTTS[1]));
		}
		
		public boolean isVarianteEscolhido(String variante){
			return (variante.equalsIgnoreCase(configTTS[2]));
		}
		
		public void setIdiomaTTS(String idioma){
			try {
				configTTS[1] = idioma;
				LeitorADV.setConfiguracao(configTTS);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public String getIdiomaComVarianteTTS(){
			if(configTTS[2] == null || configTTS[2].equals(""))
				return configTTS[1];
			else
				return configTTS[1] + "+" + configTTS[2];
		}
		
		public void setVarianteTTS(String variante){
			try {
				configTTS[2] = variante;
				LeitorADV.setConfiguracao(configTTS);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public Linguagem getLinguagem() {
			return i18n.getLinguagem();
		}

		public void setLinguagem(Linguagem linguagem) {
			try {
				i18n.setLinguagem(linguagem);
				view.setIdiomaSoftware();
				switch (linguagem) {
					case ESPANHOL : configTTS[3] = "espanhol"; break;
					case INGLES : configTTS[3] = "ingles"; break;
					default : configTTS[3] = "portugues"; break;
				}
				LeitorADV.setConfiguracao(configTTS);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public String getText(String chave){
			return i18n.getText(chave);
		}
		
		public int getNumeroAtual(){
			return multimidia.getNumeroAtual();
		}
}
