package com.cware.api.panaver.product;

import java.security.Security;
import java.security.SignatureException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.cware.api.panaver.product.type.AccessCredentialsType;
import com.nhncorp.psinfra.toolkit.SimpleCryptLib;
/**
 * @author NBP
 * 네어버 스마스트스토어 API 샘플소스
 */
public class SampleBase {
	 // 개발
	private final static String ACCESS_LICENSE = "010001000049a79ed9bec98d543aa384d2b38e2440e077d5ff6e2c3cdd2fff7de174080676";
	private final static String SECRET_KEY = "AQABAABtHMV1FOalGi833vagMCfm/4tbE4gqENSOvtYX2h6TTQ==";
	
	// 운영
//	private final static String ACCESS_LICENSE = "01000100009ac825503f2dc477e5588638a1c26ffb6fe04363d98cabb7d5ca05e2b406b61c";
//	private final static String SECRET_KEY = "AQABAACX1iwPATgXB2uy9ERHZ0TL+tWe/0khzseJ/7uNmAKYew==";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	protected static AccessCredentialsType createAccessCredentials(String serviceName, String operationName) throws SignatureException {
		String timestamp = SimpleCryptLib.getTimestamp();
		String signature = SimpleCryptLib.generateSign(timestamp + serviceName + operationName, SECRET_KEY);
		AccessCredentialsType accessCredentials = new AccessCredentialsType();
		accessCredentials.setAccessLicense(ACCESS_LICENSE);
		accessCredentials.setTimestamp(timestamp);
		accessCredentials.setSignature(signature);
		return accessCredentials;
	}

	public static void main(String[] args) throws SignatureException {
		Security.addProvider(new BouncyCastleProvider());
		System.out.println(createAccessCredentials("ProductService", "ManageProduct"));
	}
}
