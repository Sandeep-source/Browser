import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.collections.*;
import javafx.geometry.Side;
import javafx.scene.image.*;
import java.io.File;
import java.io.FileOutputStream;
public class Browser extends Application{
	WebEngine engine;
	Stage historyStage;
	ListView<String> historyList;
	public static void main(String[] args) {
		launch(args);
	}
	public void start(Stage mystage){
		mystage.setTitle("Browser");
		BorderPane root=new BorderPane();
		WebView webview=new WebView();
		engine=webview.getEngine();

		engine.load("https://www.google.com");
		BorderPane toolbar=new BorderPane();
        Button back=new Button("<-");
       
        Button plus=new Button("+");
        Button minus=new Button("-");
        Button forword=new Button("->");
        HBox buttonpane=new HBox();

        buttonpane.getChildren().addAll(back,forword,plus,minus);
        MenuButton option=new MenuButton();
        option.setPopupSide(Side.LEFT);
        MenuItem opn=new MenuItem("Open");
		MenuItem save=new MenuItem("Save");
		MenuItem history=new MenuItem("History");
		MenuItem exit=new MenuItem("Exit");
		option.getItems().addAll(opn,save,history,exit);
		save.setAccelerator(KeyCombination.keyCombination("CTRL+S"));
		history.setAccelerator(KeyCombination.keyCombination("CTRL+H"));
		opn.setAccelerator(KeyCombination.keyCombination("CTRL+O"));
		exit.setAccelerator(KeyCombination.keyCombination("CTRL+E"));
		 exit.setOnAction((ae)->System.exit(0));
	    save.setOnAction((ae)->{
	    	FileChooser flc=new FileChooser();
	    	
	    	File fl=flc.showSaveDialog(mystage);
	    	if(fl!=null){
              try{
              	FileOutputStream fout=new FileOutputStream(fl);
              	for(char a : webview.getAccessibleText().toCharArray()){
              		fout.write(a);
              	}
              }catch(Exception ex){
              	System.out.println("bekar hay tu");
              }
	    	}
	    });
	    history.setOnAction((ae)->{
	    	if(historyStage==null){
	    	historyList=new ListView<String>();
	    	for(WebHistory.Entry entry : engine.getHistory().getEntries()){
	    		historyList.getItems().add(entry.getUrl());
	    	}
	    	Stage historyStage=new Stage();
	    	historyStage.initOwner(mystage);
	    	Scene sc=new Scene(historyList,mystage.getWidth()-50,mystage.getHeight()-50);
	    	historyList.getSelectionModel().selectedItemProperty().addListener((va,o,n)->{
	    		if(n!=null){
	    			engine.load(n);
	    			historyStage.close();
	    		}
	    	});
	    	
	    
	        historyStage.setTitle("History");
	    	historyStage.setScene(sc);
	    	historyStage.show();
	    }else{
	    	historyList.getItems().clear();
	    	for(WebHistory.Entry entry : engine.getHistory().getEntries()){
	    		historyList.getItems().add(entry.getUrl());
	    	}
	    	historyStage.show();
	    }
	    });
	    opn.setOnAction((ae)->{
	    	FileChooser flc=new FileChooser();
	    	flc.setTitle("open file");
	    	File opend=flc.showOpenDialog(mystage);
	    	if(opend!=null)
	    	{
	    		try{
	    		engine.load(opend.toURI().toString());
	    	}catch(Exception ek){
	    			System.out.print("lag gaye");
	    		}
	    	}
	    });
        back.setOnAction((ae)->{
        	engine.getHistory().go(-1);
        });
        forword.setOnAction((ae)->{
        	engine.getHistory().go(1);
        });
        plus.setOnAction((ae)->{
                webview.setZoom(webview.getZoom()+.1);
        });
        minus.setOnAction((ae)->{
               webview.setZoom(webview.getZoom()-.1);
        });
        toolbar.setLeft(buttonpane);
        toolbar.setRight(option);
        TextField search=new TextField("");
        search.setPromptText("Enter url");
        search.setOnAction((ae)->{
        	String q=search.getText();
        if(q.startsWith("https://")&&q.endsWith(".com")){
        	engine.load(search.getText());
        }else if(q.endsWith(".com")){
        	engine.load("https://"+search.getText());
        }else{
        	engine.load("https://www.google.com/search?q="+q);
        }
        });
        engine.locationProperty().addListener((va,o,n)->{
                search.setText(n);
        });
        toolbar.setCenter(search);
        Scene scene=new Scene(root);
	scene.getStylesheets().add("bro.css");	
        root.setCenter(webview);
		
        root.setTop(toolbar);
        mystage.setScene(scene);
        mystage.show();
	}


}

