package program.galaxias.toybox.menus;

import java.awt.Graphics2D;

import program.GestorCentral;
import program.galaxias.Game;

public interface Menu {
	
	void inter(final Graphics2D g, final GestorCentral gc, final Game gm);
	
	void dispose();
	
	String name();
}
