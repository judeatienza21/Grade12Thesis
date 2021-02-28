/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;


/**
 *
 * @author Jude A Atienza
 */

public class LoadingScreen {

    /**
    * @param args the command line arguments
    */
    
    public static void main(String[] args) {
      Splash tab = new Splash();
      tab.setVisible(true);
      Home start = new Home();
        try {
            for (int i = 0; i <= 100 ; i++) {
                Thread.sleep(50);
                tab.loadingNum.setText(Integer.toString(i)+"%");
                tab.loadingBar.setValue(i);
                if( i == 100) {
                    tab.setVisible(false);
                    start.setVisible(true);
                }
            }
            
        } catch (Exception e) {
            
        }
    }
    
}
