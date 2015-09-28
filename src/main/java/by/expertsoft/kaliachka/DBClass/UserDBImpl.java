package by.expertsoft.kaliachka.DBClass;

import com.sun.java.util.jar.pack.*;


import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Created by AndreiK-PC on 28.09.2015.
 */
public class UserDBImpl implements UserDB {

    static Connection connection;
    Statement st;
    /**
     *
     */
    private List loginList = new ArrayList();

    /**
     * <p>
     *     Создание соединения с базой данных. Все данные, которые там хранились переходят в List loginList пул,
     *     для исключения дублирования
     * </p>
     * @param url
     * @param login
     * @param password
     * @throws SQLException
     */
    public UserDBImpl(String url, String login, String password) throws SQLException {
        try {
            connection = DriverManager.getConnection(url, login, password);
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usertable");
            while (rs.next()){
                loginList.add(rs.getString(1));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Добавление пользователя при условии, что его логин уникален. Если условие выполнено, его логин заносится в пул.
     * @throws SQLException
     */
    public void addUser() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter login, name, surname, email, phone, throw enterkey");
        PreparedStatement ps = connection.prepareStatement(ADD);
        String login = sc.nextLine();
        if(!loginList.contains(login)){
            loginList.add(login);
            String name = sc.nextLine();
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

    /**
     * Изменение поля пользователя. Менять можно все, кроме логина.
     * @throws SQLException
     */
    public void updateUser() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int switchCoise = 0;
        PreparedStatement ps;
        String login;
        System.out.println("Enter login to change");
        login = sc.nextLine();
        if(loginList.contains(login)){
            System.out.println("1- name, 2- surname, 3 - e-mail, 4 - phone");
            switchCoise = sc.nextInt();
        }

        switch (switchCoise){
            case 1:
                ps = connection.prepareStatement(UPDATENAME);
                ps.setString(2, login);
                System.out.println("\nEnter info:");
                ps.setString(1, sc.next());
                ps.executeUpdate();
                break;
            case 2:
                ps = connection.prepareStatement(UPDATESURNAME);
                ps.setString(2, login);
                System.out.println("\nEnter info:");
                ps.setString(1, sc.next());
                ps.executeUpdate();
                break;
            case 3:
                ps = connection.prepareStatement(UPDATEEMAIL);
                ps.setString(2, login);
                System.out.println("\nEnter info:");
                ps.setString(1, sc.next());
                ps.executeUpdate();
                break;
            case 4:
                ps = connection.prepareStatement(UPDATEPHONE);
                ps.setString(2, login);
                System.out.println("\nEnter info:");
                ps.setString(1, sc.next());
                ps.executeUpdate();
                break;
            default:
                System.out.println("Wrong");
        }

    }

    /**
     * Вывод на экран список пользователей. В зависимости от предпочтения можно выводить сортированный по разным полям
     * список
     * @throws SQLException
     */
    public void showUserList() throws SQLException {
        ResultSet rs;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1, if U want to sort db by name, 2 -//- by surname, another one -//- by login");

        int switchChoise = sc.nextInt();

        switch (switchChoise){
            case 1:
                rs = st.executeQuery("SELECT * FROM usertable ORDER by name");
                while (rs.next()){
                    System.out.println(rs.getString(1)+" "+ rs.getString(2)+ " " + rs.getString(3)+ " "+
                            rs.getString(4) + " " + rs.getString(5));
                    loginList.add(rs.getString(1));
                }
                rs.close();
                break;
            case 2:
                rs = st.executeQuery("SELECT * FROM usertable ORDER by surname");
                while (rs.next()){
                    System.out.println(rs.getString(1)+" "+ rs.getString(2)+ " " + rs.getString(3)+ " "+
                            rs.getString(4) + " " + rs.getString(5));
                    loginList.add(rs.getString(1));
                }
                rs.close();
                break;
            default:
                rs = st.executeQuery("SELECT * FROM usertable ORDER by login");
                while (rs.next()){
                    System.out.println(rs.getString(1)+" "+ rs.getString(2)+ " " + rs.getString(3)+ " "+
                            rs.getString(4) + " " + rs.getString(5));
                    loginList.add(rs.getString(1));
                }
                rs.close();
                break;
        }
    }

    /**
     * Чтение из CSV файла. При условии, что логин уникален, значение добавляется в БД
     *
     * @param filePath
     * @throws IOException
     * @throws SQLException
     */
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

    /**
     * Запись в CSV файл всех данных из БД.
     * @param filePath
     * @throws IOException
     * @throws SQLException
     */
    public void writeFromDBToCSV(String filePath) throws IOException, SQLException {
        BufferedWriter fileWriter = null;
        PreparedStatement ps = null;
        final String DELIMITER = ",";
        final String ENDLINE = "\n";
try {
    File file = new File(filePath);
    if (!file.exists()) {
        file.createNewFile();
    }
    FileWriter fw = new FileWriter(file.getAbsoluteFile());
    fileWriter = new BufferedWriter(fw);
    ResultSet rs = st.executeQuery("SELECT * FROM usertable");
    while (rs.next()) {
        fileWriter.write(rs.getString(1));
        fileWriter.write(DELIMITER);
        fileWriter.write(rs.getString(2));
        fileWriter.write(DELIMITER);
        fileWriter.write(rs.getString(3));
        fileWriter.write(DELIMITER);
        fileWriter.write(rs.getString(4));
        fileWriter.write(DELIMITER);
        fileWriter.write(rs.getString(5));
        fileWriter.write(ENDLINE);
    }
    fileWriter.close();
}catch (IOException e){
    e.printStackTrace();
}
    }

    /**
     * Закрытие соединения
     */
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
