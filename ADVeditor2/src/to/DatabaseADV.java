package to;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Adonias Caetano de Oliveira
 * 
 * Classe de camada TO (entidades) que mantém todas as descrições de um video
 * As descrições são armazendas na estrutura HasMap.
 *
 */
public class DatabaseADV {
	
	private HashMap<Integer, DescricaoADV> descricoes;
	private Comparator<DescricaoADV> comparator;
	
	/**
	 * Construtor da classe
	 */
	public DatabaseADV(){
		descricoes = new HashMap<Integer, DescricaoADV>();
		
		comparator = new Comparator<DescricaoADV>() {
			public int compare(DescricaoADV adv1, DescricaoADV adv2) {
				
				if( adv1.getTempoInicial() < adv2.getTempoInicial() )
					return -1;
				else 
					return 1;
			}	
		};
	}
	
	/**
	 * @return a quantidade de descrições do video
	 */
	public int size(){
		return descricoes.size();
	}
	
	/**
	 * Adiciona uma descrição ao video
	 * @param adv : representa a descrição do video. 
	 */
	public void add(DescricaoADV adv){
		descricoes.put(adv.getTempoInicial(), adv );
	}
	
	/**
	 * Remove uma descrição sobre o video baseado no tempo
	 * @param tempo : valor do tempo em milisegundos
	 * @return a descrição removida.
	 */
	public DescricaoADV remove(int tempo){
		return descricoes.remove(tempo);
	}
	
	/**
	 * Remove a descrição sobre video.
	 * @param descricao : representa a descrição que será removida.
	 * @return a descrição removida
	 */
	public DescricaoADV remove(DescricaoADV descricao){
		return descricoes.remove(descricao);
	}

	/**
	 * Obtém a descrição baseado no tempo em milisegundos
	 * @param tempo : valor de tempo em milisegundos
	 * @return a descrição encontrada
	 */
	public DescricaoADV getDescricaoByTempo( int tempo ){	
		return descricoes.get( tempo );
	}
	
	/**
	 * Obtém a descrição de acordo com o índice.
	 * @param indice : representa a posição da descrição no array de ordenadas pelo tempo.
	 * Varia [0, N - 1] onde N é o tamanho.
	 * @return a descrição encontrada.
	 */
	public DescricaoADV getDescricaoByIndex(int indice){
		ArrayList<DescricaoADV> lista = new ArrayList<DescricaoADV>(descricoes.values());
		Collections.sort(lista, comparator);
		
		return lista.get( indice );
	}
	
	/**
	 * Obtém uma lista de descrições do video.
	 * @return um ArrayList de todas as descrições do video ordenadas pelo tempo.
	 */
	public ArrayList<DescricaoADV> getDescricoesADV(){
		
		ArrayList<DescricaoADV> lista = new ArrayList<DescricaoADV>(descricoes.values());
		Collections.sort(lista, comparator);
		
		DescricaoADV descricao;
		int i = 0;
		
		while( i < lista.size() ){
			descricao = lista.get(i);
			
			if( descricao.getDescricao() != null && !descricao.getDescricao().isEmpty() ){
				descricao.setNumero(i + 1);
				i++;
			}else
				remove(descricao);		
		}
		
		return  lista;
	}
}
