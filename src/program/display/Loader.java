package program.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Loader {
	public static BufferedImage upCIO(final String ruta) {
		Image imag = null;

		try {
			imag = ImageIO.read(Loader.class.getResource(ruta));
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		BufferedImage upImage = gc.createCompatibleImage(imag.getWidth(null), imag.getHeight(null), Transparency.OPAQUE);

		Graphics g = upImage.getGraphics();
		g.drawImage(imag, 0, 0, null);
		g.dispose();

		return upImage;
	}

	public static BufferedImage upCIT(final String ruta) {
		Image imag = null;

		try {
			imag = ImageIO.read(Loader.class.getResource(ruta));
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

		BufferedImage upImage = gc.createCompatibleImage(imag.getWidth(null), imag.getHeight(null), Transparency.TRANSLUCENT);

		Graphics g = upImage.getGraphics();
		g.drawImage(imag, 0, 0, null);
		g.dispose();

		return upImage;
	}

	public static String read(final String ruta, boolean enter_split) {
		String dato = "";

		InputStream entradaBytes = Loader.class.getResourceAsStream(ruta);
		if (entradaBytes != null) {
			BufferedReader lector = new BufferedReader(new InputStreamReader(entradaBytes));

			String linea;

			try {
				if (enter_split) {
					while ((linea = lector.readLine()) != null) {
						dato += linea + '\n';
					}
				} else {
					while ((linea = lector.readLine()) != null) {
						dato += linea;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (entradaBytes != null) {
						entradaBytes.close();
					}
					if (lector != null) {
						lector.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		return dato;
	}

	public static String read_part(final String ruta, int part) {
		String dato = "";

		InputStream entradaBytes = Loader.class.getResourceAsStream(ruta);
		if (entradaBytes != null) {
			BufferedReader lector = new BufferedReader(new InputStreamReader(entradaBytes));

			String linea;

			try {
				while (part > 0) {
					if (lector.readLine().startsWith("@")) {
						part--;
					}
				}
				while ((linea = lector.readLine()) != null && linea.startsWith("@")) {
					dato += linea;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (entradaBytes != null) {
						entradaBytes.close();
					}
					if (lector != null) {
						lector.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		return dato;
	}

	public static void write(final String ruta, final String dato) {
		try {
			final File fl = new File(ruta);
			final FileWriter fw = new FileWriter(fl);
			// si pones FileWriter(fl, true), no sobreescribe los datos
			final BufferedWriter bw = new BufferedWriter(fw);

			bw.write(dato);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Clip LAF(final String ruta) {
		Clip clip = null;

		try {
			InputStream is = Loader.class.getResourceAsStream(ruta);
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(ais);

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clip;
	}
	
	
	// Para propositos de manipulacion del sistema
	public static BufferedImage change_color(BufferedImage input, final Color dye) {
		input = deepCopy(input);
		final Graphics2D g = input.createGraphics();
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(dye);
        g.fillRect(0, 0, input.getWidth(), input.getHeight());
		g.dispose();
		
		return input;
	}
	private static BufferedImage deepCopy(final BufferedImage bi) {
		 final ColorModel cm = bi.getColorModel();
		 final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 final WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
