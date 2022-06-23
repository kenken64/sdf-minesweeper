package myapp.minesweeper;

/**
 * Minesweeper program
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Minesweeper game = null;
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        System.out.printf("Row > %s \n", rows );
        System.out.printf("Cols > %s \n", cols );
        
        game = new Minesweeper(rows, cols);
        game.run();
    }
}
