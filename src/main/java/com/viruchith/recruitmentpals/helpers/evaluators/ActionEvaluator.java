package com.viruchith.recruitmentpals.helpers.evaluators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viruchith.recruitmentpals.helpers.AuthenticationHelper;
import com.viruchith.recruitmentpals.helpers.StandardResponse;
import com.viruchith.recruitmentpals.helpers.UserTypes;
import com.viruchith.recruitmentpals.models.PlacementCoordinator;
import com.viruchith.recruitmentpals.services.AdminUserService;
import com.viruchith.recruitmentpals.services.PlacementCoordiantorService;

import lombok.Data;

@Service
@Data
public class ActionEvaluator {

	protected AuthenticationHelper authenticationHelper;

	protected boolean valid;

	protected StandardResponse errorResponse;

	@Autowired
	protected PlacementCoordiantorService placementCoordiantorService;

	@Autowired
	protected AdminUserService adminUserService;

	public boolean isUserAuthorized(String... authorizedUsers) {
		Set<String> set = new HashSet<>(Arrays.asList(authorizedUsers));

		if (!set.contains(authenticationHelper.getUserType())) {
			errorResponse = new StandardResponse(false, String
					.format("Only %S users are authorized to perform this action !", Arrays.toString(authorizedUsers)));
			return false;
		}

		return true;
	}

	public boolean canAdminOrCoordinatorPerformCoordinatorAction(long coordinatorId) {
		Optional<PlacementCoordinator> placementCoordinatorOptional = placementCoordiantorService
				.findFirstById(coordinatorId);

		if (!placementCoordinatorOptional.isPresent()) {
			errorResponse = new StandardResponse(false, String.format(
					"The %S user your'e trying to perform this action on, does not exist !", UserTypes.COORDINATOR));
		} else if (isAdmin(authenticationHelper)) {
			return true;
		} else if (isCoordinator(authenticationHelper)) {
			PlacementCoordinator coordinator = (PlacementCoordinator) authenticationHelper.getUser();
			if (coordinator.getId() == coordinatorId) {
				valid = true;
			} else {
				errorResponse = new StandardResponse(false,
						String.format("You are not authorized to perform this action on another %S !",
								authenticationHelper.getUserType()));
			}
		} else {
			errorResponse = new StandardResponse(false,
					String.format("%S is not authorized to perform this action !", authenticationHelper.getUserType()));
		}

		return false;
	}

	public static boolean isAdmin(AuthenticationHelper authenticationHelper) {
		return authenticationHelper.getUserType().equals(UserTypes.ADMIN);
	}

	public static boolean isCoordinator(AuthenticationHelper authenticationHelper) {
		return authenticationHelper.getUserType().equals(UserTypes.COORDINATOR);
	}

}
