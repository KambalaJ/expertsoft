
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
        UserDBImpl userDB = new UserDBImpl();
        userDB.updateUser();
        userDB.showUserList();
    }
}
