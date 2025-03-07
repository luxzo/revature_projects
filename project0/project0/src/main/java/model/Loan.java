package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;

public class Loan {
    private int loan_id;
    private BigDecimal loan_amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date loan_start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date loan_end_date;
    private int user_id;
    private int loan_status_id;

    public Loan() {
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLoan_status_id() {
        return loan_status_id;
    }

    public void setLoan_status_id(int loan_status_id) {
        this.loan_status_id = loan_status_id;
    }
}
