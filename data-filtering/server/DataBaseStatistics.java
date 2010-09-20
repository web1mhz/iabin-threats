package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import model.ShapeFile;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import client.correctormanager.WorldMaskManager;

public class DataBaseStatistics {

	private static final String XML_FILE = "statistics.xml";
	private static final String XML_ITEM = "item";
	private static final String XML_NAME = "name";
	private static final String XML_DESCRIPTION = "description";
	private static final String XML_TOTAL = "total";
	private static final String XML_PERCENT = "percent_total";
	private static final String XML_PERCENT_UNRELIABLE = "percent_unreliables";
	private static final String XML_PERCENT_MASK = "percent_mask";
	private static final String XML_PERCENT_SHAPE = "percent_shape";
	private static final String XML_ERROR_TAG = "tag";
	private static final String XML_PERCENT_GGOODS = "percent_geo_goods";

	public static void main(String[] args) {

		ServerConfig.init();
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
		generateXMLStatistics(DataBaseManager.openConnection(
				server.ServerConfig.database_user,
				ServerConfig.database_password));
	}

	/**
	 * creates a XML with the evaluation statistics
	 * @param conx is the connection to the database
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void generateXMLStatistics(Connection conx)
			throws IOException, SQLException {

		
		int nNullCountry = 0, nGGoods = 0, nOutliers = 0, nEGoods = 0, nUnreliable = 0, nUnreliableByMask = 0, nUnreliableByShape = 0, nWrongCountry = 0, nNotInLand = 0, nNearLand = 0, nNotInMask = 0, nTotal = 0;
		String rPercent = "";

		ResultSet rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableGoods, conx);
		if (rs.next())
			nGGoods = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableGoods + " where outlier>"
				+ ((int) (ServerConfig.dbVariablesName.size() * 0.8)), conx);
		if (rs.next())
			nOutliers = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableUnreliable + " where error='"
				+ ShapeFile.E_WRONG_COUNTRY + "'", conx);
		if (rs.next())
			nWrongCountry = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableUnreliable + " where error='"
				+ ShapeFile.E_NULL_COUNTRY + "'", conx);
		if (rs.next())
			nNullCountry = rs.getInt("c");

		nUnreliableByShape = nWrongCountry + nNullCountry;

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NEAR_LAND + "'", conx);
		if (rs.next())
			nNearLand = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NOT_IN_LAND + "'", conx);
		if (rs.next())
			nNotInLand = rs.getInt("c");

		rs = DataBaseManager.makeQuery("select count(*)\"c\" from "
				+ ServerConfig.dbTableUnreliable + " where error='"
				+ WorldMaskManager.E_NOT_IN_MASK + "'", conx);
		if (rs.next())
			nNotInMask = rs.getInt("c");

		nUnreliableByMask = nNearLand + nNotInLand + nNotInMask;
		nUnreliableByShape = nWrongCountry + nNullCountry;
		nUnreliable = nUnreliableByMask + nUnreliableByShape;
		nTotal = nUnreliable + nGGoods;
		nEGoods = nGGoods - nOutliers;

		Element stats = new Element("statistics");

		Element total = new Element(XML_ITEM);
		total.setAttribute(XML_NAME, "total_records");
		total.setAttribute(XML_TOTAL, nTotal + "");
		total.setAttribute(XML_DESCRIPTION, "total_records_evaluated");

		Element goods = new Element(XML_ITEM);
		goods.setAttribute(XML_NAME, "geographically_good_records");
		goods.setAttribute(XML_TOTAL, nGGoods + "");
		rPercent = round(((double) nGGoods / nTotal * 100));
		goods.setAttribute(XML_PERCENT, rPercent + "%");
		goods.setAttribute(XML_DESCRIPTION, "records_marked_as_reliable");

		Element unreliable = new Element(XML_ITEM);
		unreliable.setAttribute(XML_NAME, "unreliable_records");
		unreliable.setAttribute(XML_TOTAL, nUnreliable + "");
		rPercent = round(((double) nUnreliable / nTotal * 100));
		unreliable.setAttribute(XML_PERCENT, rPercent + "%");
		unreliable
				.setAttribute(XML_DESCRIPTION, "records_marked_as_unreliable");

		Element unreliableByMask = new Element(XML_ITEM);
		unreliableByMask.setAttribute(XML_NAME, "unreliable_records_by_mask");
		unreliableByMask.setAttribute(XML_TOTAL, nUnreliableByMask + "");
		rPercent = round(((double) nUnreliableByMask / nTotal * 100));
		unreliableByMask.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nUnreliableByMask / nUnreliable * 100));
		unreliableByMask.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		unreliableByMask.setAttribute(XML_DESCRIPTION,
				"unreliable_records_detected_by_mask");

		Element unreliableByShape = new Element(XML_ITEM);
		unreliableByShape.setAttribute(XML_NAME, "unreliable_records_by_shape");
		unreliableByShape.setAttribute(XML_TOTAL, nUnreliableByShape + "");
		rPercent = round(((double) nUnreliableByShape / nTotal * 100));

		unreliableByShape.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nUnreliableByShape / nUnreliable * 100));
		unreliableByShape.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		unreliableByShape.setAttribute(XML_DESCRIPTION,
				"unreliable_records_detected_by_Shape");

		Element eWrongCountry = new Element(XML_ITEM);
		eWrongCountry.setAttribute(XML_NAME, "wrong_countries");
		eWrongCountry.setAttribute(XML_ERROR_TAG, ShapeFile.E_WRONG_COUNTRY);
		eWrongCountry.setAttribute(XML_TOTAL, nWrongCountry + "");
		rPercent = round(((double) nWrongCountry / nTotal * 100));
		eWrongCountry.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nWrongCountry / nUnreliable * 100));
		eWrongCountry.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nWrongCountry / nUnreliableByShape * 100));
		eWrongCountry.setAttribute(XML_PERCENT_SHAPE, rPercent + "%");

		eWrongCountry.setAttribute(XML_DESCRIPTION,
				"records_with_wrong_country");

		Element eNullCountry = new Element(XML_ITEM);
		eNullCountry.setAttribute(XML_NAME, "null_countries");
		eNullCountry.setAttribute(XML_ERROR_TAG, ShapeFile.E_NULL_COUNTRY);
		eNullCountry.setAttribute(XML_TOTAL, nNullCountry + "");
		rPercent = round(((double) nNullCountry / nTotal * 100));
		eNullCountry.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNullCountry / nUnreliable * 100));
		eNullCountry.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNullCountry / nUnreliableByShape * 100));
		eNullCountry.setAttribute(XML_PERCENT_SHAPE, rPercent + "%");

		eNullCountry.setAttribute(XML_DESCRIPTION, "records_with_null_country");

		Element eNotInLand = new Element(XML_ITEM);
		eNotInLand.setAttribute(XML_NAME, "not_in_land");
		eNotInLand.setAttribute(XML_ERROR_TAG, WorldMaskManager.E_NOT_IN_LAND);
		eNotInLand.setAttribute(XML_TOTAL, nNotInLand + "");
		rPercent = round(((double) nNotInLand / nTotal * 100));
		eNotInLand.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNotInLand / nUnreliable * 100));
		eNotInLand.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNotInLand / nUnreliableByMask * 100));
		eNotInLand.setAttribute(XML_PERCENT_MASK, rPercent + "%");
		eNotInLand.setAttribute(XML_DESCRIPTION, "records_not_in_land");

		Element eNearLand = new Element(XML_ITEM);
		eNearLand.setAttribute(XML_NAME, "near_land");
		eNearLand.setAttribute(XML_ERROR_TAG, WorldMaskManager.E_NEAR_LAND);
		eNearLand.setAttribute(XML_TOTAL, nNearLand + "");
		rPercent = round(((double) nNearLand / nTotal * 100));
		eNearLand.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNearLand / nUnreliable * 100));
		eNearLand.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNearLand / nUnreliableByMask * 100));
		eNearLand.setAttribute(XML_PERCENT_MASK, rPercent + "%");
		eNearLand.setAttribute(XML_DESCRIPTION, "records_near_land");

		Element eNotInMask = new Element(XML_ITEM);
		eNotInMask.setAttribute(XML_NAME, "not_in_Mask");
		eNotInMask.setAttribute(XML_ERROR_TAG, WorldMaskManager.E_NOT_IN_MASK);
		eNotInMask.setAttribute(XML_TOTAL, nNotInMask + "");
		rPercent = round(((double) nNotInMask / nTotal * 100));
		eNotInMask.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nNotInMask / nUnreliable * 100));
		eNotInMask.setAttribute(XML_PERCENT_UNRELIABLE, rPercent + "%");
		rPercent = round(((double) nNotInMask / nUnreliableByMask * 100));
		eNotInMask.setAttribute(XML_PERCENT_MASK, rPercent + "%");
		eNotInMask.setAttribute(XML_DESCRIPTION, "records_not_in_mask");

		Element egoods = new Element(XML_ITEM);
		egoods.setAttribute(XML_NAME, "environmentally_good_records");
		egoods.setAttribute(XML_TOTAL, nEGoods + "");
		rPercent = round(((double) nEGoods / nTotal * 100));
		egoods.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nEGoods / nGGoods * 100));
		egoods.setAttribute(XML_PERCENT_GGOODS, rPercent + "%");
		egoods
				.setAttribute(XML_DESCRIPTION,
						"records_environmentally_reliable_from_geo_goods");

		Element outliers = new Element(XML_ITEM);
		outliers.setAttribute(XML_NAME, "outlier_records");
		outliers.setAttribute(XML_TOTAL, nOutliers + "");
		rPercent = round(((double) nOutliers / nTotal * 100));
		outliers.setAttribute(XML_PERCENT, rPercent + "%");
		rPercent = round(((double) nOutliers / nGGoods * 100));
		outliers.setAttribute(XML_PERCENT_GGOODS, rPercent + "%");
		outliers.setAttribute(XML_DESCRIPTION, "records_with_0.8_outliers");

		stats.addContent(total);
		stats.addContent(goods);
		stats.addContent(unreliable);
		stats.addContent(unreliableByShape);
		stats.addContent(eWrongCountry);
		stats.addContent(eNullCountry);
		stats.addContent(unreliableByMask);
		stats.addContent(eNearLand);
		stats.addContent(eNotInLand);
		stats.addContent(eNotInMask);
		stats.addContent(egoods);
		stats.addContent(outliers);
		

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
