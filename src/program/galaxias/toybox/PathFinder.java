package program.galaxias.toybox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import program.Punto;
import program.galaxias.Game;
import program.galaxias.toybox.jugador.Jugador;

public class PathFinder {
	/* Estoy pensando que, en un futuro, lo mejor seria crear un sistema de localizacion en 2 niveles: uno que funcione con
	 * nodos que reprecenten la interconexion de las habitaciones, y este, que opere a nivel de habitacion. Si esto se llega
	 * a hacer, entonces la variable 'path' deberia operar a nivel de habitacion... o al menos actualizarce solo a ese nivel */
	
	// Recordar que hay material para ampliar mas el tema en la parte de abajo
	
	private int[][] path; // registra el mapa de caminos
	
	private int count; // Para que se actualize cada tanto y no siempre; // Se que deberia usar otros factores para esto
	
	public PathFinder(final GMap gmap, final Jugador ply) {
		findPath(ply, gmap); count = Integer.MAX_VALUE;
	}
	
	public void findPath(final Jugador ply, final GMap map) {
		if (count < Punto.aco*4) { count++; return; }
		count = 0;
		
		final ArrayList<int[]> exam = new ArrayList<int[]>(20*20);
		path = map.instansiateMap();
		
		
		exam.add(new int[] {ply.hb.x/16, ply.hb.y/16, 1});
		path[exam.get(0)[0]][exam.get(0)[1]] = 1;
		
		for(int max = 0; max < 400 && !exam.isEmpty(); max++) {
			final int[] p = exam.remove(0);

			spread(p[0]+1, p[1], p[2], exam, map);
			spread(p[0]-1, p[1], p[2], exam, map);
			spread(p[0], p[1]+1, p[2], exam, map);
			spread(p[0], p[1]-1, p[2], exam, map);
		}
	}
	
	private void spread(final int x, final int y, int p, final ArrayList<int[]> exam, final GMap map) {
		if ((x < 0 || x >= path.length) || (y < 0 || y >= path[0].length)) return;
		if (map.getFromMap(x, y) != 0) { path[x][y] = 1000; return; } 
		
		p++;
		if (path[x][y] == 0 || path[x][y] > p) {
			exam.add(new int[] {x, y, p});
			path[x][y] = p;
		}
	}

	public boolean rayView(final Game gm, final Entity mob) {
		// Funcion: escanea en un rayo para ver si el jugador esta precente
		
		// # esto no necesariamente es lo mas optimizado posible
		
		final int px = gm.ply.hb.x/16, py = gm.ply.hb.y/16;

		float tx = px - mob.hb.x/16, ty = py - mob.hb.y/16;
		
		if (Math.abs(tx) > Math.abs(ty)) { ty = ty/Math.abs(tx); tx = tx > 0 ? 1 : -1; }
									else { tx = tx/Math.abs(ty); ty = ty > 0 ? 1 : -1; }
		
		float ex = mob.hb.x/16f + tx, ey = mob.hb.y/16f + ty;
		while(gm.getMap().getFromMap((int) ex, (int) ey) == 0) {
			if((int) ex == px && (int) ey == py) {
				return true;
			}
			
			ex += tx; ey += ty;
		}

		return false;
	}
	
	public double rigthRot(final Entity mob) {
		final int x1 = mob.hb.x/16, x2 = (mob.hb.x + mob.hb.width)/16;
		final int y1 = mob.hb.y/16, y2 = (mob.hb.y + mob.hb.height)/16;
		
//		if (GestorCentral.test.press()) {
//			System.out.println("testing...");
//		}
		
		if (x1 == x2 && y1 == y2) {
				 if (path[x1+1][y1] < path[x1][y1]) return 0;
			else if (path[x1][y1+1] < path[x1][y1]) return Math.PI/2;
			else if (path[x1-1][y1] < path[x1][y1]) return Math.PI;
			else if (path[x1][y1-1] < path[x1][y1]) return -Math.PI/2;
		} else if (x1 == x2) {
			if (path[x1][y2] < path[x1][y1]) return Math.PI/2;
			else return -Math.PI/2;
		} else if (y1 == y2) {
			if (path[x2][y1] < path[x1][y1]) return 0;
			else return Math.PI;
		} else {
			int p = path[x1][y1];
			double ret = -Math.PI*3/4;
			
			if (path[x2][y1] < p) { p = path[x2][y1]; ret = -Math.PI/4; }
			if (path[x1][y2] < p) { p = path[x1][y2]; ret = Math.PI*3/4; }
			if (path[x2][y2] < p) { p = path[x2][y2]; ret = Math.PI/4; }
				 
			return ret;
		}
		
		return 5;
	}
	
	public boolean ask(final Entity mob, int x, int y) {
		x = x/16; y = y/16;
		if ((x < 0 || x >= path.length) || (y < 0 || y >= path[0].length)) return false;
		
		if (path[x][y] < path[mob.hb.x/16][mob.hb.y/16]) return true;
		
		return false;
	}

	public void draw(final Graphics2D g) {
		g.setColor(Color.black);
		final int ref = g.getFontMetrics().getAscent();
		
		for(int x = 0; x < path.length; x++) {
			for(int y = 0; y < path[x].length; y++) {
				g.drawString(Integer.toString(path[x][y]), x*16, y*16 + ref);
			}
		}
	}
}

/*
There are several pathfinding algorithms that can be used for finding the best path through a gridmap, and which one is best for a
particular situation will depend on the specific requirements of the game. Some common options include:

+ Dijkstra's algorithm: This is a popular algorithm for finding the shortest path between two nodes in a graph. It is efficient and can
handle graphs with weights on the edges, making it well-suited for gridmaps where the cost of moving from one cell to another may
vary.

+ A* (A-star) algorithm: This is another popular algorithm that is often used for pathfinding in games. It is an extension of
Dijkstra's algorithm that uses heuristics to guide the search towards the goal, making it more efficient in many cases.

+ Breadth-first search (BFS): This is a simple algorithm that explores the graph by expanding outward from the starting point in a
breadth-first manner. It is guaranteed to find the shortest path in a graph with no weights, but it can be slower than other
algorithms when applied to graphs with weights.

+ Depth-first search (DFS): This is an algorithm that explores the graph by diving deep into the graph, rather than expanding outward.
It can be faster than BFS in some cases, but it may not always find the shortest path.

Ultimately, the best pathfinding algorithm for a particular game will depend on the specific requirements of the game, such as the
size and complexity of the gridmap, the need for real-time performance, and the relative importance of finding the shortest path
versus finding a path quickly.
*/
