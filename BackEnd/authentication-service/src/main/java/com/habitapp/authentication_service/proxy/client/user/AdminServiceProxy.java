package com.menara.authentication.proxy.client.user;

import com.internship_hiring_menara.common.http.request.admin.AdminIdRequestHttp;
import com.internship_hiring_menara.common.http.request.admin.AdminsIdsRequestHttp;
import com.internship_hiring_menara.common.http.request.search.SearchWithKeywordsRequestHttp;
import com.internship_hiring_menara.common.http.request_response.admin.AdminRequestResponseHttp;
import com.internship_hiring_menara.common.http.request_response.admin.AdminRequestResponseHttpList;
import com.menara.authentication.annotation.Proxy;
import com.menara.authentication.client.user.AdminClient;
import com.menara.authentication.domain.exception.admin.AdminsIdsNotFoundException;
import com.menara.authentication.domain.exception.admin.KeywordsNotFoundException;
import com.menara.authentication.dto.user.admin.AdminInformationDTO;
import com.menara.authentication.proxy.exception.common.UnauthorizedException;
import com.menara.authentication.proxy.exception.common.UnexpectedException;
import com.menara.authentication.proxy.exception.common.UnexpectedResponseBodyException;
import com.menara.authentication.proxy.exception.user.*;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Proxy
@AllArgsConstructor
public class AdminServiceProxy {
    private AdminClient adminClient;

