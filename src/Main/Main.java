package Main;

import java.io.FileInputStream;

//import controller.DepartementController;
//import controller.EnseignantController;
import controller.RouteController;
import database.DataBaseController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//Constant 
//SQL DATA

import java.time.LocalDate; // import the LocalDate class


public class Main extends Application {
	public static void main(String[] args) {
		DataBaseController db = DataBaseController.getObjet();
		//db.dropAllTable();
		db.createAllTables();
		 launch(args);
		
	}

	@Override
	public void start(Stage stage) throws Exception {

		try {
			// Create the FXMLLoader
			FXMLLoader loader = new FXMLLoader();
			
			
			// Path to the FXML File
			String fxmlDocPath = "src/layout/login.fxml";
			FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
			// Create the Pane and all Details
			AnchorPane root = (AnchorPane) loader.load(fxmlStream);
//			AnchorPane root = (AnchorPane) loader.load(fxmlStream);
		//	Image image=new Image("file:login2.JPG");
			//ImageView mv=new ImageView(image);
		  //   root.getChildren().addAll(mv);
			stage.getIcons().add(new Image(getClass().getResource("../img/logo.png").toExternalForm()));
			root.getStylesheets().add(getClass().getResource("../layout/login.css").toExternalForm());
		//System.out.println(getClass().getResource("../layout/style.css").toExternalForm());
			//root.getStylesheets().add("/layout/login.css");

			// Create the Scene
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(this.getClass().getResource("style.css").toString());
			// Set the Scene to the Stage
			stage.setScene(scene);
			// Set the Title to the Stage
			stage.setTitle("A SceneBuilder Example");
			// Display the Stage
			
	        stage.setTitle("gestion de personnel");

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					try {
						stop();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			
			RouteController routeController = RouteController.getRouteController();
			routeController.SetPage("loginPage", stage);
			
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}




