package com.cware.netshopping.common.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.cware.api.panaver.order.seller.SellerServiceStub.PlaceOrderStatusType;

public class NaverPlaceOrderStatusTypeHandler implements TypeHandler<PlaceOrderStatusType> {

	@Override
	public PlaceOrderStatusType getResult(ResultSet arg0, String arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlaceOrderStatusType getResult(ResultSet arg0, int arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlaceOrderStatusType getResult(CallableStatement arg0, int arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1,
			PlaceOrderStatusType arg2, JdbcType arg3) throws SQLException {
		if(null != arg2) {
			arg0.setString(arg1, arg2.getValue());
		} else {
			arg0.setString(arg1, null);
		}
	}

}
