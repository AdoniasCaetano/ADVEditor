package controller;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {
	
	private Linguagem linguagem;
	private ResourceBundle  captions;
	
	public enum Linguagem{
		PORTUGUES,
		INGLES,
		ESPANHOL
	}

	public Linguagem getLinguagem() {
		return linguagem;
	}

	public void setLinguagem(Linguagem linguagem) {
		this.linguagem = linguagem;
		String language, country;
		
		switch (linguagem) {
			case ESPANHOL:
				language = "sp";
				country =  "SP";
			break;
			
			case INGLES:
				language = "en";
				country =  "US";
			break;
			
			default:
				language = "pt";
				country =  "BR";	
			break;
		}
		
		Locale locale = new Locale(language, country); 
		captions = ResourceBundle.getBundle("Messages",locale);  
	}
	
	public String getText(String chave){
		return captions.getString(chave);
	}
}
