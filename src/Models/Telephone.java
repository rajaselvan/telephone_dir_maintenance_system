package Models;


import java.sql.*;
import javax.swing.JOptionPane;
import java.util.*;

/**
 *
 * @author rajaselvan
 */
public class Telephone {

    private long telNo;
    private int empId;
    private int deptCode;
    private String location;
    private static int telNoSeq = 2222222;
    private int lastSeq;
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "thumbelina";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/databasename";
    private Connection conn = null;
    private PreparedStatement ps;
    private String sql;
    private ResultSet rs;

    public Telephone() {
    }

    public void setTelNo(long no) {
        telNo = no;
    }

    public void setEmpId(int id) {
        empId = id;
    }

    public void setDeptCode(int code) {
        deptCode = code;
    }

    public void setLocation(String loc) {
        location = loc;
    }

    public long getTelNo() {
        return telNo;
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

            sql = "Select count(*) from Employee where empid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) == 0) {
                    
                    throw new Exception("Employee id doesnt exist");
                }
            }

            
            
            sql = "Select count(*) from Telephone where empid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1)!= 0) {
                    
                    throw new Exception("Telephone no already allocated!");
                }
            }

                       
           
            
            sql = "Select deptcode,location from Employee where empid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            rs = ps.executeQuery();

            while (rs.next()) {
                deptCode = rs.getInt(1);
                location = rs.getString(2);
            }
           
            sql = "SELECT MAX(telno) FROM Telephone";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lastSeq = rs.getInt(1);
            }
            if (lastSeq == 0) {
                telNo = telNoSeq;
                telNoSeq++;
            } else {
                telNo = ++lastSeq;
            }
            sql = "INSERT INTO Telephone VALUES(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, telNo);
            ps.setInt(2, empId);
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

            } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
        }
        return false;
    }

    public Vector searchDB(String name) {

        Vector<Vector<String>> employeeVector = new Vector<Vector<String>>();
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
          

            sql = "SELECT t.empid,e.empname,t.telno,t.deptcode,t.location FROM  Telephone t, Employee e  WHERE e.empid=t.empid and e.empname LIKE ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name+"%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Vector<String> employee = new Vector<String>();
                employee.add(rs.getString(1));
                employee.add(rs.getString(2));
                employee.add(rs.getString(3));
                employee.add(rs.getString(4));
                employee.add(rs.getString(5));
                employeeVector.add(employee);
            }
            

        } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (Exception exp) {
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Exception", JOptionPane.INFORMATION_MESSAGE);
        }
        }
        return employeeVector;
    }
}
