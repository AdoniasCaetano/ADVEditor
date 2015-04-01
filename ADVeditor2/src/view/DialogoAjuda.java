package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import controller.Controle;


/**
 * 
 * @author Adonias Caetano de Oliveira
 * 
 * Implementa a interface gráfica do dialogo que exibe informações sobre o sistema.
 * Possui as seguintes funcionalidades:
 * 	- Apresenta alguns dados sobre o software e o Projeto Acessibilidade Virtual;
 *  - Implementa os componentes do formulário de sugestões ou críticas ao projeto.
 *
 */
public class DialogoAjuda extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private Button botaoSair;
	private Table table;
	private Controle controle;

	/**
	 * Construtor da classe.
	 */
	public DialogoAjuda(Shell parent, Controle controle) {
		super(parent, SWT.DIALOG_TRIM);
		setText(controle.getText("help"));
		this.controle = controle;
	}
	
	/**
	 * Método estático para instanciação de um objeto desta classe.
	 * Serve acionar a exibição da interface gráfica.
	 * @param parent
	 * @param controle
	 */
	public static void showDialogo(Shell parent, Controle controle){
		DialogoAjuda dialogo  = new DialogoAjuda(parent, controle);
		dialogo.open();
	}

	/**
	 * Aciona a exibição do dialogo.
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
		shell.setSize(615, 398);
		shell.setText(getText());
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(21, 22, 572, 295);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnAtalho = new TableColumn(table, SWT.NONE);
		tblclmnAtalho.setWidth(100);
		tblclmnAtalho.setText(controle.getText("shortCut"));
		
		TableColumn tblclmnDescrio = new TableColumn(table, SWT.NONE);
		tblclmnDescrio.setWidth(467);
		tblclmnDescrio.setText(controle.getText("description"));
		
		TableItem item = new TableItem(table, SWT.NONE, 0);
		item.setText(0, "ALT ");
		item.setText(1, controle.getText("accessMenuBar"));
		
		item = new TableItem(table, SWT.NONE, 1);
		item.setText(0, "ALT A");
		item.setText(1, controle.getText("openVideoFile"));
		
		item = new TableItem(table, SWT.NONE, 2);
		item.setText(0, "ALT C");
		item.setText(1, controle.getText("movieAudioChannels"));
		
		item = new TableItem(table, SWT.NONE, 3);
		item.setText(0, "ALT R");
		item.setText(1, controle.getText("playVideo"));
		
		item = new TableItem(table, SWT.NONE, 4);
		item.setText(0, "ALT S");
		item.setText(1, controle.getText("stopVideo"));
		
		item = new TableItem(table, SWT.NONE, 5);
		item.setText(0, "ALT P");
		item.setText(1, controle.getText("pauseVideo"));

		item = new TableItem(table, SWT.NONE, 6);
		item.setText(0, "ALT -");
		item.setText(1, controle.getText("decreaseAudioVolume"));
		
		item = new TableItem(table, SWT.NONE, 7);
		item.setText(0, "ALT +");
		item.setText(1, controle.getText("increaseAudioVolume"));
		
		item = new TableItem(table, SWT.NONE, 8);
		item.setText(0, "ALT LEFT");
		item.setText(1, controle.getText("backVideoScenes"));
		
		item = new TableItem(table, SWT.NONE, 9);
		item.setText(0, "ALT RIGHT");
		item.setText(1, controle.getText("advanceVideoScenes"));
		
		item = new TableItem(table, SWT.NONE, 10);
		item.setText(0, "ALT I");
		item.setText(1, controle.getText("configureSpeechSynthesizerLanguage"));
		
		item = new TableItem(table, SWT.NONE, 11);
		item.setText(0, "ALT L");
		item.setText(1, controle.getText("configuresSoftwareLanguage"));
		
		item = new TableItem(table, SWT.NONE, 12);
		item.setText(0, "ALT H");
		item.setText(1, controle.getText("accessShortCuts"));
		
		item = new TableItem(table, SWT.NONE, 13);
		item.setText(0, "ALT V");
		item.setText(1, controle.getText("displayInformationSoftware"));
		
		botaoSair = new Button(shell, SWT.NONE);
		botaoSair.setBounds(235, 326, 138, 36);
		botaoSair.setText(controle.getText("exit"));
		botaoSair.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/ok.png")));
		botaoSair.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.setVisible(false);
			}
		});	
	}
}
