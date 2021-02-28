package System;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jude A Atienza
 */

import models.TeacherModel;
import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import models.ComboItem;

public class Module {
    
    static AdminLogin adminlogin;
    static AdminMenu adminmenu;
    static AdminRegister adminregister;
    static Card card;
    static Home home;
    static TeacherRegister teacherRegister;
    static TeachersLogin teacherslogin;
    static TeachersMenu teachersmenu;
    
    static int SessionTeacherId=0;
    static int getStudentsGradeCount=0;
    static TeacherModel sessionTeacher;

    
    public Module(){
        adminlogin = new AdminLogin();
        adminmenu = new AdminMenu();
        adminregister = new AdminRegister();
        card = new Card();
        home = new Home();
        teacherRegister = new TeacherRegister();
        teacherslogin = new TeachersLogin();
        
        sessionTeacher = new TeacherModel();
        teachersmenu = new TeachersMenu();
    }
    
    public static String login(String username, String password){
        Connection connection = new DBConnection().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = connection.prepareStatement("Select * From Teachers WHERE LCASE(USERNAME)=LCASE(?) and PASSWORD=?");
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            while(rs.next()){
                if(password.equals(rs.getString("PASSWORD"))){
                    
                    rs.getString("firstName");
                    SessionTeacherId = rs.getInt("ID");
                    sessionTeacher.setAdvisersName(rs.getString("firstName") + " " +rs.getString("lastName"));
                    

                    Module.teachersmenu.setAdviserName(sessionTeacher.getAdvisersName());
                    Module.teachersmenu.refreshSection();
                    
                    
                    return rs.getString("FIRSTNAME");
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {connection.close(); } catch (SQLException ex) {}
        }
        return "failed";
    }
    
    public static void JCBox(JComboBox jComboBox){
        jComboBox.removeAllItems();
        Connection connection = new DBConnection().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        try{
            
        System.out.println(connection.isClosed());
        ps = connection.prepareStatement("SELECT SECTION_ID, SECTION_NAME FROM teacher_sections a, `sections` b WHERE a.SECTION_ID=b.ID AND TEACHER_ID=?;");
        ps.setInt(1, SessionTeacherId);
        rs = ps.executeQuery();
        while(rs.next()){
            jComboBox.addItem(new ComboItem(rs.getString(1),rs.getString("SECTION_NAME")));
        }
        
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public static void getStudentsGrade(String sectionId, JTable jtable){
        getStudentsGradeCount=0;
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column)
            {
              return false;//This causes all cells to be not editable
            }
        };
        
        Connection connection =  null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, Map<String, Map<String, String>>> data = new HashMap<>();
        ArrayList<String> subjects = new ArrayList<>();
        try {
//            model.addColumn("SUBJECT");
//            model.addColumn("STUDENT NAME");
//            model.addColumn("SECTION");
//            model.addColumn("FIRST QUARTERLY");
//            model.addColumn("SECOND QUARTERLY");
//            model.addColumn("FINAL");
            connection = new DBConnection().getConnection();
            ps = connection.prepareStatement("SELECT Subjects.SUBJECT_NAME SUBJECT_NAME, b.STUDENT_NAME STUDENT_NAME, sections.SECTION_NAME, FIRST_QUARTERLY, SECOND_QUARTERLY, (FIRST_QUARTERLY+SECOND_QUARTERLY)/2 `FINAL` \n" +
                                                "FROM grades, `students` b, `sections`, `Subjects` \n" +
                                                "WHERE \n" +
                                                "b.SECTION_ID = sections.ID \n" +
                                                "AND grades.SUBJECT_ID=Subjects.SUBJECT_ID\n" +
                                                "AND grades.STUDENT_ID=b.ID \n"+
                                                "AND sections.ID = ?;");
            ps.setString(1, sectionId);
            rs = ps.executeQuery();
            while (rs.next()) {

                Map<String, Map<String, String>> subData = new LinkedHashMap<>();
                Map<String, String> subjectGrade = new LinkedHashMap<>();
                subjectGrade.put("subject_name", rs.getString("SUBJECT_NAME"));
                subjectGrade.put("first_q", rs.getString("FIRST_QUARTERLY"));
                subjectGrade.put("second_q", rs.getString("SECOND_QUARTERLY"));
                subjectGrade.put("final", rs.getString("FINAL"));
                subData.put(rs.getString("SUBJECT_NAME"), subjectGrade);
                
                if(data.containsKey(rs.getString("STUDENT_NAME"))){
                    data.get(rs.getString("STUDENT_NAME")).putAll(subData);
                }else{
                    data.put(rs.getString("STUDENT_NAME"), subData);
                }
                
                if(!subjects.contains(rs.getString("SUBJECT_NAME"))){
                    subjects.add(rs.getString("SUBJECT_NAME"));
                }
            }
            
            
            /**
            * populate column
            */
            model.addColumn("STUDENT NAME");
            for (int i = 0; i < subjects.size(); i++) {
            
                model.addColumn("SUBJECT");
                model.addColumn("FIRST QUARTERLY");
                model.addColumn("SECOND QUARTERLY");
                model.addColumn("FINAL");
                getStudentsGradeCount+=4;
            }
            model.addColumn("GENERAL AVERAGE");
            
            
            for (Map.Entry<String, Map<String, Map<String, String>>> entrySet : data.entrySet()) {
                String key = entrySet.getKey();
                ArrayList<Object> dataArray = new ArrayList<>();
                
                /**
                 * add student name
                 */
                dataArray.add(key);
                
                Map<String, Map<String, String>> value = entrySet.getValue();
                for (Map.Entry<String, Map<String, String>> entrySet1 : value.entrySet()) {
                    String key1 = entrySet1.getKey();
                    Map<String, String> value1 = entrySet1.getValue();
                    /**
                     * add data to Array list
                     */
                    
                    dataArray.add(key1);
                    dataArray.add(value1.get("first_q"));
                    dataArray.add(value1.get("second_q"));
                    dataArray.add(value1.get("final"));
                }
                
                /**
                 * add row data
                 */
                Object[] dataObject = new Object[dataArray.size()];
                for (int i = 0; i < dataArray.size(); i++) {
                    dataObject[i] = dataArray.get(i);
                }
                model.addRow(dataObject);
                System.out.println(dataObject);
            }
            
            
           
            jtable.setModel(model);
            jtable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {rs.close();} catch (SQLException ex) {Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);}
            try {ps.close();} catch (SQLException ex) {Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
    
    //Save Students
    public static void saveStudents(String studentName, String studentSecId){
        try {
            ResultSet rs;
            Statement st;           
                     Connection connect = new DBConnection().getConnection();
                      
                        st= connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        String sql = "Select * from students";
                        rs=st.executeQuery(sql);
        
            
            rs.moveToInsertRow();
            rs.updateInt("ID", 0);
            rs.updateString("STUDENT_NAME", studentName);
            rs.updateString("SECTION_ID", studentSecId);
            
            rs.insertRow();
            rs.close();
            st.close();
            
            JOptionPane.showMessageDialog(null, "Saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void saveSection(String sectionName){
        try{
            ResultSet rs;
            Statement st;           
                     Connection connect = new DBConnection().getConnection();
                      
                        st= connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        String sql = "Select * from sections";
                        rs=st.executeQuery(sql);
        
            
            rs.moveToInsertRow();
            rs.updateInt("ID", 0);
            rs.updateString("SECTION_NAME", sectionName);
            
            rs.insertRow();
            rs.close();
            st.close();
            
            JOptionPane.showMessageDialog(null, "Saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveSubject(String subjectName,String sectionId){
         try{
            ResultSet rs;
            Statement st;           
                     Connection connect = new DBConnection().getConnection();
                        st= connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        String sql = "Select * from subjects";
                        rs=st.executeQuery(sql);
        
            
            rs.moveToInsertRow();
            rs.updateInt("SUBJECT_ID", 0);
            rs.updateString("SUBJECT_NAME", subjectName);
            rs.updateString("SECTION_ID", sectionId);
            
            rs.insertRow();
            rs.close();
            st.close();
            
            JOptionPane.showMessageDialog(null, "Saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Add Section
    public static void AddSection(JComboBox jComboBox){
        jComboBox.removeAllItems();
        Connection connection = new DBConnection().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        try{
        System.out.println(connection.isClosed());
        ps = connection.prepareStatement("Select  * From sections");
        rs = ps.executeQuery();
        while(rs.next()){
            
            jComboBox.addItem(new ComboItem(rs.getString(1),rs.getString("SECTION_NAME")));
            
            }
        
       }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Add Students
    public static void AddStudents(JComboBox jComboBox){
        jComboBox.removeAllItems();
        Connection connection = new DBConnection().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        try{
            ps = connection.prepareStatement("Select * From students");
            rs = ps.executeQuery();
        
        while(rs.next()){    
            jComboBox.addItem(new ComboItem(rs.getString(1),rs.getString("STUDENT_NAME")));
            }
        
       }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    //Add Subejcts 
    public static void AddSubjects(JComboBox jComboBox){
        jComboBox.removeAllItems();
        Connection connection = new DBConnection().getConnection();
        PreparedStatement ps;
        ResultSet rs;
        try{
        ps = connection.prepareStatement("Select * From subjects");
        rs = ps.executeQuery();
        
        while(rs.next()){
            jComboBox.addItem(new ComboItem(rs.getString(2),rs.getString("SUBJECT_NAME")));
        }
        
       }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public static boolean updateGrades(String students, String firstQuarterly, String secondQuarterly, String students_id){
     
     Connection connection = new DBConnection().getConnection();
     PreparedStatement ps = null;
     
     try {   
        connection.setAutoCommit(false);
        
        ps = connection.prepareStatement("UPDATE grades SET  FIRST_QUARTERLY = ?, SECOND_QUARTERLY=? WHERE ID=?");
        ps.setString(1, firstQuarterly);
        ps.setString(2, secondQuarterly);
        ps.setString(3, students_id);
        
        if(ps.executeUpdate() == 0){
            connection.rollback();
        }

        PreparedStatement ps2 = connection.prepareStatement("UPDATE students SET STUDENT_NAME = ? WHERE ID=?");
        ps2.setString(1, students);
        
        if(ps2.executeUpdate() == 0){
            connection.rollback();
        }
        
        connection.commit();
    } catch (Exception e) {
        e.printStackTrace();
    }finally{
         try {connection.close(); } catch (SQLException ex) {}
    }
    return false;
}
    
    public static boolean deleteStudents(String id) {       
         Connection connection = new DBConnection().getConnection();
         PreparedStatement ps;
         try {
            ps = connection.prepareStatement("DELETE FROM students WHERE ID=?");
            ps.setString(1, id);
            if(ps.executeUpdate() > 0){
                return true;
            };
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
             try {connection.close(); } catch (SQLException ex) {}
        }
         return false;
    }             
}


