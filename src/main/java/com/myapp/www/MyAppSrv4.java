package com.myapp.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/alter")
public class MyAppSrv4 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        PrintWriter pw = res.getWriter();

        res.setContentType("text/html");

        String tableName = req.getParameter("tableName");

        String operation = req.getParameter("operation");

        Connection con = null;

        startPage(pw);

        try {

            Class.forName("oracle.jdbc.OracleDriver");

            con = DriverManager.getConnection(
                    System.getenv().getOrDefault("ORACLE_JDBC_URL", "jdbc:oracle:thin:@localhost:1521:XE"),
                    System.getenv().getOrDefault("ORACLE_DB_USER", "system"),
                    System.getenv().getOrDefault("ORACLE_DB_PASSWORD", "")
            );

            /*
             * =====================================================
             * FIRST STEP
             * =====================================================
             *
             * Only table name has been entered.
             */

            if (operation == null) {

                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery(
                        "select * from " + tableName + " where 1=0"
                );

                ResultSetMetaData md = rs.getMetaData();

                int columnCount = md.getColumnCount();

                /*
                 * Display ALTER form
                 */

                pw.println("<div class='card'>");

                pw.println(
                        "<div class='tag'>DATABASE OPERATION</div>"
                );

                pw.println(
                        "<div class='icon'>&#9881;</div>"
                );

                pw.println(
                        "<h1>Alter " + tableName + "</h1>"
                );

                pw.println(
                        "<p class='muted'>"
                        + "Choose an operation and provide the "
                        + "required column details."
                        + "</p>"
                );

                pw.println(
                        "<form action='alter' method='GET'>"
                );

                /*
                 * Keep table name
                 */

                pw.println(
                        "<input type='hidden' "
                        + "name='tableName' "
                        + "value='" + tableName + "'>"
                );

                pw.println("<div class='fields'>");

                /*
                 * Operation
                 */

                pw.println("<div class='field full'>");

                pw.println(
                        "<label for='operation'>"
                        + "Select Operation"
                        + "</label>"
                );

                pw.println(
                        "<select id='operation' "
                        + "name='operation'>"
                );

                pw.println(
                        "<option value='add'>"
                        + "Add Column"
                        + "</option>"
                );

                pw.println(
                        "<option value='modify'>"
                        + "Modify Column"
                        + "</option>"
                );

                pw.println(
                        "<option value='drop'>"
                        + "Drop Column"
                        + "</option>"
                );

                pw.println(
                        "<option value='rename'>"
                        + "Rename Column"
                        + "</option>"
                );

                pw.println("</select>");

                pw.println("</div>");

                /*
                 * Column name
                 */

                pw.println("<div class='field'>");

                pw.println(
                        "<label for='columnName'>"
                        + "Column Name"
                        + "</label>"
                );

                pw.println(
                        "<input type='text' "
                        + "id='columnName' "
                        + "name='columnName' "
                        + "placeholder='Enter column name'>"
                );

                pw.println("</div>");

                /*
                 * New column name
                 */

                pw.println("<div class='field'>");

                pw.println(
                        "<label for='newColumnName'>"
                        + "New Column Name"
                        + "</label>"
                );

                pw.println(
                        "<input type='text' "
                        + "id='newColumnName' "
                        + "name='newColumnName' "
                        + "placeholder='For rename operation'>"
                );

                pw.println("</div>");

                /*
                 * Data type
                 */

                pw.println("<div class='field full'>");

                pw.println(
                        "<label for='dataType'>"
                        + "Data Type"
                        + "</label>"
                );

                pw.println(
                        "<input type='text' "
                        + "id='dataType' "
                        + "name='dataType' "
                        + "placeholder='Example: VARCHAR2(50)'>"
                );

                pw.println("</div>");

                pw.println("</div>");

                /*
                 * Buttons
                 */

                pw.println("<div class='actions'>");

                pw.println(
                        "<input class='reset' "
                        + "type='reset' "
                        + "value='Reset'>"
                );

                pw.println(
                        "<input class='submit' "
                        + "type='submit' "
                        + "value='Apply Changes'>"
                );

                pw.println("</div>");

                pw.println("</form>");

                backButton(pw);

                pw.println("</div>");

                rs.close();

                st.close();

            }

            /*
             * =====================================================
             * SECOND STEP
             * =====================================================
             *
             * Execute ALTER.
             */

            else {

                String columnName =
                        req.getParameter("columnName");

                String newColumnName =
                        req.getParameter("newColumnName");

                String dataType =
                        req.getParameter("dataType");

                String query = "";

                /*
                 * ADD
                 */

                if (operation.equals("add")) {

                    query =
                            "ALTER TABLE "
                            + tableName
                            + " ADD "
                            + columnName
                            + " "
                            + dataType;
                }

                /*
                 * MODIFY
                 */

                else if (operation.equals("modify")) {

                    query =
                            "ALTER TABLE "
                            + tableName
                            + " MODIFY "
                            + columnName
                            + " "
                            + dataType;
                }

                /*
                 * DROP
                 */

                else if (operation.equals("drop")) {

                    query =
                            "ALTER TABLE "
                            + tableName
                            + " DROP COLUMN "
                            + columnName;
                }

                /*
                 * RENAME
                 */

                else if (operation.equals("rename")) {

                    query =
                            "ALTER TABLE "
                            + tableName
                            + " RENAME COLUMN "
                            + columnName
                            + " TO "
                            + newColumnName;
                }

                /*
                 * Execute query
                 */

                Statement st =
                        con.createStatement();

                st.executeUpdate(query);

                /*
                 * Success message
                 */

                pw.println(
                        "<div class='card narrow result success'>"
                );

                pw.println(
                        "<div class='tag'>DATABASE OPERATION</div>"
                );

                pw.println(
                        "<div class='result-icon'>&#10003;</div>"
                );

                pw.println(
                        "<h1>Table Altered Successfully</h1>"
                );

                pw.println(
                        "<p class='muted'>"
                        + "The "
                        + operation
                        + " operation was successfully "
                        + "performed on "
                        + tableName
                        + "."
                        + "</p>"
                );

                backButton(pw);

                pw.println("</div>");

                st.close();
            }

            con.close();

        }

        catch (Exception e) {

            pw.println(
                    "<div class='card narrow result error'>"
            );

            pw.println(
                    "<div class='tag'>DATABASE ERROR</div>"
            );

            pw.println(
                    "<div class='result-icon'>&#33;</div>"
            );

            pw.println(
                    "<h1>Something went wrong</h1>"
            );

            pw.println(
                    "<p class='muted'>"
                    + e
                    + "</p>"
            );

            backButton(pw);

            pw.println("</div>");
        }

        endPage(pw);
    }


    /*
     * =========================================================
     * START PAGE
     * =========================================================
     */

    private void startPage(PrintWriter pw) {

        pw.println("<!DOCTYPE html>");

        pw.println("<html>");

        pw.println("<head>");

        pw.println("<meta charset='UTF-8'>");

        pw.println(
                "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
        );

        pw.println("<title>Alter Table</title>");

        pw.println("<style>");

        /*
         * Global
         */

        pw.println(
                "*{"
                + "box-sizing:border-box;"
                + "}"
        );

        /*
         * Body
         */

        pw.println(
                "body{"
                + "margin:0;"
                + "min-height:100vh;"
                + "font-family:Arial,Helvetica,sans-serif;"
                + "background:linear-gradient(135deg,#0f172a,#164e63);"
                + "color:#e2e8f0;"
                + "padding:28px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "}"
        );

        /*
         * Page
         */

        pw.println(
                ".page{"
                + "width:min(900px,100%);"
                + "animation:up .6s ease;"
                + "}"
        );

        /*
         * Card
         */

        pw.println(
                ".card{"
                + "background:#ffffff12;"
                + "border:1px solid #ffffff20;"
                + "border-radius:24px;"
                + "padding:32px;"
                + "backdrop-filter:blur(14px);"
                + "box-shadow:0 20px 50px #0005;"
                + "}"
        );

        /*
         * Narrow card
         */

        pw.println(
                ".narrow{"
                + "width:min(540px,100%);"
                + "margin:auto;"
                + "}"
        );

        /*
         * Tag
         */

        pw.println(
                ".tag{"
                + "color:#5eead4;"
                + "font-size:12px;"
                + "letter-spacing:1.6px;"
                + "font-weight:bold;"
                + "}"
        );

        /*
         * Icon
         */

        pw.println(
                ".icon{"
                + "font-size:38px;"
                + "margin:10px 0;"
                + "}"
        );

        /*
         * Headings
         */

        pw.println(
                "h1{"
                + "margin:0 0 12px;"
                + "font-size:30px;"
                + "}"
        );

        /*
         * Muted text
         */

        pw.println(
                ".muted{"
                + "color:#94a3b8;"
                + "font-size:14px;"
                + "line-height:1.6;"
                + "word-break:break-word;"
                + "}"
        );

        /*
         * Form layout
         */

        pw.println(
                ".fields{"
                + "display:grid;"
                + "grid-template-columns:1fr 1fr;"
                + "gap:16px;"
                + "margin-top:24px;"
                + "}"
        );

        /*
         * Field
         */

        pw.println(
                ".field{"
                + "display:flex;"
                + "flex-direction:column;"
                + "gap:8px;"
                + "}"
        );

        /*
         * Full width field
         */

        pw.println(
                ".field.full{"
                + "grid-column:1/-1;"
                + "}"
        );

        /*
         * Labels
         */

        pw.println(
                "label{"
                + "color:#cbd5e1;"
                + "font-size:14px;"
                + "font-weight:bold;"
                + "}"
        );

        /*
         * Inputs and select
         */

        pw.println(
                "input[type=text],select{"
                + "width:100%;"
                + "padding:13px 14px;"
                + "border:1px solid #475569;"
                + "border-radius:12px;"
                + "background:#0f172acc;"
                + "color:#fff;"
                + "font-size:15px;"
                + "outline:none;"
                + "transition:.2s;"
                + "}"
        );

        /*
         * Select options
         */

        pw.println(
                "select option{"
                + "background:#0f172a;"
                + "color:#fff;"
                + "}"
        );

        /*
         * Focus
         */

        pw.println(
                "input[type=text]:focus,"
                + "select:focus{"
                + "border-color:#5eead4;"
                + "box-shadow:0 0 0 3px #5eead422;"
                + "}"
        );

        /*
         * Placeholder
         */

        pw.println(
                "input[type=text]::placeholder{"
                + "color:#64748b;"
                + "}"
        );

        /*
         * Actions
         */

        pw.println(
                ".actions{"
                + "display:flex;"
                + "gap:12px;"
                + "margin-top:24px;"
                + "flex-wrap:wrap;"
                + "}"
        );

        /*
         * Buttons
         */

        pw.println(
                "input[type=submit],"
                + "input[type=reset],"
                + ".back{"
                + "border:0;"
                + "border-radius:12px;"
                + "padding:12px 20px;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "text-decoration:none;"
                + "font-size:14px;"
                + "transition:.25s;"
                + "}"
        );

        /*
         * Submit
         */

        pw.println(
                ".submit{"
                + "background:#2dd4bf;"
                + "color:#042f2e;"
                + "}"
        );

        pw.println(
                ".submit:hover{"
                + "transform:translateY(-2px);"
                + "box-shadow:0 8px 20px #2dd4bf33;"
                + "}"
        );

        /*
         * Reset
         */

        pw.println(
                ".reset{"
                + "background:#334155;"
                + "color:#e2e8f0;"
                + "}"
        );

        pw.println(
                ".reset:hover{"
                + "background:#475569;"
                + "}"
        );

        /*
         * Back button container
         */

        pw.println(
                ".toolbar{"
                + "text-align:center;"
                + "margin-top:24px;"
                + "}"
        );

        /*
         * Back button
         */

        pw.println(
                ".back{"
                + "display:inline-block;"
                + "color:#94a3b8;"
                + "border:1px solid #334155;"
                + "background:#0f172a55;"
                + "}"
        );

        pw.println(
                ".back:hover{"
                + "color:#5eead4;"
                + "border-color:#5eead4;"
                + "transform:translateY(-2px);"
                + "}"
        );

        /*
         * Result
         */

        pw.println(
                ".result{"
                + "text-align:center;"
                + "}"
        );

        pw.println(
                ".result.success{"
                + "border-color:#2dd4bf55;"
                + "}"
        );

        pw.println(
                ".result.error{"
                + "border-color:#fb718555;"
                + "}"
        );

        /*
         * Result icon
         */

        pw.println(
                ".result-icon{"
                + "font-size:48px;"
                + "margin-bottom:10px;"
                + "}"
        );

        /*
         * Animation
         */

        pw.println(
                "@keyframes up{"
                + "from{"
                + "opacity:0;"
                + "transform:translateY(20px);"
                + "}"
                + "to{"
                + "opacity:1;"
                + "transform:translateY(0);"
                + "}"
                + "}"
        );

        /*
         * Mobile
         */

        pw.println(
                "@media(max-width:650px){"
                + "body{padding:16px;}"
                + ".card{padding:24px;}"
                + ".fields{grid-template-columns:1fr;}"
                + ".field.full{grid-column:auto;}"
                + "h1{font-size:25px;}"
                + ".actions>*{"
                + "flex:1;"
                + "text-align:center;"
                + "}"
                + "}"
        );

        pw.println("</style>");

        pw.println("</head>");

        pw.println("<body>");

        pw.println("<div class='page'>");
    }


    /*
     * =========================================================
     * BACK BUTTON
     * =========================================================
     */

    private void backButton(PrintWriter pw) {

        pw.println("<div class='toolbar'>");

        pw.println(
                "<a class='back' href='home.html'>"
                + "&lt;- Back to Dashboard"
                + "</a>"
        );

        pw.println("</div>");
    }


    /*
     * =========================================================
     * END PAGE
     * =========================================================
     */

    private void endPage(PrintWriter pw) {

        pw.println("</div>");

        pw.println("</body>");

        pw.println("</html>");
    }
}