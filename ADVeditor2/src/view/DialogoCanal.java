package view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import controller.Controle;

/**
 * Implementa a interface gr�fica respons�vel por:
 * 	- exibir os canais de �udio (dubla��o) do video;
 *  - permitir a sele��o e a escolha de um canal de �udio.
 *  
 *  A janela di�logo criada por esta classe s� � exibida se o video possuir mais de um canal.
 */
public class DialogoCanal extends Dialog {

	protected Object result;
	protected Shell shell;
	private String[] canais;
	private Controle controle;
	private Combo comboCanais;

	/**
	 * Construtor da classe
	 */
	public DialogoCanal(Shell parent, Controle controle) {
		super(parent, SWT.DIALOG_TRIM);
		setText(controle.getText("dectetedChannels"));
		this.canais = controle.getNomeCanais();
		this.controle = controle;
	}
	
	/**
	 * M�todo est�tico para instancia��o de um objeto desta classe.
	 * Serve acionar a exibi��o da interface gr�fica.
	 * @param parent
	 * @param controle
	 */
	public static void showDialogo(Shell parent, Controle controle){
		DialogoCanal dialogo  = new DialogoCanal(parent, controle);
		dialogo.open();
	}

	/**
	 * Aciona a exibi��o do dialogo.
	 */
	public Object open() {
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
	 * Constroe os componenentes do dialogo.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 139);
		shell.setText(getText());
		
		Label rotuloCanais = new Label(shell, SWT.NONE);
		rotuloCanais.setBounds(10, 24, 164, 15);
		rotuloCanais.setText(controle.getText("dectetedChannels")+ ":");
		
		comboCanais = new Combo(shell, SWT.NONE);
		comboCanais.setBounds(180, 16, 254, 23);
		
		for(String canal : canais)
			comboCanais.add(canal);
		
		comboCanais.select( Controle.CANAL_EM_USO ); 
		
		Button btnAplicar = new Button(shell,SWT.NONE);
		btnAplicar.setBounds(105, 59, 105, 35);
		btnAplicar.setText(controle.getText("apply"));
		btnAplicar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				controle.setCanalEmUso(comboCanais.getSelectionIndex());
				shell.close();
			};
		});
		
		Button btnCancelar = new Button(shell, SWT.NONE);
		btnCancelar.setBounds(216, 59, 104, 35);
		btnCancelar.setText(controle.getText("cancel"));
		btnCancelar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			};
		});
	}
}
