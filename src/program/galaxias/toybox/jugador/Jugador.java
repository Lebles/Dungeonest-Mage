package program.galaxias.toybox.jugador;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import program.GestorCentral;
import program.Punto;
import program.display.Loader;
import program.galaxias.Game;
import program.galaxias.toybox.Entity;
import program.galaxias.toybox.items.Item;
import program.galaxias.toybox.menus.Menu;
import program.galaxias.toybox.menus.magia.Esfera;
import program.galaxias.toybox.menus.magia.Simbolo;
import program.galaxias.toybox.hitbox.HitBox;

public class Jugador extends Entity {
	public static final int dis = (Punto.pal / 2) - 6;
	private static final float vel = 1.4f * 16 / Punto.aco;
	
	private final BufferedImage sprite;
	private final BufferedImage[] GUI;
	
	private final Inventario inv;
	private final Hechizario magic;
	
	private final Simbolo mam = new Esfera();
	
	public int angulo;

	public float SP; // stamina points
	private final float SPR; // SP regeneration
	
	private boolean doge; // es verdadera cuando dogueas algo realmente
	private int doge_time; // mientras sea mayor a 0, el dogueo estara activado
	private final int doge_max_time;
	
	public Menu menu;
	
	private int rueda; // cuenta de la rotacion necesaria para mover la seleccion
	
	public Jugador(final int init_x, final int init_y) {
		super(-1, new HitBox(init_x, init_y, 12, 12), 2, 4, 20, 40);
		
		sprite = Loader.upCIT("/texture/Characters/Player.png").getSubimage(0, 3 * 16, 16, 16);
		menu = null;
		SP = 100f; SPR = 100f/(Punto.aco*30);
		doge_max_time = Punto.aco/2;
		
		final BufferedImage gui = Loader.upCIT("/texture/GUI/GUI0.png");
		GUI = new BufferedImage[] {
				gui.getSubimage(6 * 16, 0, 16, 16),
				gui.getSubimage(7 * 16, 0, 16, 16),
				gui.getSubimage(8 * 16, 0, 16, 16)
		};
		
		final BufferedImage slots = Loader.upCIT("/texture/GUI/Slots.png");
		inv = new Inventario(slots);
		magic = new Hechizario(slots);
	}
	
	public void resetPlayer(final int new_x, final int new_y) { // Donde se usa esto?
		hb.setLocation(new_x, new_y);
		LP = 20; SP = 100;
		doge = false; doge_time = 0;
		if (menu != null) menu.dispose(); menu = null;
	}

	public void action(final GestorCentral gc, final Game gm) {
		if (menu != null) { // no se ejecuta el resto del codigo si esta en el inventario
			
			if (gc.tecla[6].click()) { menu.dispose(); menu = null; } // si preciones escape, sale
			
			return;
		}

		// Esto mueve el personaje cuando preciono una tecla
		moverPersonaje(gc, gm);
		
		// Esto hace que el personaje doge
		if (doge_time != 0) {
			doge_time--;
			if (doge_time == 0) {
				if (!doge) SP -= 30;
				else doge = false;
			}
		} else if (gc.tecla[5].click()) {
			doge_time = doge_max_time;
		}
		
		// regeneracion de los puntos de stamina
		SP = SP + SPR > 100f ? 100f : SP + SPR;
		
		// Deteccion del angulo mouse-personaje
		angulo = (int) Math.round(Math.toDegrees(Math.atan(((double) gc.rat.getY() - dis-6)/((double) gc.rat.getX() - dis-6))));
		angulo = gc.rat.getX() - dis - 6 < 0 ? 180-angulo : -angulo;
		
		// Sistema simple de deteccion de rueda
		final int prueda = rueda + (rueda>=0 ? 
				(inv.hand[getSel()] != null && inv.hand[getSel()].isActive() ? 0 : gc.rat.rotation()) :
				(-gc.rat.rotation()));
		rueda = rueda>=0 ? (prueda<0 ? rueda=0 : prueda) : (prueda>-wheelSpeed ? rueda=-wheelSpeed : prueda);

		if (rueda < -magic.mind.length*wheelSpeed) rueda = -magic.mind.length*wheelSpeed;
		if (rueda >= inv.hand.length*wheelSpeed) rueda = inv.hand.length*wheelSpeed - 1;

		// Aqui esta el sistema para seleccionar, recojer, tirar y/o usar items, y tambien el de acceder al inventario
		if (rueda>=0) {if (inv.use(gc, gm, this)) menu = inv;} else if (magic.use(gc, gm, this)) menu = magic;
		if (gc.tecla[8].click()) mam.execute(gm, this);
	}
	
