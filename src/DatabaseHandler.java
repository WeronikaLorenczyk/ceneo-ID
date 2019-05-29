import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;


//!!!!!!!!!!!!!!!!!!!!!!!!!!tu jest wszystko nieprzetestowane i pisane na kolanie, poogarniam to ale juz nie mialam czasu, nawet tu nie patrz

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


    //nie ma destruktorow wiec trzeba zamknac po kazdym użyciu!!!!
    void close(){
        try {
            stmt.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-----------funkcje sprawdzajace poprawnosc danych zeby nam ktos nie zrobil drop table------------------

    boolean properWord(String word){
        if(word==null || word.length()==0 || word.indexOf(' ')>=0 || word.indexOf(';')>=0)
            return false;
        return true;
    }

    boolean properText(String text){
        return text.matches("[ a-zA-Z0-9./,]+");
    }

    //mozemy ustawic dowolne ograniczenia na haslo
    boolean properPassword(String password){
        /*if(password==null || password.length()<6 || password.indexOf(' ')>=0 || password.indexOf(';')>=0)
            return false;*/
        return true;
    }

    //-------------funkcje zwracajace id

    //zwraca id lub -1 jezeli nie ma danego login+haslo
     int getUserId(String login, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
         //haslo i login nie moze zawierac spacji ani ; zeby ktos nam nie wpisal hasla drop table
         if (!properWord(login) || !properPassword(password))
             return -1;
         try {
             ResultSet rs = stmt.executeQuery("SELECT customer_id,password FROM customers WHERE login='" + login + "';");

             if (rs.next()) {
                 String p = rs.getString(2);
                 if (!Hasher.validatePassword(password, p)) {
                     System.out.println("Wrong login or password!");
                     return -1;
                 }
                 return rs.getInt(1);
             }
             return -1;
         } catch (SQLException e) {
             e.printStackTrace();
             return -1;
         }
     }


     //---------------funkcje dodajace krotki

    //zwraca id lub -1 gdy nie moze go dodac
     int addUser(String login, String password, String location){
        if(login==null || password==null || location==null)
            return -1;
         if(login.indexOf(' ')>=0 || login.indexOf(';')>=0 || password.indexOf(' ')>=0 || password.indexOf(';')>=0 || !location.matches("[ a-zA-Z0-9./]+"))
             return -1;
        String sqlTask="INSERT INTO customers (login, password, age, location) VALUES ('"+login+"', '"+password+"',121, '"+location+"');";
         try {
             stmt.executeUpdate(sqlTask);
         } catch (SQLException e) {
             e.printStackTrace();
         }
         try {
             ResultSet rs = stmt.executeQuery( "SELECT customer_id FROM customers WHERE login='"+login+"';" );
             if(rs.next())
                 return rs.getInt(1);
             return -1;
         } catch (SQLException e) {
             e.printStackTrace();return -1;
         }
     }

    //zwraca id lub -1 gdy nie moze go dodac
     int addProduct(String name, String description, int categoryId){

         if(name==null || description==null )
             return -1;
         //trzeba napisac przy dodawaniu opisu jakie znaki sa dozwolone. Chyba ze pozwalamy na wiecej ale to moze nie byc bezpieczne
         if( !name.matches("[ a-zA-Z0-9./]+") || !description.matches("[ a-zA-Z0-9./,]+"))
             return -1;
         String sqlTask="INSERT INTO products (name, description,category_id) VALUES ("+name+", "+description+", "+categoryId+");";
         try {
             stmt.executeUpdate(sqlTask);
         } catch (SQLException e) {
             e.printStackTrace();
         }
         try {
             ResultSet rs = stmt.executeQuery( "SELECT product_id FROM products WHERE name='"+name+"' AND description='"+description+"' AND category='"+categoryId+"';" );
             if(rs.next())
                 return rs.getInt(1);
             return -1;
         } catch (SQLException e) {
             e.printStackTrace();return -1;
         }
     }


    //zwraca id lub -1 gdy nie moze go dodac
     void addProductCustomer(int productId, int customerId, int rating){
         String sqlTask="INSERT INTO product_customer (product_id, customer_id,rating) VALUES ("+productId+", "+customerId+", "+rating+");";
         try {
             stmt.executeUpdate(sqlTask);
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }


    //zwraca id lub -1 gdy nie moze go dodac
    void addShop (String name, String location){
        String sqlTask="INSERT INTO shops (location, name) VALUES ("+location+", "+name+");";
        try {
            stmt.executeUpdate(sqlTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //zwraca id lub -1 gdy nie moze go dodac
    void addCustomerShop(int customerId, int shopId, int rating){
        String sqlTask="INSERT INTO customers_shops (customer_id, shop_id,rating) VALUES ("+customerId+", "+shopId+", "+rating+");";
        try {
            stmt.executeUpdate(sqlTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
