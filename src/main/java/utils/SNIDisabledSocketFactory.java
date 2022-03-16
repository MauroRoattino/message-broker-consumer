package utils;

import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SNIDisabledSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory baseSocketFactory;
    public SNIDisabledSocketFactory(SSLSocketFactory baseSocketFactory) {
        this.baseSocketFactory = baseSocketFactory;
    }
    private Socket setSni(Socket socket) {
        SSLParameters params = ((SSLSocket)socket).getSSLParameters();
        params.setServerNames(new ArrayList<SNIServerName>()); //Disable SNI by emptying the host name
        ((SSLSocket)socket).setSSLParameters(params);
        return socket;
    }
    @Override
    public String[] getDefaultCipherSuites() {
        return baseSocketFactory.getDefaultCipherSuites();
    }
    @Override
    public String[] getSupportedCipherSuites() {
        return baseSocketFactory.getSupportedCipherSuites();
    }
    @Override
    public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean) throws IOException {
        return setSni(baseSocketFactory.createSocket(paramSocket, paramString, paramInt, paramBoolean));
    }
    @Override
    public Socket createSocket(String paramString, int paramInt) throws IOException, UnknownHostException {
        return setSni(baseSocketFactory.createSocket(paramString, paramInt));
    }
    @Override
    public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2) throws IOException, UnknownHostException {
        return setSni(baseSocketFactory.createSocket(paramString, paramInt1, paramInetAddress, paramInt2));
    }
    @Override
    public Socket createSocket(InetAddress paramInetAddress, int paramInt) throws IOException {
        return setSni(baseSocketFactory.createSocket(paramInetAddress, paramInt));
    }
    @Override
    public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2) throws IOException {
        return setSni(baseSocketFactory.createSocket(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2));
    }
}