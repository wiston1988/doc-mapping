package org.doc.mapping.converter;

import org.doc.mapping.config.FieldConfig;

public interface FieldConverter {
	boolean canConvert(FieldConfig config);
}
