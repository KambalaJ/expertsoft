package by.expertsoft.kaliachka.DBClass;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by AndreiK-PC on 28.09.2015.
 */
public interface UserDB {

    void addUser() throws SQLException;
    void updateUser() throws SQLException;
    void showUserList() throws SQLException;
    void readFromCSVToDB(String filePath) throws IOException, SQLException;
    void writeFromDBToCSV(String filePath) throws IOException, SQLException;
    void closeConnection();

}
