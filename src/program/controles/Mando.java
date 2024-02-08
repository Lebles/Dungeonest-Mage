package program.controles;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Mando implements KeyListener {
	public final Tecla[] tecla;

	// public boolean far;
	// public boolean fab;
	// public boolean fde;
	// public boolean fiz;
	// public boolean pre;

	public Mando(final int[] nt) {
		tecla = new Tecla[nt.length];

		for (byte b = 0; b < tecla.length; b++) {
			tecla[b] = new Tecla(nt[b]);
		}
	}

	public void actualizar() {
		for (byte b = 0; b < tecla.length; b++) {
			tecla[b].act();
		}
	}

	public void keyPressed(final KeyEvent e) {
		for (byte b = 0; b < tecla.length; b++) {
			tecla[b].t_press(e);
		}
	}

	public void keyReleased(final KeyEvent e) {
		for (byte b = 0; b < tecla.length; b++) {
			tecla[b].des_press(e);
		}
	}

	public void keyTyped(final KeyEvent e) {
	}
}