    public AdminInformationDTO createAdmin(AdminInformationDTO adminInformationDTO) throws UnauthorizedException, UnexpectedException, AdminNotCreatedException, IdAccountNotFoundException, UserProfileInformationNotProvidedException {

        if (adminInformationDTO.getIdAccount() == null || adminInformationDTO.getIdAccount() ==0){
            throw new IdAccountNotFoundException("id account for admin not provided");
        }

        if (adminInformationDTO.getFirstName() == null ||adminInformationDTO.getFirstName().isEmpty() || adminInformationDTO.getFirstName().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin first name not provided");
        }

        if (adminInformationDTO.getLastName() == null ||adminInformationDTO.getLastName().isEmpty() || adminInformationDTO.getLastName().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin last name not provided");
        }

        if (adminInformationDTO.getEmail() == null ||adminInformationDTO.getEmail().isEmpty() || adminInformationDTO.getEmail().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin email not provided");
        }

        if (adminInformationDTO.getRegistration() == null ||adminInformationDTO.getRegistration().isEmpty() || adminInformationDTO.getRegistration().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin registration name not provided");
        }

        AdminRequestResponseHttp adminRequestHttp = new AdminRequestResponseHttp(adminInformationDTO.getIdAccount(), adminInformationDTO.getFirstName(), adminInformationDTO.getLastName(), adminInformationDTO.getEmail(), adminInformationDTO.getRegistration());
        ResponseEntity<AdminRequestResponseHttp> response;

        AdminRequestResponseHttp adminResponseHttp;
        System.out.println(adminInformationDTO.getIdAccount());

        try {
            response = adminClient.createAdmin(adminRequestHttp);
            System.out.println( "status code : " + response.getStatusCode());
            if (response.getStatusCode() != HttpStatus.CREATED
                    || response.getBody() == null){
                throw new AdminNotCreatedException("the admin not created using user-service");
            }

            adminResponseHttp = response.getBody();
            return new AdminInformationDTO(adminResponseHttp.getIdAccount(), adminResponseHttp.getFirstName(), adminResponseHttp.getLastName(), adminResponseHttp.getEmail(), adminResponseHttp.getRegistration());
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to create admin");
            } else if (e.status() == 400){
                throw new UserProfileInformationNotProvidedException("there is an admin information not provided");
            }
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        }
    }

    public Map<Long, AdminInformationDTO> readAllAdmins() throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException {
        Map<Long, AdminInformationDTO> adminsMap = new HashMap<>();

        try {
            ResponseEntity<AdminRequestResponseHttpList> response = adminClient.readAllAdmins();
            if ((response.getBody() instanceof AdminRequestResponseHttpList  admins)){
                if(admins.getAdminRequestResponseHttpList().isEmpty() ){
                    return adminsMap;
                }

                for (AdminRequestResponseHttp admin : admins.getAdminRequestResponseHttpList()) {
                    adminsMap.put(admin.getIdAccount(), new AdminInformationDTO(admin.getIdAccount(), admin.getFirstName(), admin.getLastName(), admin.getEmail(), admin.getRegistration()));
                }
                return adminsMap;
            } else {
                throw new UnexpectedResponseBodyException("unexpected response body rather than AdminRequestResponseHttpList ");
            }
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to read all admin from user-service");
            }
            System.out.println(e.getMessage());
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        }
    }

    public Map<Long, AdminInformationDTO> readAdminsByKeywords(String keywords) throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException, KeywordsNotFoundException {
        Map<Long, AdminInformationDTO> adminsMap = new HashMap<>();

        if(keywords == null
                || keywords.isEmpty()
                || keywords.isBlank()){
            throw new KeywordsNotFoundException("keywords are null or empty or blank");
        }

        try {
            ResponseEntity<AdminRequestResponseHttpList> response = adminClient.readAdminsByKeywords(new SearchWithKeywordsRequestHttp(keywords));
            if ((response.getBody() instanceof AdminRequestResponseHttpList  admins)){
                if(admins.getAdminRequestResponseHttpList().isEmpty() ){
                    return adminsMap;
                }

                for (AdminRequestResponseHttp admin : admins.getAdminRequestResponseHttpList()) {
                    adminsMap.put(admin.getIdAccount(), new AdminInformationDTO(admin.getIdAccount(), admin.getFirstName(), admin.getLastName(), admin.getEmail(), admin.getRegistration()));
                }
                return adminsMap;
            } else {
                throw new UnexpectedResponseBodyException("unexpected response body rather than AdminRequestResponseHttpList ");
            }
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to read all admin from user-service");
            }
            System.out.println(e.getMessage());
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        }
    }

    public Map<Long, AdminInformationDTO> readAdminsByIds(List<Long> ids) throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException, AdminsIdsNotFoundException {
        Map<Long, AdminInformationDTO> adminsMap = new HashMap<>();

        if(ids == null
                || ids.isEmpty()){
            throw new AdminsIdsNotFoundException("admins ids list is null or empty");
        }

        try {
            ResponseEntity<AdminRequestResponseHttpList> response = adminClient.readAdminsByIds(new AdminsIdsRequestHttp(ids));
            if ((response.getBody() instanceof AdminRequestResponseHttpList  admins)){
                if(admins.getAdminRequestResponseHttpList().isEmpty() ){
                    return adminsMap;
                }

                for (AdminRequestResponseHttp admin : admins.getAdminRequestResponseHttpList()) {
                    adminsMap.put(admin.getIdAccount(), new AdminInformationDTO(admin.getIdAccount(), admin.getFirstName(), admin.getLastName(), admin.getEmail(), admin.getRegistration()));
                }
                return adminsMap;
            } else {
                throw new UnexpectedResponseBodyException("unexpected response body rather than AdminRequestResponseHttpList ");
            }
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to read all admin from user-service");
            }
            System.out.println(e.getMessage());
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        }
    }

    public AdminInformationDTO updateAdmin(AdminInformationDTO adminInformationDTO) throws IdAccountNotFoundException, UserProfileInformationNotProvidedException, UnauthorizedException, UnexpectedException {

        if (adminInformationDTO.getIdAccount() == null || adminInformationDTO.getIdAccount() ==0){
            throw new IdAccountNotFoundException("id account for admin not provided");
        }

        if (adminInformationDTO.getFirstName() == null ||adminInformationDTO.getFirstName().isEmpty() || adminInformationDTO.getFirstName().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin firstName not provided");
        }

        if (adminInformationDTO.getLastName() == null ||adminInformationDTO.getLastName().isEmpty() || adminInformationDTO.getLastName().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin lastName not provided");
        }

        if (adminInformationDTO.getEmail() == null ||adminInformationDTO.getEmail().isEmpty() || adminInformationDTO.getEmail().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin email not provided");
        }

        if (adminInformationDTO.getRegistration() == null ||adminInformationDTO.getRegistration().isEmpty() || adminInformationDTO.getRegistration().isBlank()){
            throw new UserProfileInformationNotProvidedException("admin registration name not provided");
        }

        AdminRequestResponseHttp adminRequestHttp = new AdminRequestResponseHttp(adminInformationDTO.getIdAccount(), adminInformationDTO.getFirstName(), adminInformationDTO.getLastName(), adminInformationDTO.getEmail(), adminInformationDTO.getRegistration());
        ResponseEntity<AdminRequestResponseHttp> response;

        AdminRequestResponseHttp adminResponseHttp;
        System.out.println(adminInformationDTO.getIdAccount());

        try {
            response = adminClient.updateAdmin(adminRequestHttp);
            System.out.println( "status code : " + response.getStatusCode());
            if (response.getStatusCode() != HttpStatus.OK
                    || response.getBody() == null){
                throw new AdminNotUpdatedException("the admin not created using user-service");
            }

            adminResponseHttp = response.getBody();
            return new AdminInformationDTO(adminResponseHttp.getIdAccount(), adminResponseHttp.getFirstName(), adminResponseHttp.getLastName(), adminResponseHttp.getEmail(), adminResponseHttp.getRegistration());
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to create admin");
            } else if (e.status() == 400){
                throw new UserProfileInformationNotProvidedException("there is an admin information not provided");
            }
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        } catch (AdminNotUpdatedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAdmin(long idAccount) throws AdminIdNotProvidedException, UnauthorizedException, UnexpectedException, AdminNotDeletedException {
        if(idAccount == 0){
            throw new AdminIdNotProvidedException("admins id not provided it equal 0");
        }

        try {
            ResponseEntity<Void> response = adminClient.deleteAdmin(new AdminIdRequestHttp(idAccount));
            System.out.println( "status code : " + response.getStatusCode());

            if (response.getStatusCode() != HttpStatus.OK){
                throw new AdminNotDeletedException("the admin not created using user-service");
            }
        } catch (FeignException e){
            if (e.status() == 401) {
                throw new UnauthorizedException("unauthorized to read all admin from user-service");
            }
            System.out.println(e.getMessage());
            throw new UnexpectedException("unexpected error at user service http status : " + e.status());
        }

    }


}
