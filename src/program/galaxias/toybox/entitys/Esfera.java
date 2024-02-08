package program.galaxias.toybox.entitys;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.GMap;
import program.galaxias.toybox.hitbox.SphereBox;
import program.galaxias.toybox.menus.magia.Simbolo;

public class Esfera extends Entity {
	// Sprite:
	private final Color alpha, beta;
	private final int diametro; // no uso el diametro de la hitbox poruque tendria que acceder a dicha hitbox y no quiero hacer eso
	
	// Sybol parameters:
	private final double dir; // Tal vez en el futuro haga que sea posible alterarlo desde afuera
	private final float vel;
	
	private final Simbolo glyf;

	public Esfera(final int init_x, final int init_y, final int diametro, final int life,
			final int color_alpha, final int color_beta,
			final double direction, final float speed, final Simbolo execute) {
		super(-1, new SphereBox(init_x, init_y, diametro), 0, 0, life, life);
		
		this.diametro = diametro;
		alpha = new Color(color_alpha);
		beta = new Color(color_beta);
		
		dir = direction;
		vel = speed;
		glyf = execute;
	}
	
	public void move(final GMap map) {}

	public void act(final Game gm) {
		if (gm.getMap().hit_block(hb.hx, hb.hy, diametro, diametro)) {
			LP = -1; return;
		}
		
		glyf.execute(gm, this);
//		if (glyf.energy <= 0) LP = -1; 		// En algun momento podre hacer esto
		
		hb.hx += vel*Math.cos(dir); hb.x = (int) hb.hx;
		hb.hy += vel*Math.sin(dir); hb.y = (int) hb.hy;
	}
	
	public void draw(final Graphics2D g, final BufferedImage[] img) {
		g.setColor(beta);
		g.fillOval(hb.x, hb.y, diametro, diametro);
		
		g.setColor(alpha);
		int nd = diametro*4/5 + (diametro%2 == 0 ? 0 : 1), txy = (diametro - nd)/2;
		g.fillOval(hb.x+txy, hb.y+txy, nd, nd);
	}

	public Object[] drop() { return null; }
}