	private void moverPersonaje(final GestorCentral gc, final Game gm) {
		int spd = 1;
		
		if (gc.rat.click_der() && !(rueda>=0 && inv.hand[getSel()] != null &&
				inv.hand[getSel()].isActive())) rueda = -wheelSpeed - rueda;
		if (rueda>=0) {if (rueda >= inv.hand.length*wheelSpeed) rueda = -wheelSpeed - rueda;}
			else if (rueda < -magic.mind.length*wheelSpeed) rueda = -wheelSpeed - rueda;
		
		final int dir = (gc.tecla[0].press() ? 1 : 0) + (gc.tecla[1].press() ? 2 : 0) +
						(gc.tecla[2].press() ? 4 : 0) + (gc.tecla[3].press() ? 8 : 0);

		if (dir != 0) {
			double rot;
			if (gc.tecla[4].press()) { spd = 3; SP -= 10f/Punto.aco; }
			switch(dir) {
			case 1: { // abajo
				rot = Math.PI/2;
				break;
			}
			case 2: { // arriba
				rot = -Math.PI/2;
				break;
			}
			case 4: { // derecha
				rot = 0;
				break;
			}
			case 5: { // abajo + derecha
				rot = Math.PI/4;
				break;
			}
			case 6: { // arriba + derecha
				rot = -Math.PI/4;
				break;
			}
			case 8: { // izquierda
				rot = Math.PI;
				break;
			}
			case 9: { // abajo + izquierda
				rot = Math.PI*3/4;
				break;
			}
			case 10: { // arriba + izquierda
				rot = Math.PI*-3/4;
				break;
			}
			default: { // todo lo demas
				rot = 0;
				break;
			}
			}
			super.addAccel(rot, vel*spd, spd==1 ? 0.007f : 0.014f);
		}
	}
		
	public void draw(final Graphics2D g) {
		// Jugador
		g.setColor(Color.red);
		g.drawArc(dis-2 - 2, dis-2 - 2, 20, 20, angulo - 30, 60);

		if (doge_time > 0) {
			g.drawImage(Loader.change_color(sprite, new Color(1,1,1,doge_time/ (float) doge_max_time)), dis-2, dis-2, null);
		} else g.drawImage(sprite, dis-2, dis-2, null);
	}
	
	public void drawGUI(final Graphics2D g, final GestorCentral gc, final Game gm) {
		int init = Punto.pal + 7;
		
		inv.slotsUI(g, gc, gm.ent, this, init + 32); // se dibujan los slots y los items en ellos
		magic.slotsUI(g, gc, gm.ent, this, init + 32 + 25); // se dibujan los slots y los hechizos en ellos
		
		// Barra de resistencia
		g.setColor(Color.blue);
		final int lpv = Math.round(7*16*LP/20);
		g.fillRect(init + 7*16 - lpv, 15, lpv, 5);
		
		// Barra de stamina
		g.setColor(Color.green);
		final int stv = Math.round(7*16*SP/100);
		g.fillRect(init + 7*16 - stv, 35, stv, 5);
		
		// Marco de barras vitales
		g.drawImage(GUI[0], init, 10, null); g.drawImage(GUI[0], init, 30, null);
		for(int dis = init+6*16; init < dis; init+=16) {
			g.drawImage(GUI[1], init, 10, null);
			g.drawImage(GUI[1], init, 30, null);
		}
		g.drawImage(GUI[2], init, 10, null); g.drawImage(GUI[2], init, 30, null);
		
		// Menues
		if (menu != null) {
			g.setColor(new Color(0f,0f,0f,0.5f));
			g.fillRect(0, 0, Punto.pal, Punto.pal);
			
			g.setColor(Color.white);
			g.setFont(g.getFont().deriveFont(24f));
			g.drawString(menu.name(), 16, 10 + g.getFontMetrics().getAscent());
			g.setFont(g.getFont().deriveFont(12)); // no estoy seguro si 12 e la respuesta, por el asunto de pathfinder
			
			menu.inter(g, gc, gm); // dibujar el menu e interactuar (inter = interfaz de usuario)
		}
	}
	
	public void destroyHandItem() {
		inv.hand[getSel()] = null;
		// [cambiar] Tal vez este codigo deberia ser ejecutado desde el inventario
	}
	
	public Inventario getInventory() { return inv; }
	
	private final int wheelSpeed = 10; // medido en tics; [borrar]?
	public int getSel() { return (rueda / wheelSpeed); } /* [borrar] es realmente necesario? creo que solo una
														 		clase usa este metodo y realmente un solo integer hace lo mismo */
	public boolean atMenu() { return menu != null; }
	
	
	
	// Entity related: ==================================================================

	public void act(final Game gm) {}
	public Object[] drop() { return null; }
	
	public void draw(final Graphics2D g, final BufferedImage[] img) {}
	
	public float getLife() { return LP; }
	
	// doge overwrite
	public void addAccel(final double dir, final float F_max, float F_temp) {
		if (doge_time == 0) super.addAccel(dir, F_max, F_temp);
		else {
			SP -= F_temp+fr; // [cambair] Esto de 'SP' deberia ser cambiado
			doge = true;
			doge_time = doge_max_time;
		}
	}

	public void makeDamage(final float dam) {
		if (doge_time != 0) {
			SP -= dam*5;
			doge = true;
			doge_time = doge_max_time;
		} else LP -= dam;
	}
	
	protected void natural_repultion(final Entity ent) {
		if (!(ent instanceof Item) && doge_time == 0) {
			super.natural_repultion(ent);
		}
	}
}
