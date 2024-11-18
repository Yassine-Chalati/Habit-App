package com.menara.authentication.domain.service.imp;

import com.internship_hiring_menara.common.common.account.PermissionNameCommonConstants;
import com.internship_hiring_menara.common.common.account.RoleNameCommonConstants;
import com.menara.authentication.common.constant.AuthenticationServiceUrlPathConstants;
import com.menara.authentication.common.constant.RegexPatternConstants;
import com.menara.authentication.configuration.record.UrlDelegateService;
import com.menara.authentication.configuration.record.DurationOfGeneratedToken;
import com.menara.authentication.configuration.record.DurationResendUrl;
import com.menara.authentication.domain.entity.*;
import com.menara.authentication.domain.exception.account.*;
import com.menara.authentication.domain.exception.general.ValueNullException;
import com.menara.authentication.domain.repository.*;
import com.menara.authentication.domain.service.AccountService;
import com.menara.authentication.dto.account.*;
import com.menara.authentication.dto.account.AccountIdAndEmailAndActivationURLDTO;
import com.menara.authentication.common.utlil.regex.RegexPatternValidatorUtil;
import com.menara.authentication.common.utlil.generator.random.token.RandomTokenGeneratorUtil;
import com.menara.authentication.proxy.exception.user.AdminIdNotProvidedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    @NonNull
    private DefaultAccountCandidateRepository defaultAccountCandidateRepository;
    @NonNull
    private GoogleAccountCandidateRepository googleAccountCandidateRepository;
    @NonNull
    private DefaultAccountAdminRepository defaultAccountAdminRepository;
    @NonNull
    private ActivationAccountVerificationTokenRepository activationAccountVerificationTokenRepository;
    @NonNull
    private ResetPasswordVerificationTokenRepository resetPasswordVerificationTokenRepository;
    @NonNull
    private RoleRepository roleRepository;
    @NonNull
    private PermissionRepository permissionRepository;
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private UrlDelegateService urlDelegateService;
    @NonNull
    private DurationOfGeneratedToken durationOfGeneratedToken;
    @NonNull
    private final RegexPatternValidatorUtil regexPatternValidatorUtil;
    @NonNull
    private final RandomTokenGeneratorUtil randomToken;
    @NonNull
    private final DurationResendUrl durationResendUrl;
