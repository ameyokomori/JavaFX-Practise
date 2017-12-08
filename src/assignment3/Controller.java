package assignment3;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


/**
 * Controller class for the Character Editor.
 * 
 * @author leggy (Lachlan Healey)
 * @author Weiye Zhao
 */
public class Controller implements Initializable{
	/* The model for the editor */
	private Model model;
	
	/* GUI components, as in the .FXML file */
	@FXML
	private Button bt_loaddb;
	@FXML
	private Button bt_savedb;
	@FXML
	private Button bt_createdb;
	@FXML
	private Button bt_createchar;
	@FXML
	private Button bt_createsuperchar;
	@FXML
	private Button bt_search;
	@FXML
	private Button bt_clearsearch;
	@FXML
	private Button bt_deletechar;
	@FXML
	private Button bt_changeimg;
	@FXML
	private Button bt_savecharchange;
	@FXML
	private TextField tf_filename;
	@FXML
	private TextField tf_createcharname;
	@FXML
	private TextField tf_searchcharname;
	@FXML
	private TextField tf_description;
	@FXML
	private TextField tf_powerlevel;
	@FXML
	private ListView<String> lv_char;
	@FXML
	private TextArea ta_traits;
	@FXML
	private TextArea ta_powers;
	@FXML
	private Label lb_charname;
	@FXML
	private ImageView iv_img;
	
	public Controller() {
		this.model = new Model();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		handleLoadAction();
		handleSaveAction();
		handleCreateAction();
		handleSearchAction();
		handleDeleteAction();
		handleEditAction();
		handleImageAction();
		handleClearAction();
		handleSelectAction();
	}
	
	/**
	 * Initialize the textfield and other fields at the right page
	 */
	private void ClearInfoPage() {
		lb_charname.setText("<Character Name>");
		tf_description.setText("");
		ta_traits.setText("");
		Image img = new Image("images/default.png");
		iv_img.setImage(img);
		tf_powerlevel.setText("");
		ta_powers.setText("");
	}
	
