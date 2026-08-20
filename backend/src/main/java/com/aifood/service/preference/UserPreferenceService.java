package com.aifood.service.preference;

import com.aifood.dto.preference.CreateUserPreferenceRequest;
import com.aifood.dto.preference.UserPreferenceResponse;

public interface UserPreferenceService {

    UserPreferenceResponse createOrUpdateUserPreference(CreateUserPreferenceRequest request);

    UserPreferenceResponse getMyPreference();

}