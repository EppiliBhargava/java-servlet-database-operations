package com.myapp.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/insert")
public class MyAppSrv2 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        PrintWriter pw = res.getWriter();

        res.setContentType("text/html");

        String tableName = req.getParameter("d");

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
             * SECOND STEP
             *
             * If c1 exists, user has submitted values.
             */

            String c1 = req.getParameter("c1");

            if (c1 != null) {

                /*
                 * Get number of columns
                 */

                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery(
                        "select * from " + tableName + " where 1=0"
                );

                ResultSetMetaData md = rs.getMetaData();

                int columnCount = md.getColumnCount();

                /*
                 * Create INSERT query
                 */

                StringBuilder query = new StringBuilder();

                query.append("insert into ")
                     .append(tableName)
                     .append(" values (");

                for (int i = 1; i <= columnCount; i++) {

                    if (i > 1) {
                        query.append(",");
                    }

                    query.append("?");
                }

                query.append(")");

                PreparedStatement ps =
                        con.prepareStatement(query.toString());

                /*
                 * Set values
                 */

                for (int i = 1; i <= columnCount; i++) {

                    String value =
                            req.getParameter("c" + i);

                    ps.setString(i, value);
                }

                /*
                 * Execute INSERT
                 */

                int result = ps.executeUpdate();

                if (result > 0) {

                    pw.println("<div class='card narrow result success'>");

                    pw.println(
                            "<div class='tag'>DATABASE OPERATION</div>"
                    );

                    pw.println(
                            "<div class='result-icon'>&#10003;</div>"
                    );

                    pw.println(
                            "<h1>Record Inserted</h1>"
                    );

                    pw.println(
                            "<p class='muted'>"
                            + result
                            + " record inserted successfully into "
                            + tableName
                            + ".</p>"
                    );

                    backButton(pw);

                    pw.println("</div>");

                } else {

                    pw.println("<div class='card narrow result error'>");

                    pw.println(
                            "<div class='tag'>DATABASE OPERATION</div>"
                    );

                    pw.println(
                            "<div class='result-icon'>&#33;</div>"
                    );

                    pw.println(
                            "<h1>No Record Inserted</h1>"
                    );

                    pw.println(
                            "<p class='muted'>"
                            + "The record could not be inserted."
                            + "</p>"
                    );

                    backButton(pw);

                    pw.println("</div>");
                }

                ps.close();
                rs.close();
                st.close();

            } else {

                /*
                 * FIRST STEP
                 *
                 * Get column names and display input fields.
                 */

                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery(
                        "select * from " + tableName + " where 1=0"
                );

                ResultSetMetaData md = rs.getMetaData();

                int columnCount = md.getColumnCount();

                /*
                 * Display INSERT form
                 */

                pw.println("<div class='card'>");

                pw.println(
                        "<div class='tag'>DATABASE OPERATION</div>"
                );

                pw.println(
                        "<div class='icon'>&#10148;</div>"
                );

                pw.println(
                        "<h1>Insert into " + tableName + "</h1>"
                );

                pw.println(
                        "<p class='muted'>"
                        + "Enter values for each column."
                        + "</p>"
                );

                pw.println(
                        "<form action='insert' method='GET'>"
                );

                /*
                 * Keep table name for second request
                 */

                pw.println(
                        "<input type='hidden' "
                        + "name='d' "
                        + "value='" + tableName + "'>"
                );

                pw.println("<div class='fields'>");

                /*
                 * Generate fields dynamically
                 */

                for (int i = 1; i <= columnCount; i++) {

                    String columnName =
                            md.getColumnName(i);

                    pw.println("<div class='field'>");

                    pw.println(
                            "<label for='c"
                            + i
                            + "'>"
                            + columnName
                            + "</label>"
                    );

                    pw.println(
                            "<input type='text' "
                            + "id='c"
                            + i
                            + "' "
                            + "name='c"
                            + i
                            + "' "
                            + "placeholder='Enter "
                            + columnName
                            + "'>"
                    );

                    pw.println("</div>");
                }

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
                        + "value='Insert Record'>"
                );

                pw.println("</div>");

                pw.println("</form>");

                backButton(pw);

                pw.println("</div>");

                rs.close();
                st.close();
            }

            con.close();

        } catch (Exception e) {

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
     * ---------------------------------------------------------
     * START PAGE
     * ---------------------------------------------------------
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

        pw.println("<title>Insert Values</title>");


        pw.println("<style>");

        pw.println(
                "*{"
                + "box-sizing:border-box;"
                + "}"
        );


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


        pw.println(
                ".page{"
                + "width:min(900px,100%);"
                + "animation:up .6s ease;"
                + "}"
        );


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


        pw.println(
                ".narrow{"
                + "width:min(540px,100%);"
                + "margin:auto;"
                + "}"
        );


        pw.println(
                ".tag{"
                + "color:#5eead4;"
                + "font-size:12px;"
                + "letter-spacing:1.6px;"
                + "font-weight:bold;"
                + "}"
        );


        pw.println(
                ".icon{"
                + "font-size:38px;"
                + "margin:10px 0;"
                + "}"
        );


        pw.println(
                "h1{"
                + "margin:0 0 12px;"
                + "font-size:30px;"
                + "}"
        );


        pw.println(
                "h2{"
                + "margin:8px 0;"
                + "font-size:21px;"
                + "}"
        );


        pw.println(
                ".muted{"
                + "color:#94a3b8;"
                + "font-size:14px;"
                + "line-height:1.6;"
                + "word-break:break-word;"
                + "}"
        );


        /*
         * Form fields
         */

        pw.println(
                ".fields{"
                + "display:grid;"
                + "grid-template-columns:1fr 1fr;"
                + "gap:16px;"
                + "margin-top:24px;"
                + "}"
        );


        pw.println(
                ".field{"
                + "display:flex;"
                + "flex-direction:column;"
                + "gap:8px;"
                + "}"
        );


        pw.println(
                "label{"
                + "color:#cbd5e1;"
                + "font-size:14px;"
                + "font-weight:bold;"
                + "}"
        );


        pw.println(
                "input[type=text]{"
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


        pw.println(
                "input[type=text]:focus{"
                + "border-color:#5eead4;"
                + "box-shadow:0 0 0 3px #5eead422;"
                + "}"
        );


        pw.println(
                "input[type=text]::placeholder{"
                + "color:#64748b;"
                + "}"
        );


        /*
         * Buttons
         */

        pw.println(
                ".actions{"
                + "display:flex;"
                + "gap:12px;"
                + "margin-top:24px;"
                + "flex-wrap:wrap;"
                + "}"
        );


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
         * Back button
         */

        pw.println(
                ".toolbar{"
                + "text-align:center;"
                + "margin-top:24px;"
                + "}"
        );


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
         * Result screen
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
     * ---------------------------------------------------------
     * BACK BUTTON
     * ---------------------------------------------------------
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
     * ---------------------------------------------------------
     * END PAGE
     * ---------------------------------------------------------
     */

    private void endPage(PrintWriter pw) {

        pw.println("</div>");

        pw.println("</body>");

        pw.println("</html>");
    }
}