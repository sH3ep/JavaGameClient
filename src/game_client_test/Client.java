/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_client_test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import game_server_test.*;

/**
 *
 * @author sH3ep
 */
public class Client {

    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private PvPBattleInformation information;

    public Client() {

    }

    public void communicate() {

        while (!isConnected) {
            try {
                socket = new Socket("localHost", 9001);
                System.out.println("Connected");
                isConnected = true;

            } catch (SocketException se) {
                se.printStackTrace();
// System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void playPVE() {

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("wybierz akcje ktora chcesz wykonac 1-attack");

            Fight_Command command = new Fight_Command(scanner.nextInt());

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());

                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                information = (PvPBattleInformation) inputStream.readObject();
                System.out.println("Twoje HP: " + information.GetHeroHP() + "      HP przeciwnika: " + information.GetOponentHP());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while ((information.GetOponentHP() > 0) && (information.GetHeroHP() > 0));

        if (information.GetHeroHP() > 0) {
            System.out.println("Wygrales");
        } else {
            System.out.println("Przegrales");
        }
    }

    public void playPVP() {

        GetBattleInformation();
        showInformation();

        if (!information.GetFirstAttack()) {
            GetBattleInformation();
            showInformation();
        }

        do {
            SendCommandToServer();

            GetBattleInformation();
            showInformation();
            if ((information.GetOponentHP() <= 0) || (information.GetHeroHP() <= 0)) {
                break;
            }
            GetBattleInformation();
            showInformation();

        } while ((information.GetOponentHP() > 0) && (information.GetHeroHP() > 0));

        if (information.GetHeroHP() > 0) {
            System.out.println("Wygrales");
        } else {
            System.out.println("Przegrales");
        }

    }

    private void GetBattleInformation() {

        try {
            ObjectInputStream tempInputStream = new ObjectInputStream(socket.getInputStream());
            information = (PvPBattleInformation) tempInputStream.readObject();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SendCommandToServer() {

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(GetCommand());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Fight_Command GetCommand() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("wybierz akcje ktora chcesz wykonac 1-attack");

        return new Fight_Command(scanner.nextInt());

    }

    private void showInformation() {
        System.out.println("Twoje HP: " + information.GetHeroHP() + "     HP przeciwnika: " + information.GetOponentHP());
    }
}
