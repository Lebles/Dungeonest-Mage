package program.galaxias.toybox.hitbox;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import program.Punto;

public class SphereBox extends ShapedHB {
	private static final long serialVersionUID = Punto.asignateUID();

	private final Ellipse2D c;
	
	public SphereBox(final int x, final int y, final int diametro) {
		super(x, y, diametro);
		
		c = new Ellipse2D.Float(x, y, diametro, diametro);
	}

	public Line2D[] border() {
		c.setFrame(this); // [falta] provar si esto hay que actualizarlo cada vez o si funciona siempre
		Line2D[] lines = new Line2D[8];
		final double r = getWidth()/2;
		
		for(int t = 0; t < lines.length; t++) {
			lines[t] = new Line2D.Double(c.getCenterX() + r*Math.cos(t*Math.PI/4), c.getCenterY() + r*Math.sin(t*Math.PI/4),
					c.getCenterX() + r*Math.cos((t+1)*Math.PI/4), c.getCenterY() + r*Math.sin((t+1)*Math.PI/4));
		}
		
		return lines;
	}

	public Ellipse2D shape() {
		c.setFrame(this); // [falta] provar si esto hay que actualizarlo cada vez o si funciona siempre
		return c;
	}

}
