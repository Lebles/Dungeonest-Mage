package program.galaxias.toybox;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import program.Punto;
import program.display.Loader;
import program.galaxias.Game;
import program.galaxias.toybox.entitys.Dummy;
import program.galaxias.toybox.entitys.Skeleton;
import program.galaxias.toybox.entitys.Slime1;
import program.galaxias.toybox.items.Sword;
import program.galaxias.toybox.jugador.Jugador;

public class EntityList {
	// Soy conciente de que probablemente  tenga que cambiar este sistema de sprites,
	// pero prefiero hacerlo cuando tenga mas informacion.
	
	private final BufferedImage[] sprites;
	private final ArrayList<Entity> mob;
	
	public EntityList(final Jugador ply) {
		sprites = new BufferedImage[] {
				Loader.upCIT("/texture/Items/MedWep.png").getSubimage(0, 0, 16, 16),
				Loader.upCIT("/texture/Objects/Decor0.png").getSubimage(0, 11 * 16, 16, 16),
				Loader.upCIT("/texture/Items/Food.png").getSubimage(16, 5 * 16, 16, 16),
				Loader.upCIT("/texture/Characters/Slime1.png").getSubimage(8, 5, 16, 16),
				Loader.upCIT("/texture/Characters/Undead0.png").getSubimage(0, 16*2, 16, 16),		// 5
				Loader.upCIT("/texture/Objects/Decor0.png").getSubimage(6*16-1, 6*16, 17, 16)
		};
		
		mob = new ArrayList<Entity>(10);
		mob.add(ply);
	}
	
	public void performance(final Game gm) {
		for(int i = 0, size = mob.size(); i < size; i++) {
			final Entity ent = mob.get(i);
			
			// Para cuando se destruyen
			if (ent.LP <= 0) {
				ent.death(gm);
				mob.remove(i);
				size--; i--;
				continue;
			}
			
			// para las coliciones
			for(Entity ent2 : mob) {
				ent.natural_repultion(ent2);
			}
			
			// para todo lo demas
			ent.act(gm);
			ent.move(gm.getMap());
		}
	}
	
	public void drawEntitys(final Graphics2D g) {
		if (Punto.debMode) {
			for(final Entity ent : mob) {
				ent.draw(g, sprites);
				ent.debModeDraw(g);
			}
		} else for(final Entity ent : mob) {
			ent.draw(g, sprites);
		}
	}
	
	public void createEntity(final Entity ent) {
		mob.add(ent);
	}
	
	public Entity[] getEntitys() {
		return mob.toArray(new Entity[0]);
	}
	
	public Entity retire(final int i) {
		return mob.remove(i);
	}
	
	public BufferedImage getSprite(final int spr) { // [borrar] Es posible que si mejoro esta clase para que halla una clase
		return sprites[spr];						//          que dibuje los sprites que sea exclusiva, esto sea mas comun.
	}
	
	public void reset(final Jugador ply) { mob.clear(); mob.add(ply); }
	
	// [borrar]
	public void initSome() {
		mob.add(new Dummy(110, 150 + 64));
		mob.add(new Sword(110, 150-18));
		mob.add(new Skeleton(32, 150-18*2));
		mob.add(new Slime1(150, 32));
	}
}
