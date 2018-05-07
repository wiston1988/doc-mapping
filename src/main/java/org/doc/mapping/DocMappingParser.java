package org.doc.mapping;

import org.doc.mapping.domain.DocumentParseResult;
import org.doc.mapping.exception.MappingException;
import org.doc.mapping.io.DocumentReader;

import java.io.IOException;

/**
 * @author Chen Hui
 * @since 2018/4/18
 */
public interface DocMappingParser {
    DocumentParseResult parseDocReaderStrArr(DocumentReader<String[]> reader) throws MappingException;
    DocumentParseResult parseDocReaderStr(DocumentReader<String> reader) throws MappingException;
}
