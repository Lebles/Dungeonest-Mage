package program.display;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class Serializer {
	public static byte[] toBytes(final Serializable o) {
		ByteArrayOutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(o);
			return os.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (os != null)
					os.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
				throw new RuntimeException(ex2);
			}
		}
	}

	public static Object toObject(final byte[] s) {
		ByteArrayInputStream is = null;
		ObjectInputStream ois = null;

		try {
			is = new ByteArrayInputStream(s);
			ois = new ObjectInputStream(is);
			return ois.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (is != null)
					is.close();
			} catch (Exception ex2) {
				ex2.printStackTrace();
				throw new RuntimeException(ex2);
			}
		}
	}

	public static void save(final String ruta, final Serializable ser) {
		try {
			final OutputStream ops = new FileOutputStream(ruta);
			ops.write(toBytes(ser));

			ops.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object load(final String ruta) {
		final InputStream ips = Serializer.class.getResourceAsStream(ruta);
		if (ips == null) {
			System.out.println("mal");
			return null;
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final byte[] bufer = new byte[1024];
		int len;

		try {
			while ((len = ips.read(bufer)) != -1) {
				baos.write(bufer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ips != null) {
					ips.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return toObject(baos.toByteArray());
	}
}