//    private static final Logger logInfo = LoggerFactory.getLogger(AccountServiceImp.class);


    @Override
    public long createAdminAccountWithDefaultMethod(AccountAndRolesAndPermissionsDTO account) throws EmailNotFoundException, PasswordNotFoundException, EmailPatternNotValidException, PasswordPatternNotValidException, AccountAlreadyExistsException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, AccountNotCreatedException {
        LocalDateTime creationDate;
        DefaultAccountAdmin defaultAccountAdmin;
        List<Role> roles = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();

        if((account.getEmail() == null)
                || account.getEmail().isEmpty()
                || account.getEmail().isBlank()) {
            throw new EmailNotFoundException("The emailing is null or empty or blank");
        }

        if((account.getPassword() == null)
                || account.getPassword().isEmpty()
                || account.getPassword().isBlank()) {
            throw new PasswordNotFoundException("the password is null or empty or blank");
        }

        if(this.validateEmail(account.getEmail())){
            throw new EmailPatternNotValidException("The emailing pattern is not valid");
        }

        if(this.validatePassword(account.getPassword())){
            throw new PasswordPatternNotValidException("Password Pattern is not valid");
        }

        if(this.verifyUniquenessOfAdminEmail(account.getEmail())) {
            throw new AccountAlreadyExistsException("The email is already exists");
        }

        if(!(account.getRoles() == null)
                && !account.getRoles().isEmpty()) {
            for (String role : account.getRoles()) {
                if (!role.startsWith(RoleNameCommonConstants.PREFIX)) {
                    throw new RolePrefixException("role name not begin with prefix : " + RoleNameCommonConstants.PREFIX);
                }
                if (!RoleNameCommonConstants.allRoles.contains(role)) {
                    throw new RoleNotDefinedException("role not defined in roles list");
                }
                if (!roleRepository.existsByRole(role)) {
                    roleRepository.save(new Role(role));
                }
                roles.add(roleRepository.findByRole(role));
            }
        }


        if(!(account.getPermissions() == null)
                && !account.getPermissions().isEmpty()){
            for (String permission : account.getPermissions()){
                if (!permission.startsWith(PermissionNameCommonConstants.PREFIX)){
                    throw new PermissionPrefixException("permission name not begin with prefix : " + PermissionNameCommonConstants.PREFIX);
                }
                if (!PermissionNameCommonConstants.allPermissions.contains(permission)){
                    throw new PermissionNotDefinedException("permission ot defined in permissions list");
                }
                if (!permissionRepository.existsByPermission(permission)){
                    permissionRepository.save(new Permission(permission));
                }
                permissions.add(permissionRepository.findByPermission(permission));
            }
        }

        creationDate = LocalDateTime.now();

        defaultAccountAdmin = new DefaultAccountAdmin(account.getEmail(), passwordEncoder.encode(account.getPassword()), creationDate, roles, permissions);
        defaultAccountAdmin = defaultAccountAdminRepository.save(defaultAccountAdmin);

        if(defaultAccountAdmin.getId() == 0) {
            throw new AccountNotCreatedException("account not created correctly or may not created at all");
        }

        return defaultAccountAdmin.getId();
    }

    /*@Override
    public void reCreateAdminAccountWithDefaultMethod(AdminAccountWithPasswordDTO account) {

    }*/


    @Override
    public AccountIdAndEmailAndActivationURLDTO createCandidateAccountWithDefaultMethod(AccountAndRolesAndPermissionsDTO account) throws EmailPatternNotValidException,
            PasswordPatternNotValidException,
            EmailNotFoundException,
            PasswordNotFoundException,
            UrlConfigurationNotFoundException,
            AccountAlreadyExistsException,
            RoleNotFoundException,
            RolePrefixException,
            RoleNotDefinedException,
            AccountNotCreatedException,
            PermissionPrefixException,
            PermissionNotDefinedException {
        LocalDateTime creationDate;
        String generatedToken;
        String activationURL;
        DefaultAccountCandidate defaultAccountCandidate;
        List<Role> roles = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();


        if((urlDelegateService.url() == null)){
            throw new UrlConfigurationNotFoundException("The configuration of activation account url  is null" );
        }

        if((account.getEmail() == null)
                || account.getEmail().isEmpty()
                || account.getEmail().isBlank()) {
            throw new EmailNotFoundException("The emailingConfiguration not found exception");
        }

        if((account.getPassword() == null)
                || account.getPassword().isEmpty()
                || account.getPassword().isBlank()) {
            throw new PasswordNotFoundException("the password not found exception");
        }

        if(this.validateEmail(account.getEmail())){
            throw new EmailPatternNotValidException("The emailingConfiguration pattern is not valid");
        }

        if(this.validatePassword(account.getPassword())){
            throw new PasswordPatternNotValidException("Password Pattern is not valid");
        }

        if(this.verifyUniquenessOfCandidateEmail(account.getEmail())) {
            throw new AccountAlreadyExistsException("The emailingConfiguration is already exists");
        }

        if((account.getRoles() == null)
                || account.getRoles().isEmpty()){
            throw new RoleNotFoundException("no role found at candidate");
        }

        for(String role : account.getRoles()){
            if(!role.startsWith(RoleNameCommonConstants.PREFIX)){
                throw new RolePrefixException("role name not begin with prefix : " + RoleNameCommonConstants.PREFIX);
            }
            if(!RoleNameCommonConstants.allRoles.contains(role)){
                throw new RoleNotDefinedException("role not defined in roles list");
            }
            if (!roleRepository.existsByRole(role)){
                roleRepository.save(new Role(role));
            }
            roles.add(roleRepository.findByRole(role));
        }

        if(!(account.getPermissions() == null)
                && !account.getPermissions().isEmpty()){
            for (String permission : account.getPermissions()){
                if (!permission.startsWith(PermissionNameCommonConstants.PREFIX)){
                    throw new PermissionPrefixException("permission name not begin with prefix : " + PermissionNameCommonConstants.PREFIX);
                }
                if (!PermissionNameCommonConstants.allPermissions.contains(permission)){
                    throw new PermissionNotDefinedException("permission ot defined in permissions list");
                }
                if (!permissionRepository.existsByPermission(permission)){
                    permissionRepository.save(new Permission(permission));
                }
                    permissions.add(permissionRepository.findByPermission(permission));
            }
        }

        creationDate = LocalDateTime.now();
        generatedToken = randomToken.generateRandomToken64Characters();
        defaultAccountCandidate = new DefaultAccountCandidate(account.getEmail(), passwordEncoder.encode(account.getPassword()), creationDate, roles, permissions, new ActivationAccountVerificationToken(generatedToken, creationDate, null));
        defaultAccountCandidate.getActivationAccountVerificationToken().setCandidate(defaultAccountCandidate);
        defaultAccountCandidate = defaultAccountCandidateRepository.save(defaultAccountCandidate);

        if(defaultAccountCandidate.getId() == 0
                || !(new HashSet<>(defaultAccountCandidate.getRoles()).containsAll(roles))
                || !(new HashSet<>(defaultAccountCandidate.getPermissions()).containsAll(permissions))) {
            throw new AccountNotCreatedException("account not created correctly or may not created at all");
        }

        activationURL = urlDelegateService.url() + AuthenticationServiceUrlPathConstants.ACTIVATION_URI_PATH + generatedToken + AuthenticationServiceUrlPathConstants.URI_EMAIL_PARAMETER + defaultAccountCandidate.getEmail();
        return new AccountIdAndEmailAndActivationURLDTO(defaultAccountCandidate.getId(), defaultAccountCandidate.getEmail(), activationURL);
    }

    @Override
    public AccountIdAndAuthoritiesDTO activateTheCandidateAccountCreatedByDefaultMethod(AccountEmailAndActivationTokenDTO accountEmailAndActivationTokenDTO) throws EmailNotFoundException,
            VerificationTokenNotFoundException,
            EmailPatternNotValidException,
            VerificationTokenPatternNotValidException,
            VerificationTokenDurationExpiredException,
            VerificationTokensNotEqualsException, AccountNotFoundException, AccountIsActivatedException {
        DefaultAccountCandidate defaultAccountCandidate;
        String authorities;


        if((accountEmailAndActivationTokenDTO.getEmail() == null)
                || accountEmailAndActivationTokenDTO.getEmail().isEmpty()
                || accountEmailAndActivationTokenDTO.getEmail().isBlank()){
            throw new EmailNotFoundException("The emailingConfiguration not found exception");
        }

        if((accountEmailAndActivationTokenDTO.getActivationToken() == null)
                || accountEmailAndActivationTokenDTO.getActivationToken().isBlank()
                || accountEmailAndActivationTokenDTO.getActivationToken().isEmpty()){
            throw new VerificationTokenNotFoundException("The token is not found exception");
        }

        if (this.validateEmail(accountEmailAndActivationTokenDTO.getEmail())){
            throw new EmailPatternNotValidException("The emailingConfiguration pattern is not valid");
        }

        if (this.validateActivationToken(accountEmailAndActivationTokenDTO.getActivationToken())){
            throw new VerificationTokenPatternNotValidException("The activation token pattern is not valid");
        }

        defaultAccountCandidate = defaultAccountCandidateRepository.findDefaultAccountCandidateByEmail(accountEmailAndActivationTokenDTO.getEmail());

        if(defaultAccountCandidate == null){
//            this.mitigateEnumerationAttack(new AccountNotFoundException("the account is not created with default method"));
            throw new AccountNotFoundException("the account is not created with default method");
        }

        if(defaultAccountCandidate.isActivated()){
//            this.mitigateEnumerationAttack(new AccountIsActivatedException("the account is already activated"));
            throw new AccountIsActivatedException("the account is already activated");
        }

        if(defaultAccountCandidate.getActivationAccountVerificationToken() == null){
//            this.mitigateEnumerationAttack(new VerificationTokenNotFoundException("There is no activation token for the account in database"));
            throw new VerificationTokenNotFoundException("There is no activation token for the account in database");
        }

        if(Duration.between(defaultAccountCandidate.getActivationAccountVerificationToken().getCreationDate(), LocalDateTime.now()).toHours()
                > durationOfGeneratedToken.accountActivationToken()){
            throw new VerificationTokenDurationExpiredException("The duration of activation token is expired");
        }

        if(defaultAccountCandidate.getActivationAccountVerificationToken().getToken().equals(accountEmailAndActivationTokenDTO.getActivationToken())){

            activationAccountVerificationTokenRepository.delete(defaultAccountCandidate.getActivationAccountVerificationToken());
            defaultAccountCandidate.setActivationAccountVerificationToken(null);
            defaultAccountCandidate.setActivated(true);
            defaultAccountCandidateRepository.save(defaultAccountCandidate);
            authorities = defaultAccountCandidate.getRoles().stream().map(Role::getRole).collect(Collectors.joining(" "))
                    + " "
                    + defaultAccountCandidate.getPermissions().stream().map(Permission::getPermission).collect(Collectors.joining(" "));
            return new AccountIdAndAuthoritiesDTO(Long.toString(defaultAccountCandidate.getId()), authorities);
        } else{
            throw new VerificationTokensNotEqualsException("token not equal activation token");
        }
    }

    @Override
    public AccountEmailAndUrlDTO generateActivationUrlForAccountCreatedByDefaultMethod(String email) throws UrlConfigurationNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountNotFoundException, VerificationTokenException, VerificationTokenNotRegeneratedYetException {
        String generatedToken;
        String activationURL;
        LocalDateTime creationDate;
        DefaultAccountCandidate defaultAccountCandidate;
        ActivationAccountVerificationToken activationAccountVerificationToken;

        if((urlDelegateService.url() == null)){
            throw new UrlConfigurationNotFoundException("The configuration of activation account url  is null" );
        }

        if((email == null)
                || email.isEmpty()
                || email.isBlank()) {
            throw new EmailNotFoundException("The emailingConfiguration not found exception");
        }

        if(this.validateEmail(email)){
            throw new EmailPatternNotValidException("The emailingConfiguration pattern is not valid");
        }

        defaultAccountCandidate = defaultAccountCandidateRepository.findDefaultAccountCandidateByEmail(email);

        if (defaultAccountCandidate == null){
            throw new AccountNotFoundException("the account not found");
        }

        activationAccountVerificationToken = defaultAccountCandidate.getActivationAccountVerificationToken();
        if(!defaultAccountCandidate.isActivated()){
            if (activationAccountVerificationToken != null && LocalDateTime.now().isBefore(activationAccountVerificationToken.getCreationDate().plusHours(durationResendUrl.activationUrl()))){
                throw new VerificationTokenNotRegeneratedYetException("the token not regenerated yet after " + durationResendUrl.activationUrl() + " hours");
            } else {
                generatedToken = randomToken.generateRandomToken64Characters();
                creationDate = LocalDateTime.now();
                activationURL = urlDelegateService.url()
                        + AuthenticationServiceUrlPathConstants.ACTIVATION_URI_PATH
                        + generatedToken + AuthenticationServiceUrlPathConstants.URI_EMAIL_PARAMETER
                        + defaultAccountCandidate.getEmail();

                if (activationAccountVerificationToken == null){
                    activationAccountVerificationToken = new ActivationAccountVerificationToken();
                }

                activationAccountVerificationToken.setToken(generatedToken);
                activationAccountVerificationToken.setCreationDate(creationDate);
                activationAccountVerificationToken.setCandidate(defaultAccountCandidate);
                defaultAccountCandidate.setActivationAccountVerificationToken(activationAccountVerificationToken);
                defaultAccountCandidateRepository.save(defaultAccountCandidate);
                return new AccountEmailAndUrlDTO(defaultAccountCandidate.getEmail(), activationURL);
            }
        }  else {
            throw new VerificationTokenException("the account is already activated so you can't generate a new token!");
        }

        /*else if (verificationToken == null){
            generatedToken = randomToken.generateRandomToken64Characters();
            creationDate = LocalDateTime.now();
            activationURL = urlDelegateService.url()
                    + AuthenticationServiceUrlPathConstants.ACTIVATION_URI_PATH
                    + generatedToken + AuthenticationServiceUrlPathConstants.URI_EMAIL_PARAMETER
                    + defaultAccountCandidate.getEmail();
            verificationToken = new VerificationToken();
            verificationToken.setToken(generatedToken);
            verificationToken.setCreationDate(creationDate);
            verificationToken.setForActivation(true);
            verificationToken.setForResetPassword(false);
            defaultAccountCandidate.setVerificationToken(verificationToken);
            defaultAccountCandidateRepository.save(defaultAccountCandidate);
            return new AccountEmailAndUrlDTO(defaultAccountCandidate.getEmail(), activationURL);
        }*/
    }

    @Override
    public long createCandidateAccountWithGoogleMethod(AccountEmailAndRolesAndPermissionsDTO account) throws EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RoleNotFoundException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, AccountNotCreatedException {
        LocalDateTime creationDate;
        GoogleAccountCandidate googleAccountCandidate;
        List<Role> roles = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();

        if((account.getEmail() == null)
                || account.getEmail().isEmpty()
                || account.getEmail().isBlank()) {
            throw new EmailNotFoundException("The emailingConfiguration not found exception");
        }

        if(this.validateEmail(account.getEmail())){
            throw new EmailPatternNotValidException("The emailingConfiguration pattern is not valid");
        }

        if(this.verifyUniquenessOfCandidateEmail(account.getEmail())) {
            throw new AccountAlreadyExistsException("The emailingConfiguration is already exists");
        }

        if((account.getRoles() == null)
                || account.getRoles().isEmpty()){
            throw new RoleNotFoundException("no role found at candidate");
        }

        for(String role : account.getRoles()){
            if(!role.startsWith(RoleNameCommonConstants.PREFIX)){
                throw new RolePrefixException("role name not begin with prefix : " + RoleNameCommonConstants.PREFIX);
            }
            if(!RoleNameCommonConstants.allRoles.contains(role)){
                throw new RoleNotDefinedException("role not defined in roles list");
            }
            System.out.println("role exist : "+ roleRepository.existsByRole(role));
            if (!roleRepository.existsByRole(role)){
                roleRepository.save(new Role(role));
            }
            roles.add(roleRepository.findByRole(role));
        }

        if(!(account.getPermissions() == null)
                && !account.getPermissions().isEmpty()){
            for (String permission : account.getPermissions()){
                if (!permission.startsWith(PermissionNameCommonConstants.PREFIX)){
                    throw new PermissionPrefixException("permission name not begin with prefix : " + PermissionNameCommonConstants.PREFIX);
                }
                if (!PermissionNameCommonConstants.allPermissions.contains(permission)){
                    throw new PermissionNotDefinedException("permission ot defined in permissions list");
                }
                if (!permissionRepository.existsByPermission(permission)){
                    permissionRepository.save(new Permission(permission));
                }
                permissions.add(permissionRepository.findByPermission(permission));
            }
        }

        creationDate = LocalDateTime.now();
        googleAccountCandidate = new GoogleAccountCandidate(account.getEmail(), creationDate, roles, permissions);
        googleAccountCandidate = googleAccountCandidateRepository.save(googleAccountCandidate);

        if(googleAccountCandidate.getId() == 0
                || !(new HashSet<>(googleAccountCandidate.getRoles()).containsAll(roles))
                || !(new HashSet<>(googleAccountCandidate.getPermissions()).containsAll(permissions))) {
            throw new AccountNotCreatedException("account not created correctly or may not created at all");
        }

        return googleAccountCandidate.getId();
    }

    @Override
    public void deleteCandidateGoogleAccountById(long id) {
        GoogleAccountCandidate candidate = googleAccountCandidateRepository.findById(id).get();
        candidate.getRoles().clear();
        candidate.getPermissions().clear();
        googleAccountCandidateRepository.delete(candidate);
    }

    @Override
    public AccountEmailAndUrlDTO generateResetPasswordUrlForAccountCreatedByDefaultMethod(String email) throws UrlConfigurationNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountNotFoundException, VerificationTokenNotRegeneratedYetException {
        String generatedToken;
        String resetPasswordURL;
        LocalDateTime creationDate;
        DefaultAccountCandidate defaultAccountCandidate;
        ResetPasswordVerificationToken resetPasswordVerificationToken;

        if((urlDelegateService.url() == null)){
            throw new UrlConfigurationNotFoundException("The configuration of reset password account url  is null" );
        }

        if((email == null)
                || email.isEmpty()
                || email.isBlank()) {
            throw new EmailNotFoundException("The emailingConfiguration not found exception");
        }

        if(this.validateEmail(email)){
            throw new EmailPatternNotValidException("The emailingConfiguration pattern is not valid");
        }

        defaultAccountCandidate = defaultAccountCandidateRepository.findDefaultAccountCandidateByEmail(email);

        if (defaultAccountCandidate == null){
            throw new AccountNotFoundException("the account not found");
        }
        resetPasswordVerificationToken = defaultAccountCandidate.getResetPasswordVerificationToken();

        if(resetPasswordVerificationToken != null && LocalDateTime.now().isBefore(resetPasswordVerificationToken.getCreationDate().plusHours(durationResendUrl.resetPasswordUrl()))){
            throw new VerificationTokenNotRegeneratedYetException("the token not regenerated yet");
        } else {
            generatedToken = randomToken.generateRandomToken64Characters();
            creationDate = LocalDateTime.now();
            resetPasswordURL = urlDelegateService.url()
                    + AuthenticationServiceUrlPathConstants.REST_PASSWORD_URI_PATH
                    + generatedToken + AuthenticationServiceUrlPathConstants.URI_EMAIL_PARAMETER
                    + defaultAccountCandidate.getEmail();

            if (resetPasswordVerificationToken == null){
                resetPasswordVerificationToken = new ResetPasswordVerificationToken();
            }

            resetPasswordVerificationToken.setToken(generatedToken);
            resetPasswordVerificationToken.setCreationDate(creationDate);
            resetPasswordVerificationToken.setCandidate(defaultAccountCandidate);
            defaultAccountCandidate.setResetPasswordVerificationToken(resetPasswordVerificationToken);
            defaultAccountCandidateRepository.save(defaultAccountCandidate);
            return new AccountEmailAndUrlDTO(defaultAccountCandidate.getEmail(), resetPasswordURL);
        }
    }

    @Override
    public void resetPasswordTheCandidateAccountCreatedByDefaultMethod(AccountEmailAndNewPasswordAndActivationTokenDTO accountEmailAndNewPasswordAndActivationTokenDTO) throws EmailNotFoundException, VerificationTokenNotFoundException, EmailPatternNotValidException, VerificationTokenPatternNotValidException, AccountNotFoundException, VerificationTokenDurationExpiredException, PasswordNotFoundException, PasswordPatternNotValidException, VerificationTokensNotEqualsException {
        DefaultAccountCandidate defaultAccountCandidate;


        if((accountEmailAndNewPasswordAndActivationTokenDTO.getEmail() == null)
                || accountEmailAndNewPasswordAndActivationTokenDTO.getEmail().isEmpty()
                || accountEmailAndNewPasswordAndActivationTokenDTO.getEmail().isBlank()){
            throw new EmailNotFoundException("The emailingConfiguration not found exception");
        }

        if((accountEmailAndNewPasswordAndActivationTokenDTO.getActivationToken() == null)
                || accountEmailAndNewPasswordAndActivationTokenDTO.getActivationToken().isBlank()
                || accountEmailAndNewPasswordAndActivationTokenDTO.getActivationToken().isEmpty()){
            throw new VerificationTokenNotFoundException("The token is not found exception");
        }

        if((accountEmailAndNewPasswordAndActivationTokenDTO.getNewPassword() == null)
                || accountEmailAndNewPasswordAndActivationTokenDTO.getNewPassword().isEmpty()
                || accountEmailAndNewPasswordAndActivationTokenDTO.getNewPassword().isBlank()) {
            throw new PasswordNotFoundException("the password not found exception");
        }

        if (this.validateEmail(accountEmailAndNewPasswordAndActivationTokenDTO.getEmail())){
            throw new EmailPatternNotValidException("The emailingConfiguration pattern is not valid");
        }

        if (this.validateActivationToken(accountEmailAndNewPasswordAndActivationTokenDTO.getActivationToken())){
            throw new VerificationTokenPatternNotValidException("The activation token pattern is not valid");
        }

        if(this.validatePassword(accountEmailAndNewPasswordAndActivationTokenDTO.getNewPassword())){
            throw new PasswordPatternNotValidException("Password Pattern is not valid");
        }

        defaultAccountCandidate = defaultAccountCandidateRepository.findDefaultAccountCandidateByEmail(accountEmailAndNewPasswordAndActivationTokenDTO.getEmail());

        if(defaultAccountCandidate == null){
            throw new AccountNotFoundException("the account is not created with default method");
        }


        if(defaultAccountCandidate.getResetPasswordVerificationToken() == null){
            throw new VerificationTokenNotFoundException("There is no reset password token for the account in database");
        }

        if(Duration.between(defaultAccountCandidate.getResetPasswordVerificationToken().getCreationDate(), LocalDateTime.now()).toHours()
                > durationOfGeneratedToken.passwordResetToken()){
            throw new VerificationTokenDurationExpiredException("The duration of activation token is expired");
        }

        if(defaultAccountCandidate.getResetPasswordVerificationToken().getToken().equals(accountEmailAndNewPasswordAndActivationTokenDTO.getActivationToken())){
            resetPasswordVerificationTokenRepository.delete(defaultAccountCandidate.getResetPasswordVerificationToken());
            defaultAccountCandidate.setResetPasswordVerificationToken(null);
            defaultAccountCandidate.setPassword(passwordEncoder.encode(accountEmailAndNewPasswordAndActivationTokenDTO.getNewPassword()));
            defaultAccountCandidateRepository.save(defaultAccountCandidate);
        } else{
            throw new VerificationTokensNotEqualsException("token not equal activation token");
        }
    }

    @Override
    public List<String> readAllAdminRoles() {
        return RoleNameCommonConstants.allAdminsRoles;
    }

    @Override
    public List<String> readAllAdminPermissions() {
        return PermissionNameCommonConstants.allAdminsPermissions;
    }

    @Override
    public List<AdminAccountDTO> readAllAdminsAccount() {
        List<AdminAccountDTO> adminAccountList = new ArrayList<>();

        for (DefaultAccountAdmin admin : defaultAccountAdminRepository.findAll()){
            adminAccountList.add(new AdminAccountDTO(admin.getId(), admin.getCreationDate(), admin.getEmail(), admin.isSuspended(), admin.getRoles().stream().map(Role::getRole).toList(), admin.getPermissions().stream().map(Permission::getPermission).toList()));
        }
        return adminAccountList;
    }

    @Override
    public List<AdminAccountDTO> readAdminsAccountByKeywords(List<Long> ids) {
        List<AdminAccountDTO> adminAccountList = new ArrayList<>();
        for (DefaultAccountAdmin admin : defaultAccountAdminRepository.findAllByIdIn(ids)){
            adminAccountList.add(new AdminAccountDTO(admin.getId(), admin.getCreationDate(), admin.getEmail(), admin.isSuspended(), admin.getRoles().stream().map(Role::getRole).toList(), admin.getPermissions().stream().map(Permission::getPermission).toList()));
        }

        return adminAccountList;
    }

    @Override
    public AdminAccountDTO readAdminAccountById(Long id) throws AccountNotFoundException {
        Optional<DefaultAccountAdmin> optional;
        DefaultAccountAdmin admin;

        optional= defaultAccountAdminRepository.findById(id);
        if (optional.isEmpty()){
            throw new AccountNotFoundException("admin account not found for update his email");
        }

        admin = optional.get();

        return new AdminAccountDTO(admin.getId(),
                admin.getCreationDate(),
                admin.getEmail(),
                admin.isSuspended(),
                admin.getRoles().stream().map(Role::getRole).toList(),
                admin.getPermissions().stream().map(Permission::getPermission).toList());
    }

    @Override
    public AdminAccountWithPasswordDTO readAdminAccountAndPasswordById(Long id) throws AccountNotFoundException {
        Optional<DefaultAccountAdmin> optional;
        DefaultAccountAdmin admin;

        optional= defaultAccountAdminRepository.findById(id);
        if (optional.isEmpty()){
            throw new AccountNotFoundException("admin account not found for update his email");
        }

        admin = optional.get();

        return new AdminAccountWithPasswordDTO(admin.getId(),
                admin.getCreationDate(),
                admin.getEmail(),
                admin.getPassword(),
                admin.isSuspended(),
                admin.getRoles().stream().map(Role::getRole).toList(),
                admin.getPermissions().stream().map(Permission::getPermission).toList());
    }

    @Override
    public Map<Long,AdminAccountDTO> readAllSuspendedAdminsAccount() {
        Map<Long,AdminAccountDTO> suspendedAdmins = new HashMap<>();
        for (DefaultAccountAdmin suspendedAdmin: defaultAccountAdminRepository.findAllBySuspended(true)){
            suspendedAdmins.put(suspendedAdmin.getId(), new AdminAccountDTO(suspendedAdmin.getId(),
                    suspendedAdmin.getCreationDate(),
                    suspendedAdmin.getEmail(),
                    suspendedAdmin.isSuspended(),
                    suspendedAdmin.getRoles().stream().map(Role::getRole).toList(),
                    suspendedAdmin.getPermissions().stream().map(Permission::getPermission).toList()));
        }
        return suspendedAdmins;
    }

    @Override
    public void updateAdminAccountSuspensionState(long idAccount, Boolean value) throws AccountNotFoundException, ValueNullException {
        DefaultAccountAdmin admin;
        Optional<DefaultAccountAdmin> optional;

        if (value == null){
            throw new ValueNullException("the suspension value is null");
        }

        optional = defaultAccountAdminRepository.findById(idAccount);

        if (optional.isEmpty()){
            throw new AccountNotFoundException("admin account not found exception");
        }

        admin = optional.get();
        admin.setSuspended(value);
        defaultAccountAdminRepository.save(admin);
    }

    @Override
    public void updateAdminAccountPassword(long idAccount, String newPassword) throws AccountNotFoundException, PasswordNotFoundException, PasswordPatternNotValidException {
        DefaultAccountAdmin admin;
        Optional<DefaultAccountAdmin> optional;

        if((newPassword == null)
                || newPassword.isEmpty()
                || newPassword.isBlank()) {
            throw new PasswordNotFoundException("the password is null or empty or blank");
        }

        if(this.validatePassword(newPassword)){
            throw new PasswordPatternNotValidException("Password Pattern is not valid");
        }

        optional = defaultAccountAdminRepository.findById(idAccount);
        if (optional.isEmpty()){
            throw new AccountNotFoundException("admin account not found exception");
        }

        admin = optional.get();
        admin.setPassword(passwordEncoder.encode(newPassword));
        defaultAccountAdminRepository.save(admin);


    }

    @Override
    public AdminAccountDTO updateAdminAccountEmailOrRolesOrPermissions(AccountIdAndEmailAndRolesAndPermissionsDTO adminAccountNewModifications) throws AccountNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountAlreadyExistsException, RolePrefixException, RoleNotDefinedException, PermissionPrefixException, PermissionNotDefinedException, ValueNullException {
        DefaultAccountAdmin admin;
        Optional<DefaultAccountAdmin> optional;
        List<Role> roles;
        List<Permission> permissions;

        optional = defaultAccountAdminRepository.findById(adminAccountNewModifications.getId());
        if (optional.isEmpty()){
            throw new AccountNotFoundException("admin account not found exception");
        }

        admin = optional.get();

        if((adminAccountNewModifications.getEmail() == null)
                || adminAccountNewModifications.getEmail().isEmpty()
                || adminAccountNewModifications.getEmail().isBlank()) {
            throw new EmailNotFoundException("The emailing is null or empty or blank");
        }

        if(this.validateEmail(adminAccountNewModifications.getEmail())){
            throw new EmailPatternNotValidException("The emailing pattern is not valid");
        }

        if(this.verifyUniquenessOfAdminEmail(adminAccountNewModifications.getEmail())
                && !adminAccountNewModifications.getEmail().equals(admin.getEmail())) {
            throw new AccountAlreadyExistsException("The email is already exists");
        }

        roles = new ArrayList<>();
        permissions = new ArrayList<>();

        if(adminAccountNewModifications.getRoles() == null) {
            throw new ValueNullException("roles list is null");
        }
        for (String role : adminAccountNewModifications.getRoles()) {
                if (!role.startsWith(RoleNameCommonConstants.PREFIX)) {
                    throw new RolePrefixException("role name not begin with prefix : " + RoleNameCommonConstants.PREFIX);
                }
                if (!RoleNameCommonConstants.allRoles.contains(role)) {
                    throw new RoleNotDefinedException("role not defined in roles list");
                }
                if (!roleRepository.existsByRole(role)) {
                    roleRepository.save(new Role(role));
                }
                roles.add(roleRepository.findByRole(role));
            }



        if(adminAccountNewModifications.getPermissions() == null) {
            throw new ValueNullException("permissions list is null");
        }
        for (String permission : adminAccountNewModifications.getPermissions()){
                if (!permission.startsWith(PermissionNameCommonConstants.PREFIX)){
                    throw new PermissionPrefixException("permission name not begin with prefix : " + PermissionNameCommonConstants.PREFIX);
                }
                if (!PermissionNameCommonConstants.allPermissions.contains(permission)){
                    throw new PermissionNotDefinedException("permission ot defined in permissions list");
                }
                if (!permissionRepository.existsByPermission(permission)){
                    permissionRepository.save(new Permission(permission));
                }
                permissions.add(permissionRepository.findByPermission(permission));
            }



        admin.setEmail(adminAccountNewModifications.getEmail());
        admin.setRoles(roles);
        admin.setPermissions(permissions);
        defaultAccountAdminRepository.save(admin);

        return new AdminAccountDTO(admin.getId(),
                admin.getCreationDate(),
                admin.getEmail(),
                admin.isSuspended(),
                admin.getRoles().stream().map(Role::getRole).toList(),
                admin.getPermissions().stream().map(Permission::getPermission).toList());
    }

    @Override
    public void deleteOneAdminAccount(long idAccount) throws AccountNotFoundException {
        DefaultAccountAdmin admin;
        Optional<DefaultAccountAdmin> optional = defaultAccountAdminRepository.findById(idAccount);
        if (optional.isEmpty()){
            throw new AccountNotFoundException("admin account not found exception");
        }
        admin = optional.get();
        admin.getRoles().clear();
        admin.getPermissions().clear();
        defaultAccountAdminRepository.delete(admin);
    }

    /*@Override
    public void deleteGroupAdminAccount(List<Long> ids) {
        defaultAccountAdminRepository.deleteAllByIdIn(ids);
    }

    @Override
    public void deleteAllAdminAccount() {
        defaultAccountAdminRepository.deleteAll();
    }*/

    private boolean validateEmail(String email) {
        return !regexPatternValidatorUtil.validateStringPattern(email,
                RegexPatternConstants.EMAIL_REGEX_PATTERN);
    }

    private boolean validatePassword(String password) {
        return !regexPatternValidatorUtil.validateStringPattern(password,
                RegexPatternConstants.PASSWORD_REGEX_PATTERN);
    }

    private boolean verifyUniquenessOfCandidateEmail(String email) {
        return ((defaultAccountCandidateRepository.findDefaultAccountCandidateByEmail(email)) != (null))
                || ((googleAccountCandidateRepository.findGoogleAccountCandidateByEmail(email)) != (null));
    }

    private boolean verifyUniquenessOfAdminEmail(String email){
        return defaultAccountAdminRepository.findDefaultAccountAdminByEmail(email) != null;
    }

    private boolean validateActivationToken(String activationToken) {

        return !regexPatternValidatorUtil.validateStringPattern(activationToken,
                RegexPatternConstants.ACTIVATION_TOKEN_PATTERN);
    }
}
