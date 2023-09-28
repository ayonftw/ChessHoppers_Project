package puzzles.hoppers.gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.hoppers.model.HoppersConfig;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import static javafx.geometry.Pos.CENTER;

/**
 * Aniruddha Roy
 * ar7475
 */

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image redFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "red_frog.png")));
    private Image greenFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "green_frog.png")));
    private Image lilyPad = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "lily_pad.png")));
    private Image water = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "water.png")));
    private Stage stage;
    private HoppersModel model;
    private HoppersConfig currentConfig;
    private static GridPane gridPane;
    private BorderPane borderPane;
    private HBox hBox;
    private Label topBox;



    /**

     Initializes the Hoppers game by loading the specified file as the model and configuration,
     and adding the current instance as an observer to the model. Also updates the topBox label to
     display the name of the loaded file.
     @throws IOException if there is an error reading the file
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        this.currentConfig = new HoppersConfig(filename);
        model.addObserver(this);

        this.topBox = new Label("Loaded file: " + filename);
    }

    /**
     The start method initializes the Hoppers GUI with a stage and sets the title to "Hoppers GUI".
     It creates and initializes several UI elements, such as a top box, an HBox, a BorderPane, and a GridPane.
     It adds these elements to the BorderPane and sets their alignments to CENTER.
     It also creates a bottom row of UI elements and adds it to the BorderPane.
     Finally, it creates a Scene with the BorderPane as its root node, sets the Scene to the stage, and shows the stage.
     @param stage a Stage object to initialize the Hoppers GUI with
     @throws Exception if there is an error during initialization
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setTitle("Hoppers GUI");



        this.hBox = new HBox();
        this.borderPane = new BorderPane();
        gridPane = new GridPane();

        //top
        topBox.setFont(Font.font("SansSerif", FontWeight.BOLD, FONT_SIZE));


        this.hBox.getChildren().addAll(topBox);
        this.borderPane.setTop(hBox);
        hBox.setAlignment(CENTER);


        //center
        this.borderPane.setCenter(makeGrid());

        //bottom
        HBox ayon;
        ayon = bottomRow();
        this.borderPane.setBottom(ayon);
        ayon.setAlignment(CENTER);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }

    /**
     Creates a Button object with an icon representing the status of a frog or lily pad on the game board.
     @param row The row number of the cell to which the button corresponds.
     @param col The column number of the cell to which the button corresponds.
     @param status The status of the cell, either "L" for lily pad, "G" for green frog, "R" for red frog, or "W" for water.
     @return A Button object representing the cell on the game board.
     */
    public Button makeFrogButton(int row, int col, String status){
        Button b = new Button();
        b.setMinSize(ICON_SIZE, ICON_SIZE);
        b.setMaxSize(ICON_SIZE, ICON_SIZE);

        if (Objects.equals(status, HoppersConfig.LILLY_PAD)){
            b.setGraphic(new ImageView(lilyPad));
        } else if (Objects.equals(status, HoppersConfig.GREEN_FROG)){
            b.setGraphic(new ImageView(greenFrog));
        } else if (Objects.equals(status, HoppersConfig.RED_FROG)){
            b.setGraphic(new ImageView(redFrog));
        } else {
            b.setGraphic(new ImageView(water));
        }
        // run the select() method when a button is clicked
        b.setOnAction(actionEvent -> model.selectCell(row, col));
        return b;
    }

    /**
     * Opens a file chooser dialog to select a hoppers puzzle file to load,
     * and passes the selected file path to the model's loadPuzzle method.
     * Uses the current working directory and data/hoppers subdirectory as
     * the initial directory for the file chooser.
     */
    private void load(){
        FileChooser chooser = new FileChooser();
        String absolutePath = Paths.get(".").toAbsolutePath().normalize().toString();
        String currentPath = absolutePath + File.separator + "data" + File.separator + "hoppers";
        chooser.setInitialDirectory(new File(currentPath));

        File file = chooser.showOpenDialog(stage);
        String ayon = String.valueOf(file).replace(absolutePath + File.separator, "");
        chooser.setInitialFileName(ayon);

        this.model.loadPuzzle(chooser.getInitialFileName());
    }

    /**
     * Creates a GridPane with buttons representing each cell of the current Hoppers game configuration.
     * @return GridPane representing the current Hoppers game configuration
     */
    public GridPane makeGrid(){
        GridPane grid = new GridPane();

        for (int r = 0; r < model.currConfig().getNumRows(); r++){
            for (int c = 0; c < model.currConfig().getNumCols(); c++){
                Button b = new Button();
                int finalR = r;
                int finalC = c;


                if(model.currConfig().getGrid()[r][c].equals("G")){
                    b = makeFrogButton(r, c, model.currConfig().getGrid()[r][c]);
                    grid.add(b, c, r);
                }
                if(model.currConfig().getGrid()[r][c].equals("R")){
                    b = makeFrogButton(r, c, model.currConfig().getGrid()[r][c]);
                    grid.add(b, c, r);
                }
                if(model.currConfig().getGrid()[r][c].equals("*")){
                    b = makeFrogButton(r, c, model.currConfig().getGrid()[r][c]);
                    grid.add(b, c, r);
                }
                if(model.currConfig().getGrid()[r][c].equals(".")){
                    b = makeFrogButton(r, c, model.currConfig().getGrid()[r][c]);
                    grid.add(b, c, r);
                }
                b.setOnAction(event -> model.selectCell(finalR, finalC));
                b.setMinSize(ICON_SIZE, ICON_SIZE);
                b.setMaxSize(ICON_SIZE, ICON_SIZE);
            }

        }
        return grid;
    }


    /**
     Creates a horizontal box containing buttons to load a puzzle, reset the puzzle, and get a hint for the puzzle.
     @return a HBox containing the load, reset, and hint buttons.
     */
    public HBox bottomRow(){
        HBox bottomBox = new HBox();
        Button LOAD = new Button("Load");
        LOAD.setFont(Font.font("SansSerif", FontWeight.BOLD, FONT_SIZE));
        LOAD.setOnAction(event -> load());

        Button RESET = new Button("Reset");
        RESET.setFont(Font.font("SansSerif", FontWeight.BOLD, FONT_SIZE));
        RESET.setOnAction(event -> model.reset());

        Button HINT = new Button("Hint");
        HINT.setFont(Font.font("SansSerif", FontWeight.BOLD, FONT_SIZE));
        HINT.setOnAction(event -> model.hint());

        bottomBox.getChildren().addAll(LOAD, RESET,HINT);
        return bottomBox;
    }

    /**
     Updates the GUI with the latest state of the HoppersModel. This method is called by the HoppersModel whenever
     there is a change in the model's state.
     @param model the HoppersModel being observed
     @param message a message string describing the change in the model's state
     */
    @Override
    public void update(HoppersModel model, String message) {
        GridPane newGrid;
        topBox.setText(message);
        topBox.setFont(Font.font("SansSerif", FontWeight.BOLD, FONT_SIZE));

        newGrid = makeGrid();
        borderPane.setCenter(newGrid);
        stage.sizeToScene();
    }


    /**
     * Main method of the Hoppers GUI application. It launches the JavaFX Application
     * if one argument is provided, which is the filename of the Hoppers puzzle to be loaded.
     * Otherwise, it prints a usage message to the console.
     *
     * @param args an array of command-line arguments; it should have length 1, which is the
     *             filename of the Hoppers puzzle to be loaded
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
