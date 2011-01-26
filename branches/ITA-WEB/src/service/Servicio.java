package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DataBaseManager;

public class Servicio extends HttpServlet {
	private static final long serialVersionUID = 7332571365625168689L;
	private String user = "jacamacho";
	private String pass = "123456";
	private String ip = "gisbif.ciat.cgiar.org";
	private String port = "3306";
	private String database = "iabin_sstn";
	private Connection conx;

	@Override
	public void init() throws ServletException {
		super.init();

		DataBaseManager.registerDriver();
		conx = DataBaseManager.openConnection(user, pass, ip, port, database);
		/*
		 * Aquí irá todo lo relacionado con la creación e inicialización de la
		 * conexión a la base de datos. Hay que tener en cuenta que la conexión
		 * se puede perder en cualquier momento por falta de uso de los usuarios
		 * (que nadie entre) y porque ya se cumplió el timeout del servidor de
		 * base de datos de mysql, por tal motivo hay que estar pendiente de
		 * ello y reiniciar la conexión en caso de que lo requiera.
		 */
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		boolean respuesta = false;

		String id = req.getParameter("id");

		respuesta = consulta(id); // se hace la consulta y se devuelven los
									// objetos
		if (respuesta)
			System.out.println("se realizo la consulta");

		resp.setContentType("text/html");
		// if(id != null && id.equals("10")) {
		PrintWriter out = resp.getWriter();
		out.print("Hola Mundo - " + id);
		out.flush();
		out.close();

		// }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	public boolean consulta(String id) {

		System.out.println("id : " + id);
		ResultSet rs = DataBaseManager.makeQuery(
				"select * from georeferenced_records where id=" + id, conx);
		

		// se supone que debe devolver el hashset de los objetos que se
		// necesitan
		return true;
	}

}
