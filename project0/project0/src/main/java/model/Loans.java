package model;

import constants.Status;

import java.math.BigDecimal;
import java.sql.Date;

public record Loans(
        String loan_id,
        BigDecimal loan_amount,
        Date start_date,
        Date end_date,
        Date due_date,
        float interest_rate,
        BigDecimal amount_due,
        int loan_term,
        BigDecimal debt_payment,
        Date debt_payment_date,
        String user_id,
        String loan_status_id
) {
}
