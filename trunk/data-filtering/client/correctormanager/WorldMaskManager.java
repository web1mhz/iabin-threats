package client.correctormanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import model.WorldMask;
import server.ServerConfig;
import server.database.DataBaseManager;
import client.ClientConfig;

public class WorldMaskManager {

	private static int DISTANCIA = 5; // Kilometers

	private static RandomAccessFile lector;
	private static int[][] mIzquierda;
	private static int[][] mDerecha;
	private static int[] distanciaProxima;
	public final static String E_NOT_IN_LAND = "O";
	public final static String E_NEAR_LAND = "NL";
	public final static String E_NOT_IN_MASK = "M";
	private File mask;
	/**
	 * @uml.property name="worldMask"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:model.WorldMask"
	 */
	private WorldMask worldMask;
	private static int[][] matriz;
	private static int[][] matrizOtra;
	private static int altoMatrizCeros, anchoMatrizDerecha;
	private static int x, y = 0;
	private static byte[] arrayMask;

	private static int ncols;
	private static int nrows;
	private static double xllcorner;
	private static double yllcorner;
	private static double cellsize;
	private static int nodata_value;

	private long nBits;

	/**
	 * This constructor loads a world mask in the RAM memory.
	 * 
	 * @param maskPath
	 *            - the exactly location of the world mask file. (example:
	 *            c:/worldmask.jgm or /home/user/worldmask.jgm).
	 */
	public WorldMaskManager(String maskPath) {
		this.mask = new File(maskPath);
		this.cargarMascara();
	}

