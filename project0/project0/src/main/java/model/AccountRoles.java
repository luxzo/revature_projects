package model;

import constants.Roles;

public record AccountRoles(
        String roleId,
        Roles role
) {
}
