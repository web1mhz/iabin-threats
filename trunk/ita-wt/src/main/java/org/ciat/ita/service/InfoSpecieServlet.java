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
import org.ciat.ita.model.InfoSpecieModel;

import com.google.gson.Gson;

public class InfoSpecieServlet extends HttpServlet {

	private static final long serialVersionUID = 3667132459005103094L;
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
		try {
			String id = req.getParameter("id");
			InfoSpecieModel infoSpecies = makeQuery(id);

			// Converting to JSON			
			String json = gson.toJson(infoSpecies);

			// Sending
			PrintWriter out = resp.getWriter();
			out.print(json);
			out.flush();
			out.close();
		} catch (SQLException e) {			
			e.printStackTrace();
			PrintWriter out = resp.getWriter();
			out.print(e.getMessage());
			out.flush();
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		doGet(req, resp);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	public InfoSpecieModel makeQuery(String id) throws SQLException {
		InfoSpecieModel infoSpecie = new InfoSpecieModel();
		conx = DataBaseManager.openConnection(Info.getInstance().getUser(), Info.getInstance().getPass(),
				Info.getInstance().getIp(), Info.getInstance().getPort(), Info.getInstance().getDatabase());
		rs = DataBaseManager.makeQuery("select * from species_information where id=" + id, conx);

		while (!rs.isClosed() && rs.next()) {
			infoSpecie.setId(rs.getInt("id") + "");
			infoSpecie.setAccessPopMean(rs.getString("ta_access_pop_mean"));
			infoSpecie.setAccessPopSd(rs.getString("ta_access_pop_sd"));
			infoSpecie.setAggregateMean(rs.getString("ta_aggregate_mean"));
			infoSpecie.setAggregateSd(rs.getString("ta_aggregate_sd"));
			infoSpecie.setAvgAucTest(rs.getString("avg_auc_test"));
			infoSpecie.setAvgAucTrain(rs.getString("avg_auc_train"));
			infoSpecie.setClassName(rs.getString("class"));
			infoSpecie.setConvAgMean(rs.getString("ta_conv_ag_mean"));
			infoSpecie.setConvAgSd(rs.getString("ta_conv_ag_sd"));
			infoSpecie.setFamilyName(rs.getString("family"));
			infoSpecie.setFiresMean(rs.getString("ta_fires_mean"));
			infoSpecie.setFiresSd(rs.getString("ta_fires_sd"));
			infoSpecie.setGenusName(rs.getString("genus"));
			infoSpecie.setGrazingMean(rs.getString("ta_grazing_mean"));
			infoSpecie.setGrazingSd(rs.getString("ta_grazing_sd"));
			infoSpecie.setInfrastrMean(rs.getString("ta_infrastr_mean"));
			infoSpecie.setInfrastrSd(rs.getString("ta_infrastr_sd"));
			infoSpecie.setLostMeanProbability(rs.getString("lost_mean_occ_probability"));
			infoSpecie.setNotLostMeanProbability(rs.getString("not_lost_mean_occ_probability"));
			infoSpecie.setNumberPoints(rs.getString("number_of_points"));
			infoSpecie.setOccProbMean(rs.getString("occ_prob_mean_in_pa"));
			infoSpecie.setOccProbMeanOut(rs.getString("occ_prob_mean_outside_pa"));
			infoSpecie.setOccProbSd(rs.getString("occ_prob_sd_in_pa"));
			infoSpecie.setOccProbSdOut(rs.getString("occ_prob_sd_outside_pa"));
			infoSpecie.setOilGasMean(rs.getString("ta_oil_gas_mean"));
			infoSpecie.setOilGasSd(rs.getString("ta_oil_gas_sd"));
			infoSpecie.setPercentAreaProtected(rs.getString("percent_area_protected"));
			infoSpecie.setPercentLost(rs.getString("percent_lost"));
			infoSpecie.setPrevalence(rs.getString("prevalence"));
			infoSpecie.setRecConvMean(rs.getString("ta_rec_conv_mean"));
			infoSpecie.setRecConvSd(rs.getString("ta_rec_conv_sd"));
			infoSpecie.setSdAuc(rs.getString("sd_auc"));
			infoSpecie.setSpecieName(rs.getString("specie"));				
		}

		rs.close();

		DataBaseManager.closeConnection(conx);
		return infoSpecie;
	}

}
