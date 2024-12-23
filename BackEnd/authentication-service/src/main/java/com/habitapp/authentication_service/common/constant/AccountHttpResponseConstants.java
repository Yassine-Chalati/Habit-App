package com.habitapp.authentication_service.common.constant;

public class AccountHttpResponseConstants {
    public static final String EMAIL_PATTERN_NOT_VALID = "The pattern of your email is not valid";
    public static final String PASSWORD_PATTERN_NOT_VALID = "The pattern of your password is not valid";
    public static final String EMAIL_NOT_FOUND = "You have to provide an email for create the account";
    public static final String PASSWORD_NOT_FOUND = "You have to provide a password for create the account";
    public static final String ERROR_AT_CREATION_ACCOUNT = "There is an internal error at the server";
    public static final String ACCOUNT_CREATION = "A link to activate your account has been emailed to the address provided.";
    public static final String UNPROCESSABLE_ENTITY = "the account is created but the email cannot be send";
    public static final String ACCOUNT_NOT_CREATED = "the account is not created because an internal error";
}
