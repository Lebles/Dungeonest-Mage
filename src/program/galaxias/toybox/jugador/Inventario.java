package program.galaxias.toybox.jugador;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import program.GestorCentral;
import program.Punto;
import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.EntityList;
import program.galaxias.toybox.entitys.Mesa;
import program.galaxias.toybox.items.Bread;
import program.galaxias.toybox.items.Item;
import program.galaxias.toybox.items.Sword;
import program.galaxias.toybox.menus.Menu;
import program.galaxias.toybox.menus.MesaMagia;

public class Inventario implements Menu {
	/* Soy conciente de que aqui he accidentalmente mezclado los procesos de ejecucion y de dibujado.
	   Sin embargo, pretendo arreglarlo cuando sea importante. */
	
	public final Item[] hand; // creo que hand deberia ser la capacidad de carga + 1 (el slot de reserva)
	
	private final BufferedImage slot, plus;
	
	private final ArrayList<Item> bag;
	
	private Item grab;
	private int dx, dy;
	
	// [borrar]?
	public static final int spacing = 20;
	public static final int pos_init = 20;
	
	public Inventario(final BufferedImage base) {
		hand = new Item[3];
		slot = base.getSubimage(0, 0, 18, 18);
		plus = base.getSubimage(0, 18, 18, 18);
		
		bag = new ArrayList<Item>(20);
		for(int i = 0; i < 20; i++) { // [borrar]
			if (Punto.random.nextBoolean()) {
				bag.add(new Bread(0, 0));
			} else {
				bag.add(new Sword(0, 0));
			}
		}
	}
	
	public void slotsUI(final Graphics2D g, final GestorCentral gc, final EntityList ent, final Jugador ply, final int init_x) {
		final int slot_init = 56; // posicion donde inician los slots
		final int slot_gap = 25; // separacion de los slots
		final int sel = ply.getSel();
		
		// dibujando slots de la mano
		for (int j = 0; j < hand.length; j++) {
			if (j != hand.length-1) {
				g.setColor(j != sel ? new Color(0f,0f,0f,0.5f) : Color.lightGray);
				g.fillRect(init_x, j*slot_gap + slot_init, 18, 18); // dibujar background del item
			} else if (j == sel) {
				g.setColor(Color.lightGray);
				g.fillRect(init_x, j*slot_gap + slot_init, 18, 18);
			}
			
			if (hand[j] != null) { // dibujar item en el slot, cuando esta
				g.drawImage(ent.getSprite(hand[j].sprite), init_x+1, j*slot_gap + slot_init+1, null);
			}
			
			g.drawImage(slot, init_x, j*slot_gap + slot_init, null); // dibujar carcasa del slot
			
			if (ply.menu instanceof Inventario) { // para manipular los items en los slots
				final Rectangle rec = new Rectangle(init_x, j*slot_gap + slot_init, 18, 18);
				if (rec.contains(gc.rat.getX(), gc.rat.getY())) {
					if (grab != null) {
						g.setColor(Color.yellow);
						g.draw(rec);
						
						if (!gc.rat.press_izq()) {
							if (hand[j] != null) {
								bag.add(0, hand[j]); // esto es para cuando quiero reemplazar un item con otro
							}
							hand[j] = grab;
							grab = null;
						}
					} else if (gc.rat.click_izq()) {
						grab = hand[j];
						hand[j] = null;
						dx = rec.x - gc.rat.getX();
						dy = rec.y - gc.rat.getY();
					}
				}
			}
		}
		
		g.drawImage(plus, init_x, (hand.length-1)*slot_gap + slot_init, null);
	}
	
