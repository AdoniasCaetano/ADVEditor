package to;

/**
* 
* @author Adonias Caetano de Oliveira
* 
* Representa a descri��o da cena de um determinado v�deo em execu��o.
* Os atributos s�o:
*	- numero : o n�mero de identifica��o da descri��o.
* 	- tempoInicial : o tempo em que a descri��o � iniciada.
*	- descricao : um texto descrevendo a cena.
*	- velocidade : representa a velocidade em que a descri��o deve ser falada.
*	- volume : a intensidade do �udio.
*
*/
public class DescricaoADV {
	
	private int numero;
	private int tempoInicial;
	private int tempoFinal;
	private String descricao;
	private int velocidade;
	private int volume;
	private int tomVoz;
	
	public DescricaoADV() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return o n�mero de identifica��o da descri��o.
	 */
	public int getNumero() {
		return numero;
	}
	
	/**
	 * Altera o n�mero de identifica��o da descri��o.
	 * @param numero
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	/**
	 * Obt�m o tempo inicial que a descri��o deve ser falada.
	 * @return o tempo incial em milisegundos
	 */
	public int getTempoInicial() {
		return tempoInicial;
	}
	
	/**
	 * Altera o tempo inicial da descri��o
	 * @param tempoInicial : o valor do tempo em milisegundos
	 */
	public void setTempoInicial(int tempoInicial) {
		this.tempoInicial = tempoInicial;
	}
	
	/**
	 * Obt�m o tempo final que a descri��o deve ser falada.
	 * @return o tempo final em milisegundos
	 */
	public int getTempoFinal() {
		return tempoFinal;
	}
	
	/**
	 * Altera o tempo final da descri��o
	 * @param tempoFinal : o valor do tempo em milisegundos
	 */
	public void setTempoFinal(int tempoFinal) {
		this.tempoFinal = tempoFinal;
	}
	
	/**
	 * @return o texto que descreve a cena.
	 */
	public String getDescricao() {	
		return descricao;
	}
	
	/**
	 * Altera o texto da descri��o
	 * @param descricao
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * Adiciona mais descri��es.
	 * @param descricao
	 */
	public void addDescricao(String descricao){
		this.descricao = (this.descricao == null ? descricao : this.descricao + "\n" + descricao);
	}

	/**
	 * Obt�m o tempo inicial e final da descri��o em forma de Hora : minutos : segundos
	 * @return o tempo formatado 
	 */
	public String[] getTempoFormatado(){
		return new String[]{getTempoFormatado(tempoInicial), getTempoFormatado(tempoFinal)};
	}
	
	private String getTempoFormatado(int tempo){
		int hora = tempo/3600000;
		int minutos = (tempo - hora*3600000)/60000;
		int segundos = (tempo - hora*3600000 - minutos*60000)/1000;
		int milisegundos = tempo - hora*3600000 - minutos*60000 - segundos*1000;
		
		return (hora > 9 ? hora : "0" + hora) + ":" +
			   (minutos > 9 ? minutos : "0" + minutos) + ":" +
			   (segundos > 9 ? segundos : "0" + segundos) + "," +
			   (milisegundos > 99 ? milisegundos :  (milisegundos > 9 ? "0" + milisegundos : "00" + milisegundos));
	}
	
	/**
	 * @return a hora do tempo inicial
	 */
	public int getHoraInicial(){
		return (int)tempoInicial/3600000;
	}
	
	/**
	 * @return os minutos do tempo inicial.
	 */
	public int getMinutoInicial(){
		int hora = getHoraInicial();
		return (int)(tempoInicial - hora*3600000)/60000;
	}
	
	/**
	 * @return os segundos do tempo inicial
	 */
	public int getSegundosInicial(){
		int hora = getHoraInicial();
		int minutos = getMinutoInicial();
		return (int)(tempoInicial - hora*3600000 - minutos*60000)/1000;
	}
	
	/**
	 * @return os milisegundos do tempo inicial
	 */
	public int getMiliSegundosIncial(){
		int hora = getHoraInicial();
		int minutos = getMinutoInicial();
		int segundos = getSegundosInicial();
		
		return (int)tempoInicial - hora*3600000 - minutos*60000 - segundos*1000;
	}	
	
	/**
	 * @return a hora do tempo final
	 */
	public int getHoraFinal(){
		return (int)tempoFinal/3600000;
	}
	
	/**
	 * @return os minutos do tempo final.
	 */
	public int getMinutoFinal(){
		int hora = getHoraFinal();
		return (int)(tempoFinal - hora*3600000)/60000;
	}
	
	/**
	 * @return os segundos do tempo final
	 */
	public int getSegundosFinal(){
		int hora = getHoraFinal();
		int minutos = getMinutoFinal();
		return (int)(tempoFinal - hora*3600000 - minutos*60000)/1000;
	}
	
	/**
	 * @return os milisegundos do tempo final
	 */
	public int getMiliSegundosFinal(){
		int hora = getHoraFinal();
		int minutos = getMinutoFinal();
		int segundos = getSegundosFinal();
		
		return (int)tempoFinal - hora*3600000 - minutos*60000 - segundos*1000;
	}
	
	/**
	 * @return a velocidade da audiodescri��o
	 */
	public int getVelocidade() {
		return velocidade;
	}
	
	
	/**
	 * Altera a valor da velocidade
	 * @param velocidade
	 */
	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}
	
	/**
	 * @return a intensidade da audiodescri��o
	 */
	public int getVolume() {
		return volume;
	}
	
	/**
	 * Altera o valor de volume
	 * @param volume
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/**
	 * @return o valor de tom de voz
	 * 0 - grave
	 * 99 - agudo
	 */
	public int getTomVoz(){
		return tomVoz;
	}
	
	/**
	 * Altera o valor de tom de voz
	 * @param tomVoz
	 */
	public void setTomVoz(int tomVoz) {
		this.tomVoz = tomVoz;
	}
	
	/**
	 * Retorna o valor de cada atributo da classe em formato String
	 */
	public String toString(){
		String[] tempo = getTempoFormatado();
		
		return numero + "\n" +
			   tempo[0] + " --> " + tempo[1] + "\n" +
			   velocidade + ":" + volume + ":" + tomVoz + "\n" +
			   descricao + "\n";
	}	
	
	/**
	 * Verifica se a descri��o � nula (null) ou do tipo vazio ("")
	 * @return true se for nula ou vazia.
	 */
	public boolean isDescricaoNula(){
		return ( descricao == null || descricao.isEmpty() );
	}
}
