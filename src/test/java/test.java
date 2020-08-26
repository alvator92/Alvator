import javax.crypto.spec.PSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class test {

    private static final String url = "jdbc:mysql://localhost:3306/testbd?UseTimezone=true&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "imdfFks&fsk24msf@";


    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs;
    private static int rss;



    public static void main(String args[]) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String value = "";
        String SQL = "";

        String query = "select count(*) from test";

        try {

            System.out.println("Вас приветствует Влад Нагрузочный");
            Thread.sleep(2000);
            System.out.println("Давай мы попробуем добавить кого-нибудь в наш справочник");
            Thread.sleep(2000);
            System.out.println("Что думаешь?");
            Thread.sleep(2000);
            System.out.println("Да / Нет");

            if (reader.readLine().equals("Да")){
                System.out.println("Тогда погнали");
            }else{
                System.out.println("Ну и ладно, хорошего вечера");
            }

            System.out.println("Устанавливаем соединение с базой");
            con = DriverManager.getConnection(url, user, password);




            System.out.println("Давай добавим кого-нибудь");
            Thread.sleep(3000);
            while(true) {
                System.out.println("Введи имя");
                String fname = reader.readLine();
                System.out.println("Введи Фамилию");
                String lname = reader.readLine();
                System.out.println("Введи Номер");
                String pnumber = reader.readLine();
                selectOne(lname);
                insertBD(fname, lname, pnumber);

                System.out.println("Хотите добавить еще абонента?");
                if (reader.readLine().equals("Да")){
                    System.out.println("Тогда погнали");
                }else{
                    break;
                }
            }


            System.out.println("Хотите посмотреть всех абонентов?");
            if (reader.readLine().equals("Да")){
                System.out.println("----------------------");
                showDB();
                System.out.println("----------------------");
            }

            System.out.println("Хотите кого-нибудь удалить?");
            if (reader.readLine().equals("Да")){
                System.out.println("Введите порядковый номер абонента которого хотите удалить");
                delete(reader.readLine());
                System.out.println("----------------------");
            }else{

            }



            System.out.println("Спасибо за то, что воспользовались этим приложением");

        } catch (SQLException | IOException | InterruptedException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
                System.out.println("Закрываем соединение с базой");
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
               rs.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
    }

    public static void showDB () throws SQLException {
        String SQL = "";
        String value = "";
        con = DriverManager.getConnection(url, user, password);


        try {
            SQL = "select * from testbd.phone_base";
            //SQL = "SELECT username FROM test WHERE id = '1'";

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {

                value = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
                System.out.println(value);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertBD (String fname, String lname, String pnumber) throws SQLException {
        String SQL = "";

        con = DriverManager.getConnection(url, user, password);


        try {
            SQL = "INSERT INTO testbd.phone_base (`fname`, `lname`, pnumber) \n" +
                    "VALUES (?, ?, ?);";

            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, lname);
            preparedStatement.setString(3, pnumber);

            rss = preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void selectOne(String lname) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            con = DriverManager.getConnection(url, user, password);

                String sql = "SELECT * FROM testbd.phone_base WHERE lname = ?";
                try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
                    preparedStatement.setString(1, lname);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(resultSet.next()){
                        String id = resultSet.getString(1);
                        String fname_1 = resultSet.getString(2);
                        String lname_1 = resultSet.getString(3);
                        String pnumber_1 = resultSet.getString(4);
                        System.out.println("Абонент с такой фамилией уже есть");
                        System.out.println("----------------------");
                        System.out.println(id + " " + fname_1 + " " + lname_1 + " " + pnumber_1);
                        System.out.println("----------------------");
                        System.out.println("[Хотите обновить его данные?");

                        if (reader.readLine().equals("Да")){
                            System.out.println("Введите новый номер телефона");
                            String pnumber_new = reader.readLine();
                            update(id, fname_1, lname, pnumber_new);
                            System.out.println("Данные обновлены");
                            System.out.println("----------------------");
                        }else{
                            System.out.println("Создаем абонента с такой же фамилией");
                        }

                    }else {
                        System.out.println("Такого абонента нет в базе данных");
                    }
                }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static void update(String id, String fname, String lname, String pnumber) {

        try{
            con = DriverManager.getConnection(url, user, password);

            String sql = "UPDATE testbd.phone_base SET fname = ?, lname = ?, pnumber = ? WHERE id = ?";
            try(PreparedStatement preparedStatement = con.prepareStatement(sql)){

                preparedStatement.setString(1, fname);
                preparedStatement.setString(2, lname);
                preparedStatement.setString(3, pnumber);
                preparedStatement.setString(4, id);
                preparedStatement.executeUpdate();

            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static int delete(String id) {

        try{
            con = DriverManager.getConnection(url, user, password);

            String sql = "DELETE FROM testbd.phone_base WHERE id = ?";
            try(PreparedStatement preparedStatement = con.prepareStatement(sql)){

                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return 0;
    }

}