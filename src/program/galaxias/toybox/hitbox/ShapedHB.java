package program.galaxias.toybox.hitbox;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;

import program.Punto;

// El nombre significa: hitbox con forma, osease no cuadrada
public abstract class ShapedHB extends HitBox {
	private static final long serialVersionUID = Punto.asignateUID();

	public ShapedHB(final int x, final int y, final int operate_diameter) {
		super(x, y, operate_diameter, operate_diameter); // [borrar] operate_diameter siquiera sirve de algo?
	}

	public abstract Line2D[] border(); // hacer protected
	
	public abstract Shape shape();
	
	public boolean collide(final HitBox hb) {
		if (hb == this) { return false; }
		
		if (hb.intersects(this)) { // La funcion intersects() siempre implica contain()
			if (hb instanceof ShapedHB shb) {
				for(Line2D l1 : border()) {
					for(Line2D l2 : shb.border()) {
						if (l1.intersectsLine(l2)) return true;
						// Aqui comparo las lineas porque es lo unico que puedo compara, ni shape ni polygon me permiten hacer mas
					}
				}
				
				if (shb.shape().contains(center()) || shape().contains(shb.center())) return true;
				
				return false;
			} else {
				return shape().intersects(hb);
			}
		}
		
		return false;
	}
	
	protected Point center() {
		// En el momento de crear una figura irregular, recuerda mover esto
		return new Point((int) getCenterX(), (int) getCenterY());
	}
}
