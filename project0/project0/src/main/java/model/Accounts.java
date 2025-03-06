package model;
/*
public record Accounts(
        String account_id,
        String email,
        String password,
        String role_id
) {
}*/

public class Accounts {
    private int accountId;
    private String email;
    private String password;
    private int role_id;

    public Accounts(String email, String password, int role_id) {
        this.email = email;
        this.password = password;
        this.role_id = role_id;
    }

    public Accounts(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Accounts() {}

    public Accounts(String email) {
        this.email = email;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "email='" + email + '\'' +
                '}';
    }
}
