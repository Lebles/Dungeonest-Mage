package program.galaxias.toybox.entitys;

import program.Punto;
import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.hitbox.HitBox;
import program.galaxias.toybox.items.Sword;

public class Slime1 extends Entity {
	
	private int jump;
	private float dir;

	public Slime1(final int x, final int y) {
		super(3, new HitBox(x, y, 10, 8), 3, 5, 1.5f, 10f); // la vida tal vez alla que ajustarla
		
		jump = 0; dir = 0f;
	}

	public void act(final Game gm) {
		jump++;
		
		if (jump > Punto.aco*5/2) {
			addMotion((float) Math.cos(dir)*5f, (float) Math.sin(dir)*5f);
			jump = 0;
		} else if (jump == Punto.aco*2) {
			double rot = orient(this, gm.ply);
			
			if (!gm.IA.rayView(gm, this)) {
			 if (gm.IA.ask(this, hb.x+50, hb.y) && !gm.getMap().hit_block(hb.x+hb.width, hb.y, hb.width, hb.height)) rot = 0;
		else if (gm.IA.ask(this, hb.x, hb.y+50) && !gm.getMap().hit_block(hb.x, hb.y+hb.height, hb.width, hb.height)) rot = Math.PI/2;
		else if (gm.IA.ask(this, hb.x-50, hb.y) && !gm.getMap().hit_block(hb.x-hb.width, hb.y, hb.width, hb.height)) rot = Math.PI;
		else if (gm.IA.ask(this, hb.x, hb.y-50) && !gm.getMap().hit_block(hb.x, hb.y-hb.height, hb.width, hb.height)) rot = -Math.PI/2;
			}
			
			dir = (float) rot;
		}
		
		if (hb.collide(gm.ply.hb)) {
			gm.ply.makeDamage(0.093f);
			// Pienso que deberia hacer que los slimes consuman los objetos que se encuentren.
		}
		
//		for(Entity ent : gm.ent.getEntitys()) { // dato: aqui solo invoca una sola vez a la funcion
//			if (collide(ent) && ent instanceof Item) {
//				ent.makeDamage(0.33f);
//			}
//		}
	}
	
	public Object[] drop() {
		return new Object[] {new Sword(0,0), 0.05f}; // [cambiar] este no deberia ser el drop de slimes
	}
}
