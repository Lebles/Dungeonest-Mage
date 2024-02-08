package program.galaxias.toybox.items;

import program.Punto;
import program.galaxias.Game;
import program.galaxias.toybox.entitys.SwordAtack;
import program.galaxias.toybox.jugador.Jugador;

public class Sword extends Item {
	private final SwordAtack atk;
	
	private float finalAngle;
	private final float stricke_speed;
	
	public Sword(final int x, final int y) {
		super(0, x, y, 12, 100, 1f);
		
		atk = new SwordAtack(0, 0, 0.33f);
		stricke_speed = (float) ((Math.PI/3)/(Punto.aco/4));
	}

	public void use(final Game gm, final Jugador ply) {
		if (!active) {
			active = true;
			atk.setTenasity(3.3f);
			gm.ent.createEntity(atk);
			finalAngle = (float) (Math.PI-Math.toRadians(ply.angulo + 30));
			atk.setAngle(finalAngle);
			finalAngle = (float) (Math.PI-Math.toRadians(ply.angulo - 30));

			atk.hb.setRotateCenter(ply.hb.x + 6, ply.hb.y + 4);
		}
		
		if (atk.hb.getAngle() < finalAngle) {
			atk.hb.setRotateCenter(ply.hb.x + 6, ply.hb.y + 4);
			atk.hb.rotate(stricke_speed);
		} else {
			active = false;
			atk.setTenasity(-1);
		}
	}
}
