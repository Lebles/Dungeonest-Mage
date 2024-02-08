package program.galaxias.toybox.jugador;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import program.GestorCentral;
import program.Punto;
import program.display.Loader;
import program.galaxias.Game;
import program.galaxias.toybox.EntityList;
import program.galaxias.toybox.items.Bread;
import program.galaxias.toybox.items.Sword;
import program.galaxias.toybox.menus.Menu;
import program.galaxias.toybox.menus.magia.Damage;
import program.galaxias.toybox.menus.magia.Esfera;
import program.galaxias.toybox.menus.magia.Hechizo;
import program.galaxias.toybox.menus.magia.Simbolo;

public class Hechizario implements Menu {
	public static final BufferedImage icons = Loader.upCIT("/texture/GUI/Spell Icons.png");
	
	public final Hechizo[] mind; // creo que hand deberia ser la capacidad de carga + 1 (el slot de reserva)
	
	private final BufferedImage slot, plus;
	
	private final ArrayList<Hechizo> memory;
	
	private Hechizo grab;
	private int dx, dy;
	
	public Hechizario(final BufferedImage base) {
		mind = new Hechizo[5];
		slot = base.getSubimage(18, 0, 18, 18);
		plus = base.getSubimage(18, 18, 18, 18);
		
		memory = new ArrayList<Hechizo>(20);
		for(int i = 0; i < 20; i++) { // [borrar]
			Color c = null;
			switch(Punto.random.nextInt(5)) {
			case 0: { c = Color.red; break; }
			case 1: { c = Color.blue; break; }
			case 2: { c = Color.yellow; break; }
			case 3: { c = Color.gray; break; }
			case 4: { c = Color.green; break; }
			}
			
			if (Punto.random.nextBoolean()) {
				Simbolo s = new Esfera();
				memory.add(new Hechizo(Loader.change_color(icons.getSubimage(5*16, 2*16, 16, 16), c), s));
			} else {
				Simbolo s = new Damage(1f);
				memory.add(new Hechizo(Loader.change_color(icons.getSubimage(14*16, 8*16, 16, 16), c), s));
			}
		}
	}
	
	public void slotsUI(final Graphics2D g, final GestorCentral gc, final EntityList ent, final Jugador ply, final int init_x) {
		final int slot_init = 56; // posicion donde inician los slots
		final int slot_gap = 25; // separacion de los slots
		final int sel = -1-ply.getSel();
		
		// dibujando slots de la mano
		for (int j = 0; j < mind.length-1; j++) {
			g.setColor(j != sel ? new Color(0f,0f,0f,0.5f) : Color.lightGray);
			g.fillRect(init_x, j*slot_gap + slot_init, 18, 18); // dibujar background del item
			
//			if (mind[j] != null) { // dibujar item en el slot, cuando esta
//				g.drawImage(ent.getSprite(mind[j].sprite), init_x+1, j*slot_gap + slot_init+1, null);
//			}
			
			g.drawImage(slot, init_x, j*slot_gap + slot_init, null); // dibujar carcasa del slot
		}
		
		if (mind.length-1 == sel) {
			g.setColor(Color.lightGray);
			g.fillRect(init_x, (mind.length-1)*slot_gap + slot_init, 18, 18);
		}
		g.drawImage(slot, init_x, (mind.length-1)*slot_gap + slot_init, null);
		g.drawImage(plus, init_x, (mind.length-1)*slot_gap + slot_init, null);
	}
	
	public boolean use(final GestorCentral gc, final Game gm, final Jugador ply) {
		// Para abrir inventario
		if (gc.rat.click_izq() && ply.getSel() == -mind.length) {
			return true;
		}
		
		return false;
	}

	public void inter(final Graphics2D g, final GestorCentral gc, final Game gm) {
		// dibujando hechizos
		final int itemnet = (Inventario.spacing - 16) / 2;
		int r = Inventario.pos_init, c = Inventario.pos_init + 16; // r = row, c = collum
		for(int i = 0, size = memory.size(); i < size; i++) {
			if (r >= Punto.pal - 16 - Inventario.pos_init) {
				r = Inventario.pos_init;
				c += Inventario.spacing;
			}
			
			g.drawImage(memory.get(i).icon, r, c, null);
			
			if (grab == null) {
				if (gc.rat.click_izq()) {
					final Rectangle rec = new Rectangle(r, c, 16, 16);
					if (rec.contains(gc.rat.getX(), gc.rat.getY())) {
						grab = memory.remove(i);
						dx = rec.x - gc.rat.getX();
						dy = rec.y - gc.rat.getY();
						break;
					}
				}
				
			} else if (new Rectangle(r - (Inventario.spacing/2), c, Inventario.spacing, Inventario.spacing)
					.contains(gc.rat.getX(),gc.rat.getY())) {
				g.setColor(Color.yellow);
				g.drawLine(r - itemnet, c, r - itemnet, c + 16);
				
				if (!gc.rat.press_izq()) {
					memory.add(i, grab);
					grab = null;
				}
			}
			
			r += Inventario.spacing;
		}
		
		// dibujando icono en la mano
		if (grab != null) {
			g.drawImage(grab.icon, gc.rat.getX() + dx, gc.rat.getY() + dy, null);
			
			if (!gc.rat.press_izq()) {
				memory.add(grab);
				grab = null;
			}
		}
	}

	public void dispose() {}
	
	public String name() {
		return "Hechizos";
	}

}
