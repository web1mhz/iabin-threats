/**
 * 
 */
package model;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author Tim Robertson
 */
public class NamedGeometry {
	private Geometry geometry;
	private Object name;
	public NamedGeometry(Geometry geometry, Object name) {
		this.geometry = geometry;
		this.name = name;
	}
	public Geometry getGeomtery() {
		return geometry;
	}
	public Object getName() {
		return name;
	}
	public String toString() {
		return "GEOM:" + name;
	}
}
