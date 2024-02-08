package program.controles;

import java.awt.event.KeyEvent;

public class Tecla {
	private byte estado;
	private int pos;

	public Tecla(final int withc) {
		estado = 0;
		pos = withc;
	}

	public void t_press(final KeyEvent e) {
		if (e.getKeyCode() == pos && estado == 0) {
			estado = 1;
		}
	}

	public void des_press(final KeyEvent e) {
		if (e.getKeyCode() == pos) {
			estado = -2;
		}
	}

	protected void act() {
		if (estado != 0 && estado < 3) {
			estado++;
		}
	}

	public boolean press() {
		return estado > 1;
	}

	public boolean click() {
		return estado == 2;
	}

	public boolean un_click() {
		return estado == -1;
	}
}
