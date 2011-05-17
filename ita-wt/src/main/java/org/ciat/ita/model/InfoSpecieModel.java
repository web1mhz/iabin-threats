package org.ciat.ita.model;

public class InfoSpecieModel implements Comparable<InfoSpecieModel> {

	String id;
	String numberPoints;
	String specieName;
	String genusName;
	String familyName;
	String className;
	String avgAucTrain;
	String avgAucTest;
	String sdAuc;
	String prevalence;
	String percentLost;
	String lostMeanProbability;
	String notLostMeanProbability;
	String aggregateMean;
	String aggregateSd;
	String accessPopMean;
	String accessPopSd;
	String convAgMean;
	String convAgSd;
	String firesMean;
	String firesSd;
	String grazingMean;
	String grazingSd;
	String infrastrMean;
	String infrastrSd;
	String oilGasMean;
	String oilGasSd;
	String recConvMean;
	String recConvSd;
	String occProbMean;
	String occProbSd;
	String occProbMeanOut;
	String occProbSdOut;
	String percentAreaProtected;

	public InfoSpecieModel(String id, String numberPoints, String specieName, String genusName,
			String familyName, String className, String avgAucTrain, String avgAucTest, String sdAuc,
			String prevalence, String percentLost, String lostMeanProbability, String notLostMeanProbability,
			String aggregateMean, String aggregateSd, String accessPopMean, String accessPopSd,
			String convAgMean, String convAgSd, String firesMean, String firesSd, String grazingMean,
			String grazingSd, String infrastrMean, String infrastrSd, String oilGasMean, String oilGasSd,
			String recConvMean, String recConvSd, String occProbMean, String occProbSd,
			String occProbMeanOut, String occProbSdOut, String percentAreaProtected) {
		super();
		this.id=id;
		this.specieName=specieName;
		this.accessPopMean=accessPopMean;
		this.accessPopSd=accessPopSd;
		this.aggregateMean=aggregateMean;
		this.aggregateSd=aggregateSd;
		this.avgAucTest=avgAucTest;
		this.avgAucTrain=avgAucTrain;
		this.className=className;
		this.convAgMean=convAgMean;
		this.convAgSd=convAgSd;
		this.familyName=familyName;
		this.firesMean=firesMean;
		this.firesSd=firesSd;
		this.genusName=genusName;
		this.grazingMean=grazingMean;
		this.grazingSd=grazingSd;
		this.infrastrMean=infrastrMean;
		this.infrastrSd=infrastrSd;
		this.lostMeanProbability=lostMeanProbability;
		this.notLostMeanProbability=notLostMeanProbability;
		this.numberPoints=numberPoints;
		this.occProbMean=occProbMean;
		this.occProbMeanOut=occProbMeanOut;
		this.occProbSd=occProbSd;
		this.occProbSdOut=occProbSdOut;
		this.oilGasMean=oilGasMean;
		this.oilGasSd=oilGasSd;
		this.percentAreaProtected=percentAreaProtected;
		this.percentLost=percentLost;
		this.prevalence=prevalence;
		this.recConvMean=recConvMean;
		this.recConvSd=recConvSd;
		this.sdAuc=sdAuc;

	}

	public InfoSpecieModel() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecieName() {
		return specieName;
	}

	public void setSpecieName(String specieName) {
		this.specieName = specieName;
	}

	public String getGenusName() {
		return genusName;
	}

