package com.aifood.controller.preference;

import org.springframework.web.bind.annotation.*;

import com.aifood.dto.preference.CreateUserPreferenceRequest;
import com.aifood.dto.preference.UserPreferenceResponse;
import com.aifood.service.preference.UserPreferenceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(
            UserPreferenceService userPreferenceService) {

        this.userPreferenceService = userPreferenceService;
    }

    @PostMapping
    public UserPreferenceResponse createOrUpdatePreference(
            @Valid @RequestBody CreateUserPreferenceRequest request) {

        return userPreferenceService.createOrUpdateUserPreference(request);
    }

    @GetMapping("/me")
    public UserPreferenceResponse getMyPreference() {

        return userPreferenceService.getMyPreference();
    }
}