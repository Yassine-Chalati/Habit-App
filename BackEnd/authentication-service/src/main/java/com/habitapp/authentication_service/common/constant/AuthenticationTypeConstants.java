package com.menara.authentication.common.constant;

import java.util.List;

public class AuthenticationTypeConstants {
    public static final String WITHOUT_JSON_WEB_TOKEN = "WithoutJsonWebToken";
    public static final String ACCOUNT_JSON_WEB_TOKEN = "AccountJsonWebToken";
    public static final String SUPER_ADMIN_JSON_WEB_TOKEN = "SuperAdminJsonWebToken";
    public static final String SERVICE_JSON_WEB_TOKEN = "ServiceJsonWebToken";
    public static final List<String> allTypes = List.of(WITHOUT_JSON_WEB_TOKEN, ACCOUNT_JSON_WEB_TOKEN, SUPER_ADMIN_JSON_WEB_TOKEN, SERVICE_JSON_WEB_TOKEN);
}
