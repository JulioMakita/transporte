package br.com.transporte.app;

import static br.com.transporte.enumeration.EnumPath.HOME;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Named
public class MainApp extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);
    
    private ApplicationContext context;

    public static void main(final String[] args) throws Exception {
        launch(args);
    }
    
    @Override
	public void init()  {
		try {
			super.init();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					LOG.info("Inicializando o contexto do Spring...");
					context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
					LOG.info("Contexto do Spring inicializado");
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

    public void start(final Stage stage) throws Exception {

    	LOG.info("Starting Application");
        
    	LOG.debug("Loading FXML for main view from: {}", HOME.getPath());
        
        final FXMLLoader loader = new FXMLLoader();
        final Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(HOME.getPath()));
        
        final Scene scene = new Scene(rootNode, 800, 450);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setResizable(false);
        stage.setTitle("Controle Fretado");
        stage.setScene(scene);
        stage.show();
    }
}
