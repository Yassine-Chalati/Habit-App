package com.internship_hiring_menara.emailing_service.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship_hiring_menara.emailing_service.annotation.Filter;
import com.internship_hiring_menara.emailing_service.commons.constant.CookiesNameConstants;
import com.internship_hiring_menara.emailing_service.commons.constant.JwtClaimsConstants;
import com.internship_hiring_menara.emailing_service.commons.util.converter.ConverterUtil;
import com.internship_hiring_menara.emailing_service.commons.util.hash.HashUtil;
import com.internship_hiring_menara.emailing_service.security.constant.WhiteListUriConstants;
import com.internship_hiring_menara.emailing_service.security.exception.filter.CookieFingerprintNotEqualWithJwtClaimException;
import com.internship_hiring_menara.emailing_service.security.exception.filter.CookieTokenFingerprintNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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