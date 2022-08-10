package com.redcompany.receita.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "meta")
public class Meta {
	
	private Integer page;
	
	private Integer pages;
	
	private Integer perpage;
	
	private Integer total;
	
	private String sort;
	
	private String field;
	
	private String generalSearch;
	
	public Meta(){};

	public Meta(int total){
		this.page = 1;
		this.pages = 1;
		this.perpage = -1;
		this.total = total;
		this.sort = null;
		this.field = null;
		this.generalSearch = null;
	}

	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getPerpage() {
		return perpage;
	}

	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getGeneralSearch() {
		return generalSearch;
	}

	public void setGeneralSearch(String generalSearch) {
		this.generalSearch = generalSearch;
	}

	@Override
	public String toString() {
		return "Meta [page=" + page + ", pages=" + pages + ", perpage=" + perpage + ", total=" + total + ", sort="
				+ sort + ", field=" + field + ", generalSearch=" + generalSearch + "]";
	}	

	
}

