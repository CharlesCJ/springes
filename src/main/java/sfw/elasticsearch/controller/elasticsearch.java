package sfw.elasticsearch.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import sfw.elasticsearch.common.CommonJSON;
import sfw.elasticsearch.common.Messages;
import sfw.elasticsearch.dto.PageBean;
import sfw.elasticsearch.entity.InsertDataFormats;
import sfw.elasticsearch.entity.QueryDataFormats;
import sfw.elasticsearch.entity.MappingTypes;
import sfw.elasticsearch.util.EsTemplate;

@Controller
public class elasticsearch {
	@Autowired
	private EsTemplate esTemplate;
	
	@RequestMapping(value = "/createIndices")
	@ResponseBody
	public String createIndices(String indices){
		if("".equals(indices) || indices == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if(esTemplate.indexExists(indices))
			return new CommonJSON(Messages.INDEX_EXISTS).toString();
		if(esTemplate.createIndices(indices))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/createMapping")
	@ResponseBody
	public String createMapping(String indexName, String type, String fieldTypes){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if("".equals(fieldTypes) || fieldTypes == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		try {
			Gson gson = new Gson();
			List<MappingTypes> jsons = gson.fromJson(fieldTypes, new TypeToken<List<MappingTypes>>() {}.getType());
			if(esTemplate.createMapping(indexName, type, jsons))
				return new CommonJSON(Messages.SUCCESS).toString();
		} catch (IOException e) {
			return new CommonJSON(Messages.ERROR).toString();
		}
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/getMapping")
	@ResponseBody
	public String getMapping(String indexName, String type){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		Map mappings = esTemplate.getMapping(indexName, type);
		if(Messages.ERROR.getDescription().equals(mappings.get(Messages.ERROR.getCode())))
			return new CommonJSON(Messages.ERROR).toString();
		return new CommonJSON(Messages.SUCCESS,mappings.toString()).toString();
	}
	
	@RequestMapping(value = "/index")
	@ResponseBody
	public String index(String indexName, String type, String dataFormatsStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if("".equals(dataFormatsStr) || dataFormatsStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<InsertDataFormats> jsons = gson.fromJson(dataFormatsStr, new TypeToken<List<InsertDataFormats>>() {}.getType());
		if(esTemplate.index(indexName, type, jsons))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/bulkIndex")
	@ResponseBody
	public String bulkIndex(String indexName, String type, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<List<InsertDataFormats>> jsons = gson.fromJson(jsonStr, new TypeToken<List<List<InsertDataFormats>>>() {}.getType());
		
		if(esTemplate.bulkIndex(indexName, type, jsons))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(String indexName, String type, String id){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if("".equals(id) || id == null)
			return new CommonJSON(Messages.NO_ID_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		
		if(esTemplate.delete(indexName, type, id))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/deleteIndex")
	@ResponseBody
	public String deleteIndex(String indexName){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(esTemplate.deleteIndex(indexName))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/deleteType")
	@ResponseBody
	public String deleteType(String indexName, String type){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		if(esTemplate.deleteType(indexName, type))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/deleteByQuery")
	@ResponseBody
	public String deleteByQuery(String indexName, String type, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<QueryDataFormats> jsons = gson.fromJson(jsonStr, new TypeToken<List<QueryDataFormats>>() {}.getType());
		if(esTemplate.deleteByQuery(indexName, type, jsons))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
	@RequestMapping(value = "/queryById")
	@ResponseBody
	public String queryById(String indexName, String id){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if("".equals(id) || id == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		
		String result = esTemplate.queryById(indexName, id);
		if(result.equals(Messages.ERROR.toString()))
			return new CommonJSON(Messages.ERROR).toString();
		return new CommonJSON(Messages.SUCCESS,result).toString();
	}
	
	@RequestMapping(value = "/queryByI")
	@ResponseBody
	public String queryByI(String indexName, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<QueryDataFormats> jsons = gson.fromJson(jsonStr, new TypeToken<List<QueryDataFormats>>() {}.getType());
		List<String> result = esTemplate.query(indexName, jsons);
		if(result.contains(Messages.ERROR.toString()))
			return new CommonJSON(Messages.ERROR).toString();
		return new CommonJSON(Messages.SUCCESS,result.toString()).toString();
		
	}
	
	@RequestMapping(value = "/queryByIT")
	@ResponseBody
	public String queryByIT(String indexName,String type, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<QueryDataFormats> jsons = gson.fromJson(jsonStr, new TypeToken<List<QueryDataFormats>>() {}.getType());
		List<String> result = esTemplate.query(indexName,type, jsons);
		if(result.contains(Messages.ERROR.toString()))
			return new CommonJSON(Messages.ERROR).toString();
		return new CommonJSON(Messages.SUCCESS,result.toString()).toString();
	}
	
	@RequestMapping(value = "/queryByIFS")
	@ResponseBody
	public String queryByIFS(String indexName,String pageBean, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<QueryDataFormats> jsons = gson.fromJson(jsonStr, new TypeToken<List<QueryDataFormats>>() {}.getType());
		PageBean page = gson.fromJson(pageBean, new TypeToken<PageBean>() {}.getType());
		int currentPage = page.getCurrentPage();
		int size = page.getPageSize();
		List<String> result = esTemplate.query(indexName,(currentPage-1)*size,size, jsons);
		PageBean bean = new PageBean();
		
		bean.setCurrentPage(currentPage);
		bean.setPageSize(size);
		bean.setTotalRows(esTemplate.query(indexName, jsons).size());
		bean.setResult(result);
		if(result.contains(Messages.ERROR.toString()))
			return new CommonJSON(Messages.ERROR).toString();
		return new CommonJSON(Messages.SUCCESS,bean.toString()).toString();
	}
	
	@RequestMapping(value = "/queryByITFS")
	@ResponseBody
	public String queryByITFS(String indexName,String type,String pageBean, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<QueryDataFormats> jsons = gson.fromJson(jsonStr, new TypeToken<List<QueryDataFormats>>() {}.getType());
		PageBean page = gson.fromJson(pageBean, new TypeToken<PageBean>() {}.getType());
		int currentPage = page.getCurrentPage();
		int size = page.getPageSize();
		List<String> result = esTemplate.query(indexName,type,(currentPage-1)*size,size, jsons);
		PageBean bean = new PageBean();
		
		bean.setCurrentPage(currentPage);
		bean.setPageSize(size);
		bean.setTotalRows(esTemplate.query(indexName,type, jsons).size());
		bean.setResult(result);
		
		if(result.contains(Messages.ERROR.toString()))
			return new CommonJSON(Messages.ERROR).toString();
		return new CommonJSON(Messages.SUCCESS,bean.toString()).toString();
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(String indexName, String type, String id, String jsonStr){
		if("".equals(indexName) || indexName == null )
			return new CommonJSON(Messages.NO_INDEX_DEFINE).toString();
		if("".equals(type) || type == null )
			return new CommonJSON(Messages.NO_TYPE_DEFINE).toString();
		if("".equals(id) || id == null)
			return new CommonJSON(Messages.NO_ID_DEFINE).toString();
		if(!esTemplate.indexExists(indexName))
			return new CommonJSON(Messages.NO_INDEX_EXISTS).toString();
		if(!esTemplate.typeExists(indexName, type))
			return new CommonJSON(Messages.NO_TYPE_EXISTS).toString();
		if("".equals(jsonStr) || jsonStr == null )
			return new CommonJSON(Messages.NOT_EMPTY).toString();
		
		Gson gson = new Gson();
		List<InsertDataFormats> jsons = gson.fromJson(jsonStr, new TypeToken<List<InsertDataFormats>>() {}.getType());
		if(esTemplate.update(indexName, type, id, jsons))
			return new CommonJSON(Messages.SUCCESS).toString();
		return new CommonJSON(Messages.ERROR).toString();
	}
	
}
