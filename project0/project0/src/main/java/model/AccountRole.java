package model;

import constants.Roles;

public class AccountRole {
    private int role_id;
    private Roles role;

    public AccountRole() {
    }

    public AccountRole(int role_id, String role) {
        this.role_id = role_id;
        this.role = Roles.valueOf(role.toUpperCase());
    }

    public AccountRole(String role) {
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
        return "AccountRole{" +
                "role=" + role +
                '}';
    }
}
