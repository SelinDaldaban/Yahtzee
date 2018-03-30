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
public class Mesaj implements java.io.Serializable{
    
    public static enum Message_Type {None, Name, Disconnect,RivalConnected, Text, Selected, Bitis,Start,}
    
    public Message_Type type;
    public Object content;
    public Mesaj(Message_Type t)
    {
        this.type=t;
    }
 
    
}
