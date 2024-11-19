package com.habitapp.profile_service.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitapp.profile_service.annotation.Filter;
import com.habitapp.profile_service.domain.facade.AuthenticationFacade;
import com.habitapp.profile_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.profile_service.proxy.exception.common.UnexpectedException;
import com.habitapp.profile_service.proxy.exception.common.UnexpectedResponseBodyException;
import com.habitapp.profile_service.security.constant.WhiteListUriConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@RequiredArgsConstructor
@Filter
public class VerifyRevokedJwtFilter extends OncePerRequestFilter {
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    @NonNull
    private AuthenticationFacade authenticationFacade;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        System.out.println("im in VerifyRevokedJwtFilter doFilterInternal");
        String token;
        String payloadBase64;
        byte[] payloadBytes;
        String payload ;
        JsonNode jsonNode;
        ObjectMapper objectMapper;
        String jti;

        try {
            token = this.bearerTokenResolver.resolve(request);
        }
        catch (OAuth2AuthenticationException e) {
            throw new RuntimeException(e);
        }

        payloadBase64 = token.split("\\.")[1];
        payloadBytes = Base64.getDecoder().decode(payloadBase64);
        payload = new String(payloadBytes);
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(payload);

        jti = jsonNode.get(JwtClaimNames.JTI).asText();

        try {
            if (authenticationFacade.jwtIsRevoked(jti)){
                throw new InvalidBearerTokenException("the token is revoked");
            } else {
                System.out.println("verify revoked jwt filterChain.doFilter");
                filterChain.doFilter(request, response);
            }
        }catch (UnauthorizedException e){
            try {
                authenticationFacade.authenticateServiceWithDefaultMethod();
                System.out.println("to the authenticationFacade.jwtIsRevoked(jti);");
                if (authenticationFacade.jwtIsRevoked(jti)){
                    throw new InvalidBearerTokenException("the token is revoked");
                } else {
                    filterChain.doFilter(request, response);
                }
            } catch (UnauthorizedException | UnexpectedException | UnexpectedResponseBodyException ex) {
                throw new RuntimeException(ex);
            }
        } catch (UnexpectedResponseBodyException | UnexpectedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //todo add an constant list for each filter
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        System.out.println("im in VerifyRevokedJwtFilter shouldNotFilter");

        String[] uriPathList = request.getRequestURI().split("/");
        String[] uriVariableList;
        String uriRequest;

        for (String uri : WhiteListUriConstants.VerifyRevokedJwtFilter) {
            System.out.println(request.getRequestURI());
            System.out.println(uri);
            uriRequest = uri;
            if (uriRequest.contains("/*")){
                uriVariableList = uri.split("/");
                for (int i = 0; i < uriVariableList.length; i++){
                    if(uriVariableList[i].equals("*")){
                        try {
                            uriVariableList[i] = uriPathList[i];
                        } catch (Exception e){
                            return false;
                        }
                    }
                }
                uriRequest = String.join("/", uriVariableList);
            }

            System.out.println(request.getRequestURI());
            System.out.println(uri);

            if (uriRequest.endsWith("/**")){
                uriRequest = uriRequest.split("/\\*\\*$")[0];

                System.out.println(request.getRequestURI());
                System.out.println(uri);

                return request.getRequestURI().startsWith(uriRequest);
            }

            if (request.getRequestURI().equals(uriRequest)) {

                System.out.println(request.getRequestURI());
                System.out.println(uri);

                return true;
            }
        }
        return false;
    }
}
