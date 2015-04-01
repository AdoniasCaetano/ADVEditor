package model;

import java.io.IOException;
import org.eclipse.swt.widgets.Shell;
import controller.Controle;
import de.humatic.dsj.DSJException;
import to.DescricaoADV;

/**
 * @author Adonias Caetano de Oliveira
 * 
 * Esta classe reproduz a audiodescri��o do video
 * As descri��es s�o reproduzidas de acordo com o tempo atual do v�deo.
 * Os atributos s�o:
 * 	- runner : representa a thread de forma a permitir multiexecu��es.
 * 	- executar : atributo booleano indicando se a audiodescri��o est� executando
 * 	- pausado : atributo booleano indicando se a audiodescri��o est� em pause.
 *  - speak : atributo respons�vel em executar o programa audiovirtual
 *  - controle : trata os erros capturados
 *  - numero : mant�m o n�mero da �ltima descri��o reproduzida
 *  - multimidia : acessa �s informa��es de tempo atual e descri��es
 */
public class PlayerOfADV implements Runnable{
	private Thread runner;
	private boolean executar;
	private boolean pausado;
	private Speak speak;
	private Controle controle;
	private DescricaoADV advAntigo;
	private DescricaoADV advAtual;
	private Multimidia multimidia;
	private int DELTA_VOLUME;
	private int DELTA_VELOCIDADE;
	public static final  int[] INTERVALO_VELOCIDADE = {50, 300};
	public static final  int[] INTERVALO_VOLUME = {20, 140};

	/**
	 * Construtor da classe
	 */
	public PlayerOfADV( Multimidia multimidia, Controle controle ){
		this.controle = controle;
		this.multimidia = multimidia;
	}
	
	/**
	 * Executa a audiodescri��o de acordo
	 * com o tempo atual do v�deo
	 */
	public void run(){
		executar = true;
		
		advAtual = null;
		speak = new Speak();
		int tempo = 0;
		System.out.println("Inicializando o player audiodescri��o...");
		
		while(executar){//mant�m em execu��o a audiodescri��o
			try{			
					tempo = multimidia.getTime();//captura o tempo atual do filme em milisegundos
					
					//busca uma descri��o com o tempo atual do filme
					advAtual = multimidia.getDescricaoByTempo( tempo ); 
				
					if(advAtual == null){//Se n�o encontrar uma descri��o com valor exato do tempo
						for(int i = 1; i <= 500; i++){//busca descri��es anteriores de tempo at� 0,5 ms
							advAtual = multimidia.getDescricaoByTempo(tempo -  i);
							
							if( advAtual != null)
								break;
						}
					}
										
					// se a �ltima descri��o foi reproduzida a 1min anterior
					if( advAntigo != null && multimidia.getTime() - advAntigo.getTempoInicial() > 60000)
						advAntigo = null;
				
					//Se houver descri��o e ela n�o foi reproduzida anteriomente
					if( !pausado && advAtual != null && ( advAntigo == null || advAtual.getNumero() != advAntigo.getNumero() ) ){
						advAntigo = advAtual;
						speak.falar( advAtual.getDescricao(),advAtual.getVelocidade() + DELTA_VELOCIDADE, advAtual.getVolume() + DELTA_VOLUME, advAtual.getTomVoz(), controle.getIdiomaComVarianteTTS());
					}
				
				Thread.sleep(1);//dorme 1ms
			}catch(DSJException ed){
				
				if(ed.getErrorCode() == -2 && ed.getMessage().equals("Graph not active") )
					System.out.println("Frame inativo");
				
			}catch( InterruptedException ie ){
				controle.printErro("Interrup��o inesperada!\nContate o administrador!", new Shell());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				controle.printErro("FATAL ERRO DE I/O!\nContate o administrador!", new Shell());
			}
		}

		System.out.println("Finalizando o player audiodescri��o...");
		runner = null;
	}
	
	/**
	 * Inicia a reprodu��o da audiodescri��o
	 */
	public void play(){
		if (runner == null){
			runner = new Thread(this);
			runner.start();
			System.out.println("Start player audiodescri��o...");
		}else{
			pausado = false;
		}
	}
	
	public void resetNumeroADV(){
		advAntigo = null;
	}
	
	
	/**
	 * P�ra a reprodu��o da audiodescri��o
	 */
	public  void stop() {
		speak.calar();
		executar = false;
	}
	
	public void pausar(){
		speak.calar();
		pausado = true;
	}
	
	/**
	 * verifica se audiodescri��o foi pausado
	 * @return true se estiver em pausa.
	 */
	public boolean isPausado(){
		return pausado;
	}
	
	public void aumentarVolumeDescricao(){
		DELTA_VOLUME += 20;
	}
	
	public void diminuirVolumeDescricao(){
		DELTA_VOLUME -= 20;
	}
	
	public void aumentarVelocidadeDescricao(){
		DELTA_VELOCIDADE += 50;
	}
	
	public void diminuirVelocidadeDescricao(){
		DELTA_VELOCIDADE -= 50;
	}

	public int getNumeroAtual() {
		
		if(advAtual == null)return -1;
		
		return advAtual.getNumero();
	}
	
	
}
