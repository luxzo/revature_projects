package model;

import constants.Roles;

public record AccountRoles(
        String role_id,
        Roles role
) {
}
