package web.path;

import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * Class representing the signup page of the server
 *
 * @author Sherwin Okhowat, Ricky Qin
 */
public class SignUpPage extends WebPage implements HTTPPath {

    /**
     * Constructs a SignUpPage
     */
    public SignUpPage() {
        appendBodyComponents(
                "<div style='height: 165px;'></div>",
                "<img src='../images/logo.png' style='width: 400px; height: auto;'>",
                "<form style='display: flex; flex-direction: column; width: 200px; margin-top: 50px; padding: 20px;' action='/signup/submit' method='POST' style='background-color: lightgray;'>",
                "<input style='margin-bottom: 3px;' type='text' id='username' name='username' placeholder='Username (>= 3 chars)' minlength='3'>",
                "<input style='margin-bottom: 5px;' type='password' id='password' name='password' placeholder='Password (>= 3 chars)' minlength='3'>",
                "<input type='submit' value='Sign Up'>",
                "</form>",
                "<form style='display: flex; flex-direction: column; justify-content: center; align-items: center; width: 200px; margin-top: 10px; padding: 20px;' action='/login' method='GET'>",
                "<p style='font-size: 12px'>Already have an account? Log in!</p>",
                "<input type='submit' value='Log in'>",
                "</form>");

        setStyle("background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center; font-family: Arial");
    }


    /**
     * Processes the request for signing up
     *
     * @param request The request
     * @param server The server
     * @return A post request for signing up
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials != null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }
        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }
}
