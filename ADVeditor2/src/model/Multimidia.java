package model;

import java.util.ArrayList;
import java.util.StringTokenizer;
import to.DatabaseADV;
import to.DescricaoADV;
import controller.Controle;
import de.humatic.dsj.DSFilter;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSMovie;

/**
 * 
 * @author adonias
 * Representa a entidade video junto com a audiodescrição.
 * Herda da classe DSMovie para manipulação de vídeos.
 * Contém os seguintes atributos:
 * 		- playerADV : manipula a audiodescrição.
 * 		- pathVideo : o diretório para localizar o video.
 * 		- pathADV : o diretório para localizar o arquivo adv.
 * 		- database : o conjunto descrições do video.
 * 		- canaisAudio : vetor de canais de áudio do próprio video.
 */
public class Multimidia extends DSMovie{

	public static final int CONSTANTE_TEMPO = 1000;
	private static final long serialVersionUID = 1L;
	
    private PlayerOfADV playerADV;
    private String pathVideo;
	private String pathADV;
	private DatabaseADV database;
	private ArrayList<DSFilter> canaisAudio;
	
	/**
	 * Construtor da classe Multimidia.
	 * @param pathVideo : o diretório para localizar o video.
	 * @param pathADV : o diretório para localizar o arquivo adv.
	 * @param database : o conjunto descrições do video.
	 * @param controle : objeto da classe controle, pois herda de java.beans.PropertyChangeListener
	 * 
	 */
    public Multimidia(String pathVideo, String pathADV, DatabaseADV database, Controle controle) {
    	super(pathVideo, DSFiltergraph.DD7, controle);
		this.pathVideo = pathVideo;
		this.pathADV = pathADV;
		this.database = database;
		
		detectaAudio(Controle.CANAL_EM_USO);

		playerADV = new PlayerOfADV(this, controle);

        if( database.size() > 0 )
        	playerADV.play();	
	}
   
    //identifica os canais de áudio do video e determina qual utilizar
    private void detectaAudio(int canalEmUso){
    	canaisAudio = new ArrayList<DSFilter>();
    	DSFilter[] filters = super.listFilters(); //lista todos os canais do filme: path, video, mp3, etc...
		
    	StringTokenizer st;
		for(int i = 0; i < filters.length; i++){
					
			st = new StringTokenizer(filters[i].getName(), " ");
			
			while(st.hasMoreTokens())//lista todos os filtros de valor MP3 (dublação)
				if(st.nextToken().equalsIgnoreCase("MP3")){
					canaisAudio.add(filters[i]);
					break;
				}
		}
		
		for(int i = 0; i < canaisAudio.size(); i++)
			if(i != canalEmUso)//desativa os canais de áudio que não serão utilizados
				super.tearDown(canaisAudio.get(i), false);		
		
    }
    
    /**
     * Verifica se o filme possui mais de um canal de áudio
     * Se é multi-idiomas.
     * @return true se possuir mais de um canal.
     */
    public boolean isMultiCanais(){
    	return canaisAudio.size() > 1;
    }
    
    /**
     * Retorna o nomes dos canais de áudio identificados.
     * @return um array de String
     */
    public String[] getNomeCanais(){
    	
    	if(canaisAudio.size() > 0){
    		
    		String[] nomes = new String[canaisAudio.size()];
    		
    		for(int i = 0; i < canaisAudio.size(); i++)
    			nomes[i] = canaisAudio.get(i).getName();
    			
    		return nomes;
    	}
    	
    	return null;
    }
    
    /**
     * Pára a reprodução de video e audiodescrição
     */
    public void stop() { 
    	playerADV.stop();
    	super.stop();
    	
    }
    
    public void resetNumeroADV(){
    	if(playerADV != null)
    		playerADV.resetNumeroADV();
    }  
    
    /**
     * Pausa a reprodução de video e audiodescrição
     */
    public void pause(){
    	playerADV.pausar();
    	super.pause();
    }
    
    /**
     * Inicia a reprodução de video e audiodescrição
     */
    public void play(){
    	playerADV.play();
    	super.play();
    }
  
    
    /**
     * altera a porcentagem de volume do video.
     * @param volume : porcentagem de volume.
     */
    public void setVolumeVideo(float volume){
		super.setVolume(volume);
	}
	
