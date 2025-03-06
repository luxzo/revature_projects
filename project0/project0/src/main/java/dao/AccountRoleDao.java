package dao;

import constants.Roles;
import model.AccountRoles;

import javax.management.relation.Role;

public class AccountRoleDao {
    public AccountRoles findByRoleName(String roleName) {
        AccountRoles accountRoles = new AccountRoles();
        if (roleName.toUpperCase().equals("MANAGER"))
            accountRoles.setRole_id(1);
        else if (roleName.toUpperCase().equals("USER")) {
            accountRoles.setRole_id(2);
        }
        return accountRoles;
    }
}
