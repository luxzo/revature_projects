package dto;

public class UserDto {
    private String name;
    private String last_name;
    private String phone;

    public UserDto(String name, String last_name, String phone) {
        this.name = name;
        this.last_name = last_name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
