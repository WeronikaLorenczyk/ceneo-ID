package other;

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
    static PreparedStatement getShopId;
    static PreparedStatement newUser;
    static PreparedStatement newShopRating;
    static PreparedStatement newShop;
    static PreparedStatement newProductRating;
    static PreparedStatement search;
    static PreparedStatement newProduct;

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
            getShopId=conn.prepareStatement("SELECT shop_id,password FROM shops WHERE login=?");
            newUser=conn.prepareStatement("INSERT INTO customers (login, password, age, location) VALUES (?,?,121,?);");
            newShopRating =conn.prepareStatement("INSERT INTO product_customer (product_id, customer_id,rating) VALUES (?,?,?);");
            newShop=conn.prepareStatement("INSERT INTO shops (name,location,login,password) VALUES (?,?,?,?);");
            newProductRating =conn.prepareStatement("INSERT INTO product_customer (product_id, customer_id,rating) VALUES (?,?,?);");
            search=conn.prepareStatement("SELECT name, product_id, item_rating(product_id) from products p where is_of_cat((select name from categories c where c.category_id=p.category_id),?) and coalesce(item_rating(product_id),1)>=? order by ?;");
            newProduct=conn.prepareStatement("INSERT INTO products(name,description,category_id) VALUES (?,?,?);");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    Connection connection;
    Statement stmt;

    public DatabaseHandler(){
        connection=connect();
        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //nie ma destruktorow wiec trzeba zamknac po kazdym użyciu!!!!
    public void close(){
        try {
            stmt.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-----------funkcje sprawdzajace poprawnosc danych zeby nam ktos nie zrobil drop table------------------

    public boolean properWord(String word){
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
    public int getUserId(String login, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
         if (!properWord(login) || !properPassword(password))
             return -1;
         try {
             //ResultSet rs = stmt.executeQuery("SELECT customer_id,password FROM customers WHERE login='" + login + "';");
             getId.setString(1,login);
             ResultSet rs = getId.executeQuery();
             if (rs.next()) {
                 String p = rs.getString(2);
                 if (!Hasher.validatePassword(password, p)) {
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

    public int getShopId(String login, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (!properWord(login) || !properPassword(password))
            return -1;
        try {

            getShopId.setString(1,login);
            ResultSet rs = getShopId.executeQuery();
            if (rs.next()) {
                String p = rs.getString(2);
                if (!Hasher.validatePassword(password, p)) {
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

    public int getCatId(String name){
        int x=0;
        ResultSet r = null;
        try {
            r = stmt.executeQuery("Select category_id from categories where name ='"+ name +"';");
            r.next();
            x=r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(x);
        return x;
    }


     //---------------funkcje dodajace krotki
//TODO mozna dodawac puste nazwy, wszystkie pola sa wymagane nawet jak w bazie nie sa
    //zwraca czy dodano
    public boolean addUser(String login, String password, String location) throws SQLException {
        if(login==null || password==null || location==null)
            return false;
         newUser.setString(1,login);
         newUser.setString(2,password);
         newUser.setString(3,location);
         try {
             newUser.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         }
         return true;
     }

    //zwraca id lub -1 gdy nie moze go dodac
    public  boolean addProduct(String name, String description, String category) throws SQLException {

         newProduct.setString(1,name);
        newProduct.setString(2,description);
        newProduct.setInt(3,getCatId(category));
        try {
            newProduct.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
     }


    //zwraca id lub -1 gdy nie moze go dodac
    public  boolean addProductCustomer(int productId, int customerId, int rating){
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
             return false;
         }
         return true;
     }



    //zwraca id lub -1 gdy nie moze go dodac
    public boolean addShop (String name,String login,String password, String location){
        //String sqlTask="INSERT INTO shops (location, name) VALUES ("+location+", "+name+");";
        try {
            newShop.setString(1,name);
            newShop.setString(2,location);
            newShop.setString(3,login);
            newShop.setString(4,password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            newShop.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //zwraca id lub -1 gdy nie moze go dodac
    public void addCustomerShop(int customerId, int shopId, int rating){
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

    public List<String> getCategories() throws SQLException {
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

    public List<String> getLocations() throws SQLException {
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

    public List<String > getAttributes(int id){
        List<String> ret=new LinkedList<>();
        try {
            ResultSet r = stmt.executeQuery("SELECT name from attributes where attribute_id in(select * from attributes("+id+"));");
            while (r.next()){
                ret.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public  ResultSet searchCategories(String category, String sort, float lowestR) throws SQLException {
        search.setString(1,category);
        search.setFloat(2,lowestR);
        search.setString(3,sort);
        return search.executeQuery();
    }

    public ResultSet priceComp(int id) throws SQLException {
        return stmt.executeQuery("Select sh.name, sh.location, s.price from shop_product s join shops sh on sh.shop_id= s.shop_id where s.product_id="+id+"order by s.price;");
    }

}
