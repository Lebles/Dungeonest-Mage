package program.galaxias.toybox;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import program.display.Loader;

public class GMap {
	private final int[][] map;
	private final BufferedImage[] tiles;
	
	public GMap(final String mapa) {
		String[] smap = Loader.read(mapa, true).split("\n");
		
		final ArrayList<BufferedImage> ttt = new ArrayList<BufferedImage>();
		int i = 0; // variable que cuenta la cantidad de tiles que carga el mapa
		while(!smap[i].contentEquals("@")) {
			final String[] img = smap[i].split(",");
			ttt.add(Loader.upCIO(img[2])
					.getSubimage(Integer.parseInt(img[0])*16, Integer.parseInt(img[1])*16, 16, 16));
			
			i++;
		}
		tiles = ttt.toArray(new BufferedImage[0]);

		i++;
		final int[][] mmap = new int[smap.length-i][]; // Buffer para cargar el mapa rotado
		
		// Cargando mapa en buffer
		for(int y = 0; y < mmap.length; y++) {
			final String[] number = smap[y+i].split(",");
			mmap[y] = new int[number.length];
			
			for(int x = 0; x < number.length; x++) {
				mmap[y][x] = Integer.parseInt(number[x]);
			}
		}
		
		// Corregir orientacion del mapa
		map = new int[mmap[0].length][mmap.length];
		for(int y = 0; y < mmap.length; y++)
			for(int x = 0; x < mmap[y].length; x++)
				map[x][y] = mmap[y][x];
	}
	
	public boolean hit_block(final float x, final float y, final int w, final int h) {
		final int mx1 = (int) Math.floor(x/16), mx2 = (int) Math.floor((x+w)/16),
				my1 = (int) Math.floor(y/16), my2 = (int) Math.floor((y+h)/16);
		
		if ((((mx1 > -1 && mx1 < map.length) && (mx2 < map.length))
				&& ((my1 > -1 && my1 < map[mx1].length) && (my2 < map[mx2].length)))
				&&
				((map[mx1][my1] == 0 && map[mx2][my1] == 0) && (map[mx1][my2] == 0 && map[mx2][my2] == 0))) return false;
		
		return true;
	}
	
	public void draw_map(final Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				g.drawImage(tiles[map[i][j]], i * 16, j * 16, null);
			}
		}
	}
	
	public int getFromMap(final int x, final int y) {
		return map[x][y];
	}
	
	public int[][] instansiateMap() {
		return new int[map.length][map[0].length];
	}
}
