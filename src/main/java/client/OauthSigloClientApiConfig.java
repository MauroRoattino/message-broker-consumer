package client;


import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utils.SNIDisabledSocketFactory;

import javax.net.ssl.*;
import java.security.KeyStore;

@Configuration
public class OauthSigloClientApiConfig {

    @Value("${oauth.siglo.oauthUrlSiglo}")
    private String oauthSiglo;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    OauthSigloClient oauthSigloClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(oauthSiglo)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        return retrofit.create(OauthSigloClient.class);
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        try {


            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            X509TrustManager manager = (X509TrustManager) trustManagers[0];
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());

            SSLSocketFactory sslSocketFactory = new SNIDisabledSocketFactory(sslContext.getSocketFactory());

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, manager);

            OkHttpClient okHttpClient = builder.addInterceptor(loggingInterceptor).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}