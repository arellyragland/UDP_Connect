
package udpconnect;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

class QuitMenuHandler implements EventHandler<ActionEvent>
{
    @Override
    public void handle(ActionEvent event)
    {
        String result = "[quitt]";
        byte[] sendBuffer = String.valueOf(result).getBytes();
        DatagramPacket sendPack;
        try 
        {
            sendPack = new DatagramPacket(sendBuffer, 0, sendBuffer.length, InetAddress.getByName(Globals.RIpaddress), Globals.Rportno);
            Globals.clientSocket.send(sendPack);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(QuitMenuHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Shutting down the system.
        Platform.exit();
        System.exit(0);
    }
}

class UploadPhotoMenuHandler implements EventHandler<ActionEvent> 
{
    Image image;

    @Override
    public void handle(ActionEvent event)
    {
       FileChooser fileChooser = new FileChooser();
             
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
              
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
                       
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos );
                //baos.flush();
                byte[] imageInByte=baos.toByteArray();
                
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                Globals.imageLView.setImage(image);
                String imageString = "[photo]" + String.valueOf(imageInByte.length);
                byte[] imageStringByte = imageString.getBytes();
                ByteBuffer bb = ByteBuffer.allocate(imageStringByte.length + imageInByte.length);
                bb.put(imageStringByte);
                bb.put(imageInByte);
                byte[] completeImage = bb.array();
                sendImage(completeImage);
                
                } catch (IOException ex) 
            {
                //Logger.getLogger(JavaFXPixel.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    void sendImage(byte[] imageBytes) throws UnknownHostException, IOException
    {
        DatagramPacket sendPack = new DatagramPacket(imageBytes, 0, imageBytes.length, InetAddress.getByName(Globals.RIpaddress), Globals.Rportno);
        Globals.clientSocket.send(sendPack);
        //Globals.clientSocket.close();
    }
}

class btnLoadEventListener implements EventHandler<ActionEvent>
{

    @Override
    public void handle(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class SendChatMessageHandler implements EventHandler<ActionEvent>
{    
    @Override
    public void handle(ActionEvent event)
    {
       String message = "";
       message = Globals.chatMessageTextField.getText();
       Globals.chatMessageTextField.clear();
       Globals.chatHistoryTextArea.appendText(String.format("Me: %s\n", message));
       byte[] sendBuffer = String.valueOf("[chatt]"+message).getBytes();
       DatagramPacket sendPack;
       try 
       {
            sendPack = new DatagramPacket(sendBuffer, 0, sendBuffer.length, InetAddress.getByName(Globals.RIpaddress), Globals.Rportno);
            Globals.clientSocket.send(sendPack);
            
       } 
       catch (IOException ex) 
       {
            Logger.getLogger(QuitMenuHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
    }    
}

class ConnectMenuItemHandler implements EventHandler<ActionEvent>
{
    
    @Override
    public void handle(ActionEvent event)
    {
         // Use a TextInputDialog to get the IP address of the remote partnet
        TextInputDialog IPAddressDlg = new TextInputDialog("localhost");
        IPAddressDlg.setTitle("Server IP Address");
        IPAddressDlg.setHeaderText(null);
        IPAddressDlg.setContentText("Enter IP Address of the remote chat partner");
        Globals.RIpaddress =  IPAddressDlg.showAndWait()
                                        .orElseThrow( 
                                     ()-> new RuntimeException("Bad input"));
        
         // Use a TextInputDialog to get the port number of the remote chat partner
        TextInputDialog RportAddressDlg = new TextInputDialog("5000");
        RportAddressDlg.setTitle("Remote Port Number used to connect");
        RportAddressDlg.setHeaderText(null);
        RportAddressDlg.setContentText("Enter the port number of remote chat partner");
        String RPortno =  RportAddressDlg.showAndWait()
                                         .orElseThrow( 
                                         ()-> new RuntimeException("Bad input"));
        Globals.Rportno = Integer.parseInt(RPortno);
        
         // Use a TextInputDialog to get the IP address of the server
        TextInputDialog portAddressDlg = new TextInputDialog("6000");
        portAddressDlg.setTitle("Port Number to connect to Remote socket");
        portAddressDlg.setHeaderText(null);
        portAddressDlg.setContentText("Enter the port number to use for remote socket");
        String Portno =  portAddressDlg.showAndWait()
                                         .orElseThrow( 
                                         ()-> new RuntimeException("Bad input"));
        Globals.portno = Integer.parseInt(Portno);
        
        try
        {
            Globals.serverSocket = new DatagramSocket(Integer.parseInt(Portno));
            Globals.clientSocket = new DatagramSocket();
            
            // Set up network input monitoring thread
            Thread networkThread = new Thread(new NetworkInputProcessor());
            networkThread.start();
            
        } catch (IOException ex)
        {
            Logger.getLogger(ConnectMenuItemHandler.class.getName())
                  .log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }    
}



