package com.habitapp.profile_service.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitapp.profile_service.annotation.Filter;
import com.habitapp.profile_service.common.CookiesNameConstants;
import com.habitapp.profile_service.common.JwtClaimsConstants;
import com.habitapp.profile_service.common.utlil.converter.ConverterUtil;
import com.habitapp.profile_service.common.utlil.cryptography.hash.HashUtil;
import com.habitapp.profile_service.security.constant.WhiteListUriConstants;
import com.habitapp.profile_service.security.exception.CookieFingerprintNotEqualWithJwtClaimException;
import com.habitapp.profile_service.security.exception.CookieTokenFingerprintNotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * for Token SideJacking Attack
 * */
@Filter
public class VerifyTokenFingerprintFilter extends OncePerRequestFilter {

    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final HashUtil hashUtil = new HashUtil(new ConverterUtil());

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("im in VerifyTokenFingerprintFilter doFilterInternal");
        String token;
        String payloadBase64;
        byte[] payloadBytes;
        String payload ;
        JsonNode jsonNode;
        ObjectMapper objectMapper;
        String hashedCookieFingerprint;

        try {
            token = this.bearerTokenResolver.resolve(request);
            payloadBase64 = token.split("\\.")[1];
            payloadBytes = Base64.getDecoder().decode(payloadBase64);
            payload = new String(payloadBytes);
            objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(payload);
            for (Cookie cookie : request.getCookies()){
                if(cookie.getName().equals(CookiesNameConstants.TOKEN_FINGERPRINT)){
                    hashedCookieFingerprint = hashUtil.hashWithSHA256AndReturnHexadecimalString(cookie.getValue().getBytes());
                    if(hashedCookieFingerprint.equals(jsonNode.get(JwtClaimsConstants.FINGERPRINT).asText())){
                        System.out.println("fingerPrint filterChain.doFilter");
                        filterChain.doFilter(request, response);
                        return;
                    } else {
                        throw new CookieFingerprintNotEqualWithJwtClaimException("The cookie value of fingerprint is not the same with value of jwt fingerprint");
                    }
                }
            }

            throw new CookieTokenFingerprintNotFoundException("fingerprint not found within request");
        }
        catch (OAuth2AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        System.out.println("im in VerifyTokenFingerprintFilter shouldNotFilter");
        String[] uriPathList = request.getRequestURI().split("/");
        String[] uriVariableList;
        String uriRequest;

        for (String uri : WhiteListUriConstants.VerifyTokenFingerprintFilter) {
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
