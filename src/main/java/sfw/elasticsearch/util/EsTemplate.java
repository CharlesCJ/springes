package sfw.elasticsearch.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import sfw.elasticsearch.common.Messages;
import sfw.elasticsearch.entity.InsertDataFormats;
import sfw.elasticsearch.entity.MappingTypes;
import sfw.elasticsearch.entity.QueryDataFormats;
import sfw.elasticsearch.entity.QueryDataFormats.OptimizeBbox;

@Component
public class EsTemplate {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private Client client;
	/**
	 * 指定str创建空indices
	 * @param indices
	 */
	public boolean createIndices(String indices){
		return elasticsearchTemplate.createIndex(indices);
	}
	/**
	 * 是否存在indices
	 * @param indices
	 * @return boolean
	 */
	public boolean indexExists(String indices){
		return elasticsearchTemplate.indexExists(indices);
	}
	/**
	 * 是否存在type
	 * @param indices
	 * @return boolean
	 */
	public boolean typeExists(String indices,String type){
		return elasticsearchTemplate.typeExists(indices, type);
	}
	/**
	 * 创建mapping
	 * @param indexName
	 * @param type
	 * @param fieldTypes
	 * @throws IOException 
	 */
	public boolean createMapping(String indexName, String type, List<MappingTypes> fieldTypes) throws IOException{
		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties");
		try {
			for(MappingTypes ft : fieldTypes){
				mapping.startObject(ft.getFieldName());
				if(ft.getFormat()!=null)
					mapping.field(BaseCoreKeys.format.toString(),ft.getFormat());
				if(ft.getStore()!=null)
					mapping.field(BaseCoreKeys.store.toString(),ft.getStore());
				if(ft.getType()!=null){
					mapping.field(BaseCoreKeys.type.toString(),ft.getType());
				}
				if(ft.getIndex()!=null){
					mapping.field(BaseCoreKeys.index.toString(),ft.getIndex());
					if(BaseCoreIndexs.analyzed.equals(ft.getIndex()))
						mapping.field(BaseCoreKeys.index_analyzer.toString(),BaseCoreIndexAnalyzers.ik.toString());
				}
				if(ft.getNestedMappingTypes().size() != 0){
					mapping.startObject("properties");
					for(MappingTypes nestedft : ft.getNestedMappingTypes()){
						mapping.startObject(nestedft.getFieldName());
						if(nestedft.getFormat()!=null)
							mapping.field(BaseCoreKeys.format.toString(),nestedft.getFormat());
						if(nestedft.getStore()!=null)
							mapping.field(BaseCoreKeys.store.toString(),nestedft.getStore());
						if(nestedft.getType()!=null)
							mapping.field(BaseCoreKeys.type.toString(),nestedft.getType());
						if(nestedft.getIndex()!=null){
							mapping.field(BaseCoreKeys.index.toString(),nestedft.getIndex());
							if(BaseCoreIndexs.analyzed.equals(nestedft.getIndex()))
								mapping.field(BaseCoreKeys.index_analyzer.toString(),BaseCoreIndexAnalyzers.ik.toString());
						}
						mapping.endObject();
					}
					mapping.endObject();
				}
				mapping.endObject();
			}
			
			mapping.endObject().endObject();
		} catch (Exception e) {
			return false;
		}
		return elasticsearchTemplate.putMapping(indexName, type, mapping);
	}
	/**
	 * 根据indexname和type获取mapping
	 * @param indexName
	 * @param type
	 * @return
	 */
	public Map<?, ?> getMapping(String indexName, String type) {
		Map mappings = null;
		try {
			mappings = elasticsearchTemplate.getMapping(indexName, type);
			return mappings;
		} catch (Exception e) {
			mappings.put(Messages.ERROR.getCode(), Messages.ERROR.getDescription());
			return mappings;
		}
	}
	/**
	 * 插入一条数据
	 * @param indexName
	 * @param type
	 * @param json
	 */
	public boolean index(String indexName, String type, List<InsertDataFormats> dataFormats){
		IndexQuery indexQuery = new IndexQuery();
		try {
			JSONObject json = new JSONObject();
			indexQuery.setIndexName(indexName);
			indexQuery.setType(type);
			for(InsertDataFormats df : dataFormats){
				if("_id".equalsIgnoreCase(df.getField()))
					indexQuery.setId(df.getFieldValue().toString());
				if(df.getField()!=null || "".equals(df.getField()))
					json.put(df.getField(), df.getFieldValue());
				else
					return false;
			}
			indexQuery.setSource(json.toString());
			elasticsearchTemplate.index(indexQuery);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 批量插入数据
	 * @param indexName
	 * @param type
	 * @param bulkIndexs
	 */
	public boolean bulkIndex(String indexName, String type, List<List<InsertDataFormats>> bulkIndexs){
		IndexQuery indexQuery = new IndexQuery();
		try {
			List<IndexQuery> indexQuerys = new ArrayList<IndexQuery>();
			JSONObject json = new JSONObject();
			
			int len = bulkIndexs.size();
			for(int i=0; i!=len; ++i){
				indexQuery.setIndexName(indexName);
				indexQuery.setType(type);
				for(InsertDataFormats df:bulkIndexs.get(i)){
					if("_id".equalsIgnoreCase(df.getField()))
						indexQuery.setId(df.getFieldValue().toString());
					if(df.getField()!=null || "".equals(df.getField()))
						json.put(df.getField(), df.getFieldValue());
					else 
						return false;
				}
				indexQuery.setSource(json.toString());
				indexQuerys.add(indexQuery);
				indexQuery = new IndexQuery();
				json.clear();
//				if(i % 500 == 0 && i !=0){
//					elasticsearchTemplate.bulkIndex(indexQuerys);
//					indexQuerys.clear();
//				}
			}
			elasticsearchTemplate.bulkIndex(indexQuerys);
			return true;
		} catch (Exception e) {
			if (e instanceof ElasticsearchException) {
				System.out.println(((ElasticsearchException) e).getFailedDocuments());
			}
			return false;
		}
		
	}
	
	/**
	 * 指定indexname，type，id删除数据
	 * @param indexName
	 * @param type
	 * @param id
	 */
	public boolean delete(String indexName, String type, String id){
		try {
			elasticsearchTemplate.delete(indexName,type,id);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 删除indices
	 * @param indexName
	 */
	public boolean deleteIndex(String indexName){
		return elasticsearchTemplate.deleteIndex(indexName);
	}
	/**
	 * 删除指定indices的type
	 * @param indexName
	 * @param type
	 */
	public boolean deleteType(String indexName, String type){
		try {
			elasticsearchTemplate.deleteType(indexName, type);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 指定indices和type，删除查找到的记录
	 * @param indexName
	 * @param type
	 * @param deletes
	 */
	public boolean deleteByQuery(String indexName, String type, List<QueryDataFormats> deletes){
		try {
			DeleteQuery deleteQuery = new DeleteQuery();
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
			BoolFilterBuilder fb = FilterBuilders.boolFilter();
			FilteredQueryBuilder query = null;
			boolean isRelastions = false;
			
			for(QueryDataFormats df : deletes){
				if(df.getFieldValue() != null){
					QueryBuilder qb = QueryBuilders.matchQuery(df.getField(),
							df.getFieldValue()).operator(Operator.AND);
					if(QueryDataFormats.DataRelation.AND.equals(df.getDataRelation()))
						boolQuery.must(qb);
					else{
						isRelastions =true;
						fb.should(FilterBuilders.queryFilter(qb));
					}
				}else if(df.getLat() == null){
					isRelastions = true;
					if(QueryDataFormats.RangeRelation.AND.equals(df.getRangeRelation())){
						if(QueryDataFormats.Lesser.lt.equals(df.getLesser()))
							fb.must(FilterBuilders.rangeFilter(df.getField()).lt(df.getlValue()));
						else
							fb.must(FilterBuilders.rangeFilter(df.getField()).lte(df.getlValue()));
						if(QueryDataFormats.Greater.gt.equals(df.getGreater()))
							fb.must(FilterBuilders.rangeFilter(df.getField()).gt(df.getgValue()));
						else
							fb.must(FilterBuilders.rangeFilter(df.getField()).gte(df.getgValue()));
					}else if(QueryDataFormats.RangeRelation.OR.equals(df.getRangeRelation())){
						if(QueryDataFormats.Lesser.lt.equals(df.getLesser()))
							fb.should(FilterBuilders.rangeFilter(df.getField()).lt(df.getlValue()));
						else
							fb.should(FilterBuilders.rangeFilter(df.getField()).lte(df.getlValue()));
						if(QueryDataFormats.Greater.gt.equals(df.getGreater()))
							fb.should(FilterBuilders.rangeFilter(df.getField()).gt(df.getgValue()));
						else
							fb.should(FilterBuilders.rangeFilter(df.getField()).gte(df.getgValue()));
					}else{
						if(QueryDataFormats.Lesser.lt.equals(df.getLesser()))
							fb.must(FilterBuilders.rangeFilter(df.getField()).lt(df.getlValue()));
						else if(QueryDataFormats.Lesser.lte.equals(df.getLesser()))
							fb.must(FilterBuilders.rangeFilter(df.getField()).lte(df.getlValue()));
						else if(QueryDataFormats.Greater.gt.equals(df.getGreater()))
							fb.must(FilterBuilders.rangeFilter(df.getField()).gt(df.getlValue()));
						else
							fb.must(FilterBuilders.rangeFilter(df.getField()).gte(df.getlValue()));
					}
				}else{
					isRelastions = true;
					fb.must(FilterBuilders.geoDistanceRangeFilter(df.getField())
							.point(Double.parseDouble(df.getLat().toString()),Double.parseDouble(df.getLon().toString()))
							.from(df.getFrom()).to(df.getTo())
							.includeLower(df.isIncludeLower()).includeUpper(df.isIncludeUpper())
							.optimizeBbox(OptimizeBbox.memory.toString())
							.geoDistance(GeoDistance.ARC));
				}
			}
			deleteQuery.setIndex(indexName);
			deleteQuery.setType(type);
			if(isRelastions){
				query = QueryBuilders.filteredQuery( boolQuery,fb);
				deleteQuery.setQuery(query);
			}else {
				deleteQuery.setQuery(boolQuery);
			}
			elasticsearchTemplate.delete(deleteQuery);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 根据id查找制定数据
	 * @param indexName
	 * @param id
	 * @return
	 */
	public String queryById(String indexName, String id){
		String result = null;
		try{
			SearchRequestBuilder builder= client.prepareSearch(indexName);
			QueryBuilder qb = QueryBuilders.idsQuery().ids(id);
			builder.setQuery(qb);
			SearchResponse responsesearch = builder.execute().actionGet();
			result= responsesearch.getHits().getHits()[0].getSourceAsString();
		}catch(Exception es){
			return Messages.ERROR.toString();
		}
		return result;
	}

	/**
	 * 指定indices，查找field值为value的所有记录
	 * @param indexName
	 * @param querys
	 * @param querys
	 * @return
	 */
	public List<String> query(String indexName, List<QueryDataFormats> querys){
		List<String> result = new ArrayList<String>();
		SearchRequestBuilder builder= client.prepareSearch(indexName);	
		
		try{
			SearchResponse responsesearch = builder.setQuery(CommonQuery(querys))
					.setSize(Integer.MAX_VALUE).execute().actionGet();
			SearchHit[] shit = responsesearch.getHits().getHits();
			for(SearchHit sh:shit){
				result.add(sh.getSourceAsString());
			}
			return result;
		}catch(Exception es){
			result.add(Messages.ERROR.toString());
			return result;
		}
		
	}
	/**
	 * 指定indices和type，查找field值为value的所有记录
	 * @param indexName
	 * @param querys
	 * @param querys
	 * @return
	 */
	public List<String> query(String indexName,String type, List<QueryDataFormats> querys){
		List<String> result = new ArrayList<String>();
		SearchRequestBuilder builder= client.prepareSearch(indexName).setTypes(type);	
		
		try{
			SearchResponse responsesearch = builder.setQuery(CommonQuery(querys))
					.setSize(Integer.MAX_VALUE).execute().actionGet();
			SearchHit[] shit = responsesearch.getHits().getHits();
			for(SearchHit sh:shit){
				result.add(sh.getSourceAsString());
			}
			return result;
		}catch(Exception es){
			result.add(Messages.ERROR.toString());
			return result;
		}
	}
	/**
	 * 指定indices，查找field值为value的从from开始，数量为size的记录。
	 * @param indexName
	 * @param from
	 * @param size
	 * @param querys
	 * @param querys
	 * @return
	 */
	public List<String> query(String indexName,int from,int size, List<QueryDataFormats> querys){
		List<String> result = new ArrayList<String>();
		SearchRequestBuilder builder= client.prepareSearch(indexName);	
		
		try{
			SearchResponse responsesearch = builder.setQuery(CommonQuery(querys))
					.setFrom(from).setSize(size).execute().actionGet();
			SearchHit[] shit = responsesearch.getHits().getHits();
			for(SearchHit sh:shit){
				result.add(sh.getSourceAsString());
			}
			return result;
		}catch(Exception es){
			result.add(Messages.ERROR.toString());
			return result;
		}
	}
	/**
	 * 指定indices和type，查找field值为value的从from开始，数量为size的记录。
	 * @param indexName
	 * @param from
	 * @param size
	 * @param querys
	 * @param querys
	 * @return
	 */
	public List<String> query(String indexName,String type,int from,int size, List<QueryDataFormats> querys){
		List<String> result = new ArrayList<String>();
		SearchRequestBuilder builder= client.prepareSearch(indexName).setTypes(type);	
		
		try{
			SearchResponse responsesearch = builder.setQuery(CommonQuery(querys))
					.setFrom(from).setSize(size).execute().actionGet();
			SearchHit[] shit = responsesearch.getHits().getHits();
			for(SearchHit sh:shit){
				result.add(sh.getSourceAsString());
			}
			return result;
		}catch(Exception es){
			result.add(Messages.ERROR.toString());
			return result;
		}
	}
	
	/**
	 * query公共方法
	 * @param dataFormats
	 * @return
	 */
	public String CommonQuery(List<QueryDataFormats> dataFormats){
		BoolFilterBuilder fb = FilterBuilders.boolFilter();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		FilteredQueryBuilder query = null;
		boolean tmp = false;
		
		for(QueryDataFormats df:dataFormats){
			if(df.getFieldValue()!=null){
				QueryBuilder qb = QueryBuilders.matchQuery(df.getField(),
						df.getFieldValue()).operator(Operator.AND);
				if(QueryDataFormats.DataRelation.AND.equals(df.getDataRelation()))
					boolQuery.must(qb);
				else{
					tmp =true;
					fb.should(FilterBuilders.queryFilter(qb));
				}
			}else if(df.getLat() == null){
				tmp = true;
				if(QueryDataFormats.RangeRelation.AND.equals(df.getRangeRelation())){
					if(QueryDataFormats.Lesser.lt.equals(df.getLesser()))
						fb.must(FilterBuilders.rangeFilter(df.getField()).lt(df.getlValue()));
					else
						fb.must(FilterBuilders.rangeFilter(df.getField()).lte(df.getlValue()));
					if(QueryDataFormats.Greater.gt.equals(df.getGreater()))
						fb.must(FilterBuilders.rangeFilter(df.getField()).gt(df.getgValue()));
					else
						fb.must(FilterBuilders.rangeFilter(df.getField()).gte(df.getgValue()));
				}else if(QueryDataFormats.RangeRelation.OR.equals(df.getRangeRelation())){
					if(QueryDataFormats.Lesser.lt.equals(df.getLesser()))
						fb.should(FilterBuilders.rangeFilter(df.getField()).lt(df.getlValue()));
					else
						fb.should(FilterBuilders.rangeFilter(df.getField()).lte(df.getlValue()));
					if(QueryDataFormats.Greater.gt.equals(df.getGreater()))
						fb.should(FilterBuilders.rangeFilter(df.getField()).gt(df.getgValue()));
					else
						fb.should(FilterBuilders.rangeFilter(df.getField()).gte(df.getgValue()));
				}else{
					if(QueryDataFormats.Lesser.lt.equals(df.getLesser()))
						fb.must(FilterBuilders.rangeFilter(df.getField()).lt(df.getlValue()));
					else if(QueryDataFormats.Lesser.lte.equals(df.getLesser()))
						fb.must(FilterBuilders.rangeFilter(df.getField()).lte(df.getlValue()));
					else if(QueryDataFormats.Greater.gt.equals(df.getGreater()))
						fb.must(FilterBuilders.rangeFilter(df.getField()).gt(df.getlValue()));
					else
						fb.must(FilterBuilders.rangeFilter(df.getField()).gte(df.getlValue()));
				}
			}else{
				tmp = true;
				fb.must(FilterBuilders.geoDistanceRangeFilter(df.getField())
						.point(Double.parseDouble(df.getLat().toString()),Double.parseDouble(df.getLon().toString()))
						.from(df.getFrom()).to(df.getTo())
						.includeLower(df.isIncludeLower()).includeUpper(df.isIncludeUpper())
						.optimizeBbox(OptimizeBbox.memory.toString())
						.geoDistance(GeoDistance.ARC));
			}
		}
		
		if(tmp){
			query = QueryBuilders.filteredQuery( boolQuery,fb);
			return query.toString();
		}
		return boolQuery.toString();
		
	}
	
	/**
	 * 指定indices，type，id更新数据
	 * @param indexName
	 * @param type
	 * @param id
	 * @param update
	 */
	public boolean update(String indexName, String type, String id, List<InsertDataFormats> dataFormats){
		JSONObject update = new JSONObject();
		try {
			for(InsertDataFormats df:dataFormats){
				if(df.getFieldValue()!=null)
					update.put(df.getField(), df.getFieldValue());
				else
					return false;
			}
			
			UpdateRequestBuilder br = client.prepareUpdate(indexName,type,id);
			br.setDoc(update.toString().getBytes());
			br.execute().actionGet();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
