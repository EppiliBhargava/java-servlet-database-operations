package com.myapp.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class MyAppSrv5 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        String tableName = req.getParameter("tableName");
        String primaryKeyValue = req.getParameter("primaryKeyValue");

        startPage(pw);

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    System.getenv().getOrDefault("ORACLE_JDBC_URL", "jdbc:oracle:thin:@localhost:1521:XE"),
                    System.getenv().getOrDefault("ORACLE_DB_USER", "system"),
                    System.getenv().getOrDefault("ORACLE_DB_PASSWORD", "")
            );

            DatabaseMetaData md = con.getMetaData();

            ResultSet pkRs = md.getPrimaryKeys(
                    null,
                    "SYSTEM",
                    tableName.toUpperCase()
            );

            String primaryKeyColumn = null;

            if (pkRs.next()) {
                primaryKeyColumn = pkRs.getString("COLUMN_NAME");
            }

            /*
             * First stage:
             * Display the form to enter primary key value.
             */
            if (primaryKeyValue == null) {

                if (primaryKeyColumn == null) {

                    pw.println("<div class='result-card error-card'>");
                    pw.println("<div class='result-icon'>&#33;</div>");
                    pw.println("<h2>No Primary Key Found</h2>");
                    pw.println("<p>This table does not have a primary key.</p>");
                    pw.println("</div>");

                } else {

                    pw.println("<div class='form-card'>");

                    pw.println("<div class='page-icon'>&#10006;</div>");
                    pw.println("<h1>Delete Record</h1>");
                    pw.println("<p class='subtitle'>Delete a record from <b>"
                            + tableName + "</b></p>");

                    pw.println("<form action='delete' method='GET'>");

                    pw.println("<input type='hidden' name='tableName' value='"
                            + tableName + "'>");

                    pw.println("<div class='form-group'>");
                    pw.println("<label>Primary Key: "
                            + primaryKeyColumn + "</label>");

                    pw.println("<input type='text' "
                            + "name='primaryKeyValue' "
                            + "placeholder='Enter primary key value' "
                            + "required>");

                    pw.println("</div>");

                    pw.println("<button type='submit' class='delete-btn'>");
                    pw.println("&#10006; Delete Record");
                    pw.println("</button>");

                    pw.println("</form>");

                    pw.println("</div>");
                }

            }

            /*
             * Second stage:
             * Delete the record using the primary key.
             */
            else {

                pkRs.close();

                pkRs = md.getPrimaryKeys(
                        null,
                        "SYSTEM",
                        tableName.toUpperCase()
                );

                if (pkRs.next()) {

                    primaryKeyColumn = pkRs.getString("COLUMN_NAME");

                    String query =
                            "DELETE FROM " + tableName
                            + " WHERE " + primaryKeyColumn + " = ?";

                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setString(1, primaryKeyValue);

                    int count = ps.executeUpdate();

                    if (count > 0) {

                        pw.println("<div class='result-card success-card'>");

                        pw.println("<div class='result-icon'>&#10003;</div>");

                        pw.println("<h2>Record Deleted Successfully</h2>");

                        pw.println("<p>Record with "
                                + primaryKeyColumn
                                + " = <b>"
                                + primaryKeyValue
                                + "</b> has been deleted.</p>");

                        pw.println("</div>");

                    } else {

                        pw.println("<div class='result-card error-card'>");

                        pw.println("<div class='result-icon'>&#33;</div>");

                        pw.println("<h2>No Record Found</h2>");

                        pw.println("<p>No record exists with "
                                + primaryKeyColumn
                                + " = <b>"
                                + primaryKeyValue
                                + "</b>.</p>");

                        pw.println("</div>");
                    }

                    ps.close();

                } else {

                    pw.println("<div class='result-card error-card'>");

                    pw.println("<div class='result-icon'>&#33;</div>");

                    pw.println("<h2>No Primary Key Found</h2>");

                    pw.println("<p>Unable to delete the record because "
                            + "the table has no primary key.</p>");

                    pw.println("</div>");
                }
            }

            pkRs.close();
            con.close();

        } catch (Exception e) {

            pw.println("<div class='result-card error-card'>");

            pw.println("<div class='result-icon'>&#33;</div>");

            pw.println("<h2>Something Went Wrong</h2>");

            pw.println("<p>" + e.getMessage() + "</p>");

            pw.println("</div>");
        }

        backButton(pw);
        endPage(pw);
    }


    // ---------------------------------------------------------
    // START PAGE
    // ---------------------------------------------------------

    private void startPage(PrintWriter pw) {

        pw.println("<!DOCTYPE html>");
        pw.println("<html>");
        pw.println("<head>");

        pw.println("<meta charset='UTF-8'>");
        pw.println("<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>");

        pw.println("<title>Delete Record</title>");

        pw.println("<style>");

        pw.println("* {");
        pw.println("    box-sizing: border-box;");
        pw.println("}");

        pw.println("body {");
        pw.println("    margin: 0;");
        pw.println("    min-height: 100vh;");
        pw.println("    font-family: Arial, sans-serif;");
        pw.println("    background: linear-gradient(135deg, #0f2027, #203a43, #2c5364);");
        pw.println("    display: flex;");
        pw.println("    justify-content: center;");
        pw.println("    align-items: center;");
        pw.println("    color: white;");
        pw.println("    padding: 30px;");
        pw.println("}");

        pw.println(".container {");
        pw.println("    width: 100%;");
        pw.println("    max-width: 650px;");
        pw.println("}");

        pw.println(".form-card, .result-card {");
        pw.println("    background: rgba(255, 255, 255, 0.10);");
        pw.println("    border: 1px solid rgba(255, 255, 255, 0.18);");
        pw.println("    border-radius: 20px;");
        pw.println("    padding: 40px;");
        pw.println("    text-align: center;");
        pw.println("    box-shadow: 0 20px 50px rgba(0, 0, 0, 0.30);");
        pw.println("    backdrop-filter: blur(15px);");
        pw.println("}");

        pw.println(".page-icon, .result-icon {");
        pw.println("    font-size: 55px;");
        pw.println("    margin-bottom: 15px;");
        pw.println("}");

        pw.println("h1 {");
        pw.println("    margin: 0 0 10px;");
        pw.println("    font-size: 32px;");
        pw.println("}");

        pw.println("h2 {");
        pw.println("    margin: 10px 0;");
        pw.println("    font-size: 26px;");
        pw.println("}");

        pw.println(".subtitle {");
        pw.println("    color: #cbd5e1;");
        pw.println("    margin-bottom: 30px;");
        pw.println("}");

        pw.println(".form-group {");
        pw.println("    text-align: left;");
        pw.println("    margin-bottom: 25px;");
        pw.println("}");

        pw.println("label {");
        pw.println("    display: block;");
        pw.println("    margin-bottom: 10px;");
        pw.println("    font-weight: bold;");
        pw.println("}");

        pw.println("input {");
        pw.println("    width: 100%;");
        pw.println("    padding: 14px;");
        pw.println("    border: none;");
        pw.println("    border-radius: 10px;");
        pw.println("    background: rgba(255, 255, 255, 0.12);");
        pw.println("    color: white;");
        pw.println("    font-size: 16px;");
        pw.println("    outline: none;");
        pw.println("}");

        pw.println("input::placeholder {");
        pw.println("    color: #b8c2cc;");
        pw.println("}");

        pw.println("input:focus {");
        pw.println("    box-shadow: 0 0 0 2px #ff6b6b;");
        pw.println("}");

        pw.println("button {");
        pw.println("    width: 100%;");
        pw.println("    padding: 14px;");
        pw.println("    border: none;");
        pw.println("    border-radius: 10px;");
        pw.println("    font-size: 16px;");
        pw.println("    font-weight: bold;");
        pw.println("    cursor: pointer;");
        pw.println("}");

        pw.println(".delete-btn {");
        pw.println("    background: #ff6b6b;");
        pw.println("    color: white;");
        pw.println("}");

        pw.println(".delete-btn:hover {");
        pw.println("    background: #ff5252;");
        pw.println("}");

        pw.println(".success-card {");
        pw.println("    border-color: rgba(72, 219, 151, 0.5);");
        pw.println("}");

        pw.println(".success-card .result-icon {");
        pw.println("    color: #48db97;");
        pw.println("}");

        pw.println(".error-card {");
        pw.println("    border-color: rgba(255, 107, 107, 0.5);");
        pw.println("}");

        pw.println(".error-card .result-icon {");
        pw.println("    color: #ff6b6b;");
        pw.println("}");

        pw.println(".result-card p {");
        pw.println("    color: #d5dce3;");
        pw.println("    line-height: 1.6;");
        pw.println("}");

        pw.println(".back-btn {");
        pw.println("    display: inline-block;");
        pw.println("    margin-top: 25px;");
        pw.println("    padding: 12px 22px;");
        pw.println("    border-radius: 10px;");
        pw.println("    background: rgba(255, 255, 255, 0.12);");
        pw.println("    color: white;");
        pw.println("    text-decoration: none;");
        pw.println("    border: 1px solid rgba(255, 255, 255, 0.20);");
        pw.println("}");

        pw.println(".back-btn:hover {");
        pw.println("    background: rgba(255, 255, 255, 0.20);");
        pw.println("}");

        pw.println("@media (max-width: 600px) {");

        pw.println("    body {");
        pw.println("        padding: 15px;");
        pw.println("    }");

        pw.println("    .form-card, .result-card {");
        pw.println("        padding: 25px;");
        pw.println("    }");

        pw.println("    h1 {");
        pw.println("        font-size: 26px;");
        pw.println("    }");

        pw.println("}");

        pw.println("</style>");

        pw.println("</head>");
        pw.println("<body>");

        pw.println("<div class='container'>");
    }


    // ---------------------------------------------------------
    // BACK BUTTON
    // ---------------------------------------------------------

    private void backButton(PrintWriter pw) {

        pw.println("<div style='text-align:center;'>");

        pw.println("<a href='home.html' class='back-btn'>");
        pw.println("&lt;- Back to Dashboard");
        pw.println("</a>");

        pw.println("</div>");
    }


    // ---------------------------------------------------------
    // END PAGE
    // ---------------------------------------------------------

    private void endPage(PrintWriter pw) {

        pw.println("</div>");
        pw.println("</body>");
        pw.println("</html>");
    }
}