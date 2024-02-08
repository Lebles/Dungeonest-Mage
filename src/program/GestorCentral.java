package program;

import java.awt.Graphics2D;

import program.controles.Mando;
import program.controles.Raton;
import program.controles.Tecla;
import program.galaxias.Game;

public class GestorCentral {
	public Galaxia gal;
	
	public static Tecla test;

	public final Tecla[] tecla;
	public final Raton rat;

	public GestorCentral(final Mando teclado, final Raton mouse) {
		tecla = teclado.tecla;
		rat = mouse;
		
		test = tecla[4];
		
		gal = new Game();
	}

	public void usar(final Graphics2D g) {
		if (tecla[7].click()) Punto.debMode = !Punto.debMode;
		
		gal.play(g, this);
	}
}
