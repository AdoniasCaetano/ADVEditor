package view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import controller.Controle;
import controller.I18N.Linguagem;

/**
 * Implementa a interface gráfica responsável por:
 * 	- exibir os idiomas disponibilizados pelo sintetitizador de voz;
 *  - permitir a seleção e a escolha de um idioma.
 *  
 */
public class DialogoLinguagens extends Dialog {

	protected Object result;
	protected Shell shell;
	private Controle controle;
	private Linguagem linguagem;

	/**
	 * Construtor da classe
	 */
	public DialogoLinguagens(Shell parent, Controle controle) {
		super(parent, SWT.DIALOG_TRIM);
		setText(controle.getText("softwareLanguage"));
		this.controle = controle;
	}
	
	/**
	 * Método estático para instanciação de um objeto desta classe.
	 * Serve acionar a exibição da interface gráfica.
	 * @param parent
	 * @param controle
	 */
	public static void showDialogo(Shell parent, Controle controle){
		DialogoLinguagens dialogo  = new DialogoLinguagens(parent, controle);
		dialogo.open();
	}

	/**
	 * Aciona a exibição do dialogo.
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
		shell.setSize(422, 151);
		shell.setText(getText());
		
		linguagem = controle.getLinguagem();
		
		Label rotuloIdiomas = new Label(shell, SWT.NONE);
		rotuloIdiomas.setBounds(10, 10, 97, 15);
		rotuloIdiomas.setText(controle.getText("language")+ ":");
		
		final Button btnCheckPT = new Button(shell, SWT.CHECK);
		final Button btnCheckEN = new Button(shell, SWT.CHECK);
		final Button btnCheckSP = new Button(shell, SWT.CHECK);
		
		switch (linguagem) {
		case ESPANHOL :
			btnCheckSP.setSelection(true); 
			btnCheckPT.setSelection(false); 
			btnCheckEN.setSelection(false);
		break;
		
		case INGLES : 
			btnCheckSP.setSelection(false); 
			btnCheckPT.setSelection(false); 
			btnCheckEN.setSelection(true);
		break;
		
		default : 
			btnCheckSP.setSelection(false); 
			btnCheckPT.setSelection(true); 
			btnCheckEN.setSelection(false);
		break;
		}
		
		btnCheckPT.setBounds(10, 45, 147, 16);
		btnCheckPT.setText(controle.getText("brazilian"));
		btnCheckPT.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/portugues.gif") ));
		btnCheckPT.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				linguagem = Linguagem.PORTUGUES;
				btnCheckEN.setSelection(false);
				btnCheckSP.setSelection(false);
			}
		});
		
		btnCheckEN.setBounds(163, 45, 144, 16);
		btnCheckEN.setText(controle.getText("american"));
		btnCheckEN.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/english.gif") ));
		btnCheckEN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				linguagem = Linguagem.INGLES;
				btnCheckPT.setSelection(false);
				btnCheckSP.setSelection(false);
			}
		});
		
		btnCheckSP.setBounds(313, 45, 93, 16);
		btnCheckSP.setText(controle.getText("spanish"));
		btnCheckSP.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/Spain.png") ));
		btnCheckSP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				linguagem = Linguagem.ESPANHOL;
				btnCheckEN.setSelection(false);
				btnCheckPT.setSelection(false);
			}
		});

		Button btnAplicar = new Button(shell,SWT.NONE);
		btnAplicar.setBounds(102, 77, 105, 35);
		btnAplicar.setText(controle.getText("apply"));
		btnAplicar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				controle.setLinguagem(linguagem);
				shell.close();
			};
		});
		
		Button btnCancelar = new Button(shell, SWT.NONE);
		btnCancelar.setBounds(213, 77, 104, 35);
		btnCancelar.setText(controle.getText("cancel"));
		btnCancelar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			};
		});
	}

}
