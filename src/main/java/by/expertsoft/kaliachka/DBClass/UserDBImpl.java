package by.expertsoft.kaliachka.DBClass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by AndreiK-PC on 28.09.2015.
 */
public class UserDBImpl implements UserDB {

    static Connection connection;
    Statement st;
    private List loginList = new ArrayList();

    public List getLoginList() {
        return loginList;
    }

    public UserDBImpl() throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testtask", "root", "root");
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usertable");
            while (rs.next()){
                loginList.add(rs.getString(1));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void addUser() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter login, name, surname, email, phone, throw enterkey");
        PreparedStatement ps = connection.prepareStatement(ADD);
        String login = sc.nextLine();
        if(!loginList.contains(login)){
            loginList.add(login);
            ps.setString(1, login);
            ps.setString(2, sc.nextLine());
            ps.setString(3, sc.nextLine());
            ps.setString(4, sc.nextLine());
            ps.setString(5, sc.nextLine());
            ps.executeUpdate();
            ps.close();
        }
        else{
            System.out.println("User with login: "+ login +" is alredy exist");
        }
    }

    public void updateUser() throws SQLException {
        //System.out.println("1- login, 2- name, 3- surname, 4 - e-mail, 5 - phone");
        Scanner sc = new Scanner(System.in);
        PreparedStatement ps = connection.prepareStatement(UPDATEEMAIL);
        System.out.println("Enter login to change");
        ps.setString(2, sc.next());
        System.out.println("\nEnter info:");
        ps.setString(1, sc.next());

        ps.executeUpdate();
    }

    public void showUserList() throws SQLException {
        ResultSet rs = st.executeQuery("SELECT * FROM usertable");
        while (rs.next()){
            System.out.println(rs.getString(1)+" "+ rs.getString(2)+ " " + rs.getString(3)+ " "+
                    rs.getString(4) + " " + rs.getString(5));
            loginList.add(rs.getString(1));
        }
        rs.close();
    }

    public void readFromCSVToDB(String filePath) throws IOException, SQLException {
        BufferedReader fileReader = null;
        PreparedStatement ps = null;
        final String DELIMITER = ",";
        try {
            String line = "";
            fileReader = new BufferedReader(new FileReader(filePath));
            while ((line = fileReader.readLine() )!=null){
                String[] tokens = line.split(DELIMITER);
                if(!loginList.contains(tokens[0])){
                    loginList.add(tokens[0]);
                    ps = connection.prepareStatement(ADD);
                    ps.setString(1, tokens[0]);
                    ps.setString(2, tokens[1]);
                    ps.setString(3, tokens[2]);
                    ps.setString(4, tokens[3]);
                    ps.setString(5, tokens[4]);
                    ps.executeUpdate();
                }else{
                    System.out.println("User with login: "+ tokens[0] +" is alredy exist");
                }
            }

        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try{
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String ADD;
    private static final String UPDATENAME;
    private static final String UPDATESURNAME;
    private static final String UPDATEEMAIL;
    private static final String UPDATEPHONE;
    static {
        ADD = "INSERT INTO usertable VALUES (?,?,?,?,?)";
        UPDATENAME = "UPDATE usertable SET name = ? WHERE login = ?";
        UPDATESURNAME = "UPDATE usertable SET surname = ? WHERE login = ?";
        UPDATEEMAIL = "UPDATE usertable SET email = ? WHERE login = ?";
        UPDATEPHONE = "UPDATE usertable SET phone = ? WHERE login = ?";
    }


}
