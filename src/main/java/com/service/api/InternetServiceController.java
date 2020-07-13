package com.service.api;

import com.service.entities.UserProfile;
import com.service.services.InternetRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/internet")
public class InternetServiceController {

    @Autowired
    InternetRepoService internetRepoService;

    @PostMapping(value = "/createInternetServiceRequest")
    public ResponseEntity<HttpStatus> requestInternetAccess(@RequestBody UserProfile userProfile) {
        internetRepoService.internetRequestService(userProfile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "cancelSubscriptionByRequestId/{request_id}")
    public ResponseEntity<UserProfile> cancelInternetSubscriptionRequest(@PathVariable(value = "request_id") Long request_id) {
        return new ResponseEntity<>(internetRepoService.cancelInternetSubscriptionRequest(request_id), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllRequestedOrders")
    public ResponseEntity<List<UserProfile>> getAllInternetSubscriptionRequest() {
        List<UserProfile> profileList = internetRepoService.getAllInternetSubscriptionRequest();
        return new ResponseEntity<>(profileList, HttpStatus.FOUND);
    }

    @PutMapping("/updateProfileIdByPassingRequestId/{request_id}")
    public UserProfile uploadUserId(@PathVariable(value = "request_id") Long request_id, @RequestParam(value = "picture_id", required = true) File picture_id) {
        return internetRepoService.updateProfileIdRequest(request_id, picture_id);
    }
}
