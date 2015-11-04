import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Turtle on 11/2/2015.
 */
public class SMTPlayer_Main extends Application
{
    static Stage primaryStage;
    public static void main(String[] args)
    {
        launch(args);//goes into Application.java creates necessities then calls this.start()
    }
    @Override
    public void start(Stage ps) throws Exception
    {
        //all our code goes here
        primaryStage = ps;
        //System.out.printf("First!" + searchField.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_ui.fxml"));

        Parent root = (Parent) fxmlLoader.load();
        Controller_Main c = new Controller_Main();
        DB_Manager.getInstance().mainController = fxmlLoader.getController();

        ps.setTitle("Media Player");
        ps.setScene(new Scene(root, 800, 800));
        ps.show();

        ps.getScene().getWindow().setOnCloseRequest( e ->
        {
            DB_Manager.getInstance().shutdown();
        });
    }
}
