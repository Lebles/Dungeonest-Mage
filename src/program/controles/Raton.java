package program.controles;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

public class Raton extends MouseAdapter {
	private Point pos;
	private int right, left; // si se hace click izquierdo o derecho, y durante cuanto tiempo (en ticks)
	private int rotate, buff; // si se rueda la rueda del raton, y que tanto
	private int dct; // dct = doble click trigger; es para detectar el doble click

	public Raton() {
		pos = new Point();
	}

	public void actualizar(final Canvas pantalla) {
		pos = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(pos, pantalla);
		
		if (dct < 0) dct--; else dct++;

		buff = rotate;
		rotate = 0;
		
		if (right > 0) right++; // [borrar]?  // me cuestiono si realmente hay que borrarlo
		if (left > 0) left++;	// same
	}

	public boolean sel_brick(final int x, final int y, final int w, final int h) {
		if (pos.x > x * 2 && pos.x < (x + w) * 2) {
			if (pos.y > y * 2 && pos.y < (y + h) * 2) {
				return true;
			}
		}

		return false;
	}

	public boolean sel_chunk(final int x1, final int y1, final int x2, final int y2) {
		if (pos.x > x1 * 2 && pos.x < x2 * 2) {
			if (pos.y > y1 * 2 && pos.y < y2 * 2) {
				return true;
			}
		}

		return false;
	}

	// Metodos heredados del mouse
	public void mousePressed(final MouseEvent e) {
		switch(e.getButton()) {
		case 1: {
			left = 1;
			break;
		}
		case 3: {
			right = 1;
			break;
		}
		}
	}
	
	public void mouseReleased(final MouseEvent e) {
		switch(e.getButton()) {
		case 1: {
			left = 0;
			dct = -1;
			break;
		}
		case 3: {
			right = 0;
			dct = 0;
			break;
		}
		}
	}
	
	public void mouseWheelMoved(final MouseWheelEvent e) {
		rotate += e.getWheelRotation();
	}

	// Chequeador de mouse para el juego
	public boolean click_izq() {
		return left == 2;
	}
	
	public boolean press_izq() {
		return left > 1;
	}

	public boolean click_der() {
		return right == 2;
	}
	
	public boolean press_der() {
		return right > 1;
	}
	
	public int rotation() {
		return buff;
	}
	
	public boolean getDobbleClickTrigger(final boolean is_der, final int mark) {
		return ((is_der && right==2) || (!is_der && left==2)) && (is_der ? (dct >= 0 && dct < mark) : (dct < 0 && dct*-1 <= mark));
	}
	
	public int getX() { return pos.x / 2; }
	public int getY() { return pos.y / 2; }
	
	public int getX(final Graphics2D g) { return (int) (pos.x / g.getTransform().getScaleX()); }
	public int getY(final Graphics2D g) { return (int) (pos.y / g.getTransform().getScaleY()); }
}
