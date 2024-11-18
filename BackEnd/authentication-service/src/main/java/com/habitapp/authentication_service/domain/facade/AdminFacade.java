package com.menara.authentication.domain.facade;

import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.account.RoleNotDefinedException;
import com.menara.authentication.domain.exception.account.RolePrefixException;
import com.menara.authentication.domain.exception.admin.AdminsIdsNotFoundException;
import com.menara.authentication.domain.exception.admin.KeywordsNotFoundException;
import com.menara.authentication.domain.exception.admin.MissingAdminInformationException;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.dto.account.AdminAccountDTO;
import com.menara.authentication.dto.account.AdminAccountInformationDTO;
import com.menara.authentication.dto.account.AdminPartialAccountInformationDTO;
import com.menara.authentication.dto.user.admin.AdminInformationDTO;
import com.menara.authentication.proxy.exception.common.UnauthorizedException;
import com.menara.authentication.proxy.exception.common.UnexpectedException;
import com.menara.authentication.proxy.exception.common.UnexpectedResponseBodyException;
import com.menara.authentication.proxy.exception.user.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AdminFacade {
    public AdminInformationDTO createAdmin(AdminInformationDTO adminInformationDTO) throws UnexpectedException, UnauthorizedException, AdminNotCreatedException, UserProfileInformationNotProvidedException, IdAccountNotFoundException;
    public  List<AdminAccountInformationDTO> readAllAdmins() throws UnexpectedResponseBodyException, UnauthorizedException, UnexpectedException;
    public List<AdminAccountInformationDTO> readAdminsByKeywords(String keywords) throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException, KeywordsNotFoundException;
    public List<AdminAccountInformationDTO> readAllSuspendedAdmins() throws UnexpectedResponseBodyException, UnexpectedException, UnauthorizedException, AdminsIdsNotFoundException;
    public  AdminAccountInformationDTO updateAdmin(AdminPartialAccountInformationDTO adminPartialAccountInformation) throws UnexpectedException, UserProfileInformationNotProvidedException, UnauthorizedException, IdAccountNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, AccountNotFoundException, ValueNullException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException;
    public  void deleteAdmin(long idAccount) throws AdminIdNotProvidedException, UnexpectedException, AdminNotDeletedException, UnauthorizedException, AccountNotFoundException, UnexpectedResponseBodyException, AdminsIdsNotFoundException, AdminNotCreatedException, UserProfileInformationNotProvidedException, IdAccountNotFoundException;
    /*public void deleteGroupAdminAccount(List<Long> ids);
    public void deleteAllAdminAccount();*/
}
