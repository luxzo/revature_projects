package model;

import constants.Roles;

public class AccountRoles {
    private int role_id;
    private Roles role;

    public AccountRoles() {
    }

    public AccountRoles(int role_id, String role) {
        this.role_id = role_id;
        this.role = Roles.valueOf(role.toUpperCase());
    }

    public AccountRoles(String role) {
        this.role = Roles.valueOf(role.toUpperCase());
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = Roles.valueOf(role.toUpperCase());
    }

    @Override
    public String toString() {
        return "AccountRoles{" +
                "role=" + role +
                '}';
    }
}
