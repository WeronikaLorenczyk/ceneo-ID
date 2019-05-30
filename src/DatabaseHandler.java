import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;


//!!!!!!!!!!!!!!!!!!!!!!!!!!tu jest wszystko nieprzetestowane i pisane na kolanie, poogarniam to ale juz nie mialam czasu, nawet tu nie patrz

public class DatabaseHandler {


    private static final String url = "jdbc:postgresql://localhost/postgres";
    private static final String user = "postgres";
    private static final String password="postgres";

    static PreparedStatement getId;
    static PreparedStatement newUser;
    static PreparedStatement newShopRating;
    static PreparedStatement newShop;
    static PreparedStatement newProductRating;
    static PreparedStatement search;

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            getId=conn.prepareStatement("SELECT customer_id,password FROM customers WHERE login=?");
            newUser=conn.prepareStatement("INSERT INTO customers (login, password, age, location) VALUES (?,?,121,?)");
            newShopRating =conn.prepareStatement("INSERT INTO product_customer (product_id, customer_id,rating) VALUES (?,?,?);");
            newShop=conn.prepareStatement("INSERT INTO shops (location, name) VALUES (?,?)");
            newProductRating =conn.prepareStatement("INSERT INTO product_customer (product_id, customer_id,rating) VALUES (?,?,?);");
            search=conn.prepareStatement("SELECT name, item_rating(product_id) from products where is_of_cat(category_id,?) and coalesce(item_rating(product_id),1)>? order by ?;");
        } catch (SQLException e) {
            e.printStackTrace();
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
             //ResultSet rs = stmt.executeQuery("SELECT customer_id,password FROM customers WHERE login='" + login + "';");
             getId.setString(1,login);
             ResultSet rs = getId.executeQuery();
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
     int addUser(String login, String password, String location) throws SQLException {
        if(login==null || password==null || location==null)
            return -1;
         /*if(login.indexOf(' ')>=0 || login.indexOf(';')>=0 || password.indexOf(' ')>=0 || password.indexOf(';')>=0 || !location.matches("[ a-zA-Z0-9./]+"))
             return -1;*/
         //String sqlTask="INSERT INTO customers (login, password, age, location) VALUES ('"+login+"', '"+password+"',121, '"+location+"');";
         newUser.setString(1,login);
         newUser.setString(2,password);
         newUser.setString(3,location);
         try {
             newUser.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         try {
             getId.setString(1,login);
             ResultSet rs = getId.executeQuery();
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
         //String sqlTask="INSERT INTO product_customer (product_id, customer_id,rating) VALUES ("+productId+", "+customerId+", "+rating+");";
         try {
             newProductRating.setInt(1,productId);
             newProductRating.setInt(2,customerId);
             newProductRating.setInt(3,rating);
         } catch (SQLException e) {
             e.printStackTrace();
         }
         try {
             newProductRating.executeUpdate();
             //stmt.executeUpdate(sqlTask);
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }


    //zwraca id lub -1 gdy nie moze go dodac
    void addShop (String name, String location){
        //String sqlTask="INSERT INTO shops (location, name) VALUES ("+location+", "+name+");";
        try {
            newShopRating.setString(1,name);
            newShopRating.setString(2,location);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            newShopRating.executeUpdate();
            //stmt.executeUpdate(sqlTask);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //zwraca id lub -1 gdy nie moze go dodac
    void addCustomerShop(int customerId, int shopId, int rating){
        //String sqlTask="INSERT INTO customers_shops (customer_id, shop_id,rating) VALUES ("+customerId+", "+shopId+", "+rating+");";
        try {
            newShopRating.setInt(1,customerId);
            newShopRating.setInt(2,shopId);
            newShopRating.setInt(3,rating);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            //stmt.executeUpdate(sqlTask);
            newShopRating.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<String> getCategories() throws SQLException {
        ResultSet r=null;
        try {
             r = stmt.executeQuery("SELECT name from categories");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String> a =new LinkedList<>();
        while (r.next()){
            a.add(r.getString(1));
        }
        return a;
    }

    List<String> getLocations() throws SQLException {
        ResultSet r=null;
        try {
            r = stmt.executeQuery("SELECT location from shops");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String> a =new LinkedList<>();
        while (r.next()){
            a.add(r.getString(1));
        }
        return a;
    }

    ResultSet search(int category, String location, String sort, String attribute, float lowestR) throws SQLException {
        search.setInt(1,category);
        search.setFloat(2,lowestR);
        search.setString(3,sort);
        return search.executeQuery();
    }


}
