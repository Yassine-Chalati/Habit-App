package com.menara.authentication.client.user;

import com.internship_hiring_menara.common.http.request.candidate.CandidateRequestHttp;
import com.internship_hiring_menara.common.http.request_response.candidate.CandidateRequestResponseHttpList;
import com.menara.authentication.configuration.client.UserConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", contextId = "candidate", configuration = UserConfiguration.class)
public interface CandidateClient {
    @PostMapping("/user/candidate/create")
    public ResponseEntity<?> createCandidate(CandidateRequestHttp candidateRequestHttp);

    @GetMapping("/user/candidate/read/all")
    public ResponseEntity<CandidateRequestResponseHttpList> readAllCandidate();
}
