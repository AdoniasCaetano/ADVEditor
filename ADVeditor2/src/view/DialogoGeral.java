package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Adonias
 * Esta classe contém métodos estáticos para criação de diálogos comuns.
 * Os tipos de dialogos são para:
 * 	- aviso;
 *  - informação;
 *  - erro;
 *  - confirmação;
 *  - localização de video e arquivo ADV.
 */
public class DialogoGeral {
	
	/**
	 * Exibe uma informação ao usuário.
	 * Geralmente utilizado para informar o sucesso de um evento.
	 */
	public static void showMensagemInformacao(String mensagem, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setMessage(mensagem);
		messageBox.open();
	}
	
	/**
	 * Exibe a descriçao de erro ou falha do sistema.
	 */
	public static void showMensagemErro(String mensagem, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		messageBox.setMessage(mensagem);
		messageBox.open();
	}
	
	/**
	 * Exibe uma mensagem de aviso.
	 * Serve, por exemplo, para avisar que o arquivo ADV está vazio.
	 */
	public static void showMensagemAtencao(String mensagem, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
		messageBox.setMessage(mensagem);
		messageBox.open();
	}
	
	/**
	 * Exibe uma ação que poderá ser tomada de acordo com a decisão do usuário.
	 * Serve confirmar a execução de determinadas operações.
	 * Retorna true caso a resposta seja SIM (YES)
	 * ou false caso contrário.
	 */
	public static boolean showQuestion(String questao, Shell shell){
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION |SWT.YES | SWT.NO);
		messageBox.setMessage(questao);
		messageBox.setText("Diálogo de Confirmação");
		int rc = messageBox.open();
		
		return (rc == SWT.YES);
	}
	
	/**
	 * Permite que o usuário aponte o diretório e arquivo ADV para abertura.
	 * Retorna o caminho do diretório onde o arquivo adv se encontra.
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
	 * Permite que o usuário aponte o diretório e video para abertura.
	 * Retorna o caminho do diretório onde o video se encontra.
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
