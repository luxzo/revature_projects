package dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;

public class LoanByIdDto {
    private int loan_id;
    private BigDecimal loan_amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date loan_start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date loan_end_date;
    private int loan_term;
    private String name;
    private String last_name;
    private String phone;
    private String email;
    private String status;

    public LoanByIdDto(int loan_id, BigDecimal loan_amount, Date loan_start_date, Date loan_end_date, int loan_term, String name, String last_name, String phone, String email, String status) {
        this.loan_id = loan_id;
        this.loan_amount = loan_amount;
        this.loan_start_date = loan_start_date;
        this.loan_end_date = loan_end_date;
        this.loan_term = loan_term;
        this.name = name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public BigDecimal getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(BigDecimal loan_amount) {
        this.loan_amount = loan_amount;
    }

    public Date getLoan_start_date() {
        return loan_start_date;
    }

    public void setLoan_start_date(Date loan_start_date) {
        this.loan_start_date = loan_start_date;
    }

    public Date getLoan_end_date() {
        return loan_end_date;
    }

    public void setLoan_end_date(Date loan_end_date) {
        this.loan_end_date = loan_end_date;
    }

    public int getLoan_term() {
        return loan_term;
    }

    public void setLoan_term(int loan_term) {
        this.loan_term = loan_term;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
