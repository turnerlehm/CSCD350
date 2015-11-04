import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

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
    public void initialize()
    {
        DB_Manager.getInstance().init();//shutdown in main scene.onclose (can't init in main)

        //connect listview to database?
        lv_menu.getItems().addAll("Music", "Artist", "Genre", "Playlist");
        lv_menu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("menu: " + newValue);
                //newValue = name of selected item
                if(newValue.compareTo("Music") == 0)
                {
                    lv_group.getItems().setAll(DB_Manager.getInstance().getAllMusic());
                }
                else if(newValue.compareTo("Artist") == 0)
                {
                    lv_group.getItems().setAll(DB_Manager.getInstance().getArtists());
                }
                else if(newValue.compareTo("Genre") == 0)
                {
                    lv_group.getItems().setAll(DB_Manager.getInstance().getGenres());
                }
                else if(newValue.compareTo("Playlist") == 0)
                {
                    lv_group.getItems().setAll(DB_Manager.getInstance().getPlaylists());
                }
            }
        });

        lv_group.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println("group: " + newValue);
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
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Scour_UI.fxml"));
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
        */
    };

}//end Controller.java



