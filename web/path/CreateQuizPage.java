package web.path;

import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * A class representing a page which is used to create quizzes.
 *
 * @author Sherwin Okhowat and Avery Chan
 */
public class CreateQuizPage extends WebPage implements HTTPPath {
    /**
     * Constructs a QuizPage and sets the style
     */
    public CreateQuizPage() {
        // you can change any part of this, i dont mind
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    /**
     * Logistics for create a quiz page.
     *
     * @param request The request
     * @param server The server
     * @return The HTTP Response containing the HTML and CSS
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }
        appendHeadComponents("<script src='/js/formHelper.js'></script>");
        addHeader(request, server);
        appendBodyComponents(
                "<form id='questionForm' action='/create-quiz/submit' method='POST' style='display: flex; flex-direction: column; width: 300px; padding: 20px;'>",
                "<label for='quizName'>Quiz Name: </label><input type='text' maxlength='50'; id='quizName' name='quizName' placeholder='Quiz Name' required>",
                "<label for='quizDescription'>Description: </label><textarea id='quizDescription' maxlength='400'; name='quizDescription' placeholder='Quiz Description' rows='4' cols='50'></textarea>",
                "<input type='hidden' name='highestNumber' id='highestNumber' value='0'>",
                "<input type='hidden' name='numOfQuestions' id='numOfQuestions' value='0'>",
                "<input type='submit' value='Finish Quiz'>",
                "</form>",
                "<div class='options' style='display: flex'>", // please change the CSS in here to something more palatable
                "<button type='button' id='fcButton' onclick='addFlashcard()'>Add Flashcard Question</button>",
                "<button type='button' id='mcButton' onclick='addMultipleChoice()'>Add Multiple Choice Question</button>",
                "</div>" //                 "<input type='submit' value='Add Question'>",
        );
        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }
}