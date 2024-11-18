package com.menara.authentication.security.filter;

import com.menara.authentication.annotation.Filter;
import com.menara.authentication.configuration.record.AccessBadge;
import com.menara.authentication.security.common.constant.WhiteListUriConstants;
import com.menara.authentication.security.exception.filter.AccessBadgeNotFoundException;
import com.menara.authentication.security.exception.filter.AccessBadgeUnexpectedValueException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Yassine Chalati
 * emailingConfiguration : yassine.chalati.07@gmail.com
 * VerifyAccessBadgeFilter
 * date : 31/07/2024
 */

@Order(1)
@RequiredArgsConstructor
@Filter
public class VerifyAccessBadgeFilter extends OncePerRequestFilter {
    /**
     * accessBadgeConfigurationProperties is an instance of class {@link AccessBadge}
     * that contain the parameter name and parameter value of Access Badge from Configuration properties.
     */
    @NonNull
    private AccessBadge accessBadgeConfigurationProperties;

    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        System.out.println("im in VerifyAccessBadgeFilter doFilterInternal");

        String valueAlKhawarizmiAccessBadgeRequestHeader = request.getHeader(accessBadgeConfigurationProperties.name());

        if (valueAlKhawarizmiAccessBadgeRequestHeader == null
                || valueAlKhawarizmiAccessBadgeRequestHeader.isBlank())
            throw new AccessBadgeNotFoundException("the header parameter " + accessBadgeConfigurationProperties.name() + " not found");
        else if (valueAlKhawarizmiAccessBadgeRequestHeader.equals(accessBadgeConfigurationProperties.value())) {
            System.out.println("it's equal accesbadge");
            filterChain.doFilter(request, response);
        }
        else
            throw new AccessBadgeUnexpectedValueException("the value of the AlKhawarizmi Access Badge is not as expected");

    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        System.out.println("im in VerifyAccessBadgeFilter shouldNotFilter");
        String[] uriPathList = request.getRequestURI().split("/");
        String[] uriVariableList;
        String uriRequest;

        for (String uri : WhiteListUriConstants.VerifyAccessBadgeFilter) {
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
