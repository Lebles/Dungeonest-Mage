package program.galaxias;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import program.Galaxia;
import program.GestorCentral;
import program.Punto;
import program.display.Loader;
import program.galaxias.toybox.EntityList;
import program.galaxias.toybox.Entrada;
import program.galaxias.toybox.GMap;
import program.galaxias.toybox.PathFinder;
import program.galaxias.toybox.entitys.Mesa;
import program.galaxias.toybox.jugador.Jugador;

public class Game implements Galaxia {
	private final BufferedImage GUI;
	public final Jugador ply;
	
	private final GMap map;
	public final EntityList ent;
	private final Entrada gate;
	public final PathFinder IA; // esto sera la IA provicional de los enemigos
	
	private final int level;
	
	public Game() {
		GUI = Loader.upCIT("/texture/GUI.png");

		level = 0;
		ply = new Jugador(148, 64);
		map = new GMap("/level-0.txt");
		IA = null;
		ent = new EntityList(ply);
		gate = new Entrada(new int[][] {{10*16, 16, 1}, {14*16, 5*16, -1}});
		
		ent.createEntity(new Mesa(150, 80)); // [borrar]
	}
	
	public Game(final Game pre, final int nivel) {
		GUI = pre.GUI;
		ply = pre.ply; 
		ent = pre.ent;
		level = nivel;
		
		if (nivel == 1) {
			ply.resetPlayer(150, 150);
			map = new GMap("/map-prueva.txt");
			IA = new PathFinder(map, ply);
			ent.reset(ply);
			ent.initSome();
			gate = new Entrada(-160,-160, 0);
		} else if (nivel == -1) {
			ply.resetPlayer(18, 66);
			map = new GMap("/map-home.txt");
			IA = null;
			ent.reset(ply);
			gate = new Entrada(0, 4*16, 0);
		} else {
			if (pre.level == 1) ply.resetPlayer(10*16 + 2, 38);
			else if (pre.level == -1) ply.resetPlayer(13*16 + 2, 5*16 + 2);
			else ply.resetPlayer(10, 40);
			
			map = new GMap("/level-0.txt");
			IA = null;
			ent.reset(ply);
			gate = new Entrada(new int[][] {{10*16, 16, 1}, {14*16, 5*16, -1}});
		}
	}

	public void play(final Graphics2D g, final GestorCentral gc) {
		ply.action(gc, this);
		ent.performance(this);
		gate.gate(gc, this);
		
		if (IA != null) IA.findPath(ply, map);
		
		if (ply.getLife() < 0f) gc.gal = new Game(this, 0);
		
		/* De aqui para abajo son solo capas de dibujo */
		
		g.translate(-ply.hb.hx+Jugador.dis, -ply.hb.hy+Jugador.dis+2); // muevo el mundo al rededor del jugador
		
		map.draw_map(g); // Esto hace los bloques en el mapa
		
		gate.draw(g); // dibuja la puerta

		ent.drawEntitys(g); // esto dibuja las entidades en el mapa
		
		if (Punto.debMode && IA != null) IA.draw(g);
		
		g.translate(ply.hb.hx-Jugador.dis, ply.hb.hy-Jugador.dis-2); // vuelvo a mover el mundo (y me refiero a la perspectiva de
																	 // dibujo) para hacer la GUI
		ply.draw(g);
		
		GUI(g);
		ply.drawGUI(g, gc, this);
	}
	
	private void GUI(final Graphics2D g) {
		g.drawImage(GUI, 0, 0, null);  // dibujo la imagen de gui
		g.setColor(new Color(208, 176, 152));  // Cambio el color...
		g.fillRect(Punto.pal, 0, Punto.pac - Punto.pal, Punto.pal); // y luego hago un cuadrado del color de la gui para completarla
	}
	
	// funciones get
	public GMap getMap() { return map; }
}
