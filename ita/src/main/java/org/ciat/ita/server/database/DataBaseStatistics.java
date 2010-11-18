package org.ciat.ita.server.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;


import org.ciat.ita.client.manage.WorldMaskManager;
import org.ciat.ita.model.ShapeFile;
import org.ciat.ita.server.ServerConfig;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



public class DataBaseStatistics {

	private static final String XML_FILE = "statistics.xml";
	private static final String XML_ITEM = "item";
	private static final String XML_NAME = "name";
	private static final String XML_DESCRIPTION = "Description";
	private static final String XML_TOTAL = "Quantity";
	private static final String XML_PERCENT = "Evaluated";
	private static final String XML_PERCENT_UNRELIABLE = "Unreliables";
	private static final String XML_PERCENT_MASK = "MaskExlcuded";
	private static final String XML_PERCENT_SHAPE = "ShapeExcluded";
	private static final String XML_ERROR_TAG = "TAG";
	private static final String XML_PERCENT_GGOODS = "GeographicallyReliables";

	public static void main(String[] args) {

		try {
			generateXMLStatistics();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void generateXMLStatistics() throws IOException,
			SQLException {
		DataBaseManager.registerDriver();

		Connection conx=DataBaseManager.openConnection(
				ServerConfig.getInstance().database_user,
				ServerConfig.getInstance().database_password);
		generateXMLStatistics(conx);
		DataBaseManager.closeConnection(conx);
	}

	/**
	 * creates a XML with the evaluation statistics
	 * @param conx is the connection to the database
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void generateXMLStatistics(Connection conx)
			throws IOException, SQLException {

		
		int nNullCountry = 0, nGGoods = 0, nOutliers = 0, nEGoods = 0, nUnreliable = 0, nUnreliableByMask = 0, nUnreliableByShape = 0, nWrongCountry = 0, nNotInLand = 0, nNearLand = 0, nNotInMask = 0, nEvaluated = 0;
		String rPercent = "";

		ResultSet rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableGoods, conx);
		if (rs.next())
			nGGoods = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableGoods + " where outlier>"
				+ ((int) (ServerConfig.getInstance().dbVariablesName.size() * 0.8)), conx);
		if (rs.next())
			nOutliers = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableUnreliable + " where error='"
				+ ShapeFile.E_WRONG_COUNTRY + "'", conx);
		if (rs.next())
			nWrongCountry = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableUnreliable + " where error='"
				+ ShapeFile.E_NULL_COUNTRY + "'", conx);
		if (rs.next())
			nNullCountry = rs.getInt("c");

		nUnreliableByShape = nWrongCountry + nNullCountry;

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NEAR_LAND + "'", conx);
		if (rs.next())
			nNearLand = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NOT_IN_LAND + "'", conx);
		if (rs.next())
			nNotInLand = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NOT_IN_MASK + "'", conx);
		if (rs.next())
			nNotInMask = rs.getInt("c");
		
		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.getInstance().dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NOT_IN_LAND + "'", conx);
		if (rs.next())
			nNotInLand = rs.getInt("c");
		
		nUnreliableByMask = nNearLand + nNotInLand + nNotInMask;
		nUnreliableByShape = nWrongCountry + nNullCountry;
		nUnreliable = nUnreliableByMask + nUnreliableByShape;
		nEvaluated = nUnreliable + nGGoods;
		nEGoods = nGGoods - nOutliers;

		Element stats = new Element("statistics");
		
		Element evaluated = new Element(XML_ITEM);
		evaluated.setAttribute(XML_NAME, "Evaluated records");
		evaluated.setAttribute(XML_TOTAL, nEvaluated + "");
		evaluated.setAttribute(XML_DESCRIPTION, "records evaluated");

		Element goods = new Element(XML_ITEM);
		goods.setAttribute(XML_NAME, "2. Geographically reliable");
		goods.setAttribute(XML_TOTAL, nGGoods + "");
		rPercent = round(((double) nGGoods / nEvaluated * 100));
		goods.setAttribute(XML_PERCENT, rPercent + "%");
		goods.setAttribute(XML_DESCRIPTION, "records_marked_as_reliable");

		Element unreliable = new Element(XML_ITEM);
		unreliable.setAttribute(XML_NAME, "1. Geographically unreliable");
		unreliable.setAttribute(XML_TOTAL, nUnreliable + "");
		rPercent = round(((double) nUnreliable / nEvaluated * 100));
		unreliable.setAttribute(XML_PERCENT, rPercent + "%");
		unreliable
				.setAttribute(XML_DESCRIPTION, "records excluded from ");

		Element unreliableByMask = new Element(XML_ITEM);
		unreliableByMask.setAttribute(XML_NAME, "1.2 Mask excluded");
		unreliableByMask.setAttribute(XML_TOTAL, nUnreliableByMask + "");
		rPercent = round(((double) nUnreliableByMask / nEvaluated * 100));
		unreliableByMask.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nUnreliableByMask / nUnreliable * 100));
		unreliableByMask.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		unreliableByMask.setAttribute(XML_DESCRIPTION,
				"records exluded by mask condition");

		Element unreliableByShape = new Element(XML_ITEM);
		unreliableByShape.setAttribute(XML_NAME, "1.1 Shape excluded");
		unreliableByShape.setAttribute(XML_TOTAL, nUnreliableByShape + "");
		rPercent = round(((double) nUnreliableByShape / nEvaluated * 100));

		unreliableByShape.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nUnreliableByShape / nUnreliable * 100));
		unreliableByShape.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		unreliableByShape.setAttribute(XML_DESCRIPTION,
				"records excluded by shape condition");

		Element eWrongCountry = new Element(XML_ITEM);
		eWrongCountry.setAttribute(XML_NAME, "1.1.1 With wrong country");
		eWrongCountry.setAttribute(XML_ERROR_TAG, ShapeFile.E_WRONG_COUNTRY);
		eWrongCountry.setAttribute(XML_TOTAL, nWrongCountry + "");
		rPercent = round(((double) nWrongCountry / nEvaluated * 100));
		eWrongCountry.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nWrongCountry / nUnreliable * 100));
		eWrongCountry.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nWrongCountry / nUnreliableByShape * 100));
		eWrongCountry.setAttribute(XML_PERCENT_SHAPE, rPercent + "%");

		eWrongCountry.setAttribute(XML_DESCRIPTION,
				"records with wrong country");

		Element eNullCountry = new Element(XML_ITEM);
		eNullCountry.setAttribute(XML_NAME, "1.1.2 With null countries");
		eNullCountry.setAttribute(XML_ERROR_TAG, ShapeFile.E_NULL_COUNTRY);
		eNullCountry.setAttribute(XML_TOTAL, nNullCountry + "");
		rPercent = round(((double) nNullCountry / nEvaluated * 100));
		eNullCountry.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNullCountry / nUnreliable * 100));
		eNullCountry.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNullCountry / nUnreliableByShape * 100));
		eNullCountry.setAttribute(XML_PERCENT_SHAPE, rPercent + "%");

		eNullCountry.setAttribute(XML_DESCRIPTION, "records with null country");

		Element eNotInLand = new Element(XML_ITEM);
		eNotInLand.setAttribute(XML_NAME, "1.2.2 Not in land");
		eNotInLand.setAttribute(XML_ERROR_TAG, WorldMaskManager.E_NOT_IN_LAND);
		eNotInLand.setAttribute(XML_TOTAL, nNotInLand + "");
		rPercent = round(((double) nNotInLand / nEvaluated * 100));
		eNotInLand.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNotInLand / nUnreliable * 100));
		eNotInLand.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNotInLand / nUnreliableByMask * 100));
		eNotInLand.setAttribute(XML_PERCENT_MASK, rPercent + "%");
		eNotInLand.setAttribute(XML_DESCRIPTION, "records not in land");

		Element eNearLand = new Element(XML_ITEM);
		eNearLand.setAttribute(XML_NAME, "1.2.1 Near land");
		eNearLand.setAttribute(XML_ERROR_TAG, WorldMaskManager.E_NEAR_LAND);
		eNearLand.setAttribute(XML_TOTAL, nNearLand + "");
		rPercent = round(((double) nNearLand / nEvaluated * 100));
		eNearLand.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNearLand / nUnreliable * 100));
		eNearLand.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNearLand / nUnreliableByMask * 100));
		eNearLand.setAttribute(XML_PERCENT_MASK, rPercent + "%");
		eNearLand.setAttribute(XML_DESCRIPTION, "records not in land but 5km near to land");

		Element eNotInMask = new Element(XML_ITEM);
		eNotInMask.setAttribute(XML_NAME, "1.2.3 Not in mask");
		eNotInMask.setAttribute(XML_ERROR_TAG, WorldMaskManager.E_NOT_IN_MASK);
		eNotInMask.setAttribute(XML_TOTAL, nNotInMask + "");
		rPercent = round(((double) nNotInMask / nEvaluated * 100));
		eNotInMask.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNotInMask / nUnreliable * 100));
		eNotInMask.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNotInMask / nUnreliableByMask * 100));
		eNotInMask.setAttribute(XML_PERCENT_MASK, rPercent + "%");
		eNotInMask.setAttribute(XML_DESCRIPTION, "records outside the mask boundaries");

		Element egoods = new Element(XML_ITEM);
		egoods.setAttribute(XML_NAME, "2.2 Environmentally reliable");
		egoods.setAttribute(XML_TOTAL, nEGoods + "");
		rPercent = round(((double) nEGoods / nEvaluated * 100));
		egoods.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nEGoods / nGGoods * 100));
		egoods.setAttribute(XML_PERCENT_GGOODS, rPercent + "%");
		egoods
				.setAttribute(XML_DESCRIPTION,
						"records environmentally reliable");

		Element outliers = new Element(XML_ITEM);
		outliers.setAttribute(XML_NAME, "2.1 utlier records");
		outliers.setAttribute(XML_TOTAL, nOutliers + "");
		rPercent = round(((double) nOutliers / nEvaluated * 100));
		outliers.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nOutliers / nGGoods * 100));
		outliers.setAttribute(XML_PERCENT_GGOODS, rPercent + "%");
		outliers.setAttribute(XML_DESCRIPTION, "records that are outliers in more than 80% env. variables");

		stats.addContent(evaluated);
		stats.addContent(unreliable);
		stats.addContent(unreliableByShape);
		stats.addContent(eWrongCountry);
		stats.addContent(eNullCountry);
		stats.addContent(unreliableByMask);
		stats.addContent(eNearLand);
		stats.addContent(eNotInLand);
		stats.addContent(eNotInMask);
		stats.addContent(goods);
		stats.addContent(outliers);
		stats.addContent(egoods);
		

		Document doc = new Document(stats);
		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		FileOutputStream file = new FileOutputStream(XML_FILE);
		outp.output(doc, file);
		file.flush();
		file.close();
		outp.output(doc, System.out);
	}

	public static String round(double num) {
		DecimalFormat df= new DecimalFormat("0.000");
		return df.format(num);
	}

}
