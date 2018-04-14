/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static client.Client.sInput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import yahtzee.Mesaj;
import yahtzee.Yahtzee1;

/**
 *
 * @author SELİN
 */
// serverdan gelecek mesajları dinleyen thread
class Listen extends Thread {

    public void run() {
        //soket bağlı olduğu sürece dön
        while (Client.socket.isConnected()) {
            try {
                //mesaj gelmesini bloking olarak dinyelen komut
                Mesaj received = (Mesaj) (sInput.readObject());
                //mesaj gelirse bu satıra geçer
                //mesaj tipine göre yapılacak işlemi ayır.
                switch (received.type) {
                    case Name:
                        break;
                    case RivalConnected:
                        String name = received.content1.toString();
                        Yahtzee1.ThisGame.oyuncu2.setText(name);
                        Yahtzee1.ThisGame.tmr_slider.start();

                        break;
                    case Disconnect:
                        break;
                    case Text:
                        Yahtzee1.ThisGame.txt2.setText(received.content1.toString());

                        break;

                    case Secim:
                        Yahtzee1.ThisGame.RivalSelection = (String) received.content1;
                        if (Yahtzee1.ThisGame.secim.equals("1")) {
                            Yahtzee1.ThisGame.ZarAt.setEnabled(true);
                            Yahtzee1.ThisGame.bastan();

                        }

                        if (Yahtzee1.ThisGame.RivalSelection.equals("threeof")) {
                            Yahtzee1.ThisGame.jButton19.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("fourof")) {
                            Yahtzee1.ThisGame.jButton17.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("bir")) {
                            Yahtzee1.ThisGame.jButton2.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("iki")) {
                            Yahtzee1.ThisGame.jButton4.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("üc")) {
                            Yahtzee1.ThisGame.jButton7.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("dört")) {
                            Yahtzee1.ThisGame.jButton5.setText((String) received.content2);

                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("bes")) {
                            Yahtzee1.ThisGame.jButton11.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("alti")) {
                            Yahtzee1.ThisGame.jButton9.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("fullhouse")) {
                            Yahtzee1.ThisGame.jButton23.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("smallst")) {
                            Yahtzee1.ThisGame.jButton21.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("largest")) {
                            Yahtzee1.ThisGame.jButton27.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("chance")) {
                            Yahtzee1.ThisGame.jButton25.setText((String) received.content2);
                        }
                        if (Yahtzee1.ThisGame.RivalSelection.equals("yahtzee")) {
                            Yahtzee1.ThisGame.jButton3.setText((String) received.content2);
                        }
                        break;
                    case Bitis: 
                        Yahtzee1.ThisGame.setEnabled(true);
                        Yahtzee1.ThisGame.rakiptotalscore=(int) received.content1;                  
                        Yahtzee1.ThisGame.jLabel12.setText( Yahtzee1.ThisGame.oyuncu2.getText()
                                +" :"+String.valueOf(Yahtzee1.ThisGame.rakiptotalscore));
                
                        break;

                }

            } catch (IOException ex) {

                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            }
        }

    }
}

public class Client {

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenMe;

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client.socket = new Socket(ip, port);
            Client.Display("Servera bağlandı");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new Listen();
            Client.listenMe.start();

            //ilk mesaj olarak isim gönderiyorum
            Mesaj msg = new Mesaj(Mesaj.Message_Type.Name);
            System.out.println("Mesajın içeriği geldi " + Yahtzee1.ThisGame.oyuncu1.getText());
            msg.content1 = Yahtzee1.ThisGame.oyuncu1.getText();
            Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listenMe.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();

                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send(Mesaj msg) {
        try {

            System.out.println("Mesaj buraya geldi");
            System.out.println(msg.type);
            Client.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
