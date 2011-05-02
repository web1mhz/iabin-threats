package org.ciat.ita.model;

import java.util.ArrayList;
import org.ciat.ita.service.InfoSpecieEcosystemServlet;
import java.util.Collection;
import java.util.Iterator;

public class InfoSpecieEcosystemModel {		
	
	private ArrayList<String> specieEcosystem = new ArrayList<String>();
	
	public void addEcosystem(String name){
		specieEcosystem.add(name);
	}
	
	public ArrayList<String> getEcosystems(){
		return specieEcosystem;
	}

	/** 
	 * @uml.property name="infoSpecieEcosystemServlet"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse="infoSpecieEcosystemModel:org.ciat.ita.service.InfoSpecieEcosystemServlet"
	 * @uml.association name="ecosystem"
	 */
	private Collection infoSpecieEcosystemServlet;

	/** 
	 * Getter of the property <tt>infoSpecieEcosystemServlet</tt>
	 * @return  Returns the infoSpecieEcosystemServlet.
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public Collection getInfoSpecieEcosystemServlet() {
		return infoSpecieEcosystemServlet;
	}

	/**
	 * Returns an iterator over the elements in this collection. 
	 * @return  an <tt>Iterator</tt> over the elements in this collection
	 * @see java.util.Collection#iterator()
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public Iterator infoSpecieEcosystemServletIterator() {
		return infoSpecieEcosystemServlet.iterator();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains no elements.
	 * @return  <tt>true</tt> if this collection contains no elements
	 * @see java.util.Collection#isEmpty()
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public boolean isInfoSpecieEcosystemServletEmpty() {
		return infoSpecieEcosystemServlet.isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this collection contains the specified element. 
	 * @param element  whose presence in this collection is to be tested.
	 * @see java.util.Collection#contains(Object)
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public boolean containsInfoSpecieEcosystemServlet(InfoSpecieEcosystemServlet infoSpecieEcosystemServlet) {
		return this.infoSpecieEcosystemServlet.contains(infoSpecieEcosystemServlet);
	}

	/**
	 * Returns <tt>true</tt> if this collection contains all of the elements in the specified collection.
	 * @param elements  collection to be checked for containment in this collection.
	 * @see java.util.Collection#containsAll(Collection)
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public boolean containsAllInfoSpecieEcosystemServlet(Collection infoSpecieEcosystemServlet) {
		return this.infoSpecieEcosystemServlet.containsAll(infoSpecieEcosystemServlet);
	}

	/**
	 * Returns the number of elements in this collection.
	 * @return  the number of elements in this collection
	 * @see java.util.Collection#size()
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public int infoSpecieEcosystemServletSize() {
		return infoSpecieEcosystemServlet.size();
	}

	/**
	 * Returns all elements of this collection in an array.
	 * @return  an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray()
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public InfoSpecieEcosystemServlet[] infoSpecieEcosystemServletToArray() {
		return (InfoSpecieEcosystemServlet[]) infoSpecieEcosystemServlet
				.toArray(new InfoSpecieEcosystemServlet[infoSpecieEcosystemServlet.size()]);
	}

	/**
	 * Returns an array containing all of the elements in this collection;  the runtime type of the returned array is that of the specified array.
	 * @param a  the array into which the elements of this collection are to be stored.
	 * @return  an array containing all of the elements in this collection
	 * @see java.util.Collection#toArray(Object[])
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public InfoSpecieEcosystemServlet[] infoSpecieEcosystemServletToArray(
			InfoSpecieEcosystemServlet[] infoSpecieEcosystemServlet) {
				return (InfoSpecieEcosystemServlet[]) this.infoSpecieEcosystemServlet.toArray(infoSpecieEcosystemServlet);
			}

	/**
	 * Ensures that this collection contains the specified element (optional operation). 
	 * @param element  whose presence in this collection is to be ensured.
	 * @see java.util.Collection#add(Object)
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public boolean addInfoSpecieEcosystemServlet(InfoSpecieEcosystemServlet infoSpecieEcosystemServlet) {
		return this.infoSpecieEcosystemServlet.add(infoSpecieEcosystemServlet);
	}

	/** 
	 * Setter of the property <tt>infoSpecieEcosystemServlet</tt>
	 * @param infoSpecieEcosystemServlet  The infoSpecieEcosystemServlet to set.
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public void setInfoSpecieEcosystemServlet(Collection infoSpecieEcosystemServlet) {
		this.infoSpecieEcosystemServlet = infoSpecieEcosystemServlet;
	}

	/**
	 * Removes a single instance of the specified element from this collection, if it is present (optional operation).
	 * @param element  to be removed from this collection, if present.
	 * @see java.util.Collection#add(Object)
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public boolean removeInfoSpecieEcosystemServlet(InfoSpecieEcosystemServlet infoSpecieEcosystemServlet) {
		return this.infoSpecieEcosystemServlet.remove(infoSpecieEcosystemServlet);
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * @see java.util.Collection#clear()
	 * @uml.property  name="infoSpecieEcosystemServlet"
	 */
	public void clearInfoSpecieEcosystemServlet() {
		this.infoSpecieEcosystemServlet.clear();
	}
	
}
