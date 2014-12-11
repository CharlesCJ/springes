package sfw.elasticsearch.entity;

import net.sf.json.JSONObject;

public class QueryDataFormats {

	private String field;
	private Object fieldValue;
	private Greater greater;
	private Object gValue;
	private Lesser lesser;
	private Object lValue;
	private RangeRelation rangeRelation;	//范围数据的左右区间关系为and或or
	private DataRelation dataRelation;		//单条数据中的关系为and或or
	private Object lat;
	private Object lon;
	private String from;
	private String to;
	private boolean includeLower;
	private boolean includeUpper;
	
	public QueryDataFormats(){}
	
	public QueryDataFormats(String field, Object fieldValue,DataRelation dataRelation){
		this.field = field;
		this.fieldValue = fieldValue;
		this.dataRelation = dataRelation;
	}
	
	public QueryDataFormats(String field,Greater greater,Object gValue){
		this.field = field;
		this.greater = greater;
		this.gValue = gValue;
	}
	public QueryDataFormats(String field,Lesser lesser,Object lValue){
		this.field = field;
		this.lesser = lesser;
		this.lValue = lValue;
	}
	public QueryDataFormats(String field,Greater greater,Object gValue,
			Lesser lesser,Object lValue,RangeRelation rangeRelation){
		this.field = field;
		this.lesser = lesser;
		this.lValue = lValue;
		this.greater = greater;
		this.gValue = gValue;
		this.rangeRelation = rangeRelation;
	}
	public QueryDataFormats(String field,Object lat,Object lon,
			String from,String to,boolean includeLower,boolean includeUpper){
		this.field = field;
		this.lat = lat;
		this.lon = lon;
		this.from = from;
		this.to = to;
		this.includeLower = includeLower;
		this.includeUpper = includeUpper;
	}
	
	public enum RangeRelation {
		AND,OR
	}
	public enum DataRelation{
		AND,OR
	}
	public enum OptimizeBbox{
		memory,indexed,none
	}
	public enum GeoDistance{
		PLANE,FACTOR,ARC,SLOPPY_ARC
	}
	public enum Greater{
		gt,gte
	}
	public enum Lesser{
		lt,lte
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Greater getGreater() {
		return greater;
	}
	public void setGreater(Greater greater) {
		this.greater = greater;
	}
	public Lesser getLesser() {
		return lesser;
	}
	public void setLesser(Lesser lesser) {
		this.lesser = lesser;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	public Object getgValue() {
		return gValue;
	}
	public void setgValue(Object gValue) {
		this.gValue = gValue;
	}
	public Object getlValue() {
		return lValue;
	}
	public void setlValue(Object lValue) {
		this.lValue = lValue;
	}
	public Object getLat() {
		return lat;
	}
	public void setLat(Object lat) {
		this.lat = lat;
	}
	public Object getLon() {
		return lon;
	}
	public void setLon(Object lon) {
		this.lon = lon;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public boolean isIncludeLower() {
		return includeLower;
	}
	public void setIncludeLower(boolean includeLower) {
		this.includeLower = includeLower;
	}
	public boolean isIncludeUpper() {
		return includeUpper;
	}
	public void setIncludeUpper(boolean includeUpper) {
		this.includeUpper = includeUpper;
	}
	public RangeRelation getRangeRelation() {
		return rangeRelation;
	}
	public void setRangeRelation(RangeRelation rangeRelation) {
		this.rangeRelation = rangeRelation;
	}
	public DataRelation getDataRelation() {
		return dataRelation;
	}
	public void setDataRelation(DataRelation dataRelation) {
		this.dataRelation = dataRelation;
	}

	
	@Override
    public String toString() {
        JSONObject job = JSONObject.fromObject(this);
        return job.toString();
    }

}
