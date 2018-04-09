/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yahtzee;

/**
 *
 * @author SELİN
 */
public class Mesaj implements java.io.Serializable {
   // /mesaj tipleri enum 
    public static enum Message_Type {None, Name, Disconnect,RivalConnected, Text, Secim, Bitis,Start,}
    //mesajın tipi
    public Message_Type type;
    //mesajın içeriği obje tipinde ki istenilen tip içerik yüklenebilsin
    public Object content1;
    public Object content2;
    public Mesaj(Message_Type t)
    {
        this.type=t;
    }
 

}
