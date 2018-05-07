package org.doc.mapping.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface DocumentWriter<T> extends Closeable {

	void writeAll(List<T> allLines)throws IOException;
	
	void writeNext(T nextLine)throws IOException;
	
	void flush()throws IOException;
}
