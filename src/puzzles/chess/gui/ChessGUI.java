package puzzles.chess.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author gonzaloestrella
 */

public class ChessGUI extends Application implements Observer<ChessModel, String> {
    private ChessModel model;
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;
    private Stage stage;
    private Button board[][];
    private BorderPane borderPane;
    private Label label;
    private GridPane gridPane;
    private Button hint;
    private Button load;
    private Button reset;
    private String fileName;
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"bishop.png"));
    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"king.png"));
    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"knight.png"));
    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"pawn.png"));
    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"queen.png"));
    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"rook.png"));
    /** a definition of light and dark and for the button backgrounds */
    private static final Background LIGHT =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background( new BackgroundFill(Color.MIDNIGHTBLUE, null, null));

    /**
     * initializes the GUI
     * @throws IOException
     */
    @Override
    public void init() throws IOException {
        // get the file name from the command line
        String filename = getParameters().getRaw().get(0);
        model = new ChessModel(filename);
        model.addObserver(this);
        board = new Button[model.getRows()][model.getCol()];
        label = new Label("Loaded: "+filename);
        borderPane = new BorderPane();
        gridPane = new GridPane();
        this.fileName = filename;
    }

    /**
     * start the application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        gridPane = generateGrid();
        borderPane.setCenter(gridPane);
        borderPane.setTop(label);
        Region space1 = new Region();
        Region space2 = new Region();
        HBox.setHgrow(space1, Priority.ALWAYS);
        HBox.setHgrow(space2, Priority.ALWAYS);
        load = new Button("Load");
        reset = new Button("Reset");
        hint = new Button("Hint");
        hint.setOnAction(event -> model.hint());
        load.setOnAction(event -> {
            try {
                model.load(getFile());
            } catch (IOException e) {
                update(this.model, "Failed to load: "+ fileName);
            }
        });
        reset.setOnAction(event -> model.reset());
        HBox actionButtons = new HBox();
        actionButtons.getChildren().addAll(space1, load, reset, hint, space2);
        borderPane.setBottom(actionButtons);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("Chess GUI");
        stage.show();
    }

    /**
     * opens the Directory and Chooses a file
     * @return String
     */
    public String getFile(){
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "chess";  // or "hoppers"
        chooser.setInitialDirectory(new File(currentPath));
        File file = chooser.showOpenDialog(stage);
        String fileString = String.valueOf(file).replace(currentPath + File.separator, "");
        fileName = fileString;
        return fileString;
    }

    /**
     * generates the board
     * @return GridPane
     */
    public GridPane generateGrid(){
        GridPane gridPane = new GridPane();
        for(int row = 0; row < model.getRows(); row++){
            for(int col=0; col < model.getCol(); col++){
                int finalRow = row;
                int finalCol = col;
                Button button = new Button();
                if((row + col) % 2 != 0 ){
                    button.setBackground(DARK);
                }else{
                    button.setBackground(LIGHT);
                }
                if(model.getCurrentConfig().getInitialConfiguration()[row][col] == 'B'){
                    button.setGraphic(new ImageView(bishop));
                }else if (model.getCurrentConfig().getInitialConfiguration()[row][col] == 'K') {
                    button.setGraphic(new ImageView(king));
                }else if (model.getCurrentConfig().getInitialConfiguration()[row][col] == 'N') {
                    button.setGraphic(new ImageView(knight));
                }else if (model.getCurrentConfig().getInitialConfiguration()[row][col] == 'P') {
                    button.setGraphic(new ImageView(pawn));
                }else if (model.getCurrentConfig().getInitialConfiguration()[row][col] == 'Q') {
                    button.setGraphic(new ImageView(queen));
                }else if (model.getCurrentConfig().getInitialConfiguration()[row][col] == 'R') {
                    button.setGraphic(new ImageView(rook));
                }
                board[row][col] = button;
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                button.setOnAction(event -> model.select(finalRow, finalCol));
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * updates the model
     * @param chessModel the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(ChessModel chessModel, String msg) {
        new GridPane();
        GridPane newPane;
        newPane = generateGrid();
        label.setText(msg);
        borderPane.setCenter(newPane);
        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
