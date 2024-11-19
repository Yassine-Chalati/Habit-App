package com.internship_hiring_menara.emailing_service.security.constant;

import java.util.ArrayList;
import java.util.List;

public class WhiteListUriConstants {
    public static final ArrayList<String> VerifyAccessBadgeFilter = new ArrayList<>(List.of(""));
    public static final ArrayList<String> VerifyRevokedJwtFilter = new ArrayList<>(List.of(""));
    public static final ArrayList<String> VerifyTokenFingerprintFilter = new ArrayList<>(List.of(""));
}
