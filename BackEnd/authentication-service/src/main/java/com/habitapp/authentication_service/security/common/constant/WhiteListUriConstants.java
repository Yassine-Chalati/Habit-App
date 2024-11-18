package com.menara.authentication.security.common.constant;

import java.util.ArrayList;
import java.util.List;

public class WhiteListUriConstants {
    public static final ArrayList<String> VerifyAccessBadgeFilter = new ArrayList<>(List.of(""));
    public static final ArrayList<String> VerifyRevokedJwtFilter = new ArrayList<>(List.of("/authentication/super-admin/default",
            "/authentication/admin/default",
            "/authentication/candidate/default",
            "/authentication/candidate/google",
            "/authentication/candidate/google/callback",
            "/authentication/service/default",
            "/authentication/refresh/token",
            "/account/candidate/default-method/create",
            "/account/candidate/default-method/activate/token/generate",
            "/account/candidate/default-method/activate/token/*",
            "/account/candidate/default-method/reset-password/token/generate",
            "/account/candidate/default-method/reset-password/token/*"));
    public static final ArrayList<String> VerifyTokenFingerprintFilter = new ArrayList<>(List.of("/authentication/super-admin/default",
            "/authentication/admin/default",
            "/authentication/candidate/default",
            "/authentication/candidate/google",
            "/authentication/candidate/google/callback",
            "/authentication/service/default",
            "/authentication/refresh/token",
            "/account/candidate/default-method/create",
            "/account/candidate/default-method/activate/token/generate",
            "/account/candidate/default-method/activate/token/*",
            "/account/candidate/default-method/reset-password/token/generate",
            "/account/candidate/default-method/reset-password/token/*"));
}
