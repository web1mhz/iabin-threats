package proxy.object;

import iclientmanager.*;

public class ClientFactoryObject extends ClientFactory {

	public IWorkManager createWorkManager() {
		return new WorkManagerObject();
	}

	public IResultManager createResultManager() {
		return new ResultManagerObject();
	}

}
