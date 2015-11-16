import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */
public class Controller_Main
{
    public Controller_Main(){}

    @FXML
    ListView<String> lv_menu;
    @FXML
    ListView<String> lv_group;
    @FXML
    ListView<String> lv_music;
    @FXML
    BorderPane bp;

    @FXML
    public void initialize()
    {
        DB_Manager.getInstance().init();//shutdown in main scene.onclose (can't init in main)
        lv_group.setVisible(false);
        lv_music.setTranslateX(-250.0);
        //connect listview to database?
        lv_menu.getItems().addAll("Music", "Artist", "Genre", "Playlist");
        lv_menu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("menu: " + newValue);
                lv_group.setVisible(true);
                lv_music.setTranslateX(0.0);
                //newValue = name of selected item
                if(newValue.compareTo("Music") == 0)
                {
                    lv_music.getItems().setAll(DB_Manager.getInstance().getAllMusic());
                    lv_group.setVisible(false);
                    lv_music.setTranslateX(-250.0);
                }
                else if(newValue.compareTo("Artist") == 0)
                {
                    lv_group.getItems().setAll(DB_Manager.getInstance().getArtists());
                    lv_music.getItems().clear();
                }
                else if(newValue.compareTo("Genre") == 0)
                {
                    lv_group.getItems().setAll(DB_Manager.getInstance().getGenres());
                    lv_music.getItems().clear();
                }
                else if(newValue.compareTo("Playlist") == 0)
                {
                    List<String> ls = DB_Manager.getInstance().getPlaylists();
                    if(ls.isEmpty())
                    {
                        lv_group.getItems().setAll("<Empty>");
                    }
                    else
                    {
                        lv_group.getItems().setAll(ls);
                    }

                    lv_music.getItems().clear();
                }
            }
        });

        lv_group.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if(newValue!= null)
                {
                    System.out.println("group: " + newValue);
                    String curGroup = lv_menu.getSelectionModel().getSelectedItem();
                    if(curGroup.compareTo("Artist") == 0)
                    {
                        lv_music.getItems().setAll(DB_Manager.getInstance().getArtist(newValue));
                    }
                    else if(curGroup.compareTo("Genre") == 0)
                    {

                        lv_music.getItems().setAll(DB_Manager.getInstance().getGenre(newValue));
                    }
                    else if(curGroup.compareTo("Playlist") == 0)
                    {
                        lv_music.getItems().setAll(DB_Manager.getInstance().getPlaylist(newValue));
                    }
                }

            }
        });
        lv_music.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("music: " + newValue);
            }
        });

    }

    public void onScanButton()
    {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("scan_ui.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e)
        {
            System.out.println("Caught Exception in onScourButton*********************");
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.showAndWait();

    };
    public void refresh()
    {
        lv_menu.getSelectionModel().select(0);
    }
}//end Controller.java



