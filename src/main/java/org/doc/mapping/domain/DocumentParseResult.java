package org.doc.mapping.domain;

import java.util.ArrayList;
import java.util.List;

public class DocumentParseResult {

	private List records = new ArrayList();
	private int totalCount;
	private int discardCount;

	public DocumentParseResult() {
	}

	public DocumentParseResult(List records, int totalCount, int discardCount) {
		this.records = records;
		this.totalCount = totalCount;
		this.discardCount = discardCount;
	}

	public List getRecords() {
		return records;
	}

	public void setRecords(List records) {
		this.records = records;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getDiscardCount() {
		return discardCount;
	}

	public void setDiscardCount(int discardCount) {
		this.discardCount = discardCount;
	}
}
