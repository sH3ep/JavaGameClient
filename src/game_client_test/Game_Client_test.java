/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_client_test;

/**
 *
 * @author sH3ep
 */
public class Game_Client_test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Client client = new Client();
        client.communicate();
        System.out.println("Connected");
        client.play();
        
        // TODO code application logic here
    }
    
}
