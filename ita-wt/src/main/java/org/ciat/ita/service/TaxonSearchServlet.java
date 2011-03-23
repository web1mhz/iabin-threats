package org.ciat.ita.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ciat.ita.model.DataBaseManager;
import org.ciat.ita.model.TaxonObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaxonSearchServlet extends HttpServlet {

	private static final long serialVersionUID = -2765354609645409373L;
	private Connection conx;
	private static Gson gson = new Gson();
	private ResultSet rs;

	@Override
	public void init() throws ServletException {
		super.init();
		DataBaseManager.registerDriver();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			String term = req.getParameter("term");
			if (term != null) {
				ArrayList<String> canonicals = makeQuery(term);

				// Converting to JSON
				Type setType = new TypeToken<ArrayList<String>>() {
				}.getType();
				String json = gson.toJson(canonicals, setType);

				// Sending
				PrintWriter out = resp.getWriter();
				out.print(json);
				out.flush();
				out.close();
			}
			String canonical = req.getParameter("canonic");
			if (canonical != null) {
				ArrayList<TaxonObject> taxons = canonicalSearch(canonical);
				String json = null;
				if (taxons.size() > 0) {
					// Converting to JSON
					Type setType = new TypeToken<ArrayList<TaxonObject>>() {}.getType();
					json = gson.toJson(taxons, setType);	
					
				} else {
					json = "No taxons found";
				}
				// Sending
				PrintWriter out = resp.getWriter();
				out.print(json);
				out.flush();
				out.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	

	private ArrayList<String> makeQuery(String term) throws SQLException {
		ArrayList<String> canonicals = new ArrayList<String>();
		conx = DataBaseManager.openConnection(Info.getUser(), Info.getPass(), Info.getIp(), Info.getPort(), Info.getDatabase());

		rs = DataBaseManager.makeQuery("select tn.canonical from IABIN_taxon_name tn where tn.canonical like '%" + term + "%' group by tn.canonical limit 20", conx);

		while (!rs.isClosed() && rs.next()) {
			canonicals.add(rs.getString(1));
		}

		rs.close();

		DataBaseManager.closeConnection(conx);
		return canonicals;
	}

	private ArrayList<TaxonObject> canonicalSearch(String canonical) throws SQLException {
		ArrayList<TaxonObject> canonicals = new ArrayList<TaxonObject>();
		conx = DataBaseManager.openConnection(Info.getUser(), Info.getPass(), Info.getIp(), Info.getPort(), Info.getDatabase());

		rs = DataBaseManager.makeQuery("select tn.id, tn.canonical, tn.rank  from IABIN_taxon_name tn where tn.canonical like '%" + canonical + "%' and rank>4000 order by tn.rank ASC", conx);

		while (!rs.isClosed() && rs.next()) {
			canonicals.add(new TaxonObject(rs.getInt("id") + "", rs.getString("canonical"), rs.getInt("rank")));
		}

		rs.close();

		DataBaseManager.closeConnection(conx);
		return canonicals;
	}
}
