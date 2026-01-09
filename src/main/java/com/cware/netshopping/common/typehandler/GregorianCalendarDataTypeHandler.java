package com.cware.netshopping.common.typehandler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class GregorianCalendarDataTypeHandler implements TypeHandler<GregorianCalendar> {

	@Override
	public void setParameter(PreparedStatement ps, int i, GregorianCalendar parameter, JdbcType jdbcType)
			throws SQLException {
		if (null != parameter) {
			long time = ((GregorianCalendar) parameter).getTimeInMillis(); 
//			Date date = new java.sql.Date(time);
			ps.setTimestamp(i, new Timestamp(time));
		}
		else { 
			ps.setTimestamp(i, null); 
		}
	}
	
	@Override
	public GregorianCalendar getResult(ResultSet rs, String columnName) throws SQLException { 
		Date date = rs.getDate(columnName);
		if (null == date) {
			return null;
		}
		GregorianCalendar c = new GregorianCalendar();
		try {
			c.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	@Override 
	public GregorianCalendar getResult(CallableStatement cs, int columnIndex) throws SQLException { 
		return null;
	}
	
	@Override
	public GregorianCalendar getResult(ResultSet arg0, int arg1) throws SQLException {
		return null;
	}
	
}
