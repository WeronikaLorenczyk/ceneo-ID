package other;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class DatabaseHandler {

    
    private static final String url = "jdbc:postgresql://localhost/postgres";
    private static final String user = "postgres";
    private static final String password="postgres";

    static PreparedStatement getId;
    static PreparedStatement getShopId;
    static PreparedStatement newUser;
    static PreparedStatement newShopRating;
    static PreparedStatement newShop;
    static PreparedStatement search;
    static PreparedStatement newProduct;
    static PreparedStatement newShopProduct;
    static PreparedStatement newAttribute;

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
            search=conn.prepareStatement("SELECT name, product_id, item_rating(product_id) from products p where is_of_cat((select name from categories c where c.category_id=p.category_id),?) and coalesce(item_rating(product_id),0)>=? " +
                    " and ? in (select value from attribute_product where product_id=p.product_id and attribute_id=(select attribute_id from attributes where name=?)) order by ?;");
            newProduct=conn.prepareStatement("INSERT INTO products(name,description,category_id) VALUES (?,?,?) RETURNING product_id;");
            newShopProduct=conn.prepareStatement("INSERT INTO shop_product (shop_id, product_id, price) VALUES (?,?,?);");
            newAttribute=conn.prepareStatement("INSERT INTO attribute_product (product_id, attribute_id, value) VALUES (?, ?, ?);");
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


    //-------------funkcje zwracajace id

    //zwraca id lub -1 jezeli nie ma danego login+haslo
    public int getUserId(String login, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
         try {
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

    int getAttId(String att) throws SQLException {
        ResultSet r=stmt.executeQuery("SELECT attribute_id FROM attributes where name='"+att+"';");
        r.next();
        return r.getInt(1);
    }


     //---------------funkcje dodajace krotki
    //zwraca czy dodano
    public boolean addUser(String login, String password, String location) throws SQLException {
        if(login.length()==0 || password.length()==0 || location.length()==0)
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
    public  int addProduct(String name, String description, String category) throws SQLException {
        if(name.length()==0 ||  category.length()==0)
            return -1;

         newProduct.setString(1,name);
        newProduct.setString(2,description);
        newProduct.setInt(3,getCatId(category));
        try {
            ResultSet r=newProduct.executeQuery();
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

     }


    public  void addProductRating(int productId, int customerId, int rating){
        try {
            stmt.executeUpdate("INSERT INTO product_customer (product_id, customer_id,rating) VALUES ("+productId+","+customerId+","+rating+");");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                stmt.executeUpdate("UPDATE product_customer set rating="+rating+" WHERE customer_id="+customerId+" AND product_id="+productId+";");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }


    public boolean addAttribute(String att, String value, int productId){
        if(value.length()==0 || att.length()==0)
            return false;
        try {
            newAttribute.setInt(1,productId);
            newAttribute.setInt(2,getAttId(att));
            newAttribute.setString(3,value);
            newAttribute.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean addShop (String name,String login,String password, String location){
        if(login.length()==0 || password.length()==0 || location.length()==0||name.length()==0)
            return false;
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


    public boolean addShopProduct (int shopId, int productId, float price){
        try {

            newShopProduct.setInt(1,shopId);
            newShopProduct.setInt(2,productId);
            newShopProduct.setFloat(3,price);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            newShopProduct.executeUpdate();
        } catch (SQLException e) {
            try {
                stmt.executeUpdate("UPDATE shop_product set price="+price+" WHERE shop_id="+shopId+" AND product_id="+productId+";");
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }
        }
        return true;
    }


    public void addCustomerShop(int customerId, int shopId, int rating){
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


    //=----------funkcje zwracajace listy

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

    public  ResultSet searchCategories(String category, String sort, float lowestR, String attributeVal, String attribute) throws SQLException {
        if(category==null){
            return stmt.executeQuery("SELECT name, product_id, item_rating(product_id) from products p where coalesce(item_rating(product_id),0)>=" + lowestR + "order by " + sort);
        }
        if (attribute==null || attributeVal==null){
            return stmt.executeQuery("SELECT name, product_id, item_rating(product_id) from products p where is_of_cat((select name from categories c where c.category_id=p.category_id),'"+category+"') and coalesce(item_rating(product_id),0)>= " +lowestR+ "order by " + sort);
        }
        search.setString(1,category);
        search.setFloat(2,lowestR);
        search.setString(3,attributeVal);
        search.setString(4,attribute);
        search.setString(5,sort);
        System.out.println(search);
        //System.out.println(search);
        return search.executeQuery();
    }

    public ResultSet priceComp(int id) throws SQLException {
        return stmt.executeQuery("Select sh.name, sh.location, s.price from shop_product s join shops sh on sh.shop_id= s.shop_id where s.product_id="+id+"order by s.price;");
    }

    public String getProductDesc(int id) throws SQLException {
        ResultSet r= stmt.executeQuery("Select description from products where product_id="+id);
        r.next();
        return r.getString(1);
    }

    public List<String> getAttVal(String id){
        List<String> ret=new LinkedList<>();
        try {
            ResultSet r = stmt.executeQuery("SELECT distinct value from attribute_product where attribute_id=(Select attribute_id from attributes where name='"+id+"');");
            while (r.next()){
                ret.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
