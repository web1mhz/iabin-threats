package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DataBaseManager;
import model.TaxonObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Service extends HttpServlet {
	private static final long serialVersionUID = 7332571365625168689L;
	private String user;
    private String pass;
	private String ip;
	private String port;
	private String database;
	private Connection conx;
	private static Gson gson = new Gson();
	private ResultSet rs;
	

	@Override
	public void init() throws ServletException {
		super.init();
		DataBaseManager.registerDriver();
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

		try {
			String id = req.getParameter("id");
			String rank = req.getParameter("rank");
			Set<TaxonObject> taxons = makeQuery(id,Integer.parseInt(rank));
			
			// Converting to JSON
			Type setType = new TypeToken<Set<TaxonObject>>(){}.getType();
			String json = gson.toJson(taxons, setType);
			
			// Sending
			PrintWriter out = resp.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (SQLException e) {
			// TODO Hay que discutir qué sucede si hay un error en la base de datos.
			// Acaso es mejor enviar un mensaje aparte? o se envía un null?
			e.printStackTrace();
			PrintWriter out = resp.getWriter();
			out.print(e.getMessage());
			out.flush();
			out.close();
		} 

		// resp.setContentType("text/html");
		// if(id != null && id.equals("10")) {
		

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

	public Set<TaxonObject> makeQuery(String id, int rank) throws SQLException {
		HashSet<TaxonObject> taxons = new HashSet<TaxonObject>();
		conx = DataBaseManager.openConnection(Info.getUser(), Info.getPass(), Info.getIp(), Info.getPort(), Info.getDatabase());
		/*ResultSet rs = DataBaseManager.makeQuery(
						"select tn.id, tn.canonical, tc.rank from taxon_concept tc, taxon_name tn " +
						"where tc.taxon_name_id = tn.id " +
						"and tn.rank = 1000",
						conx); // Esta es una consulta temporal. No es la
								// original.htobon*/
		
		if(rank==1000){
			// buscar family 5000
			 rs= DataBaseManager.makeQuery(
					"select tc.id, tn.canonical, tc.rank " +
					"from taxon_name tn , taxon_concept tc, taxon_name tnk , taxon_concept tck " +
					"where tc.taxon_name_id=tn.id and tc.rank="+5000+" and " +
					"tc.kingdom_concept_id=tck.id and tck.taxon_name_id=tnk.id and " +
					"tck.id="+id+" order by tn.canonical",conx); // Esta es una consulta temporal. No es la
			// original. by lotvx
			
		}else{
			if(rank==3000){
				rs = DataBaseManager.makeQuery(
						"select tc.id, tn.canonical, tc.rank " +
						"from taxon_name tn , taxon_concept tc, taxon_name tnk , taxon_concept tck " +
						"where tc.taxon_name_id=tn.id and tc.rank="+5000+" and " +
						"tc.class_concept_id=tck.id and tck.taxon_name_id=tnk.id and " +
						"tck.id="+id+" order by tn.canonical",conx);
				
			}else{
				if(rank==5000){
					// buscar genus 6000
					rs = DataBaseManager.makeQuery(
							"select tc.id, tn.canonical, tc.rank " +
							"from taxon_name tn , taxon_concept tc, taxon_name tnk , taxon_concept tck " +
							"where tc.taxon_name_id=tn.id and tc.rank="+6000+" and " +
									"tc.family_concept_id=tck.id and tck.taxon_name_id=tnk.id and " +
									"tck.id="+id+" order by tn.canonical",conx); // Esta es una consulta temporal. No es la
									// original. by lotvx
				}else{
					if(rank==6000){
						// buscar specie 7000
						rs = DataBaseManager.makeQuery(
								"select tc.id, tn.canonical, tc.rank " +
								"from taxon_name tn , taxon_concept tc, taxon_name tnk , taxon_concept tck " +
								"where tc.taxon_name_id=tn.id and tc.rank="+7000+" and " +
										"tc.genus_concept_id=tck.id and tck.taxon_name_id=tnk.id and " +
										"tck.id="+id+" order by tn.canonical",conx); // Esta es una consulta temporal. No es la
										// original. by lotvx
					}
				}
			}
		}
			
	/*	ResultSet rs = DataBaseManager.makeQuery(
				"select tn.id, tn.canonical, tc.rank " +
				"from taxon_name tn , taxon_concept tc, taxon_name tnk , taxon_concept tck " +
				"where tc.taxon_name_id=tn.id and tc.rank="+(rank+1000)+" and " +
						"tc.kingdom_concept_id=tck.id and tck.taxon_name_id=tnk.id and " +
						"tnk.id="+id+" group by canonical",conx); // Esta es una consulta temporal. No es la
						// original. by louis*/
		
		
		
		while (rs.next()) {
			TaxonObject taxon = new TaxonObject();
			taxon.setId(rs.getString(1));
			taxon.setCanonical(rs.getString(2));
			taxon.setRankID(rs.getInt(3));
			taxons.add(taxon);
		}
		rs.close();
		DataBaseManager.closeConnection(conx);
		return taxons;
	}
}
