package org.ciat.ita.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ciat.ita.model.DataBaseManager;
import org.ciat.ita.model.InfoSpecieEcosystemModel;

import com.google.gson.Gson;

public class InfoSpecieEcosystemServlet extends HttpServlet {

	private static final long serialVersionUID = -7026178626454113781L;
	private Connection conx;
	private static Gson gson = new Gson();
	private ResultSet rs;

	@Override
	public void init() throws ServletException {
		super.init();
		DataBaseManager.registerDriver();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		String id = req.getParameter("id");
		resp.setCharacterEncoding("UTF-8");
		if (id != null) {
			InfoSpecieEcosystemModel infoSpeciesEcosystem = null;

			try {
				infoSpeciesEcosystem = makeQuery(id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}

			// Converting to JSON
			String json = gson.toJson(infoSpeciesEcosystem);
			PrintWriter writer = resp.getWriter();
			writer.print(json);
			writer.flush();
			writer.close();
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		doGet(req, resp);
	}

	public <InfoSpecieEcoystemModel> InfoSpecieEcosystemModel makeQuery(String id) throws SQLException {
		InfoSpecieEcosystemModel infoSpecieEcosystem = new InfoSpecieEcosystemModel(null);
		conx = DataBaseManager.openConnection(Info.getInstance().getUser(), Info.getInstance().getPass(),
				Info.getInstance().getIp(), Info.getInstance().getPort(), Info.getInstance().getDatabase());
		rs = DataBaseManager.makeQuery(
				"select se.ecosystem_id, el.description from species_ecosystem  se, ecosystem_legends el where specie_id="
						+ id + " and el.id=se.ecosystem_id", conx);
		while (!rs.isClosed() && rs.next()) {
			infoSpecieEcosystem.addEcosystem(rs.getString("description"));			
		}	
		
		DataBaseManager.closeConnection(conx);
		return infoSpecieEcosystem;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
}
