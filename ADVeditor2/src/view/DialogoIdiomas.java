package view;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
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
 * Implementa a interface gráfica responsável por:
 * 	- exibir os idiomas disponibilizados pelo sintetitizador de voz;
 *  - permitir a seleção e a escolha de um idioma.
 *  
 */
public class DialogoIdiomas extends Dialog {

	protected Object result;
	protected Shell shell;
	private Hashtable<String, String> idiomas;
	private ArrayList<String> variantes;
	private Controle controle;
	private Combo comboIdiomas;
	private Combo comboVariantes;

	/**
	 * Construtor da classe
	 */
	public DialogoIdiomas(Shell parent, Controle controle) {
		super(parent, SWT.DIALOG_TRIM);
		setText("Idiomas do Sintetizador");
		idiomas = controle.getIdiomas(shell);
		variantes = controle.getVariantes(shell);
		this.controle = controle;
	}
	
	/**
	 * Método estático para instanciação de um objeto desta classe.
	 * Serve acionar a exibição da interface gráfica.
	 * @param parent
	 * @param controle
	 */
	public static void showDialogo(Shell parent, Controle controle){
		DialogoIdiomas dialogo  = new DialogoIdiomas(parent, controle);
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
		shell.setSize(450, 180);
		shell.setText(getText());
		
		Label rotuloIdiomas = new Label(shell, SWT.NONE);
		rotuloIdiomas.setBounds(10, 24, 97, 15);
		rotuloIdiomas.setText(controle.getText("language"));
		
		comboIdiomas = new Combo(shell, SWT.NONE);
		comboIdiomas.setBounds(113, 16, 321, 23);
		
		Enumeration<String> lista = idiomas.keys();
		
		String idioma;
		int index = 0;
		while( lista.hasMoreElements() ){
			idioma = lista.nextElement();
			comboIdiomas.add( idiomas.get(idioma) );
			
			if(controle.isIdiomaEscolhido(idioma))
				comboIdiomas.select( index ); 
			
			index++;
		}
		
		Label rotuloVariante = new Label(shell, SWT.NONE);
		rotuloVariante.setText("Variante:");
		rotuloVariante.setBounds(10, 58, 97, 15);
		
		comboVariantes = new Combo(shell, SWT.NONE);
		comboVariantes.setBounds(113, 50, 321, 23);
		
		String variante;
		
		comboVariantes.add(controle.getText("noVariant"));
		if(controle.isVarianteEscolhido(""))
			comboVariantes.select(0); 
		
		for(int i = 0; i < variantes.size(); i++ ){
			variante = variantes.get(i);
			comboVariantes.add( variante );
								
			if(controle.isVarianteEscolhido(variante))
				comboVariantes.select( i + 1 ); 
		}
		
		if(comboVariantes.getSelectionIndex() == -1)
			comboVariantes.select(0); 
		
		Button btnAplicar = new Button(shell,SWT.NONE);
		btnAplicar.setBounds(105, 106, 105, 35);
		btnAplicar.setText(controle.getText("apply"));
		btnAplicar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setIdioma();
			};
		});
		
		Button btnCancelar = new Button(shell, SWT.NONE);
		btnCancelar.setBounds(216, 106, 104, 35);
		btnCancelar.setText(controle.getText("cancel"));
		btnCancelar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			};
		});
	}
	
	private void setIdioma(){
		
		int index = 0;
		
		Enumeration<String> lista = idiomas.keys();
		
		while( (comboIdiomas.getSelectionIndex() > index) && lista.hasMoreElements()){
			lista.nextElement();
			index++;
		}
		
		controle.setIdiomaTTS(lista.nextElement());
		
		if( comboVariantes.getSelectionIndex() == 0 ){
			controle.setVarianteTTS( "" );
		}else
			controle.setVarianteTTS( comboVariantes.getItem( comboVariantes.getSelectionIndex() ) );
	
		shell.close();
	}

}
