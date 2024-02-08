package program.galaxias.toybox.hitbox;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import program.Punto;

public class RotateBox extends ShapedHB {
	private static final long serialVersionUID = Punto.asignateUID();
	
	private float angulo;
	private final float[][] prot;
	private int dx, dy;
	private int rx, ry; // centro de rotacion
	
	private final int rw, rh; // real widht, real height

	public RotateBox(final int x, final int y, final int w, final int h, final float angle) {
		super(x, y, 0);
		angulo = angle;
		rw = w; rh = h;
		
		prot = new float[2][4];
		actualizar();
	}
	
	public Line2D[] border() {
		Line2D[] lines = new Line2D[prot[0].length];
		x -= dx; y -= dy;
		int i;
		for(i = 0; i < lines.length-1; i++) {
			lines[i] = new Line2D.Float(prot[0][i] + x, prot[1][i] + y, prot[0][i+1] + x, prot[1][i+1] + y);
		}
		lines[lines.length-1] = new Line2D.Float(prot[0][i] + x, prot[1][i] + y, prot[0][0] + x, prot[1][0] + y);
		x += dx; y += dy;
		
		return lines;
	}
	
	public Polygon shape() {
		final int[][] mat = new int[prot.length][prot[0].length];
		
		for(int i = 0; i < prot.length; i++) {
			for(int j = 0; j < prot[i].length; j++) {
				mat[i][j] = (i == 0 ? (int) x-dx : (int) y-dy) + Math.round(prot[i][j]);
			}
		}
		
		return new Polygon(mat[0], mat[1], mat[0].length);
	}
	
	private void actualizar() {
		prot[0][0] = (float) ((Math.cos(angulo) * rx) + (Math.sin(angulo) * ry));
		angulo -= Math.PI/2;
		prot[1][0] = (float) ((Math.cos(angulo) * rx) + (Math.sin(angulo) * ry));
		angulo += Math.PI/2;
		
		prot[0][1] = prot[0][0] + (float) (Math.cos(angulo) * rw);
		prot[1][1] = prot[1][0] + (float) (Math.sin(angulo) * rw);

		angulo += Math.PI/2;
		prot[0][2] = prot[0][1] + (float) (Math.cos(angulo) * rh);
		prot[1][2] = prot[1][1] + (float) (Math.sin(angulo) * rh);

		angulo += Math.PI/2;
		prot[0][3] = prot[0][2] + (float) (Math.cos(angulo) * rw);
		prot[1][3] = prot[1][2] + (float) (Math.sin(angulo) * rw);
		
		angulo -= Math.PI;

//		System.out.print(Math.cos(angulo) + "/" + angulo + ": ");
		
		Rectangle fig = shape().getBounds();
		dx += fig.x - x; dy += fig.y - y;
		setRect(fig);
		
			
//		for(int i = 0; i < prot.length; i++) {
//			for(int j = 0; j < prot[i].length; j++) {
//				System.out.print(prot[i][j] + ", ");
//			}
//		}
//		System.out.println();
	}
	
	public void rotate(final float rotation) {
		angulo += rotation;
		actualizar();
	}
	
	public void setAngle(final float newAngle) {
		angulo = newAngle;
		actualizar();
	}
	
	public float getAngle() { return angulo; }
	
	public void setRotateFocal(final int cx, final int cy) {
		rx = cx; ry = -cy;
	}
	public void setRotateCenter(final int cx, final int cy) {
		setLocation(cx + dx, cy + dy);
	}
	
	public int realX() { return (int) (x - dx + Math.round((Math.cos(angulo) * rx) + (Math.sin(angulo) * ry))); }
	public int realY() {
		final float an = (float) (angulo-Math.PI/2);
		return (int) (y - dy + Math.round((Math.cos(an) * rx) + (Math.sin(an) * ry)));
	}
}
