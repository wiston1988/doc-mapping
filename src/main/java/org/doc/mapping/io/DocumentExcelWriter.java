package org.doc.mapping.io;

import java.io.IOException;

public interface DocumentExcelWriter extends DocumentWriter<Object[]> {
	void writeNext(String[] nextLine, int[] cellTypes)throws IOException;
}
