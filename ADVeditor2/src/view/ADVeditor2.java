package view;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import to.DatabaseADV;
import to.DescricaoADV;
import controller.Controle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import java.awt.Frame;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 * @author Adonias Caetano de Oliveira
 *
 * Esta classe implementa a interface gráfica da tela principal do sistema manipulando os seus eventos.
 * Pertencente a camada view é responsável por:
 * 	- capturar eventos de botões e menus;
 *  - apresentar as descrições em uma tabela;
 *  - fornecer o frame para reprodução de vídeos usando a API DSJ (classe DSMovie);
 *  - implementar componentes para modificação de tempo, velocidade e volume.
 *  - exibir outras funcionalidades.
 */
public class ADVeditor2{

	protected Shell shell;
	private Display display;
	
	private Text areaDescricao;
	private Text campoQuantADV;
	
	private CCombo comboAudio;
	private CCombo comboVelocidade;
	private CCombo comboTomVoz;
	
	private Button botaoSalvarADV;
	private Button botaoRemoveADV;
	private Button botaoLimpar;
	private Button botaoSalvar;
	private Button botaoTestar;
	private Button botaoDescrever;
	private Button opcaoComDescricao;
	
	private MenuItem itemCanais;
	
	private Spinner spinnerHoraInicio;
	private Spinner spinnerMinutoInicio;
	private	Spinner spinnerSegundosInicio;
	private Spinner spinnerMiliSegundoInicio;
	
	private Label rotuloQuantCaracteres;
	
	private Table tableDescricoes;

	private Controle controle;
	
	private DatabaseADV database;
	private DescricaoADV adv;
	
	private boolean alterado;
	private boolean marcado = true;
	
	private Menu menuBar;
	private Menu menuVideo;
	private MenuItem itemOpen;
	private MenuItem itemVideo;
	private MenuItem itemPlay;
	private MenuItem itemStop;
	private MenuItem itemPause;
	private MenuItem itemDiminuirVolumeVideo;
	private MenuItem itemAumentarVolumeVideo;
	private MenuItem itemVoltar;
	private MenuItem itemAvancar;
	private MenuItem itemSintetizador;
	private MenuItem itemIdiomaSintetizador;
	private MenuItem itemOpcoes;
	private MenuItem itemLiguagem;
	private MenuItem itemVersao;
	private MenuItem itemAjuda;

	
	private TableColumn colunaNumero;
	private TableColumn colunaTempo;
	private TableColumn colunaDescricao;
	
	private Label rotuloNumero;
	private Label rotuloQuantDescricoes;
	private Label rotuloVelocidade;
	private Label rotuloVolumeDoSom;
	private Label rotuloTomVoz;
	
	private Thread runner;
	private boolean executar = true;
	private int linha;
		
	private Text campoNumero;
	
	private Composite videoComposite;
    private Frame videoFrame;
     
	public enum EnumModo{
		EDITAR,
		ADICIONAR
	}
	
	private EnumModo modo;
	private Text textTempoInicial;
	private Text textHora;
	private Text textMinutos;
	private Text textSegundos;
	private Text textMilisegundos;
	
	/**
	 * Execução da aplicação.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ADVeditor2 window = new ADVeditor2();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exibe a tela do sistema
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed ()) {
	         if (!display.readAndDispatch ())
	            display.sleep ();
	    }
		
		String questao = "Deseja salvar as alterações sobre o arquivo ADV?";
		
		if( alterado && DialogoGeral.showQuestion(questao, shell) ){
			salvar();
		}
		
		executar = false;
		controle.stop();
		display.close();
		System.exit(0);
		
		
	}

	/**
	 * Constroe os componentes da tela.
	 */
	protected void createContents() {	
		
		controle = new Controle(this);
		modo = EnumModo.ADICIONAR;
		
		shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.TITLE | SWT.MIN | SWT.NO_REDRAW_RESIZE);
		shell.setSize(1294, 623);
		shell.setText(EnumVersao.SOFTWARE.toString());
		
		controle.initConfigTTS(shell);
		
		//+++++++++++++++++++++++++++++++++++ BARRA DE MENU +++++++++++++++++++++++++++++++++++
		
		menuBar = new Menu(shell, SWT.BAR);
		
