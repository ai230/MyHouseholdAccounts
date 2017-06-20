
package MyHouseHoldAccounts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ControllDB {

    //  Database
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static final String DB_URL = "jdbc:derby://localhost:1527/QuickMoney";
    //  Database credentials
    static final String USER = "APP";
    static final String PASS = " ";

    public static ArrayList<InputData> readDB() {

        ArrayList<InputData> dataList = new ArrayList<>();

        Connection con = null;
        Statement statement = null;

        int numRow = 0;
        try {//STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = con.createStatement();
            String sql;
            sql = "SELECT * FROM DETAIL";
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            
            //STEP 5: Extract data from result set
            System.out.println("<<Display values in the DB>>");
            while (rs.next()) {
                numRow = rs.getRow();
                //Display values
                System.out.print("#: " + rs.getRow());
                System.out.print("ID: " + rs.getString("CUST_ID"));
                System.out.print(" CATEGORY: " + rs.getString("CATEGORY"));
                System.out.print(" PROPERTY: " + rs.getString("PROPERTY"));
                System.out.println(" AMOUNT: " + rs.getString("AMOUNT"));

//                int id = Integer.parseInt(numRow);
                String dateStr = rs.getString("DATE");
                String category = rs.getString("CATEGORY");
                String property = rs.getString("PROPERTY");
                Double amount = Double.parseDouble(rs.getString("AMOUNT"));

                //create an object from the data in the DB
                InputData data = new InputData(rs.getRow(), category, property, amount, dateStr);
                //Add to the arraylist
                dataList.add(data);

            }
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return dataList;
    }

    public static void insertDB(String dateStr, String property, String category, Double amount) throws SQLException {
        Connection con = null;
        Statement statement = null;

        try {//STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = con.createStatement();
            String sql;
            sql = "INSERT INTO DETAIL"
                    + "(DATE, PROPERTY, CATEGORY, AMOUNT) " + "VALUES"
                    + "('" + dateStr + "','" + property + "','" + category + "'," + amount + ")";

            System.out.println(sql);
            // STEP 5: insert the data
            statement.executeUpdate(sql);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //STEP 6: Clean-up environment
            if (statement != null) {
                statement.close();
            }

            if (con != null) {
                con.close();
            }
        }

    }

    public static void deleteDB(int selectedRow) throws SQLException {

        ArrayList<InputData> dataList = new ArrayList<>();
        boolean isDeleted = false; 
        Connection con = null;
        Statement statement = null;

        int numRow = 0;
        try {//STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = con.createStatement();
            String sql;
            sql = "SELECT * FROM DETAIL";
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            
//            //select * from ( select mystring , rownum rn  FROM teststring  ) where  rn = 2;
            //STEP 5: Extract data from result set
            System.out.println("<<Display values in the DB>>");
            while (rs.next() && isDeleted == false) {
//                numRow = rs.getRow();
                //Display values
                System.out.println("selectedRow: " + selectedRow); 
                System.out.print("#: " + rs.getRow());
                System.out.print("ID: " + rs.getString("CUST_ID"));
                System.out.print(" CATEGORY: " + rs.getString("CATEGORY"));
                System.out.print(" PROPERTY: " + rs.getString("PROPERTY"));
                System.out.println(" AMOUNT: " + rs.getString("AMOUNT"));

                if (rs.getRow() == selectedRow) {
                    //Display values
                    System.out.print("delete ");
                    System.out.print("#: " + rs.getRow());
                    System.out.print("ID: " + rs.getString("CUST_ID"));
                    System.out.print(" CATEGORY: " + rs.getString("CATEGORY"));
                    System.out.print(" PROPERTY: " + rs.getString("PROPERTY"));
                    System.out.println(" AMOUNT: " + rs.getString("AMOUNT"));
                
                    String deleteSql = "DELETE FROM DETAIL WHERE CUST_ID =" + rs.getString("CUST_ID");
                    // STEP 5: delete the data
                    System.out.println("sql=" + deleteSql);
                    statement.execute(deleteSql);
                    System.out.println("Deleted succseed!");
                    isDeleted = true;
                }
                if(isDeleted == true){
                    System.out.println("in while if: isDeleted =  " + isDeleted);
                    break;
                }
                System.out.println("in while: isDeleted =  " + isDeleted);
            }
            System.out.println("loop ended : isDeleted =  " + isDeleted);
            dataList = null;
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }
}
