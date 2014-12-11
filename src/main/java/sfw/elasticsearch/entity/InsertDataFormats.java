package sfw.elasticsearch.entity;

import net.sf.json.JSONObject;

public class InsertDataFormats {

	private String field;
	private Object fieldValue;
	
	public InsertDataFormats(){}
	
	public InsertDataFormats(String field, Object fieldValue){
		this.field = field;
		this.fieldValue = fieldValue;
	}
	
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	@Override
    public String toString() {
        JSONObject job = JSONObject.fromObject(this);
        return job.toString();
    }
}
