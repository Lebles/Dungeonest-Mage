package program.galaxias.toybox.menus.magia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import java.lang.reflect.Field;

import program.galaxias.Game;
import program.galaxias.toybox.Entity;

public abstract class Simbolo implements Iterable<Simbolo>, Cloneable {
	
	public final Simbolo[] sub;
	public final short sx, sy;
	
	public Simbolo(final int n, final int stamp_x, final int stamp_y) {
		sub = n == 0 ? null : new Simbolo[n];
		sx = (short) (stamp_x*32); sy = (short) (stamp_y*32);
	}
	
	public abstract void execute(final Game gm, final Entity executer);
	
	public int size() {
		if (sub != null) {
			int size = 1;
			for(int i = 0; i < sub.length; i++) {
				size += sub[i] != null ? sub[i].size() : 1;
			}
			return size;
		}
		return 1; // Deberia ser 0?
	}

	public boolean add(final Simbolo element) {
		if (sub != null) {
			for(int i = 0; i < sub.length; i++) {
				if (sub[i] == null) {
					sub[i] = element;
					return true;
				} else if (sub[i].add(element)) return true;
			}
		}
		
		return false;
	}

	// ======================== Object Intefaces: ==============================
	public Iterator<Simbolo> iterator() {
		final ArrayList<Simbolo> list = new ArrayList<Simbolo>(size()+1);
		list.add(this);
		
		for(int i = 0; i < list.size(); i++) {
			Simbolo s = list.get(i);
			if (s != null && s.sub != null) {
				list.addAll(i+1, Arrays.asList(s.sub));
			}
		}
		
		return list.iterator();
	}
	public Simbolo clone() {
        try {
        	final Simbolo r = (Simbolo) super.clone();
        	if (sub == null) return r;
        	
        	Field f = Simbolo.class.getDeclaredField("sub");
        	f.setAccessible(true); f.set(r, new Simbolo[sub.length]);
        	return r;
		} catch (CloneNotSupportedException | NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			// Esto teoricamente no se deberia ejecutar nunca
			e.printStackTrace(); System.exit(504);
			return null;
		}
    }
}
