package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import to.DatabaseADV;
import to.DescricaoADV;

/**
 * @author Adonias Caetano de Oliveira
 * 
 * Classe pertencente a camada negócio, responsável pela leitura e escrita de descrições
 * Contém métodos estáticos de leitura, escrita e busca de descrições em aqruivo de extensão .adv
 */
public class LeitorADV {
	
	/**
	 * Realiza a escrita de descrições.
	 * @param descricoes : representa o conjunto total das descrições de um filme
	 * @param pathADV : representa o caminho do diretório do arquivo ADV.
	 * @throws IOException : aciona uma excessão em caso de erro na escrita do arquivo
	 */
	public static void salvar(ArrayList<DescricaoADV> descricoes, String pathADV) throws IOException{
		
		PrintWriter output = new PrintWriter(new FileWriter(pathADV));
		
		for(DescricaoADV adv : descricoes)
			output.println( adv.toString().trim() + "\n");
		
		
		output.close();
	}
	
	/**
	 * Identifica os parâmetros de cada audiodescrição: numero, tempo, velocidade, volume e descrição.
	 * @param numero : recebe o atributo numero para instanciar um objeto DescricaoADV
	 * @param input : representa o objeto de leitura do arquivo ADV posicionado a partir do atributo tempo
	 * @return um objeto DescricaoADV
	 * @throws IOException : aciona uma excessão em caso de erro na leitura do arquivo
	 * @throws NumberFormatException : aciona uma excessão em caso de erro de sintaxe do arquivo
	 * 
	 */
	private static DescricaoADV fillBean( String numero, BufferedReader input) throws IOException, NumberFormatException{
		DescricaoADV adv = new DescricaoADV();
		
		adv.setNumero( Integer.parseInt(numero) );
				
		StringTokenizer st = new StringTokenizer(input.readLine(), ": -->");
		
		int hora = Integer.valueOf( st.nextToken() ) * 3600000; //1 hora = 3.600.000 milisegundos  
		int minutos = Integer.valueOf( st.nextToken() ) * 60000; //1 minuto = 60.000 milisegundos   
		int segundos = (int) ( Double.valueOf( st.nextToken().replace(',', '.') ) * 1000 );//1 segundo = 1000 milisegundos
		
		adv.setTempoInicial( hora + minutos + segundos );
		
		hora = Integer.valueOf( st.nextToken() ) * 3600000; //1 hora = 3.600.000 milisegundos  
		minutos = Integer.valueOf( st.nextToken() ) * 60000; //1 minuto = 60.000 milisegundos   
		segundos = (int) ( Double.valueOf( st.nextToken().replace(',', '.') ) * 1000 );//1 segundo = 1000 milisegundos
		
		adv.setTempoFinal( hora + minutos + segundos );

		st = new StringTokenizer(input.readLine(), ":");	
		adv.setVelocidade( Integer.parseInt( st.nextToken() ) );
		adv.setVolume( Integer.parseInt( st.nextToken() ) );
		adv.setTomVoz( Integer.parseInt( st.nextToken() ) );
			
		String descricao;
		
		while(( descricao = input.readLine() )!= null && !descricao.equals("") ){
			adv.addDescricao( descricao );
		}
		
		return adv;
	}	
	
	/**
	 * Busca e carrega as descrições de uma arquivo ADV em um objeto DatabaseADV
	 * @param String path : o diretório que localiza o arquivo adv.
	 * @return : um objeto DatabaseADV contendo as descrições
	 * @throws IOException : aciona uma excessão em caso de erro na leitura do arquivo
	 * @throws NumberFormatException : aciona uma excessão em caso de erro de sintaxe do arquivo
	 * 
	 */
	public static DatabaseADV findAll(String path) throws IOException, NumberFormatException{
		
		File file = new File(path);
		DatabaseADV database = null;
		
		if( file.exists() ){
			BufferedReader input;
			database = new DatabaseADV();
			
			System.out.println("Lendo arquivo: " + file.getName() + "\n");
			
			if( file.getName().toLowerCase().endsWith(".adv") ){
					input = new BufferedReader(new FileReader(file));
					
					String numero;
									
					while(( numero = input.readLine() )!= null ){
					
						if( !numero.equals("") )
							database.add( fillBean( numero, input ) );
					}
					
					input.close();	
			}
		}
		return database;
	}
	
	public static void setConfiguracao(String[] configTTS) throws IOException{
		PrintWriter output = new PrintWriter(new FileWriter("tts.conf"));
		output.println( configTTS[0]);
		output.println( configTTS[1]);
		output.println( configTTS[2]);
		output.println( configTTS[3]);
		output.close();
	}
	
	public static String[] getSintetizador() throws IOException{
		
		String[] configTTS = new String[4];
		
		File file = new File("tts.conf");
		
		if( file.exists() ){
			BufferedReader input = new BufferedReader(new FileReader(file));
			configTTS[0] = input.readLine();
			configTTS[1] = input.readLine();
			configTTS[2] = input.readLine();
			configTTS[3] = input.readLine();
			input.close();
		}else{
			configTTS[0] = "eSpeak";
			configTTS[1] = "pt-br";
			configTTS[2] = "";
			configTTS[3] = "portugues";
			setConfiguracao(configTTS);
		}
		return configTTS;
	}
}

