package program.galaxias.toybox.entitys;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.hitbox.HitBox;

public class Mesa extends Entity {
	// tecnicamente, las entidades van a poder permitir habrir menues e interfaces

	public Mesa(final int x, final int y) {
		super(5, new HitBox(x, y, 15, 16), 1, 0, 50, 25);
	}
	
	public void act(final Game gm) {}

	public Object[] drop() {
		return null;
	}
	
	public void makeDamage(final float dam) {} // la mesa no resive da;o
	
	public void natural_repultion(final Entity ent) {} // la mesa no tiene repulcion natural
}
