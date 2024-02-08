package program.galaxias.toybox.menus.magia;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.jugador.Jugador;
import program.galaxias.toybox.menus.magia.unuseful_interfaces.Cuantico;

public class Esfera extends Simbolo implements Cuantico {
	private int size;
	private float speed;
//	private float energy;
	
	public Esfera() {
		super(1, 11, 1);
		
		// [cambiar] Luego lo hare customizable, esto es una solucion temporal
		size = 10;
		speed = 1f;
		sub[0] = new Damage(0.5f);
	}

	public void execute(final Game gm, final Entity executer) {
		if (executer instanceof Jugador e) {
			final double ang = Math.toRadians(-e.angulo);
			final int sl = size/2;
			gm.ent.createEntity(new program.galaxias.toybox.entitys.Esfera(
					(int) (e.hb.getCenterX() + (sl+10)*Math.cos(ang) - sl), (int) (e.hb.getCenterY() + (sl+10)*Math.sin(ang) - sl),
					size, 30, 0xFF0000, 0xFFFF00, ang, speed, sub[0]));
			
		} else executer.makeDamage(executer.getLP() -1);
	}
}
