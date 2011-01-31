package model;

public class TaxonObject implements Comparable<TaxonObject> {
	private String id;
	private String canonical;
	private int rankID;	
	
	public TaxonObject() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCanonical() {
		return canonical;
	}
	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}
	public int getRankID() {
		return rankID;
	}
	public void setRankID(int rankID) {
		this.rankID = rankID;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TaxonObject) {
			TaxonObject to = (TaxonObject) obj;
			return to.id.equals(this.getId());
		}
		return false;
	}
	@Override
	public int hashCode() {		
		return id.hashCode();
	}
	@Override
	public int compareTo(TaxonObject o) {
		
		return (this.getCanonical()+this.getRankID()).compareTo(o.getCanonical()+o.getRankID());
	}
	
}
