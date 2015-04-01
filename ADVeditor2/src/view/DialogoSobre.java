package view;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

/**
 * 
 * @author Adonias Caetano de Oliveira
 * 
 * Implementa a interface gr�fica do dialogo que exibe informa��es sobre o sistema.
 * Possui as seguintes funcionalidades:
 * 	- Apresenta alguns dados sobre o software e o Projeto Acessibilidade Virtual;
 *  - Implementa os componentes do formul�rio de sugest�es ou cr�ticas ao projeto.
 *
 */
public class DialogoSobre extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private Button botao;
	private boolean showContato;
	
	private CLabel rotuloIcone;
	private CLabel rotuloVersao;

	/**
	 * Construtor da classe.
	 */
	public DialogoSobre(Shell parent) {
		super(parent, SWT.DIALOG_TRIM);
		setText("Sobre " + EnumVersao.SOFTWARE.toString());
	}
	
	/**
	 * M�todo est�tico para instancia��o de um objeto desta classe.
	 * Serve acionar a exibi��o da interface gr�fica.
	 * @param parent
	 * @param controle
	 */
	public static void showDialogo(Shell parent){
		DialogoSobre dialogo  = new DialogoSobre(parent);
		dialogo.open();
	}

	/**
	 * Aciona a exibi��o do dialogo.
	 */
	private Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Constroe os componentes do dialogo
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(644, 330);
		shell.setText(getText());
		
		rotuloIcone = new CLabel(shell, SWT.NONE | SWT.CENTER);
		rotuloIcone.setBounds(10, 10, 152, 163);
		rotuloIcone.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/audiodescricao.png") ));
		
		rotuloVersao = new CLabel(shell, SWT.NONE | SWT.CENTER);
		rotuloVersao.setBounds(143, 10, 485, 239);
		rotuloVersao.setText(
				"Instituto Federal do Cear� - IFCE \n"
				+ "Projeto Acessibilidade Virtual \n\n" 
				+ "N� de Pedido de Registro de Software: BR 51 2014000931 9\n\n"				
				+ "Equipe de desenvolvimento:\n"
				+ " - Adonias Caetano de Oliveira\n"
				+ " - Agebson Rocha Fa�anha \n"
				+ " - Marcos Vin�cius de Andrade Lima \n"
				+ " - Phyllipe do Carmo F�lix \n \n"
				
				+ "Software " + EnumVersao.NOME.toString() + " - Versao: " + EnumVersao.NUMERO_VERSAO.toString() + "\n"
				+ "Projeto  apoiado atrav�s da chamada MCTI-SECIS/CNPq N� 84/2013 - Tecnologia Assistiva \n"
				+ "Data de Modifica��o: " + EnumVersao.DATA_DE_MODIFICACAO.toString() + "\n"
				);

		
		botao = new Button(shell, SWT.NONE);
		botao.setBounds(316, 255, 138, 36);
		botao.setText("Fale conosco");
		botao.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/fale_conosco.png") ));
		botao.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				if(!showContato){
					
					rotuloVersao.setText(
							"IFCE Campus Fortaleza: \n" +
							"Av. Treze de Maio, 2081 - Benfica \n" +
							"CEP: 60040-215 - Fortaleza - CE\n" +
							"Fone: (85) 3307.3666 Fax: (85) 3307.3711\n\n" +
							"Projeto Acessibilidade Virtual:\n" +
							"acessibilidadevirtual@ifce.edu.br\n" +
							"nta.ifce@gmail.com\n\n" +
							"Coordenador: agebson@gmail.com\n" +
							"Desenvolvedor: adonias.ifce@gmail.com\n\n" +
							"Telefone: (85) 3307-3791"
					);
					
					botao.setText("OK");
					botao.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/ok.png")));
					showContato = true;
				}else
					shell.close();
				
			};
		});	
	}

}
