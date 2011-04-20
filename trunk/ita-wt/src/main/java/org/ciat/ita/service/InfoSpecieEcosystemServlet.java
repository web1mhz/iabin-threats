package org.ciat.ita.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ciat.ita.model.InfoSpecieEcosystemModel;

import com.google.gson.Gson;

public class InfoSpecieEcosystemServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7026178626454113781L;
	private static Gson gson = new Gson();

	@Override
	public void init() throws ServletException {
		super.init();

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		String id = req.getParameter("id");
		if (id != null) {
			File file = new File(Info.getInstance().getPath()+"ecosystems"+ File.separator+ id +".txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			File fileEcosystem = new File(Info.getInstance().getPath()+"ecosystems"+File.separator+"legend.csv");
			BufferedReader readerEcosystem = new BufferedReader(new FileReader(fileEcosystem));
			String line;
			String data[] = null;
			InfoSpecieEcosystemModel infoSpecieEcosystem = new InfoSpecieEcosystemModel();
			HashMap<Integer,String> names = new HashMap<Integer,String>();		
			while ((line = readerEcosystem.readLine()) != null) {
				data = line.split(",");	
				names.put(Integer.parseInt(data[0]), data[1]);			
			}
			
			while ((line = reader.readLine()) != null) {				
				infoSpecieEcosystem.addEcosystem(names.get(Integer.parseInt(line)));
			}
		
			
			reader.close();
			
			String json = gson.toJson(infoSpecieEcosystem);
			PrintWriter writer = resp.getWriter();
			writer.print(json);
			writer.flush();
			writer.close();
		}
	}	
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
