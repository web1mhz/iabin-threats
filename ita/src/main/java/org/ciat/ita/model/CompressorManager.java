package org.ciat.ita.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressorManager {

	/**
	 * decompress an object form an array of bytes
	 * @param bytes of the object compressed
	 * @return a object decompressed from the bytes 
	 * @throws IOException if an error occurs.
	 */
	public static Object toObject(byte[] bytes) throws IOException {
		try {
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(
					bytes));
			ObjectInputStream ois = new ObjectInputStream(gis);
			Object o;
			o = ois.readObject();
			gis.close();
			ois.close();
			return o;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * compress an object into an array of bytes
	 * @param object to compress
	 * @return an array of bytes of the object compressed
	 * @throws IOException if an error occurs.
	 */
	public static byte[] toZipArray(Object object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gos = new GZIPOutputStream(bos);
		ObjectOutputStream oos = new ObjectOutputStream(gos);
		oos.writeObject(object);
		oos.flush();
		gos.close();
		oos.close();
		//System.out.println("Tiempo: "+(System.currentTimeMillis()-antes));
		return bos.toByteArray();

	}
}
