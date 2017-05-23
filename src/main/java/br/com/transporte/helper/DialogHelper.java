package br.com.transporte.helper;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DialogHelper {
	
	public static FXMLLoader loadStageFromPage(final ActionEvent event, Class<?> classe, String path, Integer width, Integer height) throws IOException{
		
		final FXMLLoader loader = new FXMLLoader();
		Parent homePageParent = loader.load(classe.getResourceAsStream(path));
		
		Scene homePageScene = new Scene(homePageParent, width, height);
		homePageScene.getStylesheets().add("/styles/styles.css");
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		appStage.setResizable(false);
		appStage.setScene(homePageScene);
		appStage.show();
		
		return loader;
	}
	
	public static FXMLLoader newStage(Class<?> classe, String path, String title) throws IOException{
		
		final Stage stage = new Stage();
        final FXMLLoader loader = new FXMLLoader();
        final Parent rootNode = (Parent) loader.load(classe.getResourceAsStream(path));
        final Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");
        stage.setResizable(false);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        
        return loader;
	}
	
	public static FXMLLoader loadScreen(final ActionEvent event, final Class<?> classe, final String path, 
			final Integer width, final Integer height) throws IOException{
		
		//Close Home dialog before open next Dialog
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		appStage.close();
		
		return DialogHelper.loadStageFromPage(event, classe, path, width, height);
	}

	public static Alert alertInformation(String title, String headerText, String text){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);
		alert.show();
		
		return alert;
	}
	
	public static Alert confirmDialog(final String title, final String headerText, final String text){
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);
		
		return alert;
	}
}
