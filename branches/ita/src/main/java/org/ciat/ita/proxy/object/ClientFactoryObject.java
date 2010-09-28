package org.ciat.ita.proxy.object;

import org.ciat.ita.iclientmanager.*;

public class ClientFactoryObject extends ClientFactory {

	public IWorkManager createWorkManager() {
		return new WorkManagerObject();
	}

	public IResultManager createResultManager() {
		return new ResultManagerObject();
	}

}
