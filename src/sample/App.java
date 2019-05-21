package sample;

import java.sql.*;

/**
 *
 * @author postgresqltutorial.com
 */
public class App{

    private final String url = "jdbc:postgresql://localhost/postgres";
    private final String user = "postgres";
    private final String password="postgres";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */

    //to connect Cie łączy z baza danych tylko musisz zmienić haslo psotgresa na "postgres"
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        App app = new App();
        Connection connection=app.connect();

        //robienie czegos z baza jezeli znasz kod sql do tego

        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO shops VALUES" +
                "(nextval('seq_shop_id'),'Krakow','sklep java');";
        stmt.executeUpdate(sql);

         sql = "DROP TABLE IF EXISTS products CASCADE;";
        stmt.executeUpdate(sql);



        //pobieranie danych z bazy

        ResultSet rs = stmt.executeQuery( "SELECT * FROM shops;" );
        while ( rs.next() ) {
            int id = rs.getInt("shop_id");
            String  name = rs.getString("name");
            String  address = rs.getString("location");
            System.out.println( "ID = " + id );
            System.out.println( "NAME = " + name );
            System.out.println( "ADDRESS = " + address );
            System.out.println();
        }


        rs.close();
        stmt.close();
        connection.close();



    }
}