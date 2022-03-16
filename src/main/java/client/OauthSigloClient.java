package client;

import model.TokenInfo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OauthSigloClient {
    @FormUrlEncoded
    @POST("/menu/api/oauth2/token_info")
    Call<TokenInfo> getTokenInfo(@Field("token") String Token);
}