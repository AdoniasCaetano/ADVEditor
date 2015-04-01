package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Adonias
 * Esta classe cont�m m�todos est�ticos para cria��o de di�logos comuns.
 * Os tipos de dialogos s�o para:
 * 	- aviso;
 *  - informa��o;
 *  - erro;
 *  - confirma��o;
 *  - localiza��o de video e arquivo ADV.
 */
public class DialogoGeral {
	
	/**
	 * Exibe uma informa��o ao usu�rio.
	 * Geralmente utilizado para informar o sucesso de um evento.
	 */
	public static void showMensagemInformacao(String mensagem, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setMessage(mensagem);
		messageBox.open();
	}
	
	/**
	 * Exibe a descri�ao de erro ou falha do sistema.
	 */
	public static void showMensagemErro(String mensagem, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		messageBox.setMessage(mensagem);
		messageBox.open();
	}
	
	/**
	 * Exibe uma mensagem de aviso.
	 * Serve, por exemplo, para avisar que o arquivo ADV est� vazio.
	 */
	public static void showMensagemAtencao(String mensagem, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
		messageBox.setMessage(mensagem);
		messageBox.open();
	}
	
	/**
	 * Exibe uma a��o que poder� ser tomada de acordo com a decis�o do usu�rio.
	 * Serve confirmar a execu��o de determinadas opera��es.
	 * Retorna true caso a resposta seja SIM (YES)
	 * ou false caso contr�rio.
	 */
	public static boolean showQuestion(String questao, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION |SWT.YES | SWT.NO);
		messageBox.setMessage(questao);
		messageBox.setText("Di�logo de Confirma��o");
		int rc = messageBox.open();
		
		return (rc == SWT.YES);
	}
	
	/**
	 * Permite que o usu�rio aponte o diret�rio e arquivo ADV para abertura.
	 * Retorna o caminho do diret�rio onde o arquivo adv se encontra.
	 */
	public static String showOpenFileDialog(Shell shell){
		String[] filterExtensions = {"*.adv"};
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setText("Abrir arquivo adv");
		fileDialog.setFilterPath("C:/");
		fileDialog.setFilterExtensions(filterExtensions);
		
		return fileDialog.open();
	}
	
	/**
	 * Permite que o usu�rio aponte o diret�rio e video para abertura.
	 * Retorna o caminho do diret�rio onde o video se encontra.
	 */
	public static String showFileDialog(Shell shell){
		
		String[] filterExtensions = {"*.avi","*.mpg","*.midi","*.rmf","*.wav","*.aiff"};
		
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setText("");
		fileDialog.setFilterPath("Abrir Media");					
		fileDialog.setFilterExtensions(filterExtensions);
		return fileDialog.open();
	}
}
