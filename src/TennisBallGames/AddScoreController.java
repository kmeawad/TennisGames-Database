package TennisBallGames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class AddScoreController implements Initializable {
    // Some @FXML declarations
    @FXML
    Button cancelBtn;
    @FXML
    Button saveBtn;
    @FXML
    ComboBox matchBox;
    @FXML
    TextField homeTeamField;
    @FXML
    TextField visitorTeamField;


    // The data variable is used to populate the ComboBoxs
    final ObservableList<String> data = FXCollections.observableArrayList();
    // To reference the models inside the controller
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;
    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }

    public void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
    }

    public void save(){


        try {
            //save the match info in the matchInfo array
            String[] matchInfo = matchBox.getValue().toString().split("-");
            //get home team and visitor team from the matchInfo array
            String homeTeam = matchInfo[1].trim();
            String visitorTeam = matchInfo[2].trim();

            //Update team score
            matchesAdapter.setTeamsScore(Integer.parseInt(matchBox.getValue().toString().substring(0,1)), Integer.parseInt(homeTeamField.getText()), Integer.parseInt(visitorTeamField.getText()));

            //Update Status of the teams
            teamsAdapter.setStatus(homeTeam,visitorTeam,Integer.parseInt(homeTeamField.getText()), Integer.parseInt(visitorTeamField.getText()));

        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }

        //close window
        cancel();
    }


    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matchBox.setItems(data);

    }


    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/TennisBallGames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

}
