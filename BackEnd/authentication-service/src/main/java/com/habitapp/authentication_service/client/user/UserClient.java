package com.menara.authentication.client.user;

import com.internship_hiring_menara.common.http.request_response.admin.CandidateAndAdminRequestResponseHttpList;
import com.menara.authentication.configuration.client.UserConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", contextId = "user", configuration = UserConfiguration.class)
public interface UserClient {

    @GetMapping("/user/read/all")
    public ResponseEntity<CandidateAndAdminRequestResponseHttpList> readAllCandidatesAndAdmins();
}
