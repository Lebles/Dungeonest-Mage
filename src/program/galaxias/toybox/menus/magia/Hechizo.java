package program.galaxias.toybox.menus.magia;

import java.awt.image.BufferedImage;

public class Hechizo {
//	private final name, description;
	
	public final BufferedImage icon;
	
	public final Simbolo[] parte; // [cambiar] Convertir a private
	
	public Hechizo(final BufferedImage img, final Simbolo ... obj) {
		icon = img;
		
		parte = obj;
	}
}