	private void cargarMascara() {
		try {
			ObjectInputStream reader = new ObjectInputStream(
					new GZIPInputStream(new FileInputStream(mask)));

			worldMask = new WorldMask((int) reader.readDouble(), (int) reader
					.readDouble(), reader.readDouble(), reader.readDouble(),
					reader.readDouble(), (int) reader.readDouble(),
					(byte[]) reader.readObject(), (byte[]) reader.readObject());
			reader.close();

		} catch (FileNotFoundException e) {
			// System.out.println("The mask file did not found:\n"+mask.);
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @return the WorldMask object.
	 */
	public WorldMask getWorldMask() {
		return worldMask;
	}

	private static RandomAccessFile abrirFlujoLectura(File mask) {
		try {
			lector = new RandomAccessFile(mask, "r");
			return lector;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private boolean cerrarFlujoLectura() {
		try {
			lector.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * This method is considerate a service for this class. Normally, the mask
	 * file was designed in an ASCII format which occupies about 1GB in space
	 * memory. Therefore, we designed a new format that would allow to reduce
	 * the file size without losing information.
	 * 
	 * This method is responsible for creating a compressed mask file (Java Geo
	 * Mask - JGM)... In that file, there are two masks. The original, and the
	 * mask that shows the nearby land.
	 * 
	 * @param maskASC
	 *            - The exactly location of the ASCII mask file.
	 * @param kilometers
	 *            - Radius distance considered as a land (NEAR LAND) int the
	 *            ocean.
	 * @return true if the convertion finished succesfuly, false otherwise.
	 */
	public static boolean convertMask(File maskASC, int kilometers) {
		ObjectOutputStream writer = null;
		BufferedReader reader = null;
		WorldMaskManager.DISTANCIA = kilometers;
		try {

			long antes = System.currentTimeMillis();

			System.out.println("Reading file: .../" + maskASC.getName());

			reader = new BufferedReader(new FileReader(maskASC));
			// abrirFlujoLectura(maskASC);

			// Saltando información que no interesa.
			double[] arreglo = new double[6];
			String linea;
			for (int cont = 0; cont < 6; cont++) {
				linea = reader.readLine();
				String[] partes = linea.split(" ");
				arreglo[cont] = Double.parseDouble(partes[partes.length - 1]);
			}
			ncols = (int) arreglo[0];
			nrows = (int) arreglo[1];
			xllcorner = arreglo[2];
			yllcorner = arreglo[3];
			cellsize = arreglo[4];
			nodata_value = (int) arreglo[5];

			String[] cols = null;
			arrayMask = new byte[((ncols * nrows) / 8) + 1];
			// StringBuilder byteString = new StringBuilder(8);
			// Cargando la mascara en memoria
			System.out.println("Loading mask file in memory....");

			short result = 0;

			int w = 1;
			for (int y = 0; y < nrows; y++) {
				cols = reader.readLine().split(" ");
				for (int x = 0; x < ncols; x++, w++) {
					result += ((cols[x].equals("1") ? 1 : 0) << ((w - 1) % 8));
					// byteString.append(cols[x]);
					if (w % 8 == 0) {
						// byteString.reverse();
						arrayMask[(w / 8) - 1] = (byte) (result + Byte.MIN_VALUE);
						// System.out.println(arrayMask[(w / 8) - 1]);
						// byteString.delete(0, 8);
						result = 0;
					}
				}
				if (y % 1000 == 0) {
					System.out
							.print(((int) (((double) y / (double) nrows) * 100))
									+ "% ");
				}
			}
			if ((nrows * ncols) % 8 != 0) {
				arrayMask[(w / 8)] = (byte) (result + Byte.MIN_VALUE);
				// System.out.println(arrayMask[(w / 8)]);
			}
			System.out.println("100%");

			reader.close();
			System.out.println("Generating new information...");

			// reader = new BufferedReader(new FileReader(maskASC));

			String isEarth;
			w = 1;
			// byteString.delete(0, 8);
			result = 0;
			byte[] finalArray = new byte[arrayMask.length];

			for (int y = 0; y < nrows; y++) {
				for (int x = 0; x < ncols; x++, w++) {

					isEarth = evaluateMaskFile(y, x);
					if (isEarth == null || isEarth.equals(E_NEAR_LAND)) {
						result += (1 << ((w - 1) % 8));
					}
					if (w % 8 == 0) {
						// byteString.reverse();
						finalArray[(w / 8) - 1] = (byte) (result + Byte.MIN_VALUE);
						// System.out.println(finalArray[(w / 8) - 1]);
						result = 0;
					}

				}
				if (y % 1000 == 0) {
					System.out
							.print(((int) (((double) y / (double) nrows) * 100))
									+ "% ");
				}
			}
			if ((nrows * ncols) % 8 != 0) {
				finalArray[(w / 8)] = (byte) (result + Byte.MIN_VALUE);
				// System.out.println(finalArray[(w / 8)]);
			}
			System.out.println("100%");

			// .JGM (Java Geo Mask)
			String nameFile = maskASC.getName().substring(0,
					maskASC.getName().length() - 4)
					+ ".jgm";

			System.out.println("Saving file: .../" + nameFile);

			writer = new ObjectOutputStream(new GZIPOutputStream(
					new FileOutputStream(maskASC.getParentFile()
							.getAbsolutePath()
							+ File.separator + nameFile)));

			// Guardando información de la mascara en orden:
			// ncols, nrows, xllcorner, yllcorner, cellsize, nodata_value
			// ArrayMask, FinalArray
			writer.writeDouble(ncols);
			writer.writeDouble(nrows);
			writer.writeDouble(xllcorner);
			writer.writeDouble(yllcorner);
			writer.writeDouble(cellsize);
			writer.writeDouble(nodata_value);
			writer.writeObject(finalArray);
			writer.writeObject(arrayMask);
			writer.flush();

			System.out.println("---OK!---");

			System.out.println("TERMINO: "
					+ (System.currentTimeMillis() - antes) + "ms");

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {

			// Cerrando flujo escritura
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return false;
	}

	/**
	 * This method extract and return the general information of mask file.
	 * 
	 * @param archivoMascara
	 * @return An array of doubles: [ncols, nrows, xllcorner, yllcorner,
	 *         cellsize, NODATA_value]
	 */
	@Deprecated
	public WorldMask datosMascara() {

		double[] arreglo = new double[6];
		WorldMask ws = null;
		try {

			// this.abrirFlujoLectura(archivoMascara);
			String linea;
			for (int cont = 0; cont < 6; cont++) {
				linea = lector.readLine();
				String[] partes = linea.split(" ");
				arreglo[cont] = Double.parseDouble(partes[partes.length - 1]);
			}
			// posicionando el apuntador del flujo al principio del archivo
			lector.seek(0);
			ws = new WorldMask((int) arreglo[0], (int) arreglo[1], arreglo[2],
					arreglo[3], arreglo[4], (int) arreglo[5]);

			// this.cerrarFlujoLectura();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return ws;
	}

	/*
	 * Se da por hecho que la distancia no sobrepasará el límite de la máscara.
	 * limiteX y limiteY corresponde a limite superior-izquierdo de la matriz.
	 */
	private static int[][] getMatriz(int limiteX, int limiteY, int distanciaX,
			int distanciaY) {

		matrizOtra = new int[distanciaX][distanciaY];

		long bitPosition = (limiteY * ncols) + limiteX;

		for (y = 0; y < distanciaY; y++) {
			for (x = 0; x < distanciaX; x++) {
				// Find bitPosition from the arrayMask.
				matrizOtra[x][y] = findBit(bitPosition);
				// Saltando al siguiente caracter.
				bitPosition++;
			}
			// Saltando a la siguiente linea.
			bitPosition = bitPosition - distanciaX + ncols;
		}

		return matrizOtra;
	}

	private static int findBit(long bitPosition) {
		return (((arrayMask[(int) (bitPosition / 8)] - Byte.MIN_VALUE) >> (bitPosition % 8)) & 1);
	}

	/**
	 * This method look into the .asc file the exactly coordenate indicate by
	 * the two parameters, and return if is earth or not.
	 * 
	 * @param xMin
	 *            - The corresponding 'x' coordinate (longitude) position you
	 *            want to search in mask file.
	 * @param yMin
	 *            - The corresponding 'y' coordinate (latitude) position you
	 *            want to search in mask file.
	 * 
	 * @return
	 */
	private static String evaluateMaskFile(int yMin, int xMin) {
		// xMin number of Column<
		// yMin number of Row

		// xMin = worldMask.num_column(lng);
		// yMin = worldMask.num_raw(lat);

		if (yMin < 0 || yMin >= nrows) {
			return E_NOT_IN_MASK;
		}
		xMin = xMin % ncols;
		// Empezar en esquina superior izquierda. (restar 5 columnas y 5
		// filas)
		matriz = generarMatrizRango(xMin, yMin, ncols, nrows);

		// if(yMin == 0 && xMin == 4) {
		// for (int y = 0; y < matriz[0].length; y++) {
		// for (int x = 0; x < matriz.length; x++) {
		// System.out.print(matriz[x][y] + " ");
		// }
		// System.out.println();
		// }
		// }

		if (matriz[DISTANCIA][DISTANCIA] == 1) {
			return null;
		} else {
			distanciaProxima = calcularDistanciaProxima(matriz);
			if (distanciaProxima != null) {
				return WorldMaskManager.E_NEAR_LAND;
			} else {
				return WorldMaskManager.E_NOT_IN_LAND;
			}
		}

	}

	/**
	 * This method evaluate a coordinate and say if it founds out of the mask,
	 * in land, near land or in the ocean.
	 * 
	 * @param lat
	 *            - Latitude
	 * @param lng
	 *            - Longitude
	 * @return - null if the coordinate is in land. E_NEAR_LAND if the
	 *         coordinate is in the ocean but near exits a land. E_NOT_IN_LAND
	 *         if the coordinate is in the ocean. And E_NOT_IN_MASK if the
	 *         coordinate is not in the mask.
	 */
	public String isEarth(double lat, double lng) {

		x = worldMask.num_column(lng);
		y = worldMask.num_raw(lat);
		// System.out.println(y);
		// System.out.println(worldMask.getNrows());
		// x = 17593; y= 765;
		if (y < 0 || y >= worldMask.getNrows()) {
			return E_NOT_IN_MASK;
		}

		nBits = (y * worldMask.getNcols()) + x;
		// System.out.println(ncols);

		// System.out.println("Y: "+y+" - X: "+x);
		// System.out.println("MASCARA CAMBIADA: "+worldMask.findBitInfoMask(nBits));
		// System.out.println("MASCARA ORIGINAL: "+worldMask.findBitASCMask(nBits));
		if (worldMask.findBitInfoMask(nBits) == 1) {
			if (worldMask.findBitASCMask(nBits) == 1) {
				return null;
			} else {
				return WorldMaskManager.E_NEAR_LAND;
			}
		} else {
			return WorldMaskManager.E_NOT_IN_LAND;
		}
		// return worldMask.findBitASCMask(nBits) + "";
	}

	/**
	 * This method search in the range of some kilometers, if there is a land.
	 * 
	 * @return [int, int] = [distance to north, distance to east] from center,
	 *         or null if there is no earth
	 */
	private static int[] calcularDistanciaProxima(int[][] matriz) {

		int centroX = 5;
		int centroY = 5;
		for (int distancia = 1; distancia <= DISTANCIA; distancia++) {
			// Recorriendo Arriba (hacia derecha)
			for (int x = (centroX - distancia), y = (centroY - distancia); x < (centroX + distancia); x++) {
				if (matriz[x][y] == 1) {
					return new int[] { (centroY - y), (x - centroX) };
				}
			}

			// Recorriendo Derecha (hacia abajo)
			for (int x = (centroX + distancia), y = (centroY - distancia); y < (centroY + distancia); y++) {
				if (matriz[x][y] == 1) {
					return new int[] { (centroY - y), (x - centroX) };
				}
			}

			// Recorriendo Abajo (hacia izquierda)
			for (int x = (centroX + distancia), y = (centroY + distancia); x > (centroX - distancia); x--) {
				if (matriz[x][y] == 1) {
					return new int[] { (centroY - y), (x - centroX) };
				}
			}

			// Recorriendo Izquierda (hacia arriba)
			for (int x = (centroX - distancia), y = (centroY + distancia); y > (centroY - distancia); y--) {
				if (matriz[x][y] == 1) {
					return new int[] { (centroY - y), (x - centroX) };
				}
			}
		}
		return null;
	}

	private static int[][] generarMatrizRango(int xMin, int yMin, int columnas,
			int filas) {

		matriz = new int[(DISTANCIA * 2) + 1][(DISTANCIA * 2) + 1];
		if ((xMin - DISTANCIA) < 0) {
			// El punto esta muy al OESTE (izquierda)
			// Se harán dos matrices. Se concatena siempre
			// [mDerecha]+[mIzquierda]
			if ((yMin - DISTANCIA) < 0) {
				// El punto está muy al oeste y NORTE (arriba) [CASO 1A]
				// Se crean ambas matrices con 0s arriba
				// con la información deseada.

				// Creando matrices mIzquierda y mDrecha, y extrayendo
				// información desde el archivo.
				mIzquierda = getMatriz(0, 0, (DISTANCIA + 1) + xMin,
						(DISTANCIA + 1) + yMin);

				mDerecha = getMatriz(columnas - (DISTANCIA - xMin), 0,
						(DISTANCIA - xMin), (DISTANCIA + 1 + yMin));

				altoMatrizCeros = DISTANCIA - yMin;
				anchoMatrizDerecha = DISTANCIA - xMin;
				// Llenando matriz
				for (y = 0; y < matriz[0].length; y++) {
					// Llenando los 0s de arriba
					if (y < altoMatrizCeros) {
						for (x = 0; x < matriz.length; x++) {
							matriz[x][y] = 0;
						}
					} else {
						// Concatenando con [mDerecha] y [mIzquierda]
						for (x = 0; x < matriz.length; x++) {
							if (x < mDerecha.length) {
								matriz[x][y] = mDerecha[x][y - altoMatrizCeros];
							} else {
								matriz[x][y] = mIzquierda[x
										- anchoMatrizDerecha][y
										- altoMatrizCeros];
							}
						}
					}
				}
			} else {
				if ((yMin + DISTANCIA) >= filas) {
					// El punto está muy al oeste y SUR (abajo) [CASO
					// 2A]
					// Se crean ambas matrices con 0s abajo

					// Creando matrices mIzquierda y mDrecha, y extrayendo
					// información desde el archivo.

					mIzquierda = getMatriz(0, (yMin - DISTANCIA),
							(DISTANCIA + 1) + xMin, (DISTANCIA + 1)
									+ (filas - 1 - yMin));
					mDerecha = getMatriz(columnas - (DISTANCIA - xMin), yMin
							- DISTANCIA, (DISTANCIA - xMin), (DISTANCIA + 1)
							+ (filas - 1 - yMin));

					altoMatrizCeros = DISTANCIA - (filas - yMin - 1);
					anchoMatrizDerecha = DISTANCIA - xMin;

					// Llenando matriz.
					for (int y = 0; y < matriz[0].length; y++) {
						// Llenando los 0s de arriba
						if (y >= (matriz[0].length - altoMatrizCeros)) {
							for (int x = 0; x < matriz.length; x++) {
								matriz[x][y] = 0;
							}
						} else {
							// Concatenando con [mDerecha] y [mIzquierda]
							for (int x = 0; x < matriz.length; x++) {
								if (x < mDerecha.length) {
									matriz[x][y] = mDerecha[x][y];
								} else {
									matriz[x][y] = mIzquierda[x
											- anchoMatrizDerecha][y];
								}
							}
						}
					}

				} else {
					// El punto esta muy al oeste y normal ENTRE SUR Y
					// NORTE [CASO 3A]
					// Se crean ambas matrices

					// Creando matrices mIzquierda y mDrecha, y extrayendo
					// información desde el archivo.
					mIzquierda = getMatriz(0, (yMin - DISTANCIA),
							(DISTANCIA + 1) + xMin, (DISTANCIA * 2) + 1);

					mDerecha = getMatriz(columnas - (DISTANCIA - xMin), yMin
							- DISTANCIA, (DISTANCIA - xMin),
							(DISTANCIA * 2) + 1);

					anchoMatrizDerecha = DISTANCIA - xMin;

					// Llenando matriz.
					for (int y = 0; y < matriz[0].length; y++) {
						// No hay 0s para llenar
						// Concatenando con [mDerecha] y [mIzquierda]
						for (int x = 0; x < matriz.length; x++) {
							if (x < mDerecha.length) {
								matriz[x][y] = mDerecha[x][y];
							} else {
								matriz[x][y] = mIzquierda[x
										- anchoMatrizDerecha][y];
							}
						}
					}
				}
			}
		} else {
			if ((xMin + DISTANCIA) > columnas) {
				// El punto esta muy al ESTE (derecha)
				// Se harán dos matrices. Se concatena siempre
				// [mDerecha]+[mIzauierda]
				if ((yMin - DISTANCIA) < 0) {
					// El punto esta muy al este y NORTE (arriba) [CASO
					// 1B]
					// Se crean ambas matrices con 0s arriba

					// Creando matrices mIzquierda y mDrecha, y extrayendo
					// información desde el archivo.
					altoMatrizCeros = DISTANCIA - yMin;
					anchoMatrizDerecha = (DISTANCIA + 1)
							+ (columnas - xMin - 1);
					mIzquierda = getMatriz(0, 0, (DISTANCIA + 1)
							+ (DISTANCIA - (columnas - xMin - 1)),
							(DISTANCIA + 1) + yMin);

					mDerecha = getMatriz(xMin - DISTANCIA, 0,
							anchoMatrizDerecha, (DISTANCIA + 1) + yMin);

					// Llenando matriz
					for (int y = 0; y < matriz[0].length; y++) {
						// Llenando los 0s de arriba
						if (y < altoMatrizCeros) {
							for (int x = 0; x < matriz.length; x++) {
								matriz[x][y] = 0;
							}
						} else {
							// Concatenando con [mDerecha] y [mIzquierda]
							for (int x = 0; x < matriz.length; x++) {
								if (x < mDerecha.length) {
									matriz[x][y] = mDerecha[x][y
											- altoMatrizCeros];
								} else {
									matriz[x][y] = mIzquierda[x
											- anchoMatrizDerecha][y
											- altoMatrizCeros];
								}
							}
						}
					}
				} else {
					if ((yMin + DISTANCIA) >= filas) {
						// El punto esta muy al este y SUR (abajo) [CASO
						// 2B]
						// Se crean ambas matrices con 0s abajo

						// Creando matrices mIzquierda y mDrecha, y
						// extrayendo información desde el archivo.
						altoMatrizCeros = DISTANCIA - (filas - yMin - 1);
						anchoMatrizDerecha = (DISTANCIA + 1)
								+ (columnas - xMin - 1);

						mIzquierda = getMatriz(0, yMin - DISTANCIA,
								(DISTANCIA * 2) + 1 - anchoMatrizDerecha,
								(DISTANCIA + 1) + (DISTANCIA - altoMatrizCeros));

						mDerecha = getMatriz(xMin - DISTANCIA,
								yMin - DISTANCIA, anchoMatrizDerecha,
								(DISTANCIA + 1) + (filas - 1 - yMin));

						// Llenando matriz.
						for (int y = 0; y < matriz[0].length; y++) {
							// Llenando los 0s de arriba
							if (y >= (matriz[0].length - altoMatrizCeros)) {
								for (int x = 0; x < matriz.length; x++) {
									matriz[x][y] = 0;
								}
							} else {
								// Concatenando con [mDerecha] y
								// [mIzquierda]
								for (int x = 0; x < matriz.length; x++) {
									if (x < mDerecha.length) {
										matriz[x][y] = mDerecha[x][y];
									} else {
										matriz[x][y] = mIzquierda[x
												- anchoMatrizDerecha][y];
									}
								}
							}
						}
					} else {
						// El punto esta muy al este y normal ENTRE SUR
						// Y NORTE [CASO 3B]
						// Se crean ambas matrices.

						// Creando matrices mIzquierda y mDrecha, y
						// extrayendo
						// información desde el archivo.
						anchoMatrizDerecha = (DISTANCIA + 1)
								+ (columnas - xMin - 1);

						mIzquierda = getMatriz(0, (yMin - DISTANCIA),
								(DISTANCIA * 2) + 1 - anchoMatrizDerecha,
								(DISTANCIA * 2) + 1);

						mDerecha = getMatriz(columnas - anchoMatrizDerecha,
								yMin - DISTANCIA, anchoMatrizDerecha,
								(DISTANCIA * 2) + 1);

						// Llenando matriz.
						for (int y = 0; y < matriz[0].length; y++) {
							// No hay 0s para llenar
							// Concatenando con [mDerecha] y [mIzquierda]
							for (int x = 0; x < matriz.length; x++) {
								if (x < mDerecha.length) {
									matriz[x][y] = mDerecha[x][y];
								} else {
									matriz[x][y] = mIzquierda[x
											- anchoMatrizDerecha][y];
								}
							}
						}
					}
				}
			} else {
				if ((yMin - DISTANCIA) < 0) {
					// El punto esta normal ENTRE ESTE Y OESTE, pero
					// esta muy al NORTE [CASO 1C]
					// Se crea una matriz con 0s arriba

					// Creando matriz mIzquierda que en realidad es mArriba y
					// extrayendo información desde el archivo.

					mIzquierda = getMatriz(xMin - DISTANCIA, 0,
							(DISTANCIA * 2) + 1, (DISTANCIA + 1) + yMin);

					altoMatrizCeros = DISTANCIA - yMin;

					// Llenando matriz.
					for (int y = 0; y < matriz[0].length; y++) {
						// Llenando los 0s de arriba
						if (y < altoMatrizCeros) {
							for (int x = 0; x < matriz.length; x++) {
								matriz[x][y] = 0;
							}
						} else {
							// Llenando la informacion de mIzquierda
							for (int x = 0; x < matriz.length; x++) {
								matriz[x][y] = mIzquierda[x][y
										- altoMatrizCeros];
							}
						}
					}

				} else {
					if ((yMin + DISTANCIA) >= filas) {
						// El punto esta normal ENTRE ESTE Y OESTE. pero
						// esta muy al SUR [CASO 2C]
						// Se crea una matriz con 0s abajo

						// Creando matriz mIzquierda que en realidad es mAbajo y
						// extrayendo información desde el archivo.
						altoMatrizCeros = DISTANCIA - (filas - yMin - 1);

						mIzquierda = getMatriz(xMin - DISTANCIA, yMin
								- DISTANCIA, (DISTANCIA * 2) + 1,
								(filas - yMin) + DISTANCIA);

						// Llenando matriz.
						for (int y = 0; y < matriz[0].length; y++) {
							// Llenando los 0s de arriba
							if (y >= matriz[0].length - altoMatrizCeros) {
								for (int x = 0; x < matriz.length; x++) {
									matriz[x][y] = 0;
								}
							} else {
								// Llenando la informacion de mIzquierda
								for (int x = 0; x < matriz.length; x++) {
									matriz[x][y] = mIzquierda[x][y];
								}
							}
						}

					} else {
						// El punto esta normal.
						matriz = getMatriz(xMin - DISTANCIA, yMin - DISTANCIA,
								(DISTANCIA * 2) + 1, (DISTANCIA * 2) + 1);
					}
				}
			}
		}

		return matriz;
	}

	@SuppressWarnings("static-access")
	@Deprecated
	public int subMask2(int xMin, int yMin, File archivo) {
		try {
			// double[] datosMascara = this.datosMascara(archivo);
			// int columnas = (int) datosMascara[0];
			// int filas = (int) datosMascara[1];
			this.abrirFlujoLectura(archivo);
			if (lector != null) {
				String linea;
				for (int cont = 0; cont < yMin + 6; cont++) {
					lector.readLine();
				}
				linea = lector.readLine();
				String[] arreglo = linea.split(" ");
				linea = arreglo[xMin];
				// this.cerrarFlujoLectura();

				// posicionando el apuntador del flujo al principio del archivo
				lector.seek(0);
				return Integer.parseInt(linea);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	/**
	 * Do not use this method.... only for intern use.
	 * 
	 * En un PC con 2GB RAM debe durar alrededor de 72 minutos.
	 * 
	 * Este método se encarga de llenar cada uno de los datos correspondiente a
	 * la columna is_land encargada de reconocer si dicho registro se encuentra
	 * o no en la tierra.
	 * 
	 * @param tableName
	 */
	private static void evaluateLand_DirectSQL(String tableName, int number) {
		try {
			// double LON = 166.83, LAT = 9.33;
			// double LON = 166.82, LAT = 9.33;

			WorldMaskManager wmm = new WorldMaskManager(ClientConfig.maskFile);
			// System.out.println(wmm.isEarth(LAT, LON));

			DataBaseManager.registerDriver();
			Connection con = DataBaseManager.openConnection(
					ServerConfig.getInstance().database_user, ServerConfig.getInstance().database_password);
			int lastID = 0;
			boolean hasMoreRecords = true;
			int cont;
			long before;
			long timeTotal = 0;
			long temp;
			DecimalFormat format = new DecimalFormat("#########0.000");

			System.out.println("......... EVALUATING RECORDS ON LAND.......");

			while (hasMoreRecords) {
				before = System.currentTimeMillis();

				ResultSet res = DataBaseManager.makeQuery(
						"SELECT id, longitude, latitude FROM " + tableName
								+ " WHERE id > " + lastID
								+ " ORDER BY id LIMIT " + number, con);
				double lat, lng;
				String isLand;
				cont = 0;

				while (res.next()) {
					cont++;
					lng = res.getFloat(2);
					lat = res.getFloat(3);
					isLand = wmm.isEarth(lat, lng);
					lastID = res.getInt(1);
					if (isLand != null) {
						// 0 - MAR
						// 1 - TIERRA (Default)
						DataBaseManager.makeChange("UPDATE " + tableName
								+ " SET is_land = 0 WHERE id = " + lastID, con);
						// System.out.println("ID: "+lastID+" | lng: " + lng +
						// " | lat: " + lat + " | " + isLand);
					}
				}
				temp = System.currentTimeMillis() - before;
				timeTotal += temp;
				
				System.out.println(format.format((temp / 1000.0)) + "seg - TOTAL: "
						+ format.format((timeTotal / 60000.0)) + "min | LastID: " + lastID);
				if (cont < number)
					hasMoreRecords = false;

				res.getStatement().close();
				res.close();
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Do not use this method.... only for intern use.
	 * 
	 * En un PC con 2GB RAM debe durar alrededor de 186 min.
	 * 
	 * Calcula la cantidad de registros no terrestres por cada especia y evalúa
	 * si hace parte del 90% del total. En caso de ser así, dicha especie se
	 * considera como marítima y no se copia a la siguiente tabla.
	 * 
	 * @param tableName
	 * @param number
	 * @param newTableName
	 */
	private static void calculate_species(String tableName, int number,
			String newTableName) {
		try {
			DataBaseManager.registerDriver();
			Connection con = DataBaseManager.openConnection(ServerConfig.getInstance().database_user, ServerConfig.getInstance().database_password);

			boolean hasMoreRecords = true;
			int lastNubId = 0;
			int total_specie, ocean_specie;
			long before;
			long timeTotal = 0;
			long temp;
			int cont;
			number = number/100;
			if(number == 0) number = 1;
			DecimalFormat format = new DecimalFormat("##########0.000");
			System.out.println("......... CALCULATING SPECIES ON LAND.......");
			while (hasMoreRecords) {
				cont = 0;
				before = System.currentTimeMillis();
				ResultSet res = DataBaseManager.makeQuery(
						"SELECT DISTINCT nub_concept_id FROM " + tableName
								+ " WHERE nub_concept_id > " + lastNubId
								+ " ORDER BY nub_concept_id LIMIT " + number,
						con);
				while (res.next()) {
					cont++;
					lastNubId = res.getInt(1);
					ResultSet r = DataBaseManager.makeQuery(
							"SELECT total, no_land from	(SELECT COUNT(id) total FROM "
									+ tableName
									+ " l where l.nub_concept_id = "
									+ lastNubId
									+ ") a, (SELECT COUNT(id) no_land FROM "
									+ tableName
									+ " l WHERE l.nub_concept_id = "
									+ lastNubId + " AND l.is_land = 0) b", con);
					r.next();
					total_specie = r.getInt(1);
					ocean_specie = r.getInt(2);
					r.getStatement().close();
					r.close();
					if (!((((double) ocean_specie) / ((double) total_specie)) >= 0.9)) {
						DataBaseManager
								.makeChange(
										"INSERT INTO "
												+ newTableName
												+ " SELECT id, nub_concept_id, longitude, latitude, iso_country_code FROM "
												+ tableName
												+ " WHERE nub_concept_id = "
												+ lastNubId, con);
					}
				}
				temp = System.currentTimeMillis() - before;
				timeTotal += temp;
				System.out.println(format.format((temp / 1000.0)) + "seg - TOTAL: "
						+ format.format((timeTotal / 60000.0)) + "min | LastNubConceptID: "
						+ lastNubId);
				if (cont < number)
					hasMoreRecords = false;

				res.getStatement().close();
				res.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		if (args == null || args.length == 0) {
			System.out.println("[ARGUMENTS]");
			System.out.println();
			System.out.println("  <terreval>");
			System.out
					.println("          [-n]  <Number of records that should be analized to show the time statistics>");
			System.out.println();
			System.out.println("  <jgm>");
			System.out
					.println("          [-m] <The exactly location of the ASCII mask file>");
			System.out
					.println("          [-k] <Radius distance considered as a land (NEAR LAND) in the ocean>");

		} else {

			if (args[0].equalsIgnoreCase("terreval")) {
				if (args.length == 3) {
					if (args[1].equals("-n")) {
						try {
							ClientConfig.init();
							int number = Integer.parseInt(args[2]);
							evaluateLand_DirectSQL("temp_land_4A", number);
							calculate_species("temp_land_4A", number, "temp_land_5A");
						} catch (NumberFormatException e) {
							System.out
									.println("The last parameter should be a number.");
						}
					} else {
						System.out.println("Wrong Parameter!");
					}
				} else {
					System.out.println("Insuficient Argumment!");
				}
			} else {
				if (args[0].equalsIgnoreCase("jgm")) {
					if (args.length == 5) {
						// @return true if the convertion finished succesfuly,
						// false otherwise.
						String maskFile = "";
						String kilometers = "";
						for (int c = 0; c < args.length; c++) {
							if (args[c].equalsIgnoreCase("-m")) {
								maskFile = args[c + 1];
							}
							if (args[c].equalsIgnoreCase("-k")) {
								kilometers = args[c + 1];
							}
						}
						boolean ok = WorldMaskManager.convertMask(new File(
								maskFile), Integer.parseInt(kilometers));
						if (ok) {
							System.out.println("Succes convertion terminated");
						}
					} else {
						System.out.println("Invalid Command!");
					}

				} else {
					System.out.println("Invalid Command!");
				}
			}
		}

		// double LON = 166.82, LAT = 9.33;
		// double LON = 166.83, LAT = 9.33;
	}
}