	public boolean use(final GestorCentral gc, final Game gm, final Jugador ply) {
		// para cuando se esta usando el item
		if (hand[ply.getSel()] != null && hand[ply.getSel()].isActive()) {
			hand[ply.getSel()].use(gm, ply);
			return false;
		}
		
		// cuando se hacer click izquierdo
		if (gc.rat.click_izq()) {
			if (hand[ply.getSel()] == null) {
				final Rectangle r = new Rectangle(); r.x = (Punto.pal/2) - 24; r.y = r.x; r.width = 48; r.height = r.width;
				
				if (r.contains(gc.rat.getX(), gc.rat.getY())) {
//					r.width = 20; r.height = r.width;
//					r.x = ply.getX() - 2; r.y = ply.getY() - 2;
					final Entity[] loot = gm.ent.getEntitys();
					for(int i = 0; i < loot.length; i++) {
						if ((loot[i] instanceof Item || loot[i] instanceof Mesa) &&
								loot[i].hb.contains(gc.rat.getX() + ply.hb.x-Jugador.dis, gc.rat.getY() + ply.hb.y-Jugador.dis)) {
							
							if (loot[i] instanceof Item) {
								hand[ply.getSel()] = (Item) gm.ent.retire(i);
								hand[ply.getSel()].reset();
							} else {
								ply.menu = new MesaMagia(); // [cambiar] para que pueda interactuar con las distintas mesas y menues
							}
							
							break;
						}
					}
				} else if (ply.getSel() == hand.length-1) {
					if (grab != null) bag.add(0, grab);
					return true;
				}
			} else if (ply.getSel() == hand.length-1) {
				bag.add(0, hand[ply.getSel()]);
				hand[ply.getSel()] = null;
				return use(gc, gm, ply);
			} else hand[ply.getSel()].use(gm, ply);
		}
		
		// Arrojar item (dobe-clik derecho)
		if (gc.rat.getDobbleClickTrigger(true, 10)) { // [cambiar] es psible que sea necesario cambiar el "10" por una variable
			if (hand[ply.getSel()] != null) {
				hand[ply.getSel()].hb.setLocation(ply.hb.x+6 - hand[ply.getSel()].hb.width/2, ply.hb.y+4
						- hand[ply.getSel()].hb.height/2);
				
				final float motion = (float) Math.sqrt(Math.hypot(gc.rat.getX()-Jugador.dis-8, gc.rat.getY()-Jugador.dis-8)
						*Entity.fr*2);
				
				hand[ply.getSel()].addMotion((float) Math.cos(Math.toRadians(ply.angulo))*motion,
						(float) -Math.sin(Math.toRadians(ply.angulo))*motion);
				gm.ent.createEntity(hand[ply.getSel()]);
				ply.SP -= motion*3f; hand[ply.getSel()] = null;
			}
		}
		
		return false;
	}

//============================ Invterface Related ====================================
	
	public void inter(final Graphics2D g, final GestorCentral gc, final Game gm) {
		// items
		final int itemnet = (spacing - 16) / 2;
		int r = pos_init, c = pos_init + 16; // r = row, c = collum
		for(int i = 0, size = bag.size(); i < size; i++) {
			if (r >= Punto.pal - 16 - pos_init) {
				r = pos_init;
				c += spacing;
			}
			
			g.drawImage(gm.ent.getSprite(bag.get(i).sprite), r, c, null);
			
			if (grab == null) {
				if (gc.rat.click_izq()) {
					final Rectangle rec = new Rectangle(r, c, 16, 16);
					if (rec.contains(gc.rat.getX(), gc.rat.getY())) {
						grab = bag.remove(i);
						dx = rec.x - gc.rat.getX();
						dy = rec.y - gc.rat.getY();
						break;
					}
				}
				
			} else if (new Rectangle(r - (spacing/2), c, spacing, spacing).contains(gc.rat.getX(),gc.rat.getY())) {
				g.setColor(Color.yellow);
				g.drawLine(r - itemnet, c, r - itemnet, c + 16);
				
				if (!gc.rat.press_izq()) {
					bag.add(i, grab);
					grab = null;
				}
			}
			
			r += spacing;
		}
		
		// dibujando objeto en la mano
		if (grab != null) {
			g.drawImage(gm.ent.getSprite(grab.sprite), gc.rat.getX() + dx, gc.rat.getY() + dy, null);
			
			if (!gc.rat.press_izq()) {
				bag.add(grab);
				grab = null;
			}
		}
	}

	public void dispose() { // [cambiar] Tal vez lo mejor sea hacer que puedas lanzar el item en estas circunstancias
		if (grab != null) {
			bag.add(grab);
			grab = null;
		}
	}

	public String name() { return "Inventario"; }
}
