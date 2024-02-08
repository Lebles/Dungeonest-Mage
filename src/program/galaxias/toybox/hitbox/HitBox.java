package program.galaxias.toybox.hitbox;

import java.awt.Rectangle;

import program.Punto;

public class HitBox extends Rectangle {
	private static final long serialVersionUID = Punto.asignateUID();
	
	public float hx, hy;
	
	public HitBox(final int x, final int y, final int w, final int h) {
		super(x,y,w,h);
		
		hx = x; hy = y;
	}
	
	public boolean collide(final HitBox hb) {
		if (hb == this) { return false; }
		
		if (hb.intersects(this)) { // La funcion intersects() siempre implica contain()
			if (hb instanceof ShapedHB shb) {
				return shb.shape().intersects(this);
			} else if (this instanceof ShapedHB) {
				System.out.println("[HitBox][CollitionError]: Esto no deberia de ejecutarce nunca.");
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	public void setLocation(final int x, final int y) {
		super.setLocation(x, y); hx = x; hy = y;
	}
}
