package program.galaxias.toybox.items;

import program.galaxias.Game;
import program.galaxias.toybox.hitbox.HitBox;
import program.galaxias.toybox.jugador.Jugador;

public class Bread extends Item {
	
	public Bread(final int x, final int y) {
		super(2, new HitBox(x,y,12,8), 2, 4, 5, 0.02f);
	}

	public void use(final Game gm, final Jugador ply) {
		ply.makeDamage(-1f);
		ply.destroyHandItem();
	}
}
