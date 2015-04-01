package to;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Adonias Caetano de Oliveira
 * 
 * Classe de camada TO (entidades) que mant�m todas as descri��es de um video
 * As descri��es s�o armazendas na estrutura HasMap.
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
	 * @return a quantidade de descri��es do video
	 */
	public int size(){
		return descricoes.size();
	}
	
	/**
	 * Adiciona uma descri��o ao video
	 * @param adv : representa a descri��o do video. 
	 */
	public void add(DescricaoADV adv){
		descricoes.put(adv.getTempoInicial(), adv );
	}
	
	/**
	 * Remove uma descri��o sobre o video baseado no tempo
	 * @param tempo : valor do tempo em milisegundos
	 * @return a descri��o removida.
	 */
	public DescricaoADV remove(int tempo){
		return descricoes.remove(tempo);
	}
	
	/**
	 * Remove a descri��o sobre video.
	 * @param descricao : representa a descri��o que ser� removida.
	 * @return a descri��o removida
	 */
	public DescricaoADV remove(DescricaoADV descricao){
		return descricoes.remove(descricao);
	}

	/**
	 * Obt�m a descri��o baseado no tempo em milisegundos
	 * @param tempo : valor de tempo em milisegundos
	 * @return a descri��o encontrada
	 */
	public DescricaoADV getDescricaoByTempo( int tempo ){	
		return descricoes.get( tempo );
	}
	
	/**
	 * Obt�m a descri��o de acordo com o �ndice.
	 * @param indice : representa a posi��o da descri��o no array de ordenadas pelo tempo.
	 * Varia [0, N - 1] onde N � o tamanho.
	 * @return a descri��o encontrada.
	 */
	public DescricaoADV getDescricaoByIndex(int indice){
		ArrayList<DescricaoADV> lista = new ArrayList<DescricaoADV>(descricoes.values());
		Collections.sort(lista, comparator);
		
		return lista.get( indice );
	}
	
	/**
	 * Obt�m uma lista de descri��es do video.
	 * @return um ArrayList de todas as descri��es do video ordenadas pelo tempo.
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
