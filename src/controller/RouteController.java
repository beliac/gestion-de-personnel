package controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import controller.RouteController;

public class RouteController {
	
	ArrayList<PageData> pageDataList = new ArrayList<PageData>();
	
	
	HashMap<String, Stage> route = new HashMap<>();

	static private RouteController routeController =new RouteController(); ;

	private RouteController() {
			loadPage();
			createPage();
		
	}

	public static RouteController getRouteController() {
		return routeController;
	}

	public void registerPage(String pageName, Stage stage) {
		route.put(pageName, stage);
	}

	public Stage getPage(String pageName) {
		return route.get(pageName);
	}

	public void SetPage(String pageName, Stage stage) {
		route.put(pageName, stage);
	}

	private void loadPage() {
		// main pages
		String ensgPageCss=getClass().getResource("../layout/style.css").toExternalForm();
		//String addensgPageCss=getClass().getResource("../layout/login.css").toExternalForm();
	
		
	pageDataList.add(new PageData("EnseignantPage", "src/layout/Enseignant.fxml", ensgPageCss, PageData.VBOX));
		pageDataList.add(new PageData("congePage", "src/layout/CongePage.fxml", ensgPageCss, PageData.VBOX));
		pageDataList.add(new PageData("depPage", "src/layout/department.fxml", ensgPageCss, PageData.ANCHOR_PANE));
		pageDataList.add(new PageData("notificationPage", "src/layout/notificationPage.fxml", ensgPageCss, PageData.VBOX));
		pageDataList.add(new PageData("echlonPage", "src/layout/echlonPage.fxml", ensgPageCss, PageData.VBOX));
		
		pageDataList.add(new PageData("addEnsgPage", "src/layout/userFrom.fxml", null, PageData.SCENE));
		pageDataList.add(new PageData("addNotificationPage", "src/layout/addNotification.fxml", ensgPageCss, PageData.ANCHOR_PANE));
		pageDataList.add(new PageData("addCongePage", "src/layout/addConge.fxml", ensgPageCss, PageData.ANCHOR_PANE));

	}

	private void createPage() {

		for (PageData page : pageDataList) {
			try {

				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader();

				String fxmlDocPath = page.fxmlPath;
				FileInputStream fxmlStream;
				
		        stage.setTitle("gestion de personnel");
				fxmlStream = new FileInputStream(fxmlDocPath);

				

				if (page.parent == PageData.ANCHOR_PANE) {
					AnchorPane root = (AnchorPane) loader.load(fxmlStream);
					if (page.cssPath != null) {
						// Todo do somthing
						root.getStylesheets().add(page.cssPath);
					}
					
					Scene scene = new Scene(root);
					stage.setScene(scene);
					  stage.setTitle("gestion de personnel");
					  stage.getIcons().add(new Image(getClass().getResource("../img/logo.png").toExternalForm()));
				} else if (page.parent == PageData.VBOX) {
					VBox root = (VBox) loader.load(fxmlStream);
					if (page.cssPath != null) {
						// Todo do somthing
						root.getStylesheets().add(page.cssPath);
					}
					
					Scene scene = new Scene(root);
					  stage.setTitle("gestion de personnel");
					stage.setScene(scene);
					stage.getIcons().add(new Image(getClass().getResource("../img/logo.png").toExternalForm()));
					

				} else {
					Scene scene = (Scene) loader.load(fxmlStream);
					if (page.cssPath != null) {
						// Todo do somthing
						scene.getStylesheets().add(page.cssPath);
						stage.getIcons().add(new Image(getClass().getResource("../img/logo.png").toExternalForm()));
					}
					  stage.setTitle("gestion de personnel");
					stage.setScene(scene);
					stage.getIcons().add(new Image(getClass().getResource("../img/logo.png").toExternalForm()));
				}


				route.put(page.pageName, stage);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}




class PageData {
	static String VBOX = "vbox", ANCHOR_PANE = "anchorPane", SCENE = "scene";
	
	String pageName;
	String fxmlPath;
	String cssPath;
	String parent;

	PageData(String pageName, String fxmlPath, String cssPath, String parent) {
		this.pageName = pageName;
		this.fxmlPath = fxmlPath;
		this.cssPath = cssPath;
		this.parent = parent;
	}
}
