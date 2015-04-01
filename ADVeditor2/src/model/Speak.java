package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * 
 * @author Adonias
 * 
 * Esta classe realiza a execução do programa audiovirtual.
 * Recebe uma String e ativa o áudio para falar. 
 * Possui como atributos:
 * 	- SPEAK : diretório padrão para encontrar o programa audiovirtual no comando de linha
 *  - path : diretório no windows onde o diretório de SPEAK pode ser achado.
 *  - processo : mantém uma referência ao último processo executado.
 */
public class Speak {
	
	public static String path;
	private static Process processo;
	
	/**
	 * Construtor da classe
	 */
	public Speak(){
		findPath();
	}
	
	private static void findPath(){
		String[] paths = new String[]{
				"C:\\Arquivos de Programas\\eSpeak\\command_line",
				"C:\\Program Files\\eSpeak\\command_line",
				"C:\\Program Files (x86)\\eSpeak\\command_line",
				"C:\\Arquivos de Programas (x86)\\eSpeak\\command_line"
			};
		
		File file;
		for(int i = 0; i < paths.length; i++){
			file = new File(paths[i]);
			
			if( file.exists() ){
				path = paths[i] ;
				break;
			}
		}
		
		//System.out.println("eSpeak foi encontrado no diretório: \n" + path);
	}
	
	/**
	 * Recebe um texto a ser falado pelo programa em uma determinada velocidade e volume.
	 * @param descricao : texto a ser audiodescrito
	 * @param velocidade : velocidade com que as palavras são faladas
	 * @param volume : intensidade do som
	 * @param tomVoz : intensidade do do tom de voz
	 * @throws IOException : capatura de uma erro de I/O
	 */
	
	public synchronized void falar(String descricao, int velocidade, int volume, int tomVoz, String idioma) throws IOException{
		calar();
		//tratando enter e aspas duplas
		descricao = descricao.trim().replace('\n',' ').replace('"',' ');
		
		String cmd = path + "\\espeak " + "-s " + velocidade + " -a " + volume  + " -p " + tomVoz + " -v " + idioma + " \"" +  descricao + "\"";
		
		//mantém a referencia do processo
		processo = Runtime.getRuntime().exec(cmd); 
		
		//System.out.println(cmd.toString());
		
	}
	
	/**
	 * Interrompe o último processo em execução.
	 * Serve para cancelar a fala de um texto.
	 */
	public synchronized void calar(){
		if( processo != null)
			processo.destroy();
	}
	
	public static Hashtable<String, String> getIdiomas() throws IOException{
		Hashtable<String, String> idiomas = new Hashtable<String, String>();
		
		findPath();
			
		String[] cmd = { path + "\\espeak","--voices" };
		
		//mantém a referencia do processo
		Process processo = Runtime.getRuntime().exec(cmd); 
	
		InputStreamReader in = new InputStreamReader(processo.getInputStream());
		
		BufferedReader bis = new BufferedReader(in);
		
		String linha = bis.readLine();
		
		StringTokenizer st;
		
		while( (linha = bis.readLine()  ) != null ){
			
			st = new StringTokenizer(linha);			
			st.nextToken();	
			
			String chave = st.nextToken();
			
			st.nextToken();

			String idioma = st.nextToken();
			
			idiomas.put(chave, idioma);
			
		}
		
		return idiomas;
	}
	
	public static ArrayList<String> getVariantes() throws IOException{
		ArrayList<String> variantes = new ArrayList<String>();
		
		findPath();
			
		String[] cmd = { path + "\\espeak","--voices=variant" };
		
		//mantém a referencia do processo
		Process processo = Runtime.getRuntime().exec(cmd); 
	
		InputStreamReader in = new InputStreamReader(processo.getInputStream());
		
		BufferedReader bis = new BufferedReader(in);
		
		String linha = bis.readLine();
		
		StringTokenizer st;
		
		while( (linha = bis.readLine()  ) != null ){
			
			st = new StringTokenizer(linha);
			
			st.nextToken();
			st.nextToken();
			st.nextToken();
			
			variantes.add(st.nextToken());
		
		}
		
		return variantes;
	}
}
