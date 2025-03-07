package service;

import dao.LoanDao;
import dto.LoanByIdDto;
import model.Loan;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Date;

public class LoanService {
    public static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    private final LoanDao loanDao;

    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public Loan createNewLoan(
            BigDecimal loan_amount,
            Date loan_start_date,
            Date loan_end_date,
            int user_id,
            int loan_status_id
    ) {
        Loan newLoan = new Loan();
        newLoan.setLoan_amount(loan_amount);
        newLoan.setLoan_start_date(loan_start_date);
        newLoan.setLoan_end_date(loan_end_date);
        newLoan.setUser_id(user_id);
        newLoan.setLoan_status_id(loan_status_id);

        try {
            return loanDao.createNewLoan(newLoan);
        } catch (PSQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Get a loan by id
     * @param loanId
     * @return
     */
    public LoanByIdDto getLoanById(int loanId) {
        if (loanDao.getLoanById(loanId) == null)
            logger.error("Loan id: " + loanId + " not found");
        return loanDao.getLoanById(loanId);
    }

    public Loan updateLoan(int loanId, BigDecimal loanAmount, Date loanStartDate, Date loanEndDate, int userId, int loanStatusId) {
        Loan updatedLoan = new Loan();
        updatedLoan.setLoan_id(loanId);
        updatedLoan.setLoan_amount(loanAmount);
        updatedLoan.setLoan_start_date(loanStartDate);
        updatedLoan.setLoan_end_date(loanEndDate);
        updatedLoan.setUser_id(userId);
        updatedLoan.setLoan_status_id(loanStatusId);
        return loanDao.updateLoan(updatedLoan);
    }
}
