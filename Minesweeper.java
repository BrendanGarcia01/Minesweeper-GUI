import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Minesweeper extends Application
{
    public void start(Stage primaryStage)
    {
        //create a Minefield object.
        Minefield gui = new Minefield();

        //Put gui on top of the rootPane
        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(gui);

        //Create a scene and place rootPane in the stage
        Scene scene = new Scene(rootPane, 400, 550);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
