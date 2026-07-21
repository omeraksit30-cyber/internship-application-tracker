package com.omeraksit.internshiptracker.dto.response;

import java.util.ArrayList;
import java.util.List;

public class PagedResponse<T> {

	private List<T> content = new ArrayList<>();
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private int numberOfElements;
	private boolean first;
	private boolean last;
	private boolean empty;

	public PagedResponse() {
	}

	public PagedResponse(
			List<T> content,
			int page,
			int size,
			long totalElements,
			int totalPages,
			int numberOfElements,
			boolean first,
			boolean last,
			boolean empty) {
		this.content = content == null ? new ArrayList<>() : content;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.numberOfElements = numberOfElements;
		this.first = first;
		this.last = last;
		this.empty = empty;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content == null ? new ArrayList<>() : content;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
}
