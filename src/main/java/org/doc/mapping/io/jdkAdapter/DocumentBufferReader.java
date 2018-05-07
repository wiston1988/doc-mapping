package org.doc.mapping.io.jdkAdapter;

import org.doc.mapping.io.DocumentReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DocumentBufferReader implements DocumentReader<String> {

	private BufferedReader br;
	private boolean hasNext = true;
	
	public DocumentBufferReader(Reader reader){
		br = new BufferedReader(reader);
	}
	
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public List<String> readAll() throws IOException {
		List<String> allElements = new ArrayList<String>();
		
		while (hasNext) {
			String line = readNext();
			if(line != null)
				allElements.add(line);
		}
		
		return allElements;
	}

	@Override
	public String readNext() throws IOException {
		String line = br.readLine();
		if(line == null) {
			hasNext = false;
		}
		
		if(line == null || line.length() == 0) {
			return null;
		}
		
		return line;
	}

	@Override
	public void close() throws IOException {
		if(br != null) {
			br.close();
		}
	}
}