    /**
     * retorna a porcentagem de volume do video.
     * @param volume : porcentagem de volume.
     */
	public float getVolumeVideo(){
		return super.getVolume();
	}
	
	/**
     * Pausa a reprodução de video
     */
	public void pauseVideo(){
		super.pause();
	}
	
	/**
     * Inicia a reprodução de video
     */
	public void playVideo(){
		super.play();
	}
	
	/**
     * Reproduz video em determinado tempo
     */
	public void playVideo(int tempoInicial){
		super.setTimeValue(tempoInicial);
		super.play();
	}
    
	/**
     * Avança a reprodução de video
     */
    public synchronized void avancarVideo(){
    	int tempo = super.getTime();
    	super.setTimeValue(CONSTANTE_TEMPO + tempo);
    	resetNumeroADV();
    }
    
    /**
     * Retrocede a reprodução de video
     */
    public synchronized void voltarVideo(){
    	int tempo = super.getTime();
    	super.setTimeValue( tempo - CONSTANTE_TEMPO );
    	resetNumeroADV();
    }
    
    /**
     * Intensifica o som do video
     */
    public synchronized void aumentarVolumeVideo(){
    	float volume = super.getVolume() + 0.1f;
    		
    	if( volume >= 1)
    		volume = 1f;
    		
    	super.setVolume(volume);
    }
    
    /**
     * Diminui o som do video
     */
    public synchronized void diminuirVolumeVideo(){
    		
    	float volume = super.getVolume() - 0.1f;
    		
    	if( volume <= 0)
    		volume = 0;
    		
    	super.setVolume(volume);
    }
    
    /**
     * Pausa a reprodução da audiodescrição
     */
    public void pauseADV(){
    	playerADV.pausar();
    }
    
    /**
     * Inicia a reprodução da audiodescrição
     */
    public void playADV(){
    	playerADV.play();
    }
    
    /**
     * Verifica se a reprodução da audiodescrição está em pausa.
     * @return true se audiodescrição estiver pausado.
     */
    public boolean isPausadoADV(){
		return playerADV.isPausado();
	}

	
	/**
	 * Método de acesso do atributo pathVideo.
	 * Este atributo representa o diretório onde o vídeo está armazenado
	 */
	public String getPathVideo() {
		return pathVideo;
	}
	
	/**
	 * Métodos de modificação do atributo pathVideo.
	 * Este atributo representa o diretório onde o vídeo está armazenado
	 */
	public void setPathVideo(String pathVideo) {
		this.pathVideo = pathVideo;
	}

	/**
	 * Métodos de acesso do atributo pathADV.
	 * Este atributo representa o diretório onde o arquivo ADV está armazenado
	 */
	public String getPathADV() {
		return pathADV;
	}
	
	/**
	 * Métodos de modificação do atributo pathADV.
	 * Este atributo representa o diretório onde o arquivo ADV está armazenado
	 */
	public void setPathADV(String pathADV) {
		this.pathADV = pathADV;
	}
	
	/**
	 * Métodos de acesso do atributo database.
	 * Este atributo representa o conjunto de descrições lidas no arquivo ADV
	 */
	public DatabaseADV getDatabase() {
		return database;
	}
	
	/**
	 * Métodos de modificação do atributo database.
	 * Este atributo representa o conjunto de descrições lidas no arquivo ADV
	 */
	public void setDatabase(DatabaseADV database) {
		this.database = database;
	}

	/**
	 * busca uma descrição baseado no seu tempo de início e tempo final.
	 * @param tempo : em milisegundos
	 * @return a descrição encontrada.
	 */
	public DescricaoADV getDescricaoByTempo(int tempoInicial){
		return database.getDescricaoByTempo(tempoInicial);
	}
	
	/**
	 * @return a quantidade de descrições do video.
	 */
	public int sizeDescricao(){
		return database.size();
	}

	/**
	 * Finaliza a reprodução do video e da audiodescrição.
	 */
	public void finalize(){
		stop();
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public int getNumeroAtual(){
		return playerADV.getNumeroAtual();
	}
}  
