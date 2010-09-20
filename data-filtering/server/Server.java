package server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

import server.connection.rmi.ResultServerRMI;
import server.connection.rmi.WorkServerRMI;
import server.database.DataBaseManager;

public class Server {

	public static void main(String[] args) {
		ServerConfig.init();
		Server s = new Server();
		s.start();
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method starts all the communication protocols between server, client
	 * and the database.
	 */
	public void start() {
		initDataBase();
		initServerRMI();
		initServerTCP();
		System.out.println("# Ready to serve. To exit press 'q' and enter anytime");
		Scanner s=new Scanner(System.in);
		while(s.hasNext()){
			if(s.next().toLowerCase().contains("q")){
				p.destroy();
				System.exit(0);
			}
		}
		
	}

	private void initDataBase() {
		DataBaseManager.registerDriver();

	}

	private void initServerTCP() {
		// TODO iniciar el servidor TCP

	}
	private Process p=null;
	private void initServerRMI() {
		
		try {
			p = Runtime.getRuntime().exec("rmiregistry");
			
		} catch (IOException e1) {
			System.err
					.println("Problem running rmiregistry, "
							+ "please be sure that you installed the java jdk or reference java path on the PATH variable");
			if (p != null)
				p.destroy();
			System.exit(0);
		}

		try {
			LocateRegistry.createRegistry(ServerConfig.rmi_port);
		} catch (RemoteException e) {
			System.err
					.println("Problem running server on port "
							+ ServerConfig.rmi_port
							+ ", please change the port from server configuration file");

			System.exit(0);
		}
		try {
			WorkServerRMI wsrmi = new WorkServerRMI();
			ResultServerRMI rsrmi = new ResultServerRMI();
			try {
				Naming.rebind("WorkServerRMI", wsrmi);
				Naming.rebind("ResultServerRMI", rsrmi);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
