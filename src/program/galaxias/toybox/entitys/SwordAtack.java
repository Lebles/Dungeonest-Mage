package program.galaxias.toybox.entitys;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.hitbox.RotateBox;
import program.galaxias.toybox.items.Item;
import program.galaxias.toybox.jugador.Jugador;

public class SwordAtack extends Entity {
	
	private final float d_angle;
	
	public final RotateBox hb;
	
	private final float atk;
	
	public SwordAtack(final int init_x, final int init_y, final float ataque) {
		super(0, new RotateBox(init_x, init_y, 16, 6, 0), 4, 0, 0, 22f);
		
		d_angle = (float) -(Math.PI/4);
		hb = (RotateBox) super.hb;
		hb.setRotateFocal(-26, -3);
		
		atk = ataque;
	}
	
	public void draw(final Graphics2D g, final BufferedImage[] img) {
		g.translate(hb.realX(), hb.realY());
		g.rotate(hb.getAngle() + d_angle);
		
		g.drawImage(img[sprite], -dx, -dy, null);

		g.rotate(-hb.getAngle() - d_angle);
		g.translate(-hb.realX(), -hb.realY());
	}
	
	public void act(final Game gm) {
		for(Entity ent : gm.ent.getEntitys()) { // dato: aqui solo invoca una sola vez a la funcion
			if (collide(ent) && !(ent instanceof Item || ent instanceof Jugador)) {
				if (LP > atk) {
					ent.makeDamage(atk);
					LP -= atk;
				} else {
					ent.makeDamage(LP);
					LP = -0.5f;
				}
			}
		}
	}

	public Object[] drop() { return null; }
	
	public void setAngle(final float new_angle) { ((RotateBox)hb).setAngle(new_angle); }
	public void setTenasity(final float tenasity) { LP = tenasity; }
	
	protected void natural_repultion(final Entity ent) {
		if (ent != this && ent instanceof SwordAtack oatk) { // realmente lo que se medie es una potencial clase "Atack"
			if (LP > oatk.LP) {
				LP -= oatk.LP; oatk.LP = -1;
			} else {
				oatk.LP -= LP; LP = -1;
			}
		}
	}
}