	public void setGenusName(String genusName) {
		this.genusName = genusName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getNumberPoints() {
		return numberPoints;
	}

	public void setNumberPoints(String numberPoints) {
		this.numberPoints = numberPoints;
	}

	public String getAvgAucTrain() {
		return avgAucTrain;
	}

	public void setAvgAucTrain(String avgAucTrain) {
		this.avgAucTrain = avgAucTrain;
	}

	public String getAvgAucTest() {
		return avgAucTest;
	}

	public void setAvgAucTest(String avgAucTest) {
		this.avgAucTest = avgAucTest;
	}

	public String getSdAuc() {
		return sdAuc;
	}

	public void setSdAuc(String sdAuc) {
		this.sdAuc = sdAuc;
	}

	public String getPrevalence() {
		return prevalence;
	}

	public void setPrevalence(String prevalence) {
		this.prevalence = prevalence;
	}

	public String getPercentLost() {
		return percentLost;
	}

	public void setPercentLost(String percentLost) {
		this.percentLost = percentLost;
	}

	public String getNotLostMeanProbability() {
		return notLostMeanProbability;
	}

	public void setNotLostMeanProbability(String notLostMeanProbability) {
		this.notLostMeanProbability = notLostMeanProbability;
	}

	public String getAggregateMean() {
		return aggregateMean;
	}

	public void setAggregateMean(String aggregateMean) {
		this.aggregateMean = aggregateMean;
	}

	public String getAggregateSd() {
		return aggregateSd;
	}

	public void setAggregateSd(String aggregateSd) {
		this.aggregateSd = aggregateSd;
	}

	public String getAccessPopMean() {
		return accessPopMean;
	}

	public void setAccessPopMean(String accessPopMean) {
		this.accessPopMean = accessPopMean;
	}

	public String getAccessPopSd() {
		return accessPopSd;
	}

	public void setAccessPopSd(String accessPopSd) {
		this.accessPopSd = accessPopSd;
	}

	public String getConvAgMean() {
		return convAgMean;
	}

	public void setConvAgMean(String convAgMean) {
		this.convAgMean = convAgMean;
	}

	public String getConvAgSd() {
		return convAgSd;
	}

	public void setConvAgSd(String convAgSd) {
		this.convAgSd = convAgSd;
	}

	public String getFiresMean() {
		return firesMean;
	}

	public void setFiresMean(String firesMean) {
		this.firesMean = firesMean;
	}

	public String getFiresSd() {
		return firesSd;
	}

	public void setFiresSd(String firesSd) {
		this.firesSd = firesSd;
	}

	public String getGrazingMean() {
		return grazingMean;
	}

	public void setGrazingMean(String grazingMean) {
		this.grazingMean = grazingMean;
	}

	public String getGrazingSd() {
		return grazingSd;
	}

	public void setGrazingSd(String grazingSd) {
		this.grazingSd = grazingSd;
	}

	public String getInfrastrMean() {
		return infrastrMean;
	}

	public void setInfrastrMean(String infrastrMean) {
		this.infrastrMean = infrastrMean;
	}

	public String getInfrastrSd() {
		return infrastrSd;
	}

	public void setInfrastrSd(String infrastrSd) {
		this.infrastrSd = infrastrSd;
	}

	public String getOilGasMean() {
		return oilGasMean;
	}

	public void setOilGasMean(String oilGasMean) {
		this.oilGasMean = oilGasMean;
	}

	public String getOilGasSd() {
		return oilGasSd;
	}

	public void setOilGasSd(String oilGasSd) {
		this.oilGasSd = oilGasSd;
	}

	public String getRecConvMean() {
		return recConvMean;
	}

	public void setRecConvMean(String recConvMean) {
		this.recConvMean = recConvMean;
	}

	public String getRecConvSd() {
		return recConvSd;
	}

	public void setRecConvSd(String recConvSd) {
		this.recConvSd = recConvSd;
	}

	public String getOccProbMean() {
		return occProbMean;
	}

	public void setOccProbMean(String occProbMean) {
		this.occProbMean = occProbMean;
	}

	public String getOccProbSd() {
		return occProbSd;
	}

	public void setOccProbSd(String occProbSd) {
		this.occProbSd = occProbSd;
	}

	public String getOccProbMeanOut() {
		return occProbMeanOut;
	}

	public void setOccProbMeanOut(String occProbMeanOut) {
		this.occProbMeanOut = occProbMeanOut;
	}

	public String getOccProbSdOut() {
		return occProbSdOut;
	}

	public void setOccProbSdOut(String occProbSdOut) {
		this.occProbSdOut = occProbSdOut;
	}

	public String getPercentAreaProtected() {
		return percentAreaProtected;
	}

	public void setPercentAreaProtected(String percentAreaProtected) {
		this.percentAreaProtected = percentAreaProtected;
	}

	public String getLostMeanProbability() {
		return lostMeanProbability;
	}

	public void setLostMeanProbability(String lostMeanProbability) {
		this.lostMeanProbability = lostMeanProbability;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof InfoSpecieModel) {
			InfoSpecieModel to = (InfoSpecieModel) obj;
			return to.id.equals(this.getId());
		}
		return false;
	}
	@Override
	public int hashCode() {		
		return id.hashCode();
	}

	@Override
	public int compareTo(InfoSpecieModel o) {
		// TODO Auto-generated method stub
		return (this.getAccessPopMean()+this.getAccessPopSd()+this.getAggregateMean()+this.getAggregateSd()+this.getAvgAucTest()+this.getAvgAucTrain()+this.getClassName()+this.getConvAgMean()+this.getConvAgSd()+this.getFamilyName()+this.getFiresMean()+this.getFiresSd()+this.getGenusName()+this.getGrazingMean()+this.getGrazingSd()+this.getId()+this.getInfrastrMean()+this.getInfrastrSd()+this.getLostMeanProbability()+this.getNotLostMeanProbability()+this.getNumberPoints()+this.getOccProbMean()+this.getOccProbMeanOut()+this.getOccProbSd()+this.getOccProbSdOut()+this.getOilGasMean()+this.getOilGasSd()+this.getPercentAreaProtected()+this.getPercentLost()+this.getPrevalence()+this.getRecConvMean()+this.getRecConvSd()+this.getSdAuc()+this.getSpecieName()+this.getClass()).compareTo(o.getAccessPopMean()+o.getAccessPopSd()+o.getAggregateMean()+o.getAggregateSd()+o.getAvgAucTest()+o.getAvgAucTrain()+o.getClassName()+o.getConvAgMean()+o.getConvAgSd()+o.getFamilyName()+o.getFiresMean()+o.getFiresSd()+o.getGenusName()+o.getGrazingMean()+o.getGrazingSd()+o.getId()+o.getInfrastrMean()+o.getInfrastrSd()+o.getLostMeanProbability()+o.getNotLostMeanProbability()+o.getNumberPoints()+o.getOccProbMean()+o.getOccProbMeanOut()+o.getOccProbSd()+o.getOccProbSdOut()+o.getOilGasMean()+o.getOilGasSd()+o.getPercentAreaProtected()+o.getPercentLost()+o.getPrevalence()+o.getRecConvMean()+o.getRecConvSd()+o.getSdAuc()+o.getSpecieName()+o.getClass());
	}
		
	

}
