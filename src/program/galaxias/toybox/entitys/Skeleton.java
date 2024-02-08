package program.galaxias.toybox.entitys;

import program.Punto;
import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.hitbox.HitBox;
import program.galaxias.toybox.items.Bread;

public class Skeleton extends Entity {
	private int atack_timer;

	public Skeleton(final int x, final int y) {
		super(4, new HitBox(x, y, 12, 12), 2, 4, 5, 25);
		atack_timer = 0;
	}

	public void act(final Game gm) {
		if (atack_timer <= 0) {
			double rot = orient(this, gm.ply);
			
			if (!gm.IA.rayView(gm, this)) {
				final double rr = gm.IA.rigthRot(this);
				if (rr < 4f) rot = rr;
			}
			
			addAccel(rot, 0.29f, 0.004f);
			
			if (collide(gm.ply)) {
				gm.ply.makeDamage(1.44f);
				atack_timer = Punto.aco*3/2;
			}
		} else atack_timer--;
	}

	public Object[] drop() {
		return new Object[] {new Bread(0,0), 0.25f}; // [cambiar] este no deberia ser el drop de esqueletos
	}
}