	/**
	 * Add the action of the load database button.
	 */
	private void handleLoadAction() {
		bt_loaddb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ClearInfoPage();
				model = new Model();
				String path = tf_filename.getText();
				ObservableList<String> characters;
				
				/* Deal with empty file path */
				if (path.equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a file name.");
					alert.showAndWait();
				} else {
					try {
						characters = FXCollections.observableArrayList(model.Load(path));
						lv_char.setItems(characters);
					} catch (Exception e) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setContentText(e.toString());
						alert.showAndWait();
						e.printStackTrace();
					}
				}								
			}
		});
	}
	
	/**
	 * Add the action of the create database button and save database button.
	 */
	private void handleSaveAction() {
		bt_createdb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String path = tf_filename.getText();
				
				/* Deal with empty file path */
				if (path.equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a file name.");
					alert.showAndWait();
				} else if(new File(path).exists()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Database already exists.");
					alert.showAndWait();
				} else {
					try {
						model.CreateDB(path);
					} catch (Exception e) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setContentText(e.toString());
						alert.showAndWait();
						e.printStackTrace();
					}
				}								
			}			
		});
		
		bt_savedb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					model.Save();
				} catch (Exception e) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Save faild.\n" + e.toString());
					alert.showAndWait();
					e.printStackTrace();
				}				
			}			
		});
	}

	/**
	 * Add the action of the create character and super character button.
	 * User should input all the information in the right page after click the create button.
	 */
	private void handleCreateAction() {
		bt_createchar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = tf_createcharname.getText();
				if (!name.equals("")) {
					ClearInfoPage();
					lb_charname.setText(name);
					tf_powerlevel.setEditable(false);
					ta_powers.setEditable(false);
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a name.");
					alert.showAndWait();
				}				
			}			
		});
		
		bt_createsuperchar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = tf_createcharname.getText();
				if (!name.equals("")) {
					ClearInfoPage();
					lb_charname.setText(name);
					tf_powerlevel.setEditable(true);
					ta_powers.setEditable(true);
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a name.");
					alert.showAndWait();
				}				
			}			
		});
	}
	
	/**
	 * Add the action of the search button.
	 */
	private void handleSearchAction() {
		bt_search.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = tf_searchcharname.getText();
				if (!name.equals("")) {
					Character character = model.Search(name);
					/* Character not found */
					if (character == null) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("No result");
						alert.setContentText("No character found.");
						alert.showAndWait();
					} else {
						Set<String> traits = character.getTraits();
						String trait = "";
						for(String s : traits) {
							trait = trait + s + System.getProperty("line.separator");
						}
						lb_charname.setText(character.getName());
						tf_description.setText(character.getDescription());
						ta_traits.setText(trait);
						Image img = new Image(character.getImagePath());
						iv_img.setImage(img);
						
						if (character instanceof SuperCharacter) {
							SuperCharacter sc = (SuperCharacter) character;
							String powerlevel = sc.getPowerRanking() + "";
							Set<String> powers = sc.getPowers();
							String power = "";
							for(String s : powers) {
								power = power + s + System.getProperty("line.separator");
							}
							tf_powerlevel.setText(powerlevel);
							ta_powers.setText(power);
							tf_powerlevel.setEditable(true);
							ta_powers.setEditable(true);
						} else {
							tf_powerlevel.setText("");
							ta_powers.setText("");
							tf_powerlevel.setEditable(false);
							ta_powers.setEditable(false);
						}
					}					
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a name.");
					alert.showAndWait();
				} 				
			}			
		});
	}
	
	/**
	 * Add the action of the delete button.
	 */
	private void handleDeleteAction() {
		bt_deletechar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {						
				String searchedName = tf_searchcharname.getText();
				String selectedName = lb_charname.getText();
				Character c;
				System.out.println(searchedName);
				if (!searchedName.equals("") || !selectedName.equals("<Character Name>")) {
					/* Delete from selected item of list view */
					if (searchedName.equals("")) {
						c = model.Search(selectedName);					
					} else {
						/* Delete from input name */
						c = model.Search(searchedName);
					}
					tf_searchcharname.setText("");
					/* Character not found */
					if (c == null) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("No result");
						alert.setContentText("No character found.");
						alert.showAndWait();
					} else {
						model.Remove(c);
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a name.");
					alert.showAndWait();
				} 
				ClearInfoPage();				
			}			
		});
	}
	
	/**
	 * Add the action of the update character info button.
	 */
	private void handleEditAction() {
		bt_savecharchange.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				String name = lb_charname.getText();
				String discription = tf_description.getText();
				String[] traits = ta_traits.getText().split("\n");
				String powerrank = tf_powerlevel.getText();
				String[] powers = ta_powers.getText().split("\n");
				String img = iv_img.getImage().impl_getUrl();
				String imgpath = img.replaceAll("file:" + this.getClass().getResource("/").getPath(), "");
				if (img.equals("")) {
					imgpath = "file:images/default.png";
				}
				tf_createcharname.setText("");
				if (name.equals("") || name.equals("<Character Name>")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setContentText("Please enter a name.");
					alert.showAndWait();
				} else {
					/* Create character */
					if (powerrank.equals("") && powers[0].equals("")) {
						Character c = new Character(name, discription, imgpath);
						for (String s: traits) {
							c.addTrait(s);
						}
						if (c.validateCharacter()) {
							model.Update(c);
						}					
					} else if(!powerrank.equals("") && !powers[0].equals("")) {
						/* Create super character */
						int power = Integer.parseInt(powerrank);
						
						try {
							SuperCharacter c = new SuperCharacter(name, discription, imgpath, power);
							for (String s: traits) {
								c.addTrait(s);							
							}
							for (String p: powers) {
								c.addPower(p);;
							}
							if (c.validateSuperCharacter()) {
								model.Update(c);
							}						
						} catch (IllegalPowerRankingException e) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("ERROR");
							alert.setContentText(e.toString());
							alert.showAndWait();
							e.printStackTrace();
						} 
					}
				}
			}			
		});
	}
	
	/**
	 * Add the action of the change image button.
	 */
	private void handleImageAction() {
		bt_changeimg.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Picture");
				fileChooser.setInitialDirectory(
					new File(this.getClass().getResource("/images").getPath()) // Set the initial directory
				);
				fileChooser.getExtensionFilters().addAll(
					/* Set file extensions*/
					new FileChooser.ExtensionFilter("PNG", "*.png"),
					new FileChooser.ExtensionFilter("JPG", "*.jpg")
		        );
				File file = fileChooser.showOpenDialog(null);					
				Image img = new Image(file.toURI().toString());				
				iv_img.setImage(img);
			}			
		});
	}
	
	/**
	 * Add the action of the clear button.
	 */
	private void handleClearAction() {
		bt_clearsearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tf_searchcharname.setText("");
				lb_charname.setText("<Character Name>");
				tf_description.setText("");
				ta_traits.setText("");
				Image img = new Image("images/default.png");
				iv_img.setImage(img);
				tf_powerlevel.setText("");
				ta_powers.setText("");
			}			
		});
	}
	
	/**
	 * Add the action of the list view select action.
	 */
	private void handleSelectAction() {
		lv_char.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				ClearInfoPage();
				String name = newValue;
				if (name != null && !name.equals("")) {
					Character character = model.Search(name);
					tf_searchcharname.setText("");
					Set<String> traits = character.getTraits();
					String trait = "";
					for(String s : traits) {
						trait = trait + s + System.getProperty("line.separator");
					}
					lb_charname.setText(character.getName());
					tf_description.setText(character.getDescription());
					ta_traits.setText(trait);
					Image img = new Image(character.getImagePath());
					iv_img.setImage(img);
					
					if (character instanceof SuperCharacter) {
						SuperCharacter sc = (SuperCharacter) character;
						String powerlevel = sc.getPowerRanking() + "";
						Set<String> powers = sc.getPowers();
						String power = "";
						for(String s : powers) {
							power = power + s + System.getProperty("line.separator");
						}
						tf_powerlevel.setText(powerlevel);
						ta_powers.setText(power);
						tf_powerlevel.setEditable(true);
						ta_powers.setEditable(true);
					} else {
						tf_powerlevel.setText("");
						ta_powers.setText("");
						tf_powerlevel.setEditable(false);
						ta_powers.setEditable(false);
					}
				}				
			}			
		});
	}
}
