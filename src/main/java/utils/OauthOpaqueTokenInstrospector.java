package utils;


import client.OauthSigloClient;
import lombok.SneakyThrows;
import model.TokenInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class OauthOpaqueTokenInstrospector implements OpaqueTokenIntrospector {

    private final OauthSigloClient oauthSigloClient;

    public OauthOpaqueTokenInstrospector(OauthSigloClient oauthSigloClient) {
        this.oauthSigloClient = oauthSigloClient;
    }

    @SneakyThrows
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token)  {
        Call<TokenInfo> oauthSigloCall = oauthSigloClient.getTokenInfo(token);
        Response<TokenInfo> response = oauthSigloCall.execute();

        if(!response.isSuccessful() && !response.body().getActive()){
            throw new BadOpaqueTokenException("Provided token isn't active");
        }


        TokenInfo tokenInfo = response.body();
        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", ZonedDateTime.now().toInstant().plusMillis(tokenInfo.getExp()));
        claims.put("client_id", tokenInfo.getClientId());
        claims.put("scope", tokenInfo.getScope());

        List<GrantedAuthority> authorities = new ArrayList<>();
        tokenInfo.getScope().forEach(s -> authorities.add(new SimpleGrantedAuthority("SCOPE_"+s)));


        return new DefaultOAuth2AuthenticatedPrincipal(claims, authorities);
    }
}