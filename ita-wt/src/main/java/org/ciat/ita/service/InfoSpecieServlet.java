package org.ciat.ita.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ciat.ita.model.InfoSpecieModel;

import com.google.gson.Gson;

public class InfoSpecieServlet extends HttpServlet {

	private static final long serialVersionUID = 3667132459005103094L;
	private static String path = "\\\\172.22.33.85\\geodata\\Threat-Assement\\species\\";
	// private static String
	// path="/mnt/HD724-geodata/iabin-threats/ITA/generated-files/species/";
	private static Gson gson = new Gson();

	@Override
	public void init() throws ServletException {
		super.init();

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DecimalFormat formatter = new DecimalFormat("####.####");
		String id = req.getParameter("id");
		if (id != null) {
			File file = new File(path + id + File.separator + id + "-info.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String data[];
			InfoSpecieModel infoSpecie = new InfoSpecieModel();
			while ((line = reader.readLine()) != null) {
				data = line.split(":");
				String trimed = data[1].trim();
				if (data[0].trim().equals("specie")) {
					infoSpecie.setSpecieName(data[1].trim());
				} else if (data[0].trim().equals("genus")) {
					infoSpecie.setGenusName(data[1].trim());
				} else if (data[0].trim().equals("family")) {
					infoSpecie.setFamilyName(data[1].trim());
				} else if (data[0].trim().equals("class")) {
					infoSpecie.setClassName(data[1].trim());
				}else if (data[0].trim().equals("number.of.points")) {
					infoSpecie.setNumberPoints(Integer.parseInt(data[1].trim()));
				} else if (data[0].trim().equals("avg.auc_train")) {
					infoSpecie.setAvgAucTrain (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("avg.auc_test")) {
					infoSpecie.setAvgAucTest (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("sd.auc")) {
					infoSpecie.setSdAuc (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("prevalence")) {
					infoSpecie.setPrevalence (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("percent.lost")) {
					infoSpecie.setPercentLost (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("lost.mean.occ.probability")) {
					infoSpecie.setLostMeanProbability (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("not.lost.mean.occ.probability")) {
					infoSpecie.setNotLostMeanProbability (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.aggregate.mean")) {
					infoSpecie.setAggregateMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.aggregate.sd")) {
					infoSpecie.setAggregateSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.access_pop.mean")) {
					infoSpecie.setAccessPopMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.access_pop.sd")) {
					infoSpecie.setAccessPopSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.conv_ag.mean")) {
					infoSpecie.setConvAgMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.conv_ag.sd")) {
					infoSpecie.setConvAgSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.fires.mean")) {
					infoSpecie.setFiresMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.fires.sd")) {
					infoSpecie.setFiresSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.grazing.mean")) {
					infoSpecie.setGrazingMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.grazing.sd")) {
					infoSpecie.setGrazingSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.infrastr.mean")) {
					infoSpecie.setInfrastrMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.infrastr.sd")) {
					infoSpecie.setInfrastrSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.oil_gas.mean")) {
					infoSpecie.setOilGasMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.oil_gas.sd")) {
					infoSpecie.setOilGasSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.rec_conv.mean")) {
					infoSpecie.setRecConvMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("ta.rec_conv.sd")) {
					infoSpecie.setRecConvSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("occ.prob.mean.in.pa")) {
					infoSpecie.setOccProbMean (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("occ.prob.sd.in.pa")) {
					infoSpecie.setOccProbSd (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("occ.prob.mean.outside.pa")) {
					infoSpecie.setOccProbMeanOut (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("occ.prob.sd.outside.pa")) {
					infoSpecie.setOccProbSdOut (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				} else if (data[0].trim().equals("percent.area.protected")) {
					infoSpecie.setPercentAreaProtected (trimed.equals("NA") ? "NA" : formatter.format(Double.parseDouble(data[1].trim())));
				}
			}
			
			reader.close();
			
			String json = gson.toJson(infoSpecie);
			PrintWriter writer = resp.getWriter();
			writer.print(json);
			writer.flush();
			writer.close();
		}
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