		itemOpen = new MenuItem(menuBar, SWT.NONE);
		itemOpen.setText(controle.getText("openVideo"));
		itemOpen.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/open.png")));
		itemOpen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				abrir();
			};
		});	
		itemOpen.setAccelerator(SWT.ALT + 'A');
				
		itemCanais = new MenuItem(menuBar, SWT.NONE);
		itemCanais.setText(controle.getText("audioChannels"));
		itemCanais.setEnabled(false);
		itemCanais.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/canal.png") ) );
		itemCanais.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				eventoShowCanais();
			};
		});	
		itemCanais.setAccelerator(SWT.ALT + 'C');
		
		menuVideo = new Menu(menuBar);
		
		itemVideo = new MenuItem(menuBar, SWT.CASCADE);
		itemVideo.setText(controle.getText("videoControl"));
		itemVideo.setMenu(menuVideo);
		itemVideo.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/controle_video.png" ) ));
		
		itemPlay = new MenuItem(menuVideo, SWT.NONE);
		itemPlay.setText(controle.getText("play") + " \t ALT R");
		itemPlay.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/play.png") ) );
		itemPlay.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				play();
			};
		});	
		itemPlay.setAccelerator(SWT.ALT + 'R'); // R = REPRODUZIR
		
		itemStop = new MenuItem(menuVideo, SWT.NONE);
		itemStop.setText(controle.getText("stop") + " \t ALT S");
		itemStop.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/stop.png") ));
		itemStop.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				stop();
			};
		});	
		itemStop.setAccelerator(SWT.ALT + 'S'); // S = STOP
		
		itemPause = new MenuItem(menuVideo, SWT.NONE);
		itemPause.setText(controle.getText("pause") + " \t ALT P");
		itemPause.setImage(new Image(shell.getDisplay(), IconLoader.load(  "icones/pause.png" ) ));
		itemPause.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				pause();
			};
		});
		itemPause.setAccelerator(SWT.ALT + 'P'); // P = PAUSE
		
		itemDiminuirVolumeVideo = new MenuItem(menuVideo, SWT.NONE);
		itemDiminuirVolumeVideo.setText(controle.getText("volumeDown") + " \t ALT -");
		itemDiminuirVolumeVideo.setImage(new Image(shell.getDisplay(), IconLoader.load(  "icones/diminuir_audio.png") ) );
		itemDiminuirVolumeVideo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				controle.diminuirVolumeVideo();
			};
		});
		itemDiminuirVolumeVideo.setAccelerator(SWT.ALT + '-');
		
		itemAumentarVolumeVideo = new MenuItem(menuVideo, SWT.NONE);
		itemAumentarVolumeVideo.setText(controle.getText("volumeUp") + " \t ALT +");
		itemAumentarVolumeVideo.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/aumentar_audio.png" ) ));
		itemAumentarVolumeVideo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				controle.aumentarVolumeVideo();
			};
		});
		itemAumentarVolumeVideo.setAccelerator(SWT.ALT + '+');
		
		itemVoltar = new MenuItem(menuVideo, SWT.NONE);
		itemVoltar.setText(controle.getText("comeBack") + " \t ALT LEFT");
		itemVoltar.setImage(new Image(shell.getDisplay(), IconLoader.load(  "icones/anterior.png" ) ));
		itemVoltar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				controle.voltarVideo();
			};
		});
		itemVoltar.setAccelerator(SWT.ALT + SWT.ARROW_LEFT);

		itemAvancar = new MenuItem(menuVideo, SWT.NONE);
		itemAvancar.setText(controle.getText("goForward") +  " \t ALT RIGHT");
		itemAvancar.setImage(new Image(shell.getDisplay(), IconLoader.load(  "icones/avancar.png" ) ));
		itemAvancar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				controle.avancarVideo();
			};
		});
		itemAvancar.setAccelerator(SWT.ALT + SWT.ARROW_RIGHT);
			
		Menu menuIdioma = new Menu(menuBar);
		
		itemSintetizador = new MenuItem(menuBar, SWT.CASCADE);
		itemSintetizador.setImage(new Image(shell.getDisplay(), IconLoader.load(  "icones/sintetizador.png") ));
		itemSintetizador.setText(controle.getText("speechSynthesizer"));
		itemSintetizador.setMenu(menuIdioma);
		
		itemIdiomaSintetizador = new MenuItem(menuIdioma, SWT.NONE);
		itemIdiomaSintetizador.setText(controle.getText("synthesizerLanguage") +  " \t ALT I");
		itemIdiomaSintetizador.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/idioma-sintetizador.png")));
		itemIdiomaSintetizador.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				System.out.println("Escolher idioma do sintetizador");
				eventoShowIdiomas();
			};
		});
		itemIdiomaSintetizador.setAccelerator(SWT.ALT + 'I');
		
		Menu menuOpcoes = new Menu(menuBar);
		
		itemOpcoes = new MenuItem(menuBar, SWT.CASCADE);
		itemOpcoes.setText(controle.getText("options"));
		itemOpcoes.setMenu(menuOpcoes);
		itemOpcoes.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/opcoes.gif") ));

		itemLiguagem = new MenuItem(menuOpcoes, SWT.NONE);
		itemLiguagem.setText(controle.getText("softwareLanguage")+  " \t ALT L");
		itemLiguagem.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/icon-idioma.png")));
		itemLiguagem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DialogoLinguagens.showDialogo(shell, controle);
			};
		});
		itemLiguagem.setAccelerator(SWT.ALT + 'L');
		
		itemAjuda = new MenuItem(menuOpcoes, SWT.NONE);
		itemAjuda.setText(controle.getText("help") +  " \t ALT H");
		itemAjuda.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/ajuda.png")));
		itemAjuda.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DialogoAjuda.showDialogo(shell, controle);
			};
		});	
		itemAjuda.setAccelerator(SWT.ALT + 'H');
		
		itemVersao = new MenuItem(menuOpcoes, SWT.NONE);
		itemVersao.setText(controle.getText("about") +  " \t ALT V");
		itemVersao.setImage(new Image(shell.getDisplay(), IconLoader.load("icones/versao.png") ));
		itemVersao.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DialogoSobre.showDialogo(shell);
			};
		});	
		itemVersao.setAccelerator(SWT.ALT + 'V');
		
		shell.setMenuBar(menuBar);
		
		botaoSalvar = new Button(shell, SWT.NONE);
		botaoSalvar.setBounds(10, 10, 36, 34);
		botaoSalvar.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/salvar.png") ));
		botaoSalvar.setEnabled(alterado);
		botaoSalvar.setToolTipText(controle.getText("updateADV"));
		botaoSalvar.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("updateADV");
			}
		});
		botaoSalvar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				salvar();
			};
		});
		
		rotuloQuantDescricoes = new Label(shell, SWT.NONE);
		rotuloQuantDescricoes.setBounds(61, 26, 160, 13);
		rotuloQuantDescricoes.setText( controle.getText("quantityDescriptions") + ":" );
		
		campoQuantADV = new Text(shell, SWT.BORDER);
		campoQuantADV.setBounds(227, 23, 92, 19);
		campoQuantADV.setText("0");
		campoQuantADV.setEditable(false);
		
		tableDescricoes = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tableDescricoes.setBounds(10, 59, 538, 193);
		tableDescricoes.setHeaderVisible(true);
		tableDescricoes.setLinesVisible(true);
		
		tableDescricoes.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				if ((event.detail & SWT.SELECTED) != 0) {
					GC gc = event.gc;
					Rectangle area = tableDescricoes.getClientArea();
					/*
					* If you wish to paint the selection beyond the end of last column,
					* you must change the clipping region.
					*/
					int columnCount = tableDescricoes.getColumnCount();
					if (event.index == columnCount - 1 || columnCount == 0) {
						int width = area.x + area.width - event.x;
						
						if (width > 0) {
							Region region = new Region();
							gc.getClipping(region);
							region.add(event.x, event.y, width, event.height);
							gc.setClipping(region);
							region.dispose();
						}
					}
					gc.setAdvanced(true);
					
					if (gc.getAdvanced())
						gc.setAlpha(127);
						Rectangle rect = event.getBounds();
						Color foreground = gc.getForeground();
						Color background = gc.getBackground();
						gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
						gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
						gc.fillGradientRectangle(0, rect.y, 500, rect.height, false);
						// restore colors for subsequent drawing
						gc.setForeground(foreground);
						gc.setBackground(background);
						event.detail &= ~SWT.SELECTED;
					}
				}
		});
		
		tableDescricoes.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent event) {		    	
		    	
		        switch (event.keyCode) {
		        
		        	//captura DELETE
			        case SWT.DEL:
						eventoBotaoExcluir();
					break;
					
					//captura ENTER
			        case 13:
			        	visualizarADV();
			        break;
		        }
		      }
		 });
				
		tableDescricoes.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent evt) {}
			
			@Override
			public void mouseDown(MouseEvent evt) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent evt) {
				if(evt.count == 2)
					visualizarADV();
			}
		});
		
		colunaNumero = new TableColumn(tableDescricoes, SWT.CENTER);
		colunaNumero.setWidth(100);
		colunaNumero.setText(controle.getText("number"));
		colunaNumero.setResizable(false);
		
		colunaTempo = new TableColumn(tableDescricoes, SWT.CENTER);
		colunaTempo.setWidth(153);
		colunaTempo.setText(controle.getText("time"));
		colunaTempo.setResizable(false);
		
		colunaDescricao = new TableColumn(tableDescricoes, SWT.LEFT);
		colunaDescricao.setWidth(281);
		colunaDescricao.setText(controle.getText("description"));
		colunaDescricao.setResizable(false);
		
		//+++++++++++++++++++++++++++++ REPRODUÇÃO DE DESCRICOES ++++++++++++++++++++++++++++++
		
		rotuloNumero = new Label(shell, SWT.NONE);
		rotuloNumero.setBounds(10, 269, 55, 15);
		rotuloNumero.setText(controle.getText("number") + ":");
		
		campoNumero = new Text(shell, SWT.BORDER);
		campoNumero.setBounds(10, 290, 120, 21);
		campoNumero.setEditable(false);
		
		rotuloVelocidade = new Label(shell, SWT.NONE);
		rotuloVelocidade.setBounds(147, 266, 120, 18);
		rotuloVelocidade.setText(controle.getText("readSpeed") + ":");
			
		comboVelocidade = new CCombo(shell, SWT.BORDER);
		comboVelocidade.setBounds(147, 290, 107, 21);
		comboVelocidade.setItems(new String[]{"50","75", "100", "125", "150", "175", "200", "225", "250", "275", "300"});
		comboVelocidade.select(5);
			
		rotuloVolumeDoSom = new Label(shell, SWT.NONE);
		rotuloVolumeDoSom.setBounds(283, 266, 104, 16);
		rotuloVolumeDoSom.setText(controle.getText("volumeAudio")+ ":");
			
		comboAudio = new CCombo(shell, SWT.BORDER);
		comboAudio.setBounds(283, 290, 92, 21);
		comboAudio.setItems(new String[]{"20","40", "60", "80", "100", "120", "140", "180", "200"});
		comboAudio.select(4);
		
		rotuloTomVoz = new Label(shell, SWT.NONE);
		rotuloTomVoz.setText(controle.getText("toneVoice") + ":");
		rotuloTomVoz.setBounds(403, 266, 120, 18);
		
		comboTomVoz = new CCombo(shell, SWT.BORDER);
		comboTomVoz.setItems(new String[] {"0", "10","20","30", "40", "50", "60", "70", "80", "90", "100"});
		comboTomVoz.setBounds(403, 290, 107, 21);
		comboTomVoz.select(5);
		
		textTempoInicial = new Text(shell, SWT.BORDER);
		textTempoInicial.setEditable(false);
		textTempoInicial.setText(controle.getText("startTime"));
		textTempoInicial.setBounds(10, 317, 120, 21);
		
		textHora = new Text(shell, SWT.BORDER);
		textHora.setText(controle.getText("hours"));
		textHora.setEditable(false);
		textHora.setBounds(136, 317, 58, 21);
		
		spinnerHoraInicio = new Spinner(shell, SWT.BORDER);
		spinnerHoraInicio.setBounds(136, 339, 58, 22);
		spinnerHoraInicio.setMinimum(0);
		spinnerHoraInicio.setToolTipText("Ajustar Hora Inicial");
		spinnerHoraInicio.setToolTipText(controle.getText("setStartHours"));
		spinnerHoraInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartHours");
			}
		});
		spinnerHoraInicio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
								
				if(spinnerHoraInicio.getSelection() >= spinnerHoraInicio.getMaximum() ){
					System.out.println("hora maxima!");
				}
			}
		});
		
		textMinutos = new Text(shell, SWT.BORDER);
		textMinutos.setText(controle.getText("minutes"));
		textMinutos.setEditable(false);
		textMinutos.setBounds(200, 317, 55, 21);
		
		spinnerMinutoInicio = new Spinner(shell, SWT.BORDER);
		spinnerMinutoInicio.setBounds(200, 341, 55, 22);
		spinnerMinutoInicio.setMinimum(-1);
		spinnerMinutoInicio.setMaximum(60);
		spinnerMinutoInicio.setToolTipText(controle.getText("setStartMinutes"));
		spinnerMinutoInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartMinutes");
				
			}
		});
		spinnerMinutoInicio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		
				if(spinnerMinutoInicio.getSelection() >= spinnerMinutoInicio.getMaximum() ){
					spinnerHoraInicio.setSelection( spinnerHoraInicio.getSelection()  + 1 );
					spinnerMinutoInicio.setSelection( 0 );
				}else if(spinnerMinutoInicio.getSelection() == spinnerMinutoInicio.getMinimum() ){
						
					if( spinnerHoraInicio.getSelection() > 0 ){
						spinnerMinutoInicio.setSelection(59);
						spinnerHoraInicio.setSelection( spinnerHoraInicio.getSelection()  - 1 );
					}else{
						spinnerMinutoInicio.setSelection(0);
					}
				}
			}
		});
		
		textSegundos = new Text(shell, SWT.BORDER);
		textSegundos.setText(controle.getText("seconds"));
		textSegundos.setEditable(false);
		textSegundos.setBounds(267, 317, 64, 21);
		
		spinnerSegundosInicio = new Spinner(shell, SWT.BORDER);
		spinnerSegundosInicio.setBounds(267, 339, 64, 22);
		spinnerSegundosInicio.setMinimum(-1);
		spinnerSegundosInicio.setMaximum(60);
		spinnerSegundosInicio.setSelection(0);
		spinnerSegundosInicio.setToolTipText(controle.getText("setStartSeconds"));
		spinnerSegundosInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartSeconds");
			}
		});
		spinnerSegundosInicio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
					
				if(spinnerSegundosInicio.getSelection() >= spinnerSegundosInicio.getMaximum() ){				
					spinnerMinutoInicio.setSelection( spinnerMinutoInicio.getSelection()  + 1 );
					spinnerSegundosInicio.setSelection( 0 );
				}else if(spinnerSegundosInicio.getSelection() == spinnerSegundosInicio.getMinimum() ){
						
					if( spinnerMinutoInicio.getSelection() > 0 ){
						spinnerSegundosInicio.setSelection(59);
						spinnerMinutoInicio.setSelection( spinnerMinutoInicio.getSelection()  - 1 );
					}else{
						spinnerSegundosInicio.setSelection(0);
					}
				}
			}
		});
		
		textMilisegundos = new Text(shell, SWT.BORDER);
		textMilisegundos.setText(controle.getText("miliseconds"));
		textMilisegundos.setEditable(false);
		textMilisegundos.setBounds(336, 317, 84, 21);
		
		spinnerMiliSegundoInicio = new Spinner(shell, SWT.BORDER);
		spinnerMiliSegundoInicio.setBounds(336, 339, 84, 22);
		spinnerMiliSegundoInicio.setMaximum(999);
		spinnerMiliSegundoInicio.setMinimum(-1);	
		spinnerMiliSegundoInicio.setToolTipText(controle.getText("setStartMiliseconds"));
		spinnerMiliSegundoInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartMiliseconds");
			}
		});
		spinnerMiliSegundoInicio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		
				if(spinnerMiliSegundoInicio.getSelection() > spinnerMiliSegundoInicio.getMaximum() ){				
					spinnerSegundosInicio.setSelection( spinnerSegundosInicio.getSelection()  + 1 );
					spinnerMiliSegundoInicio.setSelection( 0 );
				}else if(spinnerMiliSegundoInicio.getSelection() == spinnerMiliSegundoInicio.getMinimum() ){
						
					if( spinnerSegundosInicio.getSelection() > 0 ){
						spinnerMiliSegundoInicio.setSelection(999);
						spinnerSegundosInicio.setSelection( spinnerSegundosInicio.getSelection()  - 1 );
					}else{
						spinnerMiliSegundoInicio.setSelection(0);
					}
				}
			}
		});
		
		areaDescricao = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP );
		areaDescricao.setBounds(10, 382, 538, 99);
		
		rotuloQuantCaracteres = new Label(shell, SWT.NONE);
		rotuloQuantCaracteres.setBounds(10, 487, 94, 15);
		rotuloQuantCaracteres.setText("0 " + controle.getText("character"));
		
		botaoSalvarADV = new Button(shell, SWT.NONE);
		botaoSalvarADV.setBounds(72, 508, 104, 30);	
		botaoSalvarADV.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				eventoBotaoSalvarADV();
			};
		});
		
		botaoRemoveADV = new Button(shell, SWT.NONE);
		botaoRemoveADV.setBounds(182, 508, 101, 30);
		botaoRemoveADV.setText(controle.getText("delete"));
		botaoRemoveADV.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/excluir.png") ));	
		botaoRemoveADV.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				eventoBotaoExcluir();
			};
		});	
		
		botaoLimpar = new Button(shell, SWT.NONE);
		botaoLimpar.setBounds(289, 508, 101, 30);
		botaoLimpar.setText(controle.getText("clear"));
		botaoLimpar.setImage(new Image(shell.getDisplay(),IconLoader.load( "icones/limpar.png") ));
		botaoLimpar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				eventoBotaoLimpar();
			};
		});
		
		botaoTestar = new Button(shell, SWT.NONE);
		botaoTestar.setBounds(396, 508, 101, 30);
		botaoTestar.setText(controle.getText("test"));
		botaoTestar.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/testar.png")));
		botaoTestar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				testar();
			};
		});
		
		videoComposite = new Composite(shell, SWT.NO_BACKGROUND | SWT.EMBEDDED);
		videoComposite.setBounds(581, 10, 687, 471);
		
		videoFrame = SWT_AWT.new_Frame(videoComposite);
		videoFrame.setBackground(java.awt.Color.black);
		
		opcaoComDescricao = new Button(shell, SWT.CHECK);
		opcaoComDescricao.setBounds(581, 487, 214, 16);
		opcaoComDescricao.setText(controle.getText("filmAudioDescription"));
		opcaoComDescricao.setSelection( marcado );
		opcaoComDescricao.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				marcado = !marcado;
				
				if(marcado)
					controle.playADV();
				else
					controle.pauseADV();
			};
		});
		
		botaoDescrever = new Button(shell, SWT.NONE);
		botaoDescrever.setBounds(581, 507, 263, 33);
		botaoDescrever.setText(controle.getText("captureEarlyDescription"));
		botaoDescrever.setToolTipText(controle.getText("captureEarlyDescription"));
		botaoDescrever.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/tempo_inicial.png" ))); 	
	
		
		botaoDescrever.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				eventoBotaoDescrever();
			};
		});
		
		areaDescricao.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				rotuloQuantCaracteres.setText(areaDescricao.getText().length() + " " + controle.getText("character"));
			}
			@Override
			public void keyPressed(KeyEvent event) {				
				switch (event.keyCode) {
					case 9:
						event.doit = false;
						botaoSalvarADV.setFocus();
						
					break;
				}
			}
		});
		
		popularTable();
		setBotaoSalvarADV();
	}

	
	/*
	 * Altera a apresentação do botaoSalvarADV
	 * Este botão apresenta dois modos:
	 * 	- ADICIONAR : Cria uma nova descrição.
	 *  - EDITAR : altera o texto de descrição existente
	 */
	private void setBotaoSalvarADV(){
		if(modo == EnumModo.ADICIONAR){
			botaoSalvarADV.setText(controle.getText("add"));
			botaoSalvarADV.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/adicionar.png" )));
		}else{
			botaoSalvarADV.setText(controle.getText("edit"));
			botaoSalvarADV.setImage(new Image(shell.getDisplay(), IconLoader.load( "icones/atualizar.png")));
		}
	}
	
	/*
	 * Carrega a tabela de descrições.
	 */
	private void popularTable(){
		//Limpa a tabela
		tableDescricoes.clearAll();
		campoQuantADV.setText("0");
				
		if( database != null && database.size() > 0 ){
			
			ArrayList<DescricaoADV> lista  = database.getDescricoesADV();
			campoQuantADV.setText("" + lista.size());
			String[] tempo;
			for (int i = lista.size() - 1; i >= 0 ; i--) {
				tempo = lista.get(i).getTempoFormatado();

				TableItem item = new TableItem(tableDescricoes, i, SWT.NONE);
				item.setText(0, "" + lista.get(i).getNumero());
				item.setText(1, "" + tempo[0] );
				item.setText(2, lista.get(i).getDescricao());
				
			}
		}
				
		for (int i = 0; i < tableDescricoes.getColumnCount(); i++) {
			tableDescricoes.getColumn(i).pack();
		}
	}
	

	/*
	 * Permite a visualização completa da descrição selecionada na tabela.
	 * Configura os campos de textos e os combobox de acordo com os parâmetros da descrição.
	 */
	private void visualizarADV(){
		if(tableDescricoes.getSelectionCount()  == 1){
			
			controle.pause();
			
			adv =  database.getDescricaoByIndex( tableDescricoes.getSelectionIndex() );
			
	    	spinnerHoraInicio.setSelection( adv.getHoraInicial() );
	    	spinnerMinutoInicio.setSelection( adv.getMinutoInicial() );
	    	spinnerSegundosInicio.setSelection( adv.getSegundosInicial() );
	    	spinnerMiliSegundoInicio.setSelection( adv.getMiliSegundosIncial() );
	    	
	    	areaDescricao.setText(adv.getDescricao());
	    	campoNumero.setText("" + adv.getNumero());
	    	
	    	comboVelocidade.select( adv.getVelocidade() / 25 - 2 );
	    	comboAudio.select( adv.getVolume() / 20 - 1);
	    	comboTomVoz.select( adv.getTomVoz()/10 );
	    	
			rotuloQuantCaracteres.setText(adv.getDescricao().length() + " " + controle.getText("character"));

	    	modo = EnumModo.EDITAR;
	    	setBotaoSalvarADV();
	    }
	}
	
	/*
	 * Formata os componentes para receber uma nova descrição.
	 */
	private void eventoBotaoLimpar(){
		spinnerHoraInicio.setSelection( 0 );
    	spinnerMinutoInicio.setSelection( 0 );
    	spinnerSegundosInicio.setSelection( 0 );
    	spinnerMiliSegundoInicio.setSelection( 0 );
    	
		campoNumero.setText("");
		areaDescricao.setText("");
		comboVelocidade.select(5);
		comboAudio.select(4);
		comboTomVoz.select(5);
		
		rotuloQuantCaracteres.setText("0 " + controle.getText("character"));
		
		adv = null;
		modo = EnumModo.ADICIONAR;
		setBotaoSalvarADV();
		
		startMidia();
	}
	
	/*
	 * Preenche os campos e combobox com uma descrição.
	 */
	private void setParametrosADV(DescricaoADV adv){
		adv.setDescricao( areaDescricao.getText() );
		
		int velocidade = Integer.valueOf( comboVelocidade.getItem( comboVelocidade.getSelectionIndex() ) );
		int audio =  Integer.valueOf( comboAudio.getItem( comboAudio.getSelectionIndex() ) );
		int tomVoz = Integer.valueOf( comboTomVoz.getItem( comboTomVoz.getSelectionIndex() ) ); 
		
		adv.setVelocidade( velocidade );
		adv.setVolume( audio );
		adv.setTomVoz( tomVoz );
		
		int hora = Integer.valueOf( spinnerHoraInicio.getText() ) * 3600000;
		int minutos = Integer.valueOf( spinnerMinutoInicio.getText() ) * 60000;
		int segundos = Integer.valueOf( spinnerSegundosInicio.getText() ) * 1000;
		int milisegundos = Integer.valueOf( spinnerMiliSegundoInicio.getText() );
		
		adv.setTempoInicial( hora + minutos + segundos + milisegundos );
		adv.setTempoFinal( hora + minutos + segundos + milisegundos );
	}
	
	/*
	 * Aciona a exibição de uma janela diálogo contendo os canais de áudio do vídeo.
	 */
	private void eventoShowCanais(){
		if(controle.isMultiCanais()){
			DialogoCanal.showDialogo(shell, controle);
		}
	}
	
	/*
	 * Aciona a exibição de uma janela diálogo contendo os idiomas do sintetizador de voz.
	 */
	private void eventoShowIdiomas(){
		System.out.println("Menu idioma do sintetizador....");
		DialogoIdiomas.showDialogo(shell, controle);
	}
	
	/*
	 * Atualiza a tabela de descrições sem alterar o arquivo ADV.
	 */
	private void eventoBotaoSalvarADV(){
		
		if(modo == EnumModo.ADICIONAR){
			
			if(adv == null)
				adv = new DescricaoADV();
			
			setParametrosADV(adv);
			
			if(adv.isDescricaoNula())
				DialogoGeral.showMensagemErro(controle.getText("descriptionVoidAdd"), shell);
			else{
				if(database != null)
					adv.setNumero(database.size() + 1);
				else{
					database = new DatabaseADV();
					adv.setNumero(1);
				}
				database.add(adv);
				popularTable();
				alterado = true;
				botaoSalvar.setEnabled(alterado);
			}
				
		}else{
			
			if( areaDescricao.getText() == null || areaDescricao.getText().isEmpty() )
				DialogoGeral.showMensagemErro(controle.getText("descriptionVoidEdit"), shell);
			else{
				setParametrosADV(adv);
				popularTable();
				alterado = true;
				botaoSalvar.setEnabled(alterado);
			}
			
		}
		eventoBotaoLimpar();
		startMidia();
	}
	
	/*
	 * Remove a descrição da tabela.
	 */
	private void eventoBotaoExcluir(){
		if(DialogoGeral.showQuestion(controle.getText("reallyDelete"), shell) ) {
			if(adv != null){
				database.remove(adv.getTempoInicial());
				alterado = true;
				botaoSalvar.setEnabled(alterado);
				popularTable();
				adv = null;
			}else if(tableDescricoes.getSelectionCount()  == 1){
					adv =  database.getDescricoesADV().get( tableDescricoes.getSelectionIndex() );
					database.remove(adv.getTempoInicial());
					alterado = true;
					botaoSalvar.setEnabled(alterado);
					popularTable();
					adv = null;
			}
		}
	}
	
	/*
	 * Permite a descrição de uma cena do video.
	 */
	private void eventoBotaoDescrever(){
		
		if( database != null){
			
			controle.pause();
			adv = new DescricaoADV();
			adv.setTempoInicial( controle.getTempoEmMiliSegundos() );
			
			if( database.getDescricaoByTempo( adv.getTempoInicial() ) == null ){
				
				spinnerHoraInicio.setSelection( adv.getHoraInicial() );
			    spinnerMinutoInicio.setSelection( adv.getMinutoInicial() );
			    spinnerSegundosInicio.setSelection( adv.getSegundosInicial() );
			    spinnerMiliSegundoInicio.setSelection( adv.getMiliSegundosIncial() );
		    	
		    	areaDescricao.setText("");
		    	campoNumero.setText("");
		    	
		    	modo = EnumModo.ADICIONAR;
		    	setBotaoSalvarADV();
			}else{
				DialogoGeral.showMensagemAtencao(controle.getText("timeAlreadyDescription"), shell);
			}
			
		}
		
	}
	
	/*
	 * Aciona o diálogo para abertura de um video.
	 */
	private void abrir(){
		if( controle.abrirMultimidia(shell)) {
			database = controle.getDatabaseADV();
			popularTable();
			controle.play();
			reproduzir();
			botaoSalvar.setEnabled(true);

			
			if(controle.isMultiCanais())
				itemCanais.setEnabled(true);
			else
				itemCanais.setEnabled(false);
		}		
	}
	
	/*
	 * Permite a visualização do video no frame.
	 */
	public void reproduzir(){
		videoFrame.setVisible(false);
		videoFrame.removeAll();
		videoFrame.add( java.awt.BorderLayout.CENTER, controle.asComponent() );
		videoFrame.add( java.awt.BorderLayout.SOUTH, controle.getSwingMovieController() );
		videoFrame.setEnabled(true);
		videoFrame.setVisible(true);
		startSelection();

	}
	
	/*
	 * Atualiza o arquivo ADV.
	 */
	private void salvar(){
		if(controle.salvar(shell, database)){
			alterado = false;
			botaoSalvar.setEnabled(alterado);
			startMidia();
		}
	}
	
	/*
	 * Testa a audiodescrição de uma determinada cena.
	 */
	private synchronized void testar(){
		if(!areaDescricao.getText().equals("")){
			int hora = Integer.valueOf( spinnerHoraInicio.getText() ) * 3600000;
			int minutos = Integer.valueOf( spinnerMinutoInicio.getText() ) * 60000;
			int segundos = Integer.valueOf( spinnerSegundosInicio.getText() ) * 1000;
			int milisegundos = Integer.valueOf( spinnerMiliSegundoInicio.getText() );
			
			int velocidade = Integer.valueOf( comboVelocidade.getItem( comboVelocidade.getSelectionIndex() ) );
			int audio =  Integer.valueOf( comboAudio.getItem( comboAudio.getSelectionIndex() ) );
			int tomVoz = Integer.valueOf( comboTomVoz.getItem( comboTomVoz.getSelectionIndex() ) ); 
	
			controle.playVideo( hora + minutos + segundos + milisegundos);
			controle.pauseADV();
			controle.speak(
						areaDescricao.getText(), 
						velocidade, 
						audio,
						tomVoz ,
						shell);
			controle.playADV();
						
			
		}else
			DialogoGeral.showMensagemInformacao(controle.getText("noDescriptionTest"), shell);
	}
	
	/*
	 * Inicia a reprodução do video e audiodescrição.
	 */
	private void startMidia(){
		controle.play();
		spinnerHoraInicio.setMaximum( controle.getDuracao() );
		botaoDescrever.setEnabled(true);
	}
	
	private void play(){
		controle.play();
	}
	
	private void pause(){
		controle.pause();
	}
	
	private void stop(){
		controle.stop();
	}
	
	public void startSelection(){
		
		executar = true;
		
		System.out.println("start selection");
		
		runner = new Thread() {
		        public void run() {
		            while (executar) {
		                try {
		                    display.syncExec(new Runnable() {
		                    	public void run() {
			                        if (shell.isDisposed()) return;
			                        
			                        linha = controle.getNumeroAtual();
			                        
			                       // System.out.println("Linha " + (linha - 1) + " selecionada");
			                        
			                        if( linha > 0){
			                        	
			                        	tableDescricoes.setSelection(linha - 1);
			                        }
			                 
			                      
			                    }
			                });
		                                   
		                    Thread.sleep(1);
		                } catch (InterruptedException e) {
							e.printStackTrace();
						} 
		            }
		            // wake the user interface thread from sleep
		            if( display.isDisposed())
		            	display.wake();
		        }
		};

		runner.start();
	}
	
	public void setIdiomaSoftware(){
		itemOpen.setText(controle.getText("openVideo"));
		itemCanais.setText(controle.getText("audioChannels"));
		itemVideo.setText(controle.getText("videoControl"));
		itemPlay.setText(controle.getText("play") + " \t ALT R");
		itemStop.setText(controle.getText("stop") + " \t ALT S");
		itemPause.setText(controle.getText("pause") +  " \t ALT P");
		itemDiminuirVolumeVideo.setText(controle.getText("volumeDown") + " \t ALT -");
		itemAumentarVolumeVideo.setText(controle.getText("volumeUp") +  " \t ALT +");
		itemVoltar.setText(controle.getText("comeBack") + " \t ALT LEFT");
		itemAvancar.setText(controle.getText("goForward") + " \t ALT RIGHT");
		itemSintetizador.setText(controle.getText("speechSynthesizer"));
		itemOpcoes.setText(controle.getText("options"));
		itemIdiomaSintetizador.setText(controle.getText("synthesizerLanguage") +  " \t ALT I");
		itemLiguagem.setText(controle.getText("softwareLanguage") +  " \t ALT L");
		itemVersao.setText(controle.getText("about") +  " \t ALT V");
		itemAjuda.setText(controle.getText("help") +  " \t ALT H");
		
		rotuloQuantDescricoes.setText( controle.getText("quantityDescriptions") + ":" );
		colunaNumero.setText(controle.getText("number"));
		colunaTempo.setText(controle.getText("time"));
		colunaDescricao.setText(controle.getText("description"));
		rotuloNumero.setText(controle.getText("number") + ":");
		rotuloVelocidade.setText(controle.getText("readSpeed") + ":");
		rotuloVolumeDoSom.setText(controle.getText("volumeAudio")+ ":");
		rotuloTomVoz.setText(controle.getText("toneVoice") + ":");
		textTempoInicial.setText(controle.getText("startTime"));
		textHora.setText(controle.getText("hours"));
		textMinutos.setText(controle.getText("minutes"));
		textSegundos.setText(controle.getText("seconds"));
		textMilisegundos.setText(controle.getText("miliseconds"));

		botaoSalvar.setToolTipText(controle.getText("updateADV"));
		botaoSalvar.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("updateADV");
			}
		});
		
		spinnerHoraInicio.setToolTipText(controle.getText("setStartHours"));
		spinnerHoraInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartHours");
			}
		});
		
		spinnerMinutoInicio.setToolTipText(controle.getText("setStartMinutes"));
		spinnerMinutoInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartMinutes");
			}
		});
		
		spinnerSegundosInicio.setToolTipText(controle.getText("setStartSeconds"));
		spinnerSegundosInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartSeconds");
			}
		});
		
		spinnerMiliSegundoInicio.setToolTipText(controle.getText("setStartMiliseconds"));
		spinnerMiliSegundoInicio.getAccessible ().addAccessibleListener (new AccessibleAdapter() {
			public void getName (AccessibleEvent e) {
				e.result = controle.getText("setStartMiliseconds");
			}
		});
		
		rotuloQuantCaracteres.setText(areaDescricao.getText().length() + " " + controle.getText("character"));
		
		setBotaoSalvarADV();
		botaoRemoveADV.setText(controle.getText("delete"));
		botaoLimpar.setText(controle.getText("clear"));
		botaoTestar.setText(controle.getText("test"));
		opcaoComDescricao.setText(controle.getText("filmAudioDescription"));
		
		botaoDescrever.setText(controle.getText("captureEarlyDescription"));
		botaoDescrever.setToolTipText(controle.getText("captureEarlyDescription"));

	}
}