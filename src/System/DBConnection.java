package System;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David
 */
public class DBConnection extends javax.swing.JFrame {
    
    public Connection conn;
    public Statement st;
    public ResultSet rs;
    public PreparedStatement pst;
    public String sql;
    
    
    public void DBConnect(){
           try {
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    conn = DriverManager.getConnection("jdbc:ucanaccess://database.accdb");
                } 
           catch (Exception e) {
               
             e.printStackTrace();
        }
    }
    
    public Connection getConnection(){
    DBConnect();
    return conn;
    
    }  
    public void createRsandSt(){
        try {
        st= conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        System.out.println("Creating connection");
        sql = getSql();
        System.out.println("Executing query " +sql);
        rs=st.executeQuery(sql);
        
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Encountered Error");
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void preparedStandRs(){
        try{
            sql=getSql();
            pst = conn.prepareStatement(sql);
            System.out.println("checked");
            rs = pst.executeQuery();
            System.out.println("checked");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void closeRsandSt(){
        try {
            st.close();
            System.out.println("Closing statement");
            rs.close();
            System.out.println("Closing resultset");
            
        } catch (Exception e) {
        }
    }
    public String getSql(){
        return this.sql;
    }
    public void setSql(String mysql){
            this.sql = mysql;
            System.out.println("setting sql to " + mysql);
            
    }
}
