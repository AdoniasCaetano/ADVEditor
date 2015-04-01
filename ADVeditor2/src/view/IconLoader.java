package view;

import java.io.InputStream;

public class IconLoader {

	
	public static InputStream load(String path) {
		return IconLoader.class.getResourceAsStream(path);
	
	}
}
