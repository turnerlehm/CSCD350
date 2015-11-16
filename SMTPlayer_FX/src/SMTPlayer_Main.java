import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Sam Arutyunyan on 11/2/2015.
 */
public class SMTPlayer_Main extends Application
{
    static Stage primaryStage;
    static Controller_Main mainController;
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
        mainController = fxmlLoader.getController();

        ps.setTitle("Media Player");
        ps.setScene(new Scene(root, 800, 400));
        ps.show();

        ps.getScene().getWindow().setOnCloseRequest( e ->
        {
            DB_Manager.getInstance().shutdown();
        });
    }
    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }
    public static Controller_Main getMainController()
    {
        return mainController;
    }
}
