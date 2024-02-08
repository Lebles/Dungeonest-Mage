package program.galaxias.toybox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import program.Punto;
import program.galaxias.Game;
import program.galaxias.toybox.hitbox.HitBox;
import program.galaxias.toybox.hitbox.ShapedHB;
import program.galaxias.toybox.items.Item;

public abstract class Entity {
	public static final float fr = 0.25f; // friccion del suelo
	
	public final int sprite; // en un futuro a;adire el sistema por el que los sprites se seleccionan segun el doc donde estaban y tal
	
	protected int dx, dy;
	public final HitBox hb;
	private float vx, vy, vm; // vectores de velocidad X y Y, junto con su modulo
	public final float peso; // Si alguna vez necesitas cambiar esto, solo usa reflexion
	
	protected float LP; // Life Points
	
	public Entity(final int spr, final HitBox hitbox, final int displace_image_x, final int displace_image_y,
			final float life_points, final float weigth) {
		sprite = spr;
		hb = hitbox;
		dx = displace_image_x; dy = displace_image_y;
		
		LP = life_points;
		peso = weigth;
	}
	
	public Entity(final int spr, final int x, final int y, final int dense, final float life_points, final float weigth) {
		sprite = spr;
		hb = new HitBox(x, y, dense, dense);
		dx = 8-(dense/2); dy = dx;
		
		LP = life_points;
		peso = weigth;
	}
	
	// Accion y dibujado
	
	public void draw(final Graphics2D g, final BufferedImage[] img) {
		g.drawImage(img[sprite], hb.x - dx, hb.y - dy, null);
	}
	public void debModeDraw(final Graphics2D g) {
		g.setColor(Color.white);	g.draw(hb);
		if (hb instanceof ShapedHB nhb) { g.setColor(Color.red); g.draw(nhb.shape()); /*for(Line2D l : nhb.border()) g.draw(l);*/ }
		g.setColor(Color.green);
		g.drawLine(imgX(), imgY(), (int) Math.round(imgX() + LP*16/20), imgY()); // [cambiar] el "20" reprecenta la vida del jugador
		g.drawLine(hb.x+6, hb.y+6, hb.x+6 + (int) ((Math.abs(vx) > 0.0001f ? vx+(fr*vx/vm) : 0 )*30),
								   hb.y+6 + (int) ((Math.abs(vy) > 0.0001f ? vy+(fr*vy/vm) : 0 )*30));
	}
	
	public abstract void act(final Game gm);
	
	public void death(final Game gm /*Esta instancia de Game solo esta para cuando el metodo sea sobrescrito*/) {
		
		/* [cambiar] realmente creo que esto deberia ir solo en las entidades pensantes, aunque no sabria
		 			 como implementarlo junto a todas las demas entidades. Tal vez deberia separar las entidades en
		 			 mobs y no mobs? */
		
		final Object[] data = drop();
		if (data == null) return;
		
		if (data[0] instanceof Item drop) {
			if (Punto.random.nextFloat() < (float) data[1]) {
				drop.hb.setLocation(hb.x - drop.hb.width + Punto.random.nextInt(hb.width + drop.hb.width),
									   hb.y - drop.hb.height + Punto.random.nextInt(hb.height + drop.hb.height));
				gm.ent.createEntity(drop);
			}
		} else {
			final Item[] drop  = (Item[]) data[0];
			final float[] rate = (float[]) data[1];
			
			for(int i = 0; i < drop.length; i++) {
				if (Punto.random.nextFloat() < rate[i]) {
					drop[i].hb.setLocation(hb.x - drop[i].hb.width + Punto.random.nextInt(hb.width + drop[i].hb.width),
										   hb.y - drop[i].hb.height + Punto.random.nextInt(hb.height + drop[i].hb.height));
					gm.ent.createEntity(drop[i]);
				}
			}
		}
	}
	
	public abstract Object[] drop();
	
	// Relacionado a la mocion
	
