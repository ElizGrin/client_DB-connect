package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    Button addButton= new Button("Add");
    Button delButton= new Button("Delete");
    Button editButton= new Button("Edit");
    Button disconnectButton = new Button("Disconnect");

    Button searchButton = new Button("Search by bookname");

    TextField numberTf = new TextField();
    TextField YearTf = new TextField();
    TextField AuthorTf = new TextField();
    TextField PublTf = new TextField();
    TextField NameTf = new TextField();

    Label isbn = new Label("ISBN");
    Label authorN = new Label("Author");
    Label bookn = new Label("Book");
    Label publish = new Label("Publisher");
    Label yearp = new Label("Year");

    CheckBox f = new CheckBox("full");
    CheckBox p = new CheckBox("partly");

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("My window");
        FlowPane root = new FlowPane(Orientation.VERTICAL);
        root.setAlignment(Pos.CENTER);
        FlowPane layout1 = new FlowPane(Orientation.VERTICAL);
        FlowPane layout2 = new FlowPane(Orientation.HORIZONTAL);

        layout2.getChildren().add(addButton);
        layout2.getChildren().add(editButton);
        layout2.getChildren().add(delButton);
        layout2.getChildren().add(disconnectButton);

        layout2.getChildren().add(searchButton);

        layout1.getChildren().add(isbn);
        layout1.getChildren().add(numberTf);
        layout1.getChildren().add(authorN);
        layout1.getChildren().add(AuthorTf);
        layout1.getChildren().add(bookn);
        layout1.getChildren().add(NameTf);
        layout1.getChildren().add(publish);
        layout1.getChildren().add(PublTf);
        layout1.getChildren().add(yearp);
        layout1.getChildren().add(YearTf);

        layout2.getChildren().add(f);
        layout2.getChildren().add(p);

        root.getChildren().add(layout1);
        root.getChildren().add(layout2);

        Scene scene = new Scene(root,500,500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

        Socket socket;
        String clientMessage, serverMessage;
        String uNumber, author, bookName, publisher, year;


    public Main() throws Exception{

            InetAddress loc = InetAddress.getByName("localhost");
            socket = new Socket(loc, 8000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            addButton.setOnAction(actionEvent -> {      //после каждой кнопки ждем ответ в консоль
                uNumber = numberTf.getText();
                author = AuthorTf.getText();
                bookName = NameTf.getText();
                publisher = PublTf.getText();
                year = YearTf.getText();
                clientMessage = "1/"+ uNumber + "/" + author + "/" + bookName + "/" + publisher + "/" + year + "/\n";
                try {
                    out.write(clientMessage);
                    out.flush();
                    System.out.println("Message is sent. Waiting for the server answer..");

                    numberTf.setText(null);
                    AuthorTf.setText(null);
                    NameTf.setText(null);
                    PublTf.setText(null);
                    YearTf.setText(null);
                } catch (IOException e) {
                }


            });

            editButton.setOnAction(actionEvent -> {
                uNumber = numberTf.getText();
                author = AuthorTf.getText();
                bookName = NameTf.getText();
                publisher = PublTf.getText();
                year = YearTf.getText();
                clientMessage ="2/"+uNumber + "/" + author + "/" + bookName + "/" + publisher + "/" + year + "/\n";
                try {
                    out.write(clientMessage);
                    out.flush();
                    System.out.println("Message is sent. Waiting for the server answer..");
                    serverMessage = in.readLine();
                    System.out.println(serverMessage);

                } catch (IOException e) {
                }
            });

            delButton.setOnAction(actionEvent -> {
                uNumber = numberTf.getText()+"/\n";
                try {
                    out.write(uNumber);
                    out.flush();
                    System.out.println("Message is sent. Waiting for the server answer..");
                    serverMessage = in.readLine();
                    System.out.println(serverMessage);

                    numberTf.setText(null);
                } catch (IOException e) {
                }
            });

            disconnectButton.setOnAction(actionEvent -> {
                try {
                    out.write("Disconnect\n");
                    out.flush();
                    in.close();
                    out.close();
                    socket.close();
                    System.out.println("Client is disconnected");
                } catch (IOException e) {
                }

            });

            searchButton.setOnAction(actionEvent -> {


                bookName = NameTf.getText();
                try {

                    if(f.isSelected()==true) {
                        try {
                            out.write("searchfull/"+bookName+"/\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    out.flush();
                    if(p.isSelected()==true)
                        out.write("searchpartly/"+bookName+"/\n");
                    out.flush();
                    System.out.println("Message is sent. Waiting for the server answer..");
                    serverMessage = in.readLine();
                    System.out.println(serverMessage);

                } catch (IOException e) {
                }
            });

        }
    }


