package top.jfunc.common.http.base.ssl;

import java.security.cert.X509Certificate;

/**
 * 防止OKHttp3的空指针
 * @author xiongshiyan
 */
public class DefaultTrustManager2 extends DefaultTrustManager {

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		X509Certificate[] certificates = {};
		return certificates;
	}
}
