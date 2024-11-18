package com.menara.authentication.client.user;

import com.internship_hiring_menara.common.http.request.admin.AdminIdRequestHttp;
import com.internship_hiring_menara.common.http.request.admin.AdminsIdsRequestHttp;
import com.internship_hiring_menara.common.http.request.search.SearchWithKeywordsRequestHttp;
import com.internship_hiring_menara.common.http.request_response.admin.AdminRequestResponseHttp;
import com.internship_hiring_menara.common.http.request_response.admin.AdminRequestResponseHttpList;
import com.menara.authentication.configuration.client.UserConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", contextId = "admin", configuration = UserConfiguration.class)
public interface AdminClient {
    @PostMapping("/user/admin/create")
    public ResponseEntity<AdminRequestResponseHttp> createAdmin(@RequestBody AdminRequestResponseHttp adminRequestResponseHttp);

    @GetMapping("/user/admin/read/all")
    public ResponseEntity<AdminRequestResponseHttpList> readAllAdmins();

    @PostMapping("/user/admin/read/search/keywords")
    public ResponseEntity<AdminRequestResponseHttpList> readAdminsByKeywords(@RequestBody SearchWithKeywordsRequestHttp searchKeywordsRequestHttp);

    @PostMapping("/user/admin/read/search/ids")
    public ResponseEntity<AdminRequestResponseHttpList> readAdminsByIds(AdminsIdsRequestHttp adminsIdsRequestHttp);

    @PutMapping("/user/admin//update/all")
    public ResponseEntity<AdminRequestResponseHttp> updateAdmin(@RequestBody AdminRequestResponseHttp adminRequestResponseHttp);

    @DeleteMapping("/user/admin/delete/one")
    public ResponseEntity<Void> deleteAdmin(@RequestBody AdminIdRequestHttp adminIdRequestHttp);

}
