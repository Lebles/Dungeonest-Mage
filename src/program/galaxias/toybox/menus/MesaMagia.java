package program.galaxias.toybox.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import program.GestorCentral;
import program.display.Loader;
import program.galaxias.Game;
import program.galaxias.toybox.menus.magia.Damage;
import program.galaxias.toybox.menus.magia.Esfera;
import program.galaxias.toybox.menus.magia.Simbolo;

public class MesaMagia implements Menu {
	public static final BufferedImage alphabet = Loader.upCIT("/texture/GUI/Runas - 32p.png");
	
	private final Simbolo[] stock; // Reprecenta a los simbolos a los que tienes acceso
	private Simbolo buffer;
	
	private final ArrayList<Simbolo> circulo; /* [cambiar] Deberia de simplificar esto para que una clase tan compleja como
															ArryList no tenga que hacerlo. */
	public MesaMagia() {
		circulo = new ArrayList<Simbolo>(5);
		
		stock = new Simbolo[] {
			new Esfera(), new Damage(0.2f)
		};
	}
	
	public void inter(final Graphics2D g, final GestorCentral gc, final Game gm) {
		// BackGound:
		g.setColor(new Color(0xFFD8BA));
		g.fillRect(55, 42, 256, 256);
		g.fillRect(10, 42, 40, 256);
		
		// Iterfaz de colocacion:
		g.scale(0.5, 0.5);
		
		Rectangle sel = null;
		g.setColor(Color.white);
		for(int i = 0; i < stock.length; i++) {
			if (i%2 == 0) sel = new Rectangle(25, 116 +     i*37, 32, 32);
			else 		  sel = new Rectangle(62, 116 + (i-1)*37, 32, 32);
			
			if (sel.contains(gc.rat.getX(g), gc.rat.getY(g))) {
				g.fill(sel);
				if (gc.rat.click_izq()) buffer = stock[i].clone();
			}
			g.drawImage(alphabet.getSubimage(stock[i].sx, stock[i].sy, 32, 32), sel.x, sel.y, null);
		}
		
		g.scale(2, 2);
		
		// Circulo
		if (circulo.isEmpty()) {
			g.setColor(Color.DARK_GRAY);
			g.drawString("Circulo basio", 110, 170);
			if (buffer != null) {
				circulo.add(buffer); buffer = null;
			}
			return;
		}
		
		for(int i = 0, size = circulo.size(); i < size; i++) { // [falta] hacer que puedan colocarce mas circulos
			final Simbolo e = circulo.get(i);
			final int nods = e.size();
			
			if (nods == 1) {
				// Esto le falta lo que pasa cuando tienes a varios simbolos de tama;o minimo
				g.drawImage(alphabet.getSubimage(e.sx, e.sy, 32, 32), 167, 154, null);
				continue;
			}
			
			final double gap = 2*Math.PI/nods; // calculando rotacion de cada parte
			
			// Dibujando simbolos del circulo:
			g.translate(183, 170);
			g.rotate(-Math.PI/2);
			
			// Nota: puse el set color white mas arriba
			for(Simbolo s : e) {
				g.rotate(gap);
				if (s != null) g.drawImage(alphabet.getSubimage(s.sx, s.sy, 32, 32), -16, -122, null);
				else {
					if (buffer != null) {
						e.add(buffer); buffer = null;
					} else g.drawRect(-16, -122, 32, 32);
				}
			}

			g.rotate(Math.PI/2);
			g.translate(-183, -170);

			// Dibujando "circulo" (poligono):
			g.setColor(Color.black);
			for(int j = 0; j < nods; j++) {
				g.drawLine(183 - (int) (90*Math.cos(gap*j)), 170 + (int) (90*Math.sin(gap*j)),
						183 - (int) (90*Math.cos(gap*(j+1))), 170 + (int) (90*Math.sin(gap*(j+1))));
			}
		}
	}

	public void dispose() {}

	public String name() { return "Mesa de Investigacion"; }
}
