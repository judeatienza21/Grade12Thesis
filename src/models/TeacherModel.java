/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Jude A Atienza
 */
public class TeacherModel {
    
    private String AdvisersName;

    public TeacherModel() {
    }

    public String getAdvisersName() {
        return AdvisersName;
    }

    public void setAdvisersName(String AdvisersName) {
        this.AdvisersName = AdvisersName;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
