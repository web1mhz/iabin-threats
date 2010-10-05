package org.ciat.ita.proxy.local;

import org.ciat.ita.iclient.*;

public class ClientFactoryLocal extends ClientFactory {

	public IWorkManager createWorkManager() {
		return new WorkManagerLocal();
	}

	public IResultManager createResultManager() {
		return new ResultManagerLocal();
	}

}
