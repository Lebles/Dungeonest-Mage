package program.controles;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import program.Punto;

public class Teclado implements KeyListener {
	public boolean read;

	private String txt;            // Donde se guarda el texto tipeado
	private int time;              // Contador de tiempo para que no escribas 50 veces a un letra al precionarla solo una vez
									// - tambien se reinicia cuando dejas de precionar una tecla, para que puedas escribir rapido
	private final int maxTime;     // Maximo de tiempo antes de que la letra se repita con velocidad
	private boolean enter;

	public Teclado() {
		read = false;
		txt = "";
		maxTime = Punto.aco / 5;
	}

	public String clearText() { // recoje el texto tipeado
		final String rem = txt;
		txt = "";
		return rem;
	}

	public String getText() {
		return txt;
	}

	public boolean enter() {
		if (enter) {
			enter = false;
			return true;
		}
		return false;
	}

	public void keyPressed(final KeyEvent kev) {
		if (!read) {
			return;
		}

		if (time == 0 || time >= maxTime) {
			if (kev.getKeyCode() == KeyEvent.VK_BACK_SPACE) { // Esta parte es para que funcione la letra de borrar
				if (txt.length() > 0) {
					txt = txt.substring(0, txt.length() - 1);
				}
			} else if (kev.getKeyCode() == KeyEvent.VK_ENTER) { // Esta es para que funcione enter
				enter = true;
			} else {
				if (isReal(kev.getKeyChar())) {
					txt += kev.getKeyChar();
				}
				// System.out.print(kev.getKeyCode() + ",");
			}
		}

		if (isReal(kev.getKeyChar())) {
			time++;
		}
	}

	private boolean isReal(final char c) {
		if (c != '\n') {
			return Character.isDefined(c);
		} else {
			return false;
		}
	}

	public void keyReleased(KeyEvent arg0) {
		time = 0;
	}

	public void keyTyped(KeyEvent arg0) {
	}
}
