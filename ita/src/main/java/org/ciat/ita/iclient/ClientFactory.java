package org.ciat.ita.iclient;

/**
 * 
 * abstract class to provide generalized methods to create objects of
 * communication interfaces, like RMI or TCP
 * 
 */
public abstract class ClientFactory {

	
	/**
	 * create an object that provides data to work
	 * @return IWorkManager
	 */
	public IWorkManager createWorkManager() {
		return null;
	}

	/**
	 * create an object that receives the worked data
	 * @return IResultManager
	 */
	public IResultManager createResultManager() {
		return null;
	}
}
