package com.jtalis.jdbc;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class CreateTableStatement {

	private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS";
	private static final String SPACE = " ";
	private static final String COMMA = ", ";

	private String tableName;
	private List<String> params;

	public CreateTableStatement(String tableName) {
		this.tableName = tableName;
		params = new LinkedList<String>();
	}

	public CreateTableStatement addStatement(String c) {
		params.add(c);
		return this;
	}

	public CreateTableStatement addColumn(String name, String stm) {
		return addStatement(name + " " + stm);
	}

	public CreateTableStatement addPrimaryKey(String name) {
		return addStatement("PRIMARY KEY (" + name + ")");
	}

	public CreateTableStatement addConstraint(String stm) {
		return addStatement("CONSTRAINT " + stm);
	}

	public CreateTableStatement addForeignKey(String col, String fTable, String fCol) {
		return addStatement("FOREIGN KEY (" + col + ") REFERENCES " + fTable + " (" + fCol + ")");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(CREATE_TABLE_IF_NOT_EXISTS).append(SPACE).append(tableName).append(SPACE).append("(");
		for (String c : params) {
			sb.append(c).append(COMMA);
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(");");

		return sb.toString();
	}

}
