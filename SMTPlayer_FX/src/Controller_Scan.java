
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */

public class Controller_Scan
{
    @FXML    CheckBox cb_wav, cb_mp3;
    @FXML    TextField dir_text;
    ArrayList<String> exts;
    public ArrayList<MediaFile> retrievedFileNames;
    @FXML CheckBox cb_subdir;

    Scene browseScene, createScene;//todo for back button support?

    public void onBrowseButton() throws IOException
    {
        //open scour window
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(SMTPlayer_Main.getPrimaryStage());
        if (selectedDirectory != null)
        {
            dir_text.setText(selectedDirectory.toString());
        }

    }
    public void onScanButton()//currently in browseScene
    {
        System.out.println("dirText: " + dir_text.getText());
       // retrievedFileNames = new ArrayList<String>();

        if(dir_text.getText().isEmpty() || dir_text == null)
        {
            //dir_text.getScene().getWindow().hide();
            return;
        }
        File directory = new File(dir_text.getText());
        if(!directory.isDirectory())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Directory Path ", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        exts = new ArrayList<String>();
        if( cb_wav.isSelected()) exts.add("wav");
        if( cb_mp3.isSelected()) exts.add("mp3");
        retrievedFileNames = Utils.getMediaFiles(dir_text.getText(), cb_subdir.isSelected(), exts);


        browseScene = dir_text.getScene();
        Stage stage = (Stage)browseScene.getWindow();
        //open creation window
        if(createScene == null)
        {
            System.out.println("createScene from onScanButton");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("results_ui.fxml"));
            Controller_Results c = new Controller_Results();
            c.sc = this;

            fxmlLoader.setController(c);
            Parent root1 = null;
            try
            {
                root1 = (Parent) fxmlLoader.load();
                //browse_table = (TableView) root1.lookup("#browse_table");
            } catch (IOException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            createScene = new Scene(root1);
            stage.setScene(createScene);
            //stage.showAndWait();
        }
        else
        {
            stage.setScene(createScene);
        }

    }


}
