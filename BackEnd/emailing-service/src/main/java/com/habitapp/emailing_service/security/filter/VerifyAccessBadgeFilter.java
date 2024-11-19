package com.internship_hiring_menara.emailing_service.security.filter;

import com.internship_hiring_menara.emailing_service.annotation.Filter;
import com.internship_hiring_menara.emailing_service.configuration.record.AccessBadge;
import com.internship_hiring_menara.emailing_service.security.constant.WhiteListUriConstants;
import com.internship_hiring_menara.emailing_service.security.exception.filter.AccessBadgeNotFoundException;
import com.internship_hiring_menara.emailing_service.security.exception.filter.AccessBadgeUnexpectedValueException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Yassine Chalati
 * emailingConfiguration : yassine.chalati.07@gmail.com
 * VerifyingAlKhawarizmiAccessBadgeFilter
 * date : 31/07/2024
 */

@Order(1)
@AllArgsConstructor
@Filter
public class VerifyAccessBadgeFilter extends OncePerRequestFilter {
    /**
     * accessBadgeConfigurationProperties is an instance of class {@link AccessBadge}
     * that contain the parameter name and parameter value of Access Badge from Configuration properties.
     */
    private AccessBadge accessBadgeConfigurationProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String valueAlKhawarizmiAccessBadgeRequestHeader = request.getHeader(accessBadgeConfigurationProperties.name());

        if (valueAlKhawarizmiAccessBadgeRequestHeader == null
                || valueAlKhawarizmiAccessBadgeRequestHeader.isBlank())
            throw new AccessBadgeNotFoundException("the header parameter " + accessBadgeConfigurationProperties.name() + " not found");
        else if (valueAlKhawarizmiAccessBadgeRequestHeader.equals(accessBadgeConfigurationProperties.value()))
            filterChain.doFilter(request, response);
        else
            throw new AccessBadgeUnexpectedValueException("the value of the AlKhawarizmi Access Badge is not as expected");
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
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
