package program.galaxias.toybox.items;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.hitbox.HitBox;
import program.galaxias.toybox.jugador.Jugador;

public abstract class Item extends Entity {
	
	protected boolean active;
	
	public Item(int spr, int x, int y, final int dense, int life_points, final float friccion) {
		super(spr, x, y, dense, life_points, friccion);
		active = false;
	}
	
	public Item(int spr, final HitBox hb, final int displace_image_x, final int displace_image_y, int life_points, final float friccion) {
		super(spr, hb, displace_image_x, displace_image_y, life_points, friccion);
		active = false;
	}

	public abstract void use(final Game gm, final Jugador ply);
	
	public boolean isActive() {
		return active;
	}
	
	public void act(final Game gm) {} // esto significa que los items no suelen hacer nada cuando estan en forma de entidad
	
	public Object[] drop() { return null; }
	
	protected void natural_repultion(final Entity ent) {
		if (!(ent instanceof Jugador)) {
			super.natural_repultion(ent);
		}
	}
	
	public void reset() { super.resetMotion(); }
	
	// Tal vez luego coloque que tengan efectos pasivos tambien. O eso se lo dejare a alguna clase armadura?
	
	// Los graficos los aconstruire en otra parte
}
