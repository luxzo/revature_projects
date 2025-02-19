package model;

import constants.Status;

import java.math.BigDecimal;
import java.sql.Date;

public record Loans(
        String loanId,
        BigDecimal loanAmount,
        Date startDate,
        Date endDate,
        Date dueDate,
        Status status,
        float interestRate,
        BigDecimal amountDue,
        int loanTerm,
        String userId
) {
}
