package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import struct.Quiz;
import struct.QuizItem; 
import struct.User; 
import utility.Pair;
import utility.SQLStatementBuilder;

/**
 * A class that manages {@code Quiz} objects
 *
 * @author Ricky Qin and Sherwin Okhowat
 */
public class QuizManager extends DatabaseManager {

    private HashMap<Integer, Quiz> cache = new HashMap<Integer, Quiz>();

    /**
     * Constructs a QuizManager class for the specified database
     *
     * @param dbName The name of database to manage (must include {@code .db} file extension)
     */
    public QuizManager(String dbName) {
        super(dbName);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void initialize() {
        StringBuilder statement = new StringBuilder();
        statement.append("CREATE TABLE IF NOT EXISTS QUIZZES (");
        statement.append("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        statement.append("NAME TEXT NOT NULL CHECK(LENGTH(NAME) > 0),");
        statement.append("DESCRIPTION TEXT,");
        statement.append("CREATOR_ID INTEGER NOT NULL,");
        statement.append("FOREIGN KEY (CREATOR_ID) REFERENCES USERS (ID)");
        statement.append(");");
        executeWriteOperation(statement.toString());
    }

    /**
     * Creates a quiz with no elements in it. (alternately could add a bunch of quiz items to it)
     * @param creator the user that created it.
     * @param name 
     * @param description 
     * @return whether addition was successful or not.
     */
    public boolean addQuiz(User creator, String name, String description) {
        return executeWriteOperation(new SQLStatementBuilder().insertInto("QUIZZES", "NAME", "DESCRIPTION", "CREATOR_ID").values("'" + name + "'", "'" + description + "'", Integer.toString(creator.getID())).toString());
    }

    /**
     * Deletes a quiz based on the User who made it and 
     * @param requestor
     * @param quiz
     * @return whether deletion was successful or not. 
     */
    public boolean deleteQuiz (User requestor, Quiz quiz) {
        return executeWriteOperation(new SQLStatementBuilder().deleteFrom("QUIZZES").where("ID='"+quiz.getID() + "' AND CREATOR_ID='" + requestor.getID() + "'").toString());
    }

    /**
     * Returns the quiz with a certain ID and Name
     * @param id the quiz's ID
     * @param name the name of the quiz (case sensitive)
     * @return
     */
    public Quiz getQuiz (int id, String name) {
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
        .select().from("QUIZZES")
        .where("ID='" + id + "' AND NAME='" + name + "'").toString());
        if (dbResult.size() == 1) {
            return (Quiz) dbResult.get(1); 
        } else {
            return null;
        }
    }

    public void addQuizItems (Quiz quiz, QuizItem... items) {
        
    }


    @Override
    public Object getById(int id) {
        Quiz cacheResult = cache.get(id);
        if(cacheResult != null) {
            return cacheResult;
        }
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("QUIZZES").where("ID="+id).toString());
        if(dbResult.size() == 1) {
            Quiz user = (Quiz)(dbResult.get(1));
            cache.put(user.getID(), user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<? extends Object> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> result = getReadOperationResultSet(statement);
        try {
            ResultSet rs = result.first();
            ArrayList<Quiz> list = new ArrayList<Quiz>();
            while(rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String description = rs.getString("DESCRIPTION");
                int creatorId = rs.getInt("CREATOR");
                list.add(new Quiz(id, name, description, creatorId, this));
            }
            result.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all the quizzes created by a user.
     * @param user The user
     * @return an ArrayList containing all the quizzes returned by a user. 
     */
    public ArrayList<? extends Object> getUserCreatedQuizzes(User user) {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES").where("CREATOR_ID="+user.getID()).toString());
    }

    /**
     * Returns all the quizzes created.
     * @return an ArrayList containing all the quizzes stored in the database.
     */
    public ArrayList<? extends Object> getAllCreatedQuizzes() {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES").toString());
    }


}
