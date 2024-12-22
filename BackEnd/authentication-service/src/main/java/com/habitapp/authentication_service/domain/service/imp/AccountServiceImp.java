package com.habitapp.authentication_service.domain.service.imp;


import com.habitapp.authentication_service.common.constant.AuthenticationServiceUrlPathConstants;
import com.habitapp.authentication_service.common.constant.RegexPatternConstants;
import com.habitapp.authentication_service.common.utlil.generator.random.token.RandomTokenGeneratorUtil;
import com.habitapp.authentication_service.common.utlil.regex.RegexPatternValidatorUtil;
import com.habitapp.authentication_service.configuration.record.DurationOfGeneratedToken;
import com.habitapp.authentication_service.configuration.record.DurationResendUrl;
import com.habitapp.authentication_service.configuration.record.UrlDelegateService;
import com.habitapp.authentication_service.domain.entity.ActivationAccountVerificationToken;
import com.habitapp.authentication_service.domain.entity.DefaultAccountIndividual;
import com.habitapp.authentication_service.domain.entity.Permission;
import com.habitapp.authentication_service.domain.entity.Role;
import com.habitapp.authentication_service.domain.exception.account.*;
import com.habitapp.authentication_service.domain.repository.ActivationAccountVerificationTokenRepository;
import com.habitapp.authentication_service.domain.repository.DefaultAccountIndividualRepository;
import com.habitapp.authentication_service.domain.repository.PermissionRepository;
import com.habitapp.authentication_service.domain.repository.RoleRepository;
import com.habitapp.authentication_service.domain.service.AccountService;
import com.habitapp.authentication_service.dto.account.*;
import com.habitapp.common.common.account.PermissionNameCommonConstants;
import com.habitapp.common.common.account.RoleNameCommonConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    @NonNull
    private DefaultAccountIndividualRepository defaultAccountIndividualRepository;
    @NonNull
    private ActivationAccountVerificationTokenRepository activationAccountVerificationTokenRepository;
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
    public AccountIdAndEmailAndActivationURLDTO createIndividualAccountWithDefaultMethod(AccountAndInformationDTO account) throws EmailPatternNotValidException,
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
        DefaultAccountIndividual defaultAccountIndividual;
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

        if(this.verifyUniquenessOfIndividualEmail(account.getEmail())) {
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
        defaultAccountIndividual = new DefaultAccountIndividual(account.getEmail(), passwordEncoder.encode(account.getPassword()), creationDate, roles, permissions, new ActivationAccountVerificationToken(generatedToken, creationDate, null));
        defaultAccountIndividual.getActivationAccountVerificationToken().setIndividual(defaultAccountIndividual);
        defaultAccountIndividual = defaultAccountIndividualRepository.save(defaultAccountIndividual);

        if(defaultAccountIndividual.getId() == 0
                || !(new HashSet<>(defaultAccountIndividual.getRoles()).containsAll(roles))
                || !(new HashSet<>(defaultAccountIndividual.getPermissions()).containsAll(permissions))) {
            throw new AccountNotCreatedException("account not created correctly or may not created at all");
        }

        activationURL = urlDelegateService.url() + AuthenticationServiceUrlPathConstants.ACTIVATION_URI_PATH + generatedToken + AuthenticationServiceUrlPathConstants.URI_EMAIL_PARAMETER + defaultAccountIndividual.getEmail();
        return new AccountIdAndEmailAndActivationURLDTO(defaultAccountIndividual.getId(), defaultAccountIndividual.getEmail(), activationURL);
    }

    @Override
    public void updatePasswordIndividualAccountWithDefaultMethod(AccountAndInformationDTO account) throws PasswordPatternNotValidException,
            PasswordNotFoundException,
            AccountNotFoundException {
        DefaultAccountIndividual defaultAccountIndividual;

        if((account.getPassword() == null)
                || account.getPassword().isEmpty()
                || account.getPassword().isBlank()) {
            throw new PasswordNotFoundException("the password not found exception");
        }

        if(this.validatePassword(account.getPassword())){
            throw new PasswordPatternNotValidException("Password Pattern is not valid");
        }


        defaultAccountIndividual =  defaultAccountIndividualRepository.findById(account.getIdAccount())
                .orElseThrow(() -> new AccountNotFoundException("Account not found for email: " + account.getIdAccount()));
        defaultAccountIndividual.setPassword(passwordEncoder.encode(account.getPassword()));
        defaultAccountIndividualRepository.save(defaultAccountIndividual);

    }


    @Override
    public AccountIdAndAuthoritiesDTO activateTheIndividualAccountCreatedByDefaultMethod(AccountEmailAndActivationTokenDTO accountEmailAndActivationTokenDTO) throws EmailNotFoundException,
            VerificationTokenNotFoundException,
            EmailPatternNotValidException,
            VerificationTokenPatternNotValidException,
            VerificationTokenDurationExpiredException,
            VerificationTokensNotEqualsException, AccountNotFoundException, AccountIsActivatedException {
        DefaultAccountIndividual defaultAccountIndividual;
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

        defaultAccountIndividual = defaultAccountIndividualRepository.findDefaultAccountIndividualByEmail(accountEmailAndActivationTokenDTO.getEmail());

        if(defaultAccountIndividual == null){
//            this.mitigateEnumerationAttack(new AccountNotFoundException("the account is not created with default method"));
            throw new AccountNotFoundException("the account is not created with default method");
        }

        if(defaultAccountIndividual.isActivated()){
//            this.mitigateEnumerationAttack(new AccountIsActivatedException("the account is already activated"));
            throw new AccountIsActivatedException("the account is already activated");
        }

        if(defaultAccountIndividual.getActivationAccountVerificationToken() == null){
//            this.mitigateEnumerationAttack(new VerificationTokenNotFoundException("There is no activation token for the account in database"));
            throw new VerificationTokenNotFoundException("There is no activation token for the account in database");
        }

        if(Duration.between(defaultAccountIndividual.getActivationAccountVerificationToken().getCreationDate(), LocalDateTime.now()).toHours()
                > durationOfGeneratedToken.accountActivationToken()){
            throw new VerificationTokenDurationExpiredException("The duration of activation token is expired");
        }

        if(defaultAccountIndividual.getActivationAccountVerificationToken().getToken().equals(accountEmailAndActivationTokenDTO.getActivationToken())){

            activationAccountVerificationTokenRepository.delete(defaultAccountIndividual.getActivationAccountVerificationToken());
            defaultAccountIndividual.setActivationAccountVerificationToken(null);
            defaultAccountIndividual.setActivated(true);
            defaultAccountIndividualRepository.save(defaultAccountIndividual);
            authorities = defaultAccountIndividual.getRoles().stream().map(Role::getRole).collect(Collectors.joining(" "))
                    + " "
                    + defaultAccountIndividual.getPermissions().stream().map(Permission::getPermission).collect(Collectors.joining(" "));
            return new AccountIdAndAuthoritiesDTO(Long.toString(defaultAccountIndividual.getId()), authorities);
        } else{
            throw new VerificationTokensNotEqualsException("token not equal activation token");
        }
    }

    @Override
    public AccountEmailAndUrlDTO generateActivationUrlForAccountCreatedByDefaultMethod(String email) throws UrlConfigurationNotFoundException, EmailNotFoundException, EmailPatternNotValidException, AccountNotFoundException, VerificationTokenException, VerificationTokenNotRegeneratedYetException {
        String generatedToken;
        String activationURL;
        LocalDateTime creationDate;
        DefaultAccountIndividual defaultAccountIndividual;
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

        defaultAccountIndividual = defaultAccountIndividualRepository.findDefaultAccountIndividualByEmail(email);

        if (defaultAccountIndividual == null){
            throw new AccountNotFoundException("the account not found");
        }

        activationAccountVerificationToken = defaultAccountIndividual.getActivationAccountVerificationToken();
        if(!defaultAccountIndividual.isActivated()){
            if (activationAccountVerificationToken != null && LocalDateTime.now().isBefore(activationAccountVerificationToken.getCreationDate().plusHours(durationResendUrl.activationUrl()))){
                throw new VerificationTokenNotRegeneratedYetException("the token not regenerated yet after " + durationResendUrl.activationUrl() + " hours");
            } else {
                generatedToken = randomToken.generateRandomToken64Characters();
                creationDate = LocalDateTime.now();
                activationURL = urlDelegateService.url()
                        + AuthenticationServiceUrlPathConstants.ACTIVATION_URI_PATH
                        + generatedToken + AuthenticationServiceUrlPathConstants.URI_EMAIL_PARAMETER
                        + defaultAccountIndividual.getEmail();

                if (activationAccountVerificationToken == null){
                    activationAccountVerificationToken = new ActivationAccountVerificationToken();
                }

                activationAccountVerificationToken.setToken(generatedToken);
                activationAccountVerificationToken.setCreationDate(creationDate);
                activationAccountVerificationToken.setIndividual(defaultAccountIndividual);
                defaultAccountIndividual.setActivationAccountVerificationToken(activationAccountVerificationToken);
                defaultAccountIndividualRepository.save(defaultAccountIndividual);
                return new AccountEmailAndUrlDTO(defaultAccountIndividual.getEmail(), activationURL);
            }
        }  else {
            throw new VerificationTokenException("the account is already activated so you can't generate a new token!");
        }


    }

    private boolean validateEmail(String email) {
        return !regexPatternValidatorUtil.validateStringPattern(email,
                RegexPatternConstants.EMAIL_REGEX_PATTERN);
    }

    private boolean validatePassword(String password) {
        return !regexPatternValidatorUtil.validateStringPattern(password,
                RegexPatternConstants.PASSWORD_REGEX_PATTERN);
    }

    private boolean verifyUniquenessOfIndividualEmail(String email) {
        return ((defaultAccountIndividualRepository.findDefaultAccountIndividualByEmail(email)) != (null));
    }

    private boolean validateActivationToken(String activationToken) {

        return !regexPatternValidatorUtil.validateStringPattern(activationToken,
                RegexPatternConstants.ACTIVATION_TOKEN_PATTERN);
    }
}
