package program.galaxias.toybox.entitys;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;

public class Dummy extends Entity {

	public Dummy(final int init_x, final int init_y) {
		super(1, init_x, init_y, 16, 160, 20f);
	}

	public void act(final Game gm) {}

	public Object[] drop() { return null; }
}
