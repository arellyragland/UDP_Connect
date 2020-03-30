
package udpconnect;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

enum PlayerID { NONE, LOCAL, REMOTE };
public class Globals
{
    // If goesFirst is true, this application makes the first move when a new
    // game is started.
    static boolean goesFirst;
    // If isServer is true, this application starts up its server socket and
    // waits for a client to connect. The server goes first for the first game.
    static boolean isServer;
    // determine whose turn it is to make a move. Initially the server will
    // go first.
    static boolean localPlayerTurn;
    // The following variable  keeps track of number of moves so we can tell
    // when the board is filled up with moves.    
    static int numberOfTurnsPlayed = 0;
    // The following variable keeps track of the number of games already placed.
    static int numberOfGamesPlayed = 0;
    // The following two variables are used at the end of a game
    static boolean waitForPlayAgainConfirmation = false;
    static boolean localPlayerWantsToPlayAgain = false;
    // The color used by the local player: Server is BLUE while Client is RED.
    static   Color  localPlayerColor; 
    static   Color  remotePlayerColor;
    // These are used for network communication   
    static Socket socket;
    static BufferedReader bReader;
    static PrintWriter pWriter;
    
    static ImageView imageLView;
    static ImageView imageRView;
    
    // Gui components that have to be globally accesible
    static TextArea chatHistoryTextArea;
    static TextField chatMessageTextField;
    static GridPane gameGrid;
    static TextField statusBar;
    static DatagramSocket serverSocket;
    static DatagramSocket clientSocket;
    static int[] avail = {6,6,6,6,6,6,6};
    static byte[] receiveData = new byte[7000];
    static byte[] sendData = new byte[7000];
    static String RIpaddress;
    static int Rportno;
    static int portno;
    
    // Call this method to get ready for a new game
    static void reset()
    {
        // Server goes first at beginning of an even numbered game
        // and client goes first at beginning of odd numbered games.
        localPlayerTurn = isServer && numberOfGamesPlayed % 2 == 0 
                || !isServer && numberOfGamesPlayed %2 != 0;  
        
        for (int j=0;j<7;j++)
        {
            avail[j]=6;
        }
    }
    
    
}
