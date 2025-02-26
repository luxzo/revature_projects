package model;

import constants.Status;

public record LoanStatus(
        String loan_status_id,
        Status status
) {
}
