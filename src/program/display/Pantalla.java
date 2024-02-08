package program.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;

import program.controles.Mando;
import program.controles.Raton;
import program.GestorCentral;

public class Pantalla extends Canvas {
	private static final long serialVersionUID = 2L;

	private final int ancho, alto;

	public Mando teclado;
	public Raton rat;

	public static Font fuente;

	public Pantalla(final int ancho, final int alto) {
		this.ancho = ancho;
		this.alto = alto;

		final int[] fim = { KeyEvent.VK_S, KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_A,
				KeyEvent.VK_SPACE, KeyEvent.VK_E, KeyEvent.VK_ESCAPE, KeyEvent.VK_F3, /*[borrar] ->*/KeyEvent.VK_H };
		teclado = new Mando(fim);
		rat = new Raton();

		fuente = cargarFuente("/other/Pixel.ttf", 8F);

		setIgnoreRepaint(true);
		setPreferredSize(new Dimension(ancho, alto));
		addKeyListener(teclado);
		addMouseListener(rat);
		addMouseWheelListener(rat);
		setFocusable(true);
		requestFocus();
	}

	// public void reChan(final byte tam) {
	// setPreferredSize(new Dimension(Punto.pac * tam, Punto.pac * tam));
	// ancho = Punto.pac * tam;
	// alto = Punto.pal * tam;
	// }

	public void dibujar(final GestorCentral gc) {
		BufferStrategy buffer = getBufferStrategy();

		if (buffer == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) buffer.getDrawGraphics();

		g.setFont(fuente);
		g.setColor(Color.black);
		g.fillRect(0, 0, ancho, alto);

		g.scale(2, 2);
		// g.scale(0.5, 0.5);

		teclado.actualizar();
		rat.actualizar(this);
		gc.usar(g);

		Toolkit.getDefaultToolkit().sync();

		g.dispose();
		buffer.show();
	}

	private Font cargarFuente(final String ruta, final float size) {
		Font fuente = null;

		InputStream enByte = this.getClass().getResourceAsStream(ruta);

		try {
			fuente = Font.createFont(Font.TRUETYPE_FONT, enByte);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		// fuente = fuente.deriveFont(tam);

		return fuente.deriveFont(size);
	}
}
