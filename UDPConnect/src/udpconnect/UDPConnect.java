package udpconnect;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class UDPConnect extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        // Menu bar, game menu, and playagain menu
        MenuBar menuBar = new MenuBar();
        Menu chatMenu = new Menu("Chat Menu");
        Menu uploadMenu = new Menu("Upload");
        Menu quitMenu = new Menu("Quit");
        //Adding Menu to the Menu Bar.
        menuBar.getMenus().addAll(chatMenu, uploadMenu, quitMenu);
        
        // Create Menu Items and add to Main Menu
        MenuItem connectMenuItem = new MenuItem();
        connectMenuItem.setText("Connect");
        
        chatMenu.getItems().add(connectMenuItem);
        MenuItem quitItem = new MenuItem();
        quitItem.setText("Close");
        quitMenu.getItems().add(quitItem);
        MenuItem uploadPhotoMenuItem = new MenuItem();
        uploadPhotoMenuItem.setText("Upload Photos");
        uploadMenu.getItems().add(uploadPhotoMenuItem);
        
        connectMenuItem.setOnAction(new ConnectMenuItemHandler());   
        quitItem.setOnAction (new QuitMenuHandler());
        uploadPhotoMenuItem.setOnAction(new UploadPhotoMenuHandler());
        
        BorderPane root = new BorderPane();       
        root.setTop(menuBar);
        Scene scene = new Scene(root);     
        
        HBox ImageHBox = new HBox(60);
        VBox ImageVLBox = new VBox(10);
        ImageView imageLView = new ImageView();
        imageLView.setFitHeight(150);
        imageLView.setFitWidth(150);
        ImageVLBox.setAlignment(Pos.TOP_LEFT);
        Label yourLabel = new Label ("Last Image Uploaded by you.");
        ImageVLBox.getChildren().addAll(yourLabel,imageLView);
        VBox ImageVRBox = new VBox(10);
        ImageVRBox.setAlignment(Pos.TOP_RIGHT);
        Label chattyLabel = new Label ("Last Image sent by your chatty friend.");
        ImageView imageRView = new ImageView();
        imageRView.setFitHeight(150);
        imageRView.setFitWidth(150);
        Globals.imageLView = imageLView;
        Globals.imageRView = imageRView;
        ImageVRBox.getChildren().addAll(chattyLabel,imageRView);
        ImageHBox.getChildren().addAll(ImageVLBox,ImageVRBox);
        
        // ChatHistory TextArea
        TextArea chatHistoryTextArea = new TextArea();
        Globals.chatHistoryTextArea = chatHistoryTextArea;  // make globally accessible
        chatHistoryTextArea.setPrefColumnCount(30);
        chatHistoryTextArea.setPrefRowCount(4);  
        root.setCenter(ImageHBox);
        
        // ChatMessage VBox and its contents
        VBox chatMessageVBox = new VBox(10);
        chatMessageVBox.setAlignment(Pos.TOP_LEFT);
        Label chatHLabel = new Label("Chat History");
        Label chatLabel = new Label("Type a message to send:");
        
       
        TextField chatMessageTextField = new TextField();
        Globals.chatMessageTextField = chatMessageTextField;
        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.setOnAction(new SendChatMessageHandler());
        chatMessageVBox.getChildren().addAll(chatHLabel,
                                             chatHistoryTextArea,
                                             chatLabel, 
                                             chatMessageTextField, 
                                             sendMessageButton);
        TextField statusBar = new TextField("JAVAFX Networked Chat with Photo on UDP");
        Globals.statusBar = statusBar;
        statusBar.setEditable(false);
        chatMessageVBox.getChildren().add(statusBar);
        root.setBottom(chatMessageVBox);        
        
        for (Node node : root.getChildren())
        {
           BorderPane.setMargin(node, new Insets(10));
        }        
        
        primaryStage.setTitle("CSC 469-569 UDP Program (Arelly Ragland)");
        primaryStage.setScene(scene);
        primaryStage.show();
            
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}

