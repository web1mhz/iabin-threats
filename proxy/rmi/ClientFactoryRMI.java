package proxy.rmi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import iclientmanager.*;

public class ClientFactoryRMI extends ClientFactory {

	public ClientFactoryRMI() {
		init();
	}

	private boolean init() {
		final String POLICY = "client.policy";
		File arch = new File(POLICY);
		/**
		 * if the policy file does not exist, create a new one by default
		 */
		if (!arch.exists()) {
			try {
				FileWriter file = new FileWriter(POLICY);
				file
						.write("grant \n"
								+ "{ \n"
								+ "permission java.net.SocketPermission \n"
								+ "\"*:0-65535\", \"accept,listen,connect,resolve\"; \n"
								+ "permission java.io.FilePermission  \n"
								+ "\"E://parasid/data/-\", \"read,write,execute,delete\"; \n"
								+ "permission java.io.FilePermission  \n"
								+ "\"Z://-\", \"read,write,execute\"; \n"
								+ "permission java.io.FilePermission  \n"
								+ "\"C://-\", \"read,write,execute\"; \n"
								+ " permission java.io.FilePermission \n"
								+ "\"<<ALL FILES>>\", \"execute\"; \n" + "};");
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.setProperty("java.security.policy", POLICY);
		System.setSecurityManager(new RMISecurityManager());
		return true;
	}

	public IWorkManager createWorkManager() {
		try {
			return new WorkManagerRMI();
		} catch (RemoteException e) {
			System.out.println();
		}
		return null;
	}

	public IResultManager createResultManager() {
		try {
			return new ResultManagerRMI();
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
