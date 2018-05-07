package org.doc.mapping.io.enhanced;

import org.doc.mapping.io.DocumentReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RowLimitDocumentReader<T> implements DocumentReader {

	private DocumentReader<T> reader;
	private int LimitNum;
	
	private int currLine = 0;
	
	public RowLimitDocumentReader(DocumentReader<T> reader, int LimitNum){
		this.reader = reader;
		this.LimitNum = LimitNum;
	}
	
	@Override
	public boolean hasNext() {
		return currLine < LimitNum && reader != null && reader.hasNext();
	}

	@Override
	public List<T> readAll() throws IOException {
		List<T> lines = new ArrayList<T>();
		if(!hasNext()) {
			return lines;
		}
		
		for(int i=currLine;i<LimitNum;i++){
			T line = readNext();
			if(line != null) {
				lines.add(line);
			}
		}
		
		return lines;
	}

	@Override
	public T readNext() throws IOException {
		if(hasNext()){
			T line = reader.readNext();
			currLine += 1;
			return line;
		}
		
		return null;
	}

	public void reWind(){
		currLine = 0;
	}
	
	@Override
	public void close() throws IOException {
		if(reader != null) {
			reader.close();
		}
	}
}
