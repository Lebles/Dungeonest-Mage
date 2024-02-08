package program.galaxias.toybox;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import program.GestorCentral;
import program.display.Loader;
import program.galaxias.Game;

public class Entrada {
	private final Rectangle gate;
	
	private final int fin;
	
	private final BufferedImage spr;

	public Entrada(final int init_x, final int init_y, final int destino) {
		gate = new Rectangle(init_x, init_y, 16, 16);
		fin = destino;

		addons = null;
		spr = Loader.upCIO("/texture/Objects/Door0.png").getSubimage(0, 5*16, 16, 16);
	}

	public void gate(final GestorCentral gc, final Game gm) {
		if (gate.contains(gm.ply.hb)) {
			gc.gal = new Game(gm, fin);
		}
		
		if (addons != null) for(int i = 0 ; i < addons.length; i++) addons[i].gate(gc, gm);
	}
	
	public void draw(final Graphics2D g) {
		g.drawImage(spr, gate.x, gate.y, null);
		
		if (addons != null) for(Entrada e : addons) g.drawImage(e.spr, e.gate.x, e.gate.y, null);
	}
	
	// ==============================================================================================

	private final Entrada[] addons;
	
	public Entrada(final int[][] extra) {
		spr = Loader.upCIO("/texture/Objects/Door0.png").getSubimage(0, 5*16, 16, 16);
		
		if (extra != null && extra.length > 0 && extra[0] != null && extra[0].length == 3) {
			gate = new Rectangle(extra[0][0], extra[0][1], 16, 16);
			fin = extra[0][2];
		} else {
			gate = new Rectangle(-1024, -1024, 16, 16);
			fin = 0;
			addons = null;
			return;
		}
		
		addons = new Entrada[extra.length-1];
		for(int i = 1 ; i < extra.length; i++) {
			if (extra[i].length == 3) {
				addons[i-1] = new Entrada(extra[i][0],extra[i][1],extra[i][2]);
			}
		}
	}
}
