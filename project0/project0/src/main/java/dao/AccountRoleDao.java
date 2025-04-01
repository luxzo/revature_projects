package dao;

import model.AccountRole;

public class AccountRoleDao {

    /*
    Find a role by name
    Todo currently is not operational, will check in future update
     */
    public AccountRole findByRoleName(String roleName) {
        AccountRole accountRole = new AccountRole();
        if (roleName.equalsIgnoreCase("MANAGER"))
            accountRole.setRole_id(1);
        else if (roleName.equalsIgnoreCase("USER")) {
            accountRole.setRole_id(2);
        }
        return accountRole;
    }
}
