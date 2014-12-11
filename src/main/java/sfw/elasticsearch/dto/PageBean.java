package sfw.elasticsearch.dto;

import java.util.List;

import net.sf.json.JSONObject;

public class PageBean {
	
	private int currentPage = 1;// 当前页数

	public int totalPages = 0;// 总页数

	private int pageSize = 10;// 每页显示数

	private int totalRows = 0;// 总数据数

	private boolean hasNextPage = true;// 是否有下一页

	private boolean hasPreviousPage = true;// 是否有前一页 
	
	private List<String> result;//返回结果

	public int getCurrentPage() {
		if(currentPage<1){
			currentPage=1;
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		if(totalRows%pageSize==0){
			setTotalPages(totalRows/pageSize);
		}else{
			setTotalPages(totalRows/pageSize+1);
		}
		if(currentPage>getTotalPages()){
			currentPage = getTotalPages();
		}
		if(getCurrentPage() >=getTotalPages()){
			hasNextPage=false;
		}
		if(currentPage==1){
			hasPreviousPage=false;
		}
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}
	
	public String toString(){
		JSONObject job = JSONObject.fromObject(this);
        return job.toString();
	}
}