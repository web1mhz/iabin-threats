package org.ciat.ita.proxy.local;

import org.ciat.ita.iclient.*;

public class ClientFactoryObject extends ClientFactory {

	public IWorkManager createWorkManager() {
		return new WorkManagerObject();
	}

	public IResultManager createResultManager() {
		return new ResultManagerObject();
	}

}
