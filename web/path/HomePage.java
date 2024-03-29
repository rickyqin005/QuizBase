package web.path;
import java.util.ArrayList;

import struct.Quiz;
import struct.User;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
import web.Server;
import web.WebPage;

/**
 * Class representing the homepage of the program
 *
 * @author Sherwin Okhowat, Ricky Qin
 */
public class HomePage extends WebPage implements HTTPPath {

    /**
     * Constructs a HomePage and sets the style
     */
    public HomePage() {
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    /**
     * Displays everything related to the homepage
     *
     * @param request The request
     * @param server The server
     * @return The HTTPResponse containing the HTML and CSS
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        boolean displayAll;
        if("quizzes=my".equals(request.getQueryString())) {
            displayAll = false;
        } else {
            displayAll = true;
        }
        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        User user = server.getUserManager().authenticateUser(
                credentials.first(), credentials.second());

        String buttonStyle1 = "margin-right: 10px; background-color: " + (displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";
        String buttonStyle2 = "margin-right: 10px; background-color:" + (!displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";
        String buttonStyle3 = "color: black; background-color: #F2F2F2; padding: 10px; text-decoration: none; border: 1px solid black;";

        addHeader(request, server);

        // Content section
        appendBodyComponents("<div style='display: flex; justify-content: center; margin-bottom: 20px; width: 100%; display: flex; justify-content: center;'>",
                new Hyperlink("/home?quizzes=all", "All Quizzes", false).setStyle(buttonStyle1),
                new Hyperlink("/home?quizzes=my", "My Quizzes", false).setStyle(buttonStyle2),
                new Hyperlink("/create-quiz", "Create Quiz", false).setStyle(buttonStyle3),
                "</div>");

        String query = request.getQueryString();

        appendBodyComponents("<div id='quizzes' style='display: flex; flex-direction: row; flex-wrap: wrap; justify-content: flex-start; align-items: flex-start; width: 100%;'>");
        if("quizzes=my".equals(query)) { // display user's quizzes
            ArrayList<Quiz> quizzes = server.getQuizManager().getUserCreatedQuizzes(user);
            for(Object quiz : quizzes) {
                appendBodyComponents(((Quiz) quiz).toHTMLString());
            }
        } else { // display all quizzes
            ArrayList<Quiz> quizzes = server.getQuizManager().getAllCreatedQuizzes();
            for(Object quiz : quizzes) {
                appendBodyComponents(((Quiz) quiz).toHTMLString());
            }
        }
        appendBodyComponents("</div>");

        response.appendBody(toHTMLString());
        return response;
    }








}
