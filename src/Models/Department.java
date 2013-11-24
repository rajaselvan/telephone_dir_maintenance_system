package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajaselvan
 */
public class Department {

    private int deptCode;
    private String deptName;
    private static int depSeq = 100;
    private int lastSeq;
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "thumbelina";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/databasename";
    private Connection conn = null;
    private PreparedStatement ps;
    private String sql;
    private ResultSet rs;

    public Department() {
    }

    public void setDeptCode(int code) {
        deptCode = code;
    }

    public void setDeptName(String name) {
        deptName = name;
    }

    public int getDeptCode() {
        return deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public boolean addToDB() {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "SELECT MAX(deptcode) FROM Department";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lastSeq = rs.getInt(1);
            }
            if (lastSeq == 0) {
                deptCode = depSeq;
                depSeq++;
            } else {
                deptCode = ++lastSeq;
            }
            sql = "INSERT INTO Department VALUES(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptCode);
            ps.setString(2, deptName);
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
            
        }
        finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Department.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public boolean delFromDB() {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "DELETE FROM Department WHERE deptcode=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptCode);
            
            if (ps.executeUpdate() != 1) {
                throw new Exception("Department does not exist");
            }
            conn.close();
            return true;
            
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            try {
                conn.close();
              } catch (SQLException ex) {
                Logger.getLogger(Department.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public Vector listEmp() {

        Vector<Vector<String>> employeeVector = new Vector<Vector<String>>();
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "Select empid,empname,location from Employee WHERE deptcode=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptCode);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Vector<String> employee = new Vector<String>();
                employee.add(rs.getString(1));
                employee.add(rs.getString(2));
                employee.add(rs.getString(3));
                employeeVector.add(employee);
            }
            rs.close();
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Department.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return employeeVector;
    }
}
