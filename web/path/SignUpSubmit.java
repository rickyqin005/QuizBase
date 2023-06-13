package web.path;

import struct.User;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
import web.Server;
import web.WebPage;

/**
 * Class which handles a signup related request
 *
 * @author Sherwin Okhowat and Ricky Qin
 */
public class SignUpSubmit implements HTTPPath {

    /**
     * Processes the submit logistics of the sign-up page. Essentially adds the username and password to the database
     *
     * @param request The request
     * @param server The server
     * @return The appropriate GET HTTPResponse
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        String username = request.getPostBody("username");
        String password = request.getPostBody("password");

        if ("/login".equals(request.getPathWithoutQueryString())) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        User user = server.getUserManager().registerUser(username, password);

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        if(user == null) {
            response.appendBody("<html><head><meta http-equiv='refresh' content='2;url=/signup' /></head><body><p>Unable to sign up! This may be because your username has already been taken, credentials are invalid or a network error occurred.<br>Redirecting you back... </p></body></html>");
        } else {
            WebPage webPage = new WebPage().appendBodyComponents("Sign up successful!", WebPage.BR_TAG,
                    new Hyperlink("../../login", "Log in", true));
            response.appendBody(webPage.toHTMLString());
        }
        return response;
    }
}
