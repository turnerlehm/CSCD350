import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */
import java.util.ArrayList;

public class Controller_Results
{
    @FXML
    TableView<MediaFile> browse_table;
    @FXML
    TableColumn<MediaFile, String> c1, c2, c3;
    @FXML
    ChoiceBox chb_choiceBox;
    @FXML
    TextField db_name;
    @FXML TextField tf_createNew;

    public Controller_Scan sc;//ref to previous window controller
    String newPlaylist = "";

    ObservableList<MediaFile> tableItems;
    @FXML
    public void initialize()
    {
        //load in temp table data
        //System.out.println("dastext: " + sc.dir_text.getText());

        if(sc.dir_text.getText().isEmpty() != true)
        {

            tableItems =  FXCollections.<MediaFile>observableArrayList();
            for (MediaFile mf : sc.retrievedFileNames)
            {
                //System.out.println(s);
                tableItems.add(mf);
            }
            c1.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("filename"));
            c2.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("ext"));
            c3.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("directory"));
            browse_table.setItems(tableItems);
            //browse_table.setItems(generateDummyData());

            browse_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            ObservableList<String> ol = FXCollections.observableArrayList(DB_Manager.getInstance().getPlaylists());
            ol.add(0, "Default");
            chb_choiceBox.setItems(ol);
            chb_choiceBox.getSelectionModel().selectFirst();

            //todo THIS fucking string data crap doesnt work T_T
            //todo inefficient data passing: first i have a string from getFileNames, turn that into dummydata list, then turn in to node list, pass list to db_manager
            /*
            ObservableList<String> oFileNames = FXCollections.observableArrayList(retrievedFileNames);
            browse_table.setItems(oFileNames);
            c1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>()
            {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(0));
                }
            });
            */
        }

    }

    public void onCreateButton()
    {
        //create new playlist name, call add with that name
        //make sure tableview is not empty
        if(browse_table.getItems().isEmpty())
        {
            //=(
        }
        else
        {
            if(tf_createNew.getText().isEmpty())
            {
                System.out.println("=(");
            }
            else
            {
                DB_Manager.getInstance().createPlaylist(tf_createNew.getText());
                newPlaylist = tf_createNew.getText();
                onAddButton();
            }
        }
    }
    public void onAddButton()//add to existing
    {
        if(newPlaylist == "")
        {
            newPlaylist = (String)chb_choiceBox.getItems().get(chb_choiceBox.getSelectionModel().getSelectedIndex());
        }
        ObservableList<MediaFile> selectedItems =  browse_table.getSelectionModel().getSelectedItems();

        //call database(db_name, nodelist)
        ObservableList<MediaFile> finalData = FXCollections.<MediaFile>observableArrayList();
        if(newPlaylist == "Default")
        {
            for(MediaFile media : selectedItems)
            {
                DB_Manager.getInstance().addMedia(media);
            }
        }
        else
        {
            for(MediaFile media : selectedItems)
            {
                int mId = DB_Manager.getInstance().addMedia(media);
                DB_Manager.getInstance().addToPlaylist(newPlaylist, mId);
            }
        }



        SMTPlayer_Main.getMainController().refresh();
        //close create window
        Stage createStage = (Stage) browse_table.getScene().getWindow();
        createStage.close();
    }

    public void onCancelButton()
    {
        Stage createStage = (Stage) browse_table.getScene().getWindow();
        createStage.close();
    }
    private ObservableList<MediaFile> generateDummyData()
    {
        ObservableList<MediaFile> nodes = FXCollections.<MediaFile>observableArrayList();
        MediaFile n1 = new MediaFile("fur elise", "wmv", "C://");
        MediaFile n2 = new MediaFile("Strobe", "wmv", "C://");
        MediaFile n3 = new MediaFile("lalalala", "wmv", "C://");

        nodes.addAll(n1, n2, n3);
        return nodes;
    }
}
