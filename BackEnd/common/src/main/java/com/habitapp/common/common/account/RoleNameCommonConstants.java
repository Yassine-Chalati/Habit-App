package com.habitapp.common.common.account;

import java.util.List;

public class RoleNameCommonConstants {
    public static final String PREFIX = "ROLE_";

    // Person roles
    public static final String NEW_PERSON = PREFIX + "NEW_PERSON";
    public static final String PERSON = PREFIX + "PERSON";

    // Server roles
    public static final String SERVER = PREFIX + "SERVER";
    public static final String AUTHENTICATION_SERVER = PREFIX + "AUTHENTICATION_SERVER";
    public static final String EMAILING_SERVER = PREFIX + "EMAILING_SERVER";
    public static final String USER_SERVER = PREFIX + "USER_SERVER";

    //List of roles
    public static final List<String> allRoles = List.of(NEW_PERSON, PERSON, SERVER, AUTHENTICATION_SERVER, EMAILING_SERVER, USER_SERVER);
}
