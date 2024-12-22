package com.habitapp.authentication_service.domain.facade.imp;

import com.menara.authentication.annotation.Facade;
import com.menara.authentication.domain.exception.account.*;
import com.habitapp.authentication_service.domain.exception.account.RoleNotDefinedException;
import com.habitapp.authentication_service.domain.exception.account.RolePrefixException;
import com.habitapp.authentication_service.domain.exception.admin.AdminsIdsNotFoundException;
import com.habitapp.authentication_service.domain.exception.admin.KeywordsNotFoundException;
import com.habitapp.authentication_service.domain.exception.admin.MissingAdminInformationException;
import com.habitapp.authentication_service.domain.exception.general.ValueNullException;
import com.menara.authentication.domain.service.AccountService;
import com.menara.authentication.dto.account.*;
import com.habitapp.authentication_service.dto.user.admin.AdminInformationDTO;
import com.habitapp.authentication_service.proxy.exception.common.UnauthorizedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedException;
import com.habitapp.authentication_service.proxy.exception.common.UnexpectedResponseBodyException;
import com.menara.authentication.proxy.exception.user.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Facade
@AllArgsConstructor
public class AdminFacadeImp implements AdminFacade {
    private AdminServiceProxy adminServiceProxy;
    private AccountService accountService;


    @Override
    public AdminInformationDTO createAdmin(AdminInformationDTO adminInformationDTO) throws UnexpectedException, UnauthorizedException, AdminNotCreatedException, UserProfileInformationNotProvidedException, IdAccountNotFoundException {
        return adminServiceProxy.createAdmin(adminInformationDTO);
    }

    @Override
    public  List<AdminAccountInformationDTO> readAllAdmins() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException {
        Map<Long, AdminInformationDTO> adminInformationMap = adminServiceProxy.readAllAdmins();
        List<AdminAccountDTO> adminAccountList = accountService.readAllAdminsAccount();

        return this.mergeAdminAccountWithAdminInformation(adminInformationMap, adminAccountList);
    }

    @Override
    public List<AdminAccountInformationDTO> readAdminsByKeywords(String keywords) throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException, KeywordsNotFoundException {
        if (keywords == null){
            throw new KeywordsNotFoundException("keywords not found exception");
        }
        String FormedKeywords = String.join("|", keywords);
        Map<Long, AdminInformationDTO> adminInformationMap = adminServiceProxy.readAdminsByKeywords(FormedKeywords);
        List<AdminAccountDTO> adminAccountList = accountService.readAdminsAccountByKeywords(adminInformationMap.keySet().stream().toList());

        return this.mergeAdminAccountWithAdminInformation(adminInformationMap, adminAccountList);
    }

    @Override
    public List<AdminAccountInformationDTO> readAllSuspendedAdmins() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException, AdminsIdsNotFoundException {
        Map<Long,AdminAccountDTO>  adminAccountMap = accountService.readAllSuspendedAdminsAccount();
        List<AdminAccountDTO> adminAccountList = adminAccountMap.values().stream().toList();

        Map<Long, AdminInformationDTO> adminInformationMap = adminServiceProxy.readAdminsByIds(adminAccountMap.keySet().stream().toList());
        return this.mergeAdminAccountWithAdminInformation(adminInformationMap, adminAccountList);
    }

