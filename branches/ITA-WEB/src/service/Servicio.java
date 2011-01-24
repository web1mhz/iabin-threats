package service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servicio extends HttpServlet {
	private static final long serialVersionUID = 7332571365625168689L;

	@Override
	public void init() throws ServletException {
		super.init();
		/*
		 * Aquí irá todo lo relacionado con la creación e inicialización de la conexión a la base de datos.
		 * Hay que tener en cuenta que la conexión se puede perder en cualquier momento por
		 * falta de uso de los usuarios (que nadie entre) y porque ya se cumplió el timeout del servidor
		 * de base de datos de mysql, por tal motivo hay que estar pendiente de ello y
		 * reiniciar la conexión en caso de que lo requiera.
		 */
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		String id = req.getParameter("id");
		resp.setContentType("text/html");
		//if(id != null && id.equals("10")) {
		PrintWriter out = resp.getWriter();
		out.print("Hola Mundo - "+id);
		out.flush();
		out.close();
			
		//}
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
	

	
}
