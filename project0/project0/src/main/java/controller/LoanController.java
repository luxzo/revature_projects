package controller;

import dto.LoanByIdDto;
import dto.UserDto;
import io.javalin.http.Context;
import model.Loan;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LoanService;

public class LoanController {
    public static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Create a new loan controller
     * @param ctx
     */
    public void createNewLoan(Context ctx) {
        Loan loan = ctx.bodyAsClass(Loan.class);

        Loan newLoan;
        newLoan = loanService.createNewLoan(
                loan.getLoan_amount(),
                loan.getLoan_start_date(),
                loan.getLoan_end_date(),
                loan.getLoan_term(),
                loan.getUser_id(),
                loan.getLoan_status_id()
        );

        if (newLoan != null) {
            ctx.json("{\"message\": \"Loan created\"}");
        }
    }


    /**
     * Retrieve loan by Id
     * @param ctx
     */
    public void getLoanById(Context ctx) {
        int loanId = Integer.parseInt(ctx.pathParam("id"));
        LoanByIdDto loan = null;

        //In case loanId is null, will catch NullPointerException
        try {
            loan = loanService.getLoanById(loanId);
            ctx.json(loan);
            logger.info("Loan id: " + loanId + " found");
        }
        catch (NullPointerException e) {
            logger.error(e.getMessage());
            ctx.status(404);
            ctx.json("{\n\"loan_id\": \"" + loanId + "\"\n}");
        }
    }
}