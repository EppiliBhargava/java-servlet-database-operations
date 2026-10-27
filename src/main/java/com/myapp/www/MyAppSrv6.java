package com.myapp.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/create")
public class MyAppSrv6 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        String tableName = req.getParameter("tableName");
        String columnCount = req.getParameter("columnCount");

        startPage(pw);

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    System.getenv().getOrDefault("ORACLE_JDBC_URL", "jdbc:oracle:thin:@localhost:1521:XE"),
                    System.getenv().getOrDefault("ORACLE_DB_USER", "system"),
                    System.getenv().getOrDefault("ORACLE_DB_PASSWORD", "")
            );

            /*
             * First stage:
             * Display the form for entering column names and data types.
             */
            if (req.getParameter("columnName1") == null) {

                int count = Integer.parseInt(columnCount);

                pw.println("<div class='form-card'>");

                pw.println("<div class='page-icon'>&#9881;</div>");

                pw.println("<h1>Create Table</h1>");

                pw.println("<p class='subtitle'>");
                pw.println("Define the columns for <b>"
                        + tableName
                        + "</b>");
                pw.println("</p>");

                pw.println("<form action='create' method='GET'>");

                pw.println("<input type='hidden' "
                        + "name='tableName' "
                        + "value='" + tableName + "'>");

                pw.println("<input type='hidden' "
                        + "name='columnCount' "
                        + "value='" + columnCount + "'>");

                for (int i = 1; i <= count; i++) {

                    pw.println("<div class='column-card'>");

                    pw.println("<div class='column-number'>");
                    pw.println("Column " + i);
                    pw.println("</div>");

                    pw.println("<div class='form-row'>");

                    pw.println("<div class='form-group'>");

                    pw.println("<label>Column Name</label>");

                    pw.println("<input type='text' "
                            + "name='columnName" + i + "' "
                            + "placeholder='Enter column name' "
                            + "required>");

                    pw.println("</div>");

                    pw.println("<div class='form-group'>");

                    pw.println("<label>Data Type</label>");

                    pw.println("<input type='text' "
                            + "name='dataType" + i + "' "
                            + "placeholder='Example: VARCHAR2(50)' "
                            + "required>");

                    pw.println("</div>");

                    pw.println("</div>");

                    pw.println("</div>");
                }

                pw.println("<button type='submit' class='create-btn'>");
                pw.println("&#10003; Create Table");
                pw.println("</button>");

                pw.println("</form>");

                pw.println("</div>");
            }

            /*
             * Second stage:
             * Build and execute the CREATE TABLE query.
             */
            else {

                int count = Integer.parseInt(columnCount);

                StringBuilder query = new StringBuilder();

                query.append("CREATE TABLE ")
                     .append(tableName)
                     .append(" (");

                for (int i = 1; i <= count; i++) {

                    String columnName =
                            req.getParameter("columnName" + i);

                    String dataType =
                            req.getParameter("dataType" + i);

                    query.append(columnName)
                         .append(" ")
                         .append(dataType);

                    if (i < count) {
                        query.append(", ");
                    }
                }

                query.append(")");

                PreparedStatement ps =
                        con.prepareStatement(query.toString());

                ps.executeUpdate();

                pw.println("<div class='result-card success-card'>");

                pw.println("<div class='result-icon'>&#10003;</div>");

                pw.println("<h2>Table Created Successfully</h2>");

                pw.println("<p>");
                pw.println("Table <b>" + tableName
                        + "</b> has been created successfully.");
                pw.println("</p>");

                pw.println("</div>");

                ps.close();
            }

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

        pw.println("<title>Create Table</title>");

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
        pw.println("    max-width: 850px;");
        pw.println("}");

        pw.println(".form-card, .result-card {");
        pw.println("    background: rgba(255, 255, 255, 0.10);");
        pw.println("    border: 1px solid rgba(255, 255, 255, 0.18);");
        pw.println("    border-radius: 20px;");
        pw.println("    padding: 40px;");
        pw.println("    box-shadow: 0 20px 50px rgba(0, 0, 0, 0.30);");
        pw.println("    backdrop-filter: blur(15px);");
        pw.println("}");

        pw.println(".page-icon, .result-icon {");
        pw.println("    text-align: center;");
        pw.println("    font-size: 55px;");
        pw.println("    margin-bottom: 15px;");
        pw.println("}");

        pw.println("h1 {");
        pw.println("    text-align: center;");
        pw.println("    margin: 0 0 10px;");
        pw.println("    font-size: 32px;");
        pw.println("}");

        pw.println("h2 {");
        pw.println("    text-align: center;");
        pw.println("    margin: 10px 0;");
        pw.println("    font-size: 26px;");
        pw.println("}");

        pw.println(".subtitle {");
        pw.println("    text-align: center;");
        pw.println("    color: #cbd5e1;");
        pw.println("    margin-bottom: 30px;");
        pw.println("}");

        pw.println(".column-card {");
        pw.println("    background: rgba(255, 255, 255, 0.06);");
        pw.println("    border: 1px solid rgba(255, 255, 255, 0.12);");
        pw.println("    border-radius: 14px;");
        pw.println("    padding: 20px;");
        pw.println("    margin-bottom: 18px;");
        pw.println("}");

        pw.println(".column-number {");
        pw.println("    font-size: 18px;");
        pw.println("    font-weight: bold;");
        pw.println("    margin-bottom: 15px;");
        pw.println("    color: #7dd3fc;");
        pw.println("}");

        pw.println(".form-row {");
        pw.println("    display: grid;");
        pw.println("    grid-template-columns: 1fr 1fr;");
        pw.println("    gap: 20px;");
        pw.println("}");

        pw.println(".form-group {");
        pw.println("    margin-bottom: 5px;");
        pw.println("}");

        pw.println("label {");
        pw.println("    display: block;");
        pw.println("    margin-bottom: 9px;");
        pw.println("    font-weight: bold;");
        pw.println("}");

        pw.println("input {");
        pw.println("    width: 100%;");
        pw.println("    padding: 13px;");
        pw.println("    border: none;");
        pw.println("    border-radius: 9px;");
        pw.println("    background: rgba(255, 255, 255, 0.12);");
        pw.println("    color: white;");
        pw.println("    font-size: 15px;");
        pw.println("    outline: none;");
        pw.println("}");

        pw.println("input::placeholder {");
        pw.println("    color: #b8c2cc;");
        pw.println("}");

        pw.println("input:focus {");
        pw.println("    box-shadow: 0 0 0 2px #38bdf8;");
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

        pw.println(".create-btn {");
        pw.println("    background: #38bdf8;");
        pw.println("    color: #082f49;");
        pw.println("    margin-top: 10px;");
        pw.println("}");

        pw.println(".create-btn:hover {");
        pw.println("    background: #7dd3fc;");
        pw.println("}");

        pw.println(".success-card {");
        pw.println("    text-align: center;");
        pw.println("    border-color: rgba(72, 219, 151, 0.5);");
        pw.println("}");

        pw.println(".success-card .result-icon {");
        pw.println("    color: #48db97;");
        pw.println("}");

        pw.println(".error-card {");
        pw.println("    text-align: center;");
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
        pw.println("    display: block;");
        pw.println("    width: fit-content;");
        pw.println("    margin: 25px auto 0;");
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

        pw.println("@media (max-width: 650px) {");

        pw.println("    body {");
        pw.println("        padding: 15px;");
        pw.println("    }");

        pw.println("    .form-card, .result-card {");
        pw.println("        padding: 25px;");
        pw.println("    }");

        pw.println("    .form-row {");
        pw.println("        grid-template-columns: 1fr;");
        pw.println("        gap: 10px;");
        pw.println("    }");

        pw.println("    h1 {");
        pw.println("        font-size: 27px;");
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

        pw.println("<a href='home.html' class='back-btn'>");
        pw.println("&lt;- Back to Dashboard");
        pw.println("</a>");
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