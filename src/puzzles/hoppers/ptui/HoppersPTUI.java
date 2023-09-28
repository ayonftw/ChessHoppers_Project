package puzzles.hoppers.ptui;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.chess.ptui.ChessPTUI;

import java.io.IOException;
import java.util.Scanner;
/**
 * Aniruddha Roy
 * ar7475
 */

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;

    /**
     * Initializes this PTUI by creating a new HoppersModel, adding it as an observer,
     * and loading a puzzle file. Displays the help message to the console.
     * @param filename the filename of the puzzle file to load
     * @throws IOException if there is an error loading the puzzle file
     */
    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.model.loadPuzzle(filename);
        displayHelp();
    }

    /**
     * Called when the underlying HoppersModel changes state.
     * Displays the updated model and any associated data to the console.
     * @param model the HoppersModel that has changed
     * @param data any associated data with the change
     */
    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * Displays a set of available commands that the user can enter.
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * Reads input from the user and executes the corresponding command.
     * The commands are parsed from the input string using a regular expression.
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith("h")) {
                    this.model.hint();
                    System.out.println(model.ptuiToString());

                }
                else if (words[0].startsWith("l")) {
                    this.model.loadPuzzle(words[1]);
                    System.out.println(model.ptuiToString());

                }
                else if (words[0].startsWith("s")) {
                    this.model.selectCell(Integer.parseInt(words[1]),Integer.parseInt(words[2]));
                    System.out.println(model.ptuiToString());

                }
                else if (words[0].startsWith("r")) {
                    this.model.reset();
                    System.out.println(model.ptuiToString());

                }
                else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * The main entry point for the HoppersPTUI application.
     * Takes a filename as a command line argument and initializes an instance
     * of this class to run the game.
     * @param args an array of command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}