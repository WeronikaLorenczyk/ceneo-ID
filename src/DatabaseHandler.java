import java.sql.*;

public class DatabaseHandler {

    private static final String url = "jdbc:postgresql://localhost/postgres";
    private static final String user = "postgres";
    private static final String password="postgres";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    Connection connection;
    Statement stmt;

    DatabaseHandler(){
        connection=connect();
        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //nie ma destruktorow wiec trzeba zamknac po kazdym urzyciu!!!!
    void close(){
        try {
            stmt.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //zwraca id lub -1 jezeli nie ma danego login+haslo
     int getUserId(String login, String password){
         try {
             ResultSet rs = stmt.executeQuery( "SELECT customer_id FROM customers WHERE login='"+login+"' AND password='"+password+"';" );
             if(rs.next())
                 return rs.getInt(1);
             return -1;
         } catch (SQLException e) {
             e.printStackTrace();return -1;
         }

     }

}
