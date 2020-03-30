package udpconnect;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;


/*class ChatMessageReceived implements Runnable
{    
    private final String message;
    ChatMessageReceived(String message)
    {        
        this.message = message;
    }
    @Override
    public void run()
    {
       Globals.chatHistoryTextArea.appendText(String.format("Remote: %s\n", message));
    }    
}

class QuitCommandReceived implements Runnable
{
    @Override
    public void run()
    {
       
    }    
}

class PlayCommandReceived implements Runnable
{
    @Override
    public void run()
    {
       
    }    
}*/
// This runnable will wait for network input and post
// work orders on the Application thread when network
// input is received
class NetworkInputProcessor implements Runnable
{   
    String message, line;
    String[] messages = null;
    @Override
    public void run()
    { 
        try 
        {
            while (true)
            {
                String sentence = null;
                DatagramPacket receivePacket = null;
                receivePacket = new DatagramPacket(Globals.receiveData, Globals.receiveData.length);
                Globals.serverSocket.receive(receivePacket);
                byte[] data = new byte[receivePacket.getLength()];
                System.arraycopy(receivePacket.getData(), 0, data, 0, 7);
                byte[] buff = receivePacket.getData();
                sentence = new String(data);
                Pattern p = Pattern.compile("\\[(.*?)\\]");
                Matcher m = p.matcher(sentence);

                while(m.find()) {
                message = m.group(1);
                }
                
                switch (message)
                {
                    case "chatt":
                        byte[] chatbuffer = new byte[receivePacket.getLength()];;
                        System.arraycopy(buff, 0, chatbuffer, 0, receivePacket.getLength());
                        String chat = new String(chatbuffer);
                        messages = chat.split("]");
                        Globals.chatHistoryTextArea.appendText(String.format("Remote: %s\n", messages[1]));
                        break;
                    case "photo":
                        //get the length
                        byte[] len = new byte[4];
                        System.arraycopy(receivePacket.getData(), 7, len, 0, 4);
                        String lenstring = new String(len);
                        Integer length = Integer.parseInt(lenstring);
                        byte[] imageb = new byte[length];
                        System.arraycopy(receivePacket.getData(), 11, imageb, 0, length-1);
			BufferedImage bImageFromConvert = ImageIO.read(new ByteArrayInputStream(imageb));
                        Image image = SwingFXUtils.toFXImage(bImageFromConvert, null);
                        Globals.imageRView.setImage(image);
                        break;
                    case "quitt":
                        Platform.exit();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(NetworkInputProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

              
    }   
}
