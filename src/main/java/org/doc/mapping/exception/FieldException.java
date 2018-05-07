package org.doc.mapping.exception;


public class FieldException extends MappingException {
	private static final long serialVersionUID = 5965358038133948442L;
	private String fieldValue;
	
	public FieldException(String filedIdentifier)
	{
		super(filedIdentifier);
		setErrorLevel(FIELD_LEVEL);
	}
	public FieldException(String filedIdentifier,String reason)
	{
		super(filedIdentifier, reason);
		setErrorLevel(FIELD_LEVEL);
	}
	public FieldException(String filedIdentifier, Throwable e)
	{
		super(filedIdentifier, e);
		setErrorLevel(FIELD_LEVEL);
	}
	public FieldException(String filedIdentifier,String fieldValue,String reason)
	{
		super(filedIdentifier, reason);
		setErrorLevel(FIELD_LEVEL);
		this.fieldValue = fieldValue;
	}

	public FieldException(String filedIdentifier, String reason,Throwable e)
	{
		super(filedIdentifier, reason, e);
		setErrorLevel(FIELD_LEVEL);
	}
	public FieldException(String filedIdentifier, String fieldValue, String reason, Throwable e)
	{
		super(filedIdentifier, reason, e);
		setErrorLevel(FIELD_LEVEL);
		this.fieldValue = fieldValue;
	}
	public String getFieldValue(){
		return fieldValue;
	}
}
