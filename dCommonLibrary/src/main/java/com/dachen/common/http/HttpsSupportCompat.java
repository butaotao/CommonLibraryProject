package com.dachen.common.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by pqixi on 2016/9/8 0008.
 * https兼容支持
 */
public class HttpsSupportCompat {


    private Context mContext;
    private static final String TAG = "HttpsSupportCompat";
    private static final String KEYNAME = "server.crt";

    public HttpsSupportCompat(Context context) {
        if (context == null) {
            throw new RuntimeException("HttpsSupportCompat construtor Context can't be null");
        }
        mContext = context.getApplicationContext();
    }

    /**
     * 兼容asynchttp框架支持证书验证
     *
     * @param httpClient
     * @return
     */
    public HttpClient compatAsyncHttp(HttpClient httpClient) {
        KeyStore keyStore = loadKeyStore(KEYNAME);
        SSLSocketFactory ayncHttpSSLFactory = createAyncHttpSSLFactory(keyStore);
        httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", ayncHttpSSLFactory, 443));//注册https链接的通道
        return httpClient;
    }

    /**
     * 兼容Volley框架支持https证书校验
     *
     * @return
     */
    public RequestQueue compatVolley() {
        return Volley.newRequestQueue(mContext, new HurlStack(null, createVolleySSLFactory()));
    }

    /**
     * 创建Volley可用的SSL加密通道的工厂
     *
     * @return
     */
    private javax.net.ssl.SSLSocketFactory createVolleySSLFactory() {
        try {
            KeyStore keyStore = loadKeyStore(KEYNAME);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            javax.net.ssl.SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            return socketFactory;
        } catch (Exception e) {
            Log.e(TAG, "createVolleySSLFactory: error = ", e);
        }
        return null;
    }

    /**
     * 创建AsyncHttp可用的SSL加密通道的工厂
     *
     * @param keyStore
     * @return
     */
    private SSLSocketFactory createAyncHttpSSLFactory(KeyStore keyStore) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = new SSLSocketFactory(keyStore);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            Log.e(TAG, "createAyncHttpSSLFactory: error = ", e);
        }
        return sslSocketFactory;
    }


    /**
     * 加载密钥
     *
     * @param keyName 证书名称，默认取asserts目录下的文件
     * @return
     */
    private KeyStore loadKeyStore(String keyName) {

        try {
            //生成默认的密钥对象
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            if (TextUtils.isEmpty(keyName)) //如果证书路径为空，返回默认密钥，否则加载证书到密钥对象中
                return keyStore;

            InputStream ins = mContext.getAssets().open(keyName);
            Certificate cer = CertificateFactory.getInstance("X.509").generateCertificate(ins);
            keyStore.setCertificateEntry("trust", cer);// 加载证书到密钥库中

            return keyStore;
        } catch (Exception e) {//加载过程中异常基本上无法做处理，统一打日志
            Log.e(TAG, "loadKeyStore: error = ", e);
        }
        return null;//加载过程中有异常返回null
    }
}
