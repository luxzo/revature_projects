package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LoanService;

public class LoanController {
    public static final Logger logger = LoggerFactory.getLogger(LoanController.class);


    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
/*
    public Javalin startApi() {


//        app.post("/loans", this::createNewLoan);
//        app.get("/loans", this::getLoans);
//        app.get("/loans/{loanId}", this::getLoanById);
//        app.put("/loans/{loanId}", this::updateLoan);
        /*Todo: tengo duda con estos endpoints, bien podría ser solo con el anterior,
        * o bien con el endpoint siguiente:
        */
//        app.patch("/loans/{loanId}", this::changeLoanStatus);
//        app.put("/loans/{loanId}/approve", this::approveLoan);
//        app.put("/loans/{loanId}/reject", this::rejectLoan);
//        return app;

}
