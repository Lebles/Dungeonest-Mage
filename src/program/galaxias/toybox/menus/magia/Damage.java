package program.galaxias.toybox.menus.magia;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;

public class Damage extends Simbolo {
	
	private final float dpt; // dpt = damage per tic

	public Damage(final float damage_per_tic) {
		super(0, 6, 0);
		
		dpt = damage_per_tic;
	}

	public void execute(final Game gm, final Entity ex) {
		for(Entity e : gm.ent.getEntitys()) {
			if (ex.hb.collide(e.hb)) {
				if (ex.getLP() < dpt) 
					e.makeDamage(ex.getLP());
				else e.makeDamage(dpt);
				
				ex.makeDamage(dpt);
			}
		}
	}
}
