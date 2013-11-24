package Models;


import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author rajaselvan
 */
public class Employee {

    private String empName;
    private int empId;
    private int deptCode;
    private String location;
    private static int empSeq = 1000;
    private int lastSeq;
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "thumbelina";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/databasename";
    private static Connection conn = null;
    private static PreparedStatement ps = null;
    private static String sql = null;
    private static ResultSet rs = null;

    public Employee() {
    }

    public void setEmpId(int id) {
        empId = id;
    }

    public void setEmpName(String name) {
        empName = name;
    }

    public void setDeptCode(int code) {
        deptCode = code;
    }

    public void setLocation(String loc) {
        location = loc;
    }

    public String getEmpName() {
        return empName;
    }

    public int getEmpId() {
        return empId;
    }

    public int getDeptCode() {
        return deptCode;
    }

    public String getLocation() {
        return location;
    }

    public boolean addToDB() {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "SELECT count(*) from Department where deptcode=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptCode);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) == 0) {
                    
                    throw new Exception("Dept code doesnt exist!");
                }
            }
            

            sql = "SELECT MAX(empid) FROM Employee";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lastSeq = rs.getInt(1);
            }
            if (lastSeq == 0) {
                empId = empSeq;
                empSeq++;
            } else {
                empId = ++lastSeq;
            }
            
            sql = "INSERT INTO Employee VALUES(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            ps.setString(2, empName);
            ps.setInt(3, deptCode);
            ps.setString(4, location);
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (Exception exp) {

            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
            
        } finally {
            try {
                conn.close();
                
            }
            catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
        }
        return false;
    }

    public boolean updateDB() {
        try {
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE Employee SET empname=? ,  deptcode=? ,  location=? WHERE empid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, empName);
            ps.setInt(2,deptCode);
            ps.setString(3, location);
            ps.setInt(4, empId);
            ps.executeUpdate();
            
            sql = "UPDATE Telephone SET deptcode=? WHERE empid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptCode);
            ps.setInt(2,empId);
            ps.executeUpdate();
            
            conn.close();
            return true;
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }

    public boolean findEmp() {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "Select empname,deptcode,location from Employee where empid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                empName = rs.getString(1);
                deptCode = rs.getInt(2);
                location = rs.getString(3);
            } else {
                
                throw new Exception("Employee does not exist");
            }
            
            conn.close();
            return true;
            
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
        }
        return false;
        
    }

    public void delEmp() {
        try {
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM Employee WHERE empid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            ps.executeUpdate();
            conn.close();
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static ArrayList<String> populateComboBox() {
        ArrayList<String> DeptList = new ArrayList<String>();
        try {
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            sql = "SELECT deptcode FROM Department ORDER BY deptcode";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery(sql);

            while (rs.next()) {
                int deptcode = rs.getInt(1);

                
                DeptList.add(Integer.toString(deptcode));
            }

            
            conn.close();

        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return DeptList;
    }

}