    @Override
    public AdminAccountInformationDTO updateAdmin(AdminPartialAccountInformationDTO adminPartialAccountInformation) throws AccountNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, UnexpectedException, UserProfileInformationNotProvidedException, UnauthorizedException, IdAccountNotFoundException, ValueNullException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException {
        AdminAccountDTO adminAccount;
        AdminInformationDTO adminInformation;
        AdminAccountDTO oldAdminAccount;

        oldAdminAccount = accountService.readAdminAccountById(adminPartialAccountInformation.getIdAccount());
        adminAccount = accountService.updateAdminAccountEmailOrRolesOrPermissions(
                new AccountIdAndEmailAndRolesAndPermissionsDTO(adminPartialAccountInformation.getIdAccount(),
                        adminPartialAccountInformation.getEmail(),
                        adminPartialAccountInformation.getRoles(),
                        adminPartialAccountInformation.getPermissions()));
        try {
            adminInformation = adminServiceProxy.updateAdmin(
                    new AdminInformationDTO(adminPartialAccountInformation.getIdAccount(),
                            adminPartialAccountInformation.getFirstName(),
                            adminPartialAccountInformation.getLastName(),
                            adminPartialAccountInformation.getEmail(),
                            adminPartialAccountInformation.getRegistration()));
        } catch (IdAccountNotFoundException | UserProfileInformationNotProvidedException | UnexpectedException |
                 UnauthorizedException e) {
            accountService.updateAdminAccountEmailOrRolesOrPermissions(
                    new AccountIdAndEmailAndRolesAndPermissionsDTO(oldAdminAccount.getIdAccount(),
                            oldAdminAccount.getEmail(),
                            oldAdminAccount.getRoles(),
                            oldAdminAccount.getPermissions()));
            throw e;
        }

        return new AdminAccountInformationDTO(adminInformation.getIdAccount(),
                adminAccount.getCreationDate(),
                adminInformation.getEmail(),
                adminAccount.isSuspended(),
                adminInformation.getFirstName(),
                adminInformation.getLastName(),
                adminInformation.getRegistration(),
                adminAccount.getRoles(),
                adminAccount.getPermissions());
    }

    @Override
    public void deleteAdmin(long idAccount) throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException, AdminsIdsNotFoundException, AccountNotFoundException, AdminIdNotProvidedException, AdminNotDeletedException, AdminNotCreatedException, UserProfileInformationNotProvidedException, IdAccountNotFoundException {
        Map<Long, AdminInformationDTO> adminInformationMap = adminServiceProxy.readAdminsByIds(List.of(idAccount));
        if (adminInformationMap.isEmpty()){
            accountService.deleteOneAdminAccount(idAccount);
        }
        adminServiceProxy.deleteAdmin(idAccount);
        try {
            accountService.deleteOneAdminAccount(idAccount);
        } catch ( AccountNotFoundException e) {
            throw e;
        } catch (Exception e){
            adminServiceProxy.createAdmin(adminInformationMap.get(idAccount));
        }

    }

    /**
     * this method is for merge the result of admin information that come from user-service microservice with the result of account admin information that comme from this authentication-service
     * @param adminInformationMap this parameter hold the admin information
     * @param adminAccountList this parameter hold the account admin information
     * @return an object list of class List<AdminAccountInformationDTO> where merged admin information by idAccount
     * @throws MissingAdminInformationException
     */
    private List<AdminAccountInformationDTO> mergeAdminAccountWithAdminInformation(Map<Long, AdminInformationDTO> adminInformationMap, List<AdminAccountDTO> adminAccountList) {
        List<AdminAccountInformationDTO> adminAccountInformationList = new ArrayList<>();
        AdminInformationDTO adminInformation;

        for (AdminAccountDTO admin : adminAccountList){
            adminInformation = adminInformationMap.get(admin.getIdAccount());
            if (adminInformation == null){
                adminInformation = new AdminInformationDTO(0L, "NotFound", "NotFound", "NotFound", "NotFound");
//                throw new MissingAdminInformationException("there is a messing admin information from user-service");
            }
            adminAccountInformationList.add(new AdminAccountInformationDTO(admin.getIdAccount(),
                    admin.getCreationDate(),
                    admin.getEmail(),
                    admin.isSuspended(),
                    adminInformation.getFirstName(),
                    adminInformation.getLastName(),
                    adminInformation.getRegistration(),
                    admin.getRoles(),
                    admin.getPermissions()));
        }

        return adminAccountInformationList;
    }
}
