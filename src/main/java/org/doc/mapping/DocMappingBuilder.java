package org.doc.mapping;

import org.doc.mapping.exception.MappingException;
import org.doc.mapping.io.DocumentWriter;

import java.io.IOException;
import java.util.List;

/**
 * @author Chen Hui
 * @since 2018/4/18
 */
public interface DocMappingBuilder<T> {
    void build(final DocumentWriter<T> output, List records) throws MappingException, IOException;
}
