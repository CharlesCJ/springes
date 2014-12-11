package sfw.elasticsearch.entity;

import java.util.List;

import sfw.elasticsearch.util.BaseCoreDateFormats;
import sfw.elasticsearch.util.BaseCoreIndexAnalyzers;
import sfw.elasticsearch.util.BaseCoreIndexs;
import sfw.elasticsearch.util.BaseCoreStores;
import sfw.elasticsearch.util.BaseCoreTypes;
import net.sf.json.JSONObject;

public class MappingTypes {
	private String fieldName;
	private BaseCoreTypes type;
	private BaseCoreIndexs index;
	private BaseCoreStores store;
	private BaseCoreDateFormats format;
	private BaseCoreIndexAnalyzers indexAnalyzer;
	private List<MappingTypes> nestedMappingTypes;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public BaseCoreTypes getType() {
		return type;
	}
	public void setType(BaseCoreTypes type) {
		this.type = type;
	}
	public BaseCoreIndexs getIndex() {
		return index;
	}
	public void setIndex(BaseCoreIndexs index) {
		this.index = index;
	}
	public BaseCoreStores getStore() {
		return store;
	}
	public void setStore(BaseCoreStores store) {
		this.store = store;
	}
	public BaseCoreDateFormats getFormat() {
		return format;
	}
	public void setFormat(BaseCoreDateFormats format) {
		this.format = format;
	}
	public BaseCoreIndexAnalyzers getIndexAnalyzer() {
		return indexAnalyzer;
	}
	public void setIndexAnalyzer(BaseCoreIndexAnalyzers indexAnalyzer) {
		this.indexAnalyzer = indexAnalyzer;
	}
	public List<MappingTypes> getNestedMappingTypes() {
		return nestedMappingTypes;
	}
	public void setNestedMappingTypes(List<MappingTypes> nestedMappingTypes) {
		this.nestedMappingTypes = nestedMappingTypes;
	}
	
	
	@Override
	public String toString() {
		JSONObject job = JSONObject.fromObject(this);
        return job.toString();
	}
	
}
