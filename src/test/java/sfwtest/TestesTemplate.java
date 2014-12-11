package sfwtest;

import java.util.ArrayList;
import java.util.List;

import sfw.elasticsearch.dto.PageBean;
import sfw.elasticsearch.entity.InsertDataFormats;
import sfw.elasticsearch.entity.MappingTypes;
import sfw.elasticsearch.entity.QueryDataFormats;
import sfw.elasticsearch.entity.QueryDataFormats.DataRelation;
import sfw.elasticsearch.util.BaseCoreIndexs;
import sfw.elasticsearch.util.BaseCoreStores;
import sfw.elasticsearch.util.BaseCoreTypes;
import junit.framework.TestCase;

public class TestesTemplate extends TestCase {

	public void testAdd() throws Exception{
//		List<MappingTypes> list = new ArrayList<MappingTypes>();
//		MappingTypes map = new MappingTypes();
//		map.setFieldName("discount");
//		map.setType(BaseCoreTypes.Double);
//		map.setIndex(BaseCoreIndexs.not_analyzed);
//		map.setStore(BaseCoreStores.TRUE);
//		list.add(map);
//		System.out.println(list.toString());
//		PageBean page = new PageBean();
//		page.setCurrentPage(2);
//		page.setPageSize(10);
//		System.out.println(page.toString());
//		List<InsertDataFormats> list = new ArrayList<InsertDataFormats>();
//		list.add(new InsertDataFormats("title","test"));
//		list.add(new InsertDataFormats("test","123"));
//		System.out.println(list.toString());
		List<QueryDataFormats> list = new ArrayList<QueryDataFormats>();
		list.add(new QueryDataFormats("citys","上海",QueryDataFormats.DataRelation.OR));
		list.add(new QueryDataFormats("cate","酒店",QueryDataFormats.DataRelation.OR));
		list.add(new QueryDataFormats("expire","2014-12-24",QueryDataFormats.DataRelation.AND));
		System.out.println(list.toString());
		
	}
	
}