	public void move(final GMap map) {
		if (vm > 0.0001f) {
			if (!map.hit_block(hb.hx + vx, hb.hy + vy, hb.width, hb.height)) {
				hb.hx += vx; hb.hy += vy;
				hb.x = (int) hb.hx; hb.y = (int) hb.hy;
				vx -= fr*vx/vm;
				vy -= fr*vy/vm;
				if (vm > fr) vm -= fr; else resetMotion();
			} else {
				if (!map.hit_block(hb.hx, hb.hy + vy, hb.width, hb.height)) {
					if (Math.abs(vx) > 0.4f) {
						hb.hx = hb.hx + (vx > 0f ? 15.99f - (hb.hx+hb.width)%16 : 0.001f - (hb.hx)%16);
						hb.x = (int) hb.hx;
					}
					hb.hy += vy; hb.y = (int) hb.hy;
					vx = 0f; vm = Math.abs(vy);
					vy -= fr*vy/vm;
					if (vm > fr) vm -= fr; else resetMotion();
				} else if (!map.hit_block(hb.hx + vx, hb.hy, hb.width, hb.height)) {
					if (Math.abs(vy) > 0.4f) {
						hb.hy = hb.hy + (vy > 0f ? 15.99f - (hb.hy+hb.height)%16 : 0.001f - (hb.hy)%16);
						hb.y = (int) hb.hy;
					}
					hb.hx += vx; hb.x = (int) hb.hx;
					vy = 0f; vm = Math.abs(vx);
					vx -= fr*vx/vm;
					if (vm > fr) vm -= fr; else resetMotion();
				} else resetMotion();
			}
		}
	}

	protected void resetMotion() { vx = 0; vy = 0; vm = 0; }
	
	public void addMotion(final float force_x, final float force_y) {
		if (Float.isNaN(vx)) vx = 0; if (Float.isNaN(vy)) vy = 0; // lo dejare aqui como precausion general
		vx += force_x; vy += force_y;
		vm = (float) Math.hypot(vx, vy);
	}	
	
	public void addAccel(final double dir, final float F_max, float F_temp) {
		F_temp += fr;
		
		final float fx = (float) Math.cos(dir)*F_temp, fy = (float) Math.sin(dir)*F_temp;
		final float fxm = (float) Math.cos(dir)*F_max, fym = (float) Math.sin(dir)*F_max;
		final int xi = fxm > 0 ? 1 : -1, yi = fym > 0 ? 1 : -1;
		
		addMotion(xi*(fxm-vx-fx) > 0 ? fx : (xi*(fxm-vx) > 0 ? fxm-vx : 0),
				  yi*(fym-vy-fy) > 0 ? fy : (yi*(fym-vy) > 0 ? fym-vy : 0));
	}
	
	protected int imgX() { return (int) hb.x - dx; }
	protected int imgY() { return (int) hb.y - dy; }
	public void makeDamage(final float dam) { LP -= dam; }
	public final float getLP() { return LP; }
	
	// Relacionado a las coliciones entre objetos
	
	public boolean collide(final Entity ent) {
		return hb.collide(ent.hb);
	}

	protected void natural_repultion(final Entity ent) {
		if (collide(ent)) { // esto funciona porque las histbox no colicionan con ellas mismas
			final float m = 1f * ent.peso / (peso + ent.peso); // m = motion, module
			final double rot = orient(ent, this); // rot = rotation
			
			addMotion((float) Math.cos(rot)*m, (float) Math.sin(rot)*m); // Solo es necesario 1, porque se analizan ambos a la larga.
		}
	}
	
	public static double orient(final Entity main, final Entity target) {
		final int sx = (int) Math.round(target.hb.getCenterX() - main.hb.getCenterX());
		final int sy = (int) Math.round(target.hb.getCenterY() - main.hb.getCenterY());
		
		final double ret = Math.acos(sx / Math.hypot(sx, sy));
		
		return (Double.isNaN(ret) ? 0 : ret) * (sy > 0 ? 1 : -1);
	}
}
