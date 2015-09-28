
import by.expertsoft.kaliachka.DBClass.UserDBImpl;

import javax.swing.text.html.HTMLDocument;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AndreiK-PC on 28.09.2015.
 */
public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        UserDBImpl userDB = new UserDBImpl("jdbc:mysql://localhost:3306/testtask", "root", "root");
        userDB.showUserList();
        userDB.addUser();
        userDB.updateUser();
        //userDB.readFromCSVToDB("test.csv");
        //userDB.writeFromDBToCSV("test5.csv");
        userDB.closeConnection();
    }
}
