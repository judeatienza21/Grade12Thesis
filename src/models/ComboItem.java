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
public class ComboItem {
    private String key;
    private String value;
    
   public ComboItem(){       
   }
   
   public ComboItem(String key, String value){
       this.key = key;
       this.value = value;
   }
   
   public void setKey(String key){
       this.key=key;
   }
   
   public String getKey(){
       return key;
   }
   
   public void setValue(String value){
       this.value=value;
   }
   
   public String getValue(){
       return value;
   }

    @Override
    public String toString() {
        return value;
    }
}
