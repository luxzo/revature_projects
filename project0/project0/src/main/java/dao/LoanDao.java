package dao;

import controller.ConnectionController;
import dto.LoanByIdDto;
import model.Loan;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanDao {
    public static final Logger logger = LoggerFactory.getLogger(LoanDao.class);


    public Loan createNewLoan(Loan newLoan) throws PSQLException {
        String sql = "INSERT INTO public.loans(loan_amount, loan_start_date, loan_end_date, user_id, loan_status_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setBigDecimal(1, newLoan.getLoan_amount());
            pstm.setDate(2, newLoan.getLoan_start_date());
            pstm.setDate(3, newLoan.getLoan_end_date());
            pstm.setInt(4, newLoan.getUser_id());
            //Set status_id as Pending (4) by default
            pstm.setInt(5, newLoan.getLoan_status_id());
            pstm.execute();
            logger.info("INSERT INTO public.loans(loan_amount, loan_start_date, loan_end_date, user_id, loan_status_id) VALUES (?, ?, ?, ?, ?)");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return newLoan;
    }

    public LoanByIdDto getLoanById(int loanId) {
        String sql = "select loan_id, loan_amount, loan_start_date, loan_end_date, u.name, u.last_name, u.phone, a.email, ls.status\n" +
                "from loans l\n" +
                "join users u on l.user_id = u.user_id \n" +
                "join accounts a on u.account_id = a.account_id \n" +
                "join loan_status ls on l.loan_status_id = ls.status_id\n" +
                "where loan_id = ?";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, loanId);
            logger.info("" + loanId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                return new LoanByIdDto(
                        rs.getInt("loan_id"),
                        rs.getBigDecimal("loan_amount"),
                        rs.getDate("loan_start_date"),
                        rs.getDate("loan_end_date"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public Loan updateLoan(Loan updatedLoan) {
        String sql = "UPDATE loans SET loan_amount = ?, loan_start_date = ?, loan_end_date = ?, loan_status_id = ? WHERE user_id = ? AND loan_id = ?";
        try (Connection conn = ConnectionController.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setBigDecimal(1, updatedLoan.getLoan_amount());
            pstm.setDate(2, updatedLoan.getLoan_start_date());
            pstm.setDate(3, updatedLoan.getLoan_end_date());
            pstm.setInt(4, updatedLoan.getLoan_status_id());
            pstm.setInt(5, updatedLoan.getUser_id());
            pstm.setInt(6, updatedLoan.getLoan_id());

            pstm.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return updatedLoan;
    }
}
