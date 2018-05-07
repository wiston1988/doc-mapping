
package org.doc.mapping.io.jdkAdapter;

import org.doc.mapping.io.DocumentWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class DocumentBufferWriter implements DocumentWriter<String> {

	private final BufferedWriter bw;
	private boolean isAppend = false;
	
	public DocumentBufferWriter(Writer writer){
		bw = new BufferedWriter(writer);
	}
	
	@Override
	public void flush() throws IOException {
		bw.flush();
	}

	@Override
	public void writeAll(List<String> allLines) throws IOException {
		if(allLines == null) {
			return;
		}
		
		int size = allLines.size();
		
		for (int i = 0; i < size; i++) {
			writeNext(allLines.get(i));
		}
	}

	@Override
	public void writeNext(String nextLine) throws IOException {
		if(isAppend == false)//After first time for writing, set append to true
		{
			isAppend = true;
		} else//If appendant write(not first line), start from new line
		{
			bw.newLine();
		}
		
		bw.write(nextLine);
	}

	@Override
	public void close() throws IOException {
		flush();
		bw.close();
	}

}
