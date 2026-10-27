package com.myapp.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/select")
public class MyAppSrv1 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        PrintWriter pw = res.getWriter();

        res.setContentType("text/html");

        String db = req.getParameter("t1");

        Connection con = null;
        Statement s = null;
        ResultSet rs = null;

        startPage(pw);

        try {

            Class.forName("oracle.jdbc.OracleDriver");

            con = DriverManager.getConnection(
                    System.getenv().getOrDefault("ORACLE_JDBC_URL", "jdbc:oracle:thin:@localhost:1521:XE"),
                    System.getenv().getOrDefault("ORACLE_DB_USER", "system"),
                    System.getenv().getOrDefault("ORACLE_DB_PASSWORD", "")
            );

            String qry = "select * from " + db;

            s = con.createStatement();

            rs = s.executeQuery(qry);

            pw.println("<div class='card'>");

            pw.println("<div class='tag'>DATABASE OPERATION</div>");

            pw.println("<div class='icon'>&#128269;</div>");

            pw.println("<h1>Records from " + db + "</h1>");

            boolean records = false;

            pw.println("<div class='data-list'>");

            while (rs.next()) {

                records = true;

                pw.println("<div class='data-row'>");

                pw.println(
                        rs.getString(1)
                        + " &nbsp;&bull;&nbsp; "
                        + rs.getString(2)
                        + " &nbsp;&bull;&nbsp; "
                        + rs.getString(3)
                );

                pw.println("</div>");
            }

            pw.println("</div>");

            if (!records) {

                pw.println("<div class='result'>");

                pw.println("<div class='result-icon'>&#8709;</div>");

                pw.println("<h2>No records found</h2>");

                pw.println("</div>");
            }

            backButton(pw);

            pw.println("</div>");

        } catch (Exception e) {

            pw.println("<div class='card narrow result error'>");

            pw.println("<div class='tag'>DATABASE ERROR</div>");

            pw.println("<div class='result-icon'>!</div>");

            pw.println("<h2>Something went wrong</h2>");

            pw.println("<p class='muted'>" + e + "</p>");

            backButton(pw);

            pw.println("</div>");

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (s != null) {
                    s.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (con != null) {
                    con.close();
                }

            } catch (Exception ignored) {
            }

            endPage(pw);
        }
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

        pw.println("<title>Select Records</title>");


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
                + "margin:0 0 24px;"
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
                + "word-break:break-word;"
                + "}"
        );


        pw.println(
                ".data-list{"
                + "margin-top:20px;"
                + "display:grid;"
                + "gap:10px;"
                + "}"
        );


        pw.println(
                ".data-row{"
                + "background:#0f172a88;"
                + "border:1px solid #ffffff12;"
                + "border-radius:12px;"
                + "padding:15px;"
                + "text-align:center;"
                + "color:#cbd5e1;"
                + "transition:.25s;"
                + "}"
        );


        pw.println(
                ".data-row:hover{"
                + "transform:translateY(-2px);"
                + "border-color:#5eead455;"
                + "background:#0f172acc;"
                + "}"
        );


        pw.println(
                ".result{"
                + "text-align:center;"
                + "margin-top:20px;"
                + "}"
        );


        pw.println(
                ".result-icon{"
                + "font-size:48px;"
                + "margin-bottom:10px;"
                + "}"
        );


        pw.println(
                ".error{"
                + "border-color:#fb718555;"
                + "}"
        );


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
                ".back:hover{"
                + "color:#5eead4;"
                + "border-color:#5eead4;"
                + "transform:translateY(-2px);"
                + "}"
        );


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


        pw.println(
                "@media(max-width:650px){"
                + "body{padding:16px;}"
                + ".card{padding:24px;}"
                + "h1{font-size:25px;}"
                + ".data-row{"
                + "font-size:14px;"
                + "overflow-wrap:anywhere;"
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