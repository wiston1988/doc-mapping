
package org.doc.mapping.io.csv;

import org.doc.mapping.io.DocumentReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements DocumentReader<String[]>,CSVFeature{

	private BufferedReader br;

	private boolean hasNext = true;

	private char separator;

	private char quotechar;

	private char escapechar;

	private int skipLines;

	private boolean linesSkiped;

	private boolean dynamicSeparator = false;
	
	private String lineSeparator = "\r\n";

	public CSVReader(Reader reader) {
		this(reader, DEFAULT_SEPARATOR);
	}

	public CSVReader(Reader reader, char separator) {
		this(reader, separator, DEFAULT_QUOTE_CHARACTER);
	}

	public CSVReader(Reader reader, char separator, char quotechar) {
		this(reader, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
	}

	public CSVReader(Reader reader, char separator, char quotechar, char escapechar) {
		this(reader, separator, quotechar, escapechar, DEFAULT_SKIP_LINES);
	}

	public CSVReader(Reader reader, char separator, char quotechar, char escapechar, int line) {
		this.br = new BufferedReader(reader);

		if(separator == NO_SEPARATOR_CHARACTER){
			dynamicSeparator = true;
		}else{
			this.separator = separator;
		}

		this.escapechar = escapechar;
		this.quotechar = quotechar;
		this.skipLines = line;
		
		this.lineSeparator = System.getProperty("line.separator");
	}

	/**
	 * Reads the entire file into a List with each element being a String[] of
	 * tokens.
	 * 
	 * @return a List of String[], with each String[] representing a line of the
	 *         file.
	 * 
	 * @throws IOException
	 *             if bad things happen during the read
	 */
	public List<String[]> readAll() throws IOException {

		List<String[]> allElements = new ArrayList<String[]>();
		while (hasNext) {
			String[] nextLineAsTokens = readNext();

			if (nextLineAsTokens != null)
				allElements.add(nextLineAsTokens);
		}
		return allElements;
	}

	/**
	 * Reads the next line from the buffer and converts to a string array.
	 * 
	 * @return a string array with each comma-separated element as a separate
	 *         entry.
	 * 
	 * @throws IOException
	 *             if bad things happen during the read
	 */
	@Override
	public String[] readNext() throws IOException {
		String nextLine = getNextLine();
		if(dynamicSeparator) {
			determineSeparator(nextLine);
		}

		return hasNext ? parseLine(nextLine) : null;
	}

	/**
	 * Reads the next line from the file.
	 * 
	 * @return the next line from the file without trailing newline
	 * @throws IOException
	 *             if bad things happen during the read
	 */
	private String getNextLine() throws IOException {
		if (!this.linesSkiped) {
			for (int i = 0; i < skipLines; i++) {
				br.readLine();
			}
			this.linesSkiped = true;
		}
		String nextLine = br.readLine();
		if (nextLine == null) {
			hasNext = false;
		}
		return hasNext ? nextLine : null;
	}

	/**
	 * Parses an incoming String and returns an array of elements.
	 * 
	 * @param nextLine
	 *            the string to parse
	 * @return the comma-tokenized list of elements, or null if nextLine is null
	 * @throws IOException if bad things happen during the read
	 */
	private String[] parseLine(String nextLine) throws IOException {

		if (nextLine == null || nextLine.trim().length()==0) {
			return null;
		}

		boolean validLine = false;
		List<String> tokensOnThisLine = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(INITIAL_BUFFER_SIZE);
		boolean inQuotes = false;
		do {
			if (inQuotes) {
				// continuing a quoted section, reappend newline
				sb.append(lineSeparator);
				nextLine = getNextLine();
				if (nextLine == null)
					break;
			}

			for (int i = 0; i < nextLine.length(); i++) {
				char c = nextLine.charAt(i);

				if(inQuotes){
					if(c == escapechar && nextLine.length() > (i+1)//If already inQuotes, escape will work if next char is indeed escapable
							&& (nextLine.charAt(i+1) == quotechar || nextLine.charAt(i+1) == escapechar)){
						sb.append(nextLine.charAt(i+1));
						i++;//escape and skip for deciding the next char
					}else if(c == quotechar){//If reach a quote when inQuotes, queto will finish
						inQuotes = !inQuotes;
					}else{
						sb.append(c);
					}
				}else{
					if((i==0 || nextLine.charAt(i-1) == separator)//current char is at the begining of a field
							&& c == quotechar){
						inQuotes = !inQuotes;
					}else if(c == separator){//If reach a quote when not inQuotes, this field shall be ended.
						validLine = addToLine(tokensOnThisLine, sb.toString()) || validLine;
						sb = new StringBuilder(INITIAL_BUFFER_SIZE); // start work on next token
					}else{
						sb.append(c);
					}
				}
			}
		}while (inQuotes);

		validLine = addToLine(tokensOnThisLine, sb.toString()) || validLine;//append the left string
		
		/***There must be one or more column has value, otherwise it's a empty line.***/
		if(validLine)
			return tokensOnThisLine.toArray(new String[0]);
		else
			return null;
	}

	/***If column has value,return true***/
	private boolean addToLine(List<String> tokensOnThisLine, String column){
		if(tokensOnThisLine == null)
			return false;

		tokensOnThisLine.add(column);

		if(column != null && column.length() > 0)
			return true;
		else
			return false;
	}

	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException if the close fails
	 */
	public void close() throws IOException {
		if(br != null)
			br.close();
	}

	private void determineSeparator(String text){
		if(text == null)
			return;

		if(text.indexOf(DEFAULT_SEPARATOR) != -1)
			separator = DEFAULT_SEPARATOR;
		else
			separator = '\t';

		dynamicSeparator = false;
	}

	public boolean hasNext() {
		return hasNext;
	}
}
