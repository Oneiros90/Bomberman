package bomberman;

import java.awt.Point;
import support.Image;

public class Arena extends Image {

    public enum State {

        FREE, BOMB, FIRE, WALL, OBSTACLE, BONUS;
    }

    public class Square {

        final State state;
        Image image;

        public Square() {
            this.state = State.FREE;
        }

        public Square(State s) {
            this.state = s;
        }

        public void setImage(Image pImage) {
            this.image = pImage;
        }

        public Image getImage() {
            return this.image;
        }
    }
    private static State[][] matrix;
    protected static int SQUARE_SIZE = 40;
    protected static int BOMBER_SIZE = 10;

    public Arena(int n) {
        super("stage" + n + ".png");
        this.setMatrix(n);
    }

    private void setMatrix(int n) {
        if (n == 1) {
            Arena.matrix = new State[19][15];
            for (int i = 0; i < Arena.matrix.length; i++) {
                for (int j = 0; j < Arena.matrix[0].length; j++) {
                    Arena.matrix[i][j] = State.WALL;
                }
            }
            for (int i = 1; i < Arena.matrix.length - 1; i++) {
                for (int j = 1; j < Arena.matrix[0].length - 1; j++) {
                    if (i % 2 == 1 || j % 2 == 1) {
                        matrix[i][j] = State.FREE;
                        /*Image image = new Image(new java.awt.Dimension(SQUARE_SIZE, SQUARE_SIZE),
                        java.awt.Color.red);
                        image.setVisible(true);
                        image.setLocation(i*SQUARE_SIZE, j*SQUARE_SIZE);
                        this.add(image);*/
                    }
                }
            }
        }
    }

    public void bomberMoves(Bomber b, int xOffset, int yOffset) {
        int x1 = b.getFeetLocation().x + (SQUARE_SIZE / 2) * xOffset;
        int y1 = b.getFeetLocation().y + (SQUARE_SIZE / 2) * yOffset;
        int x2 = x1, y2 = y1;
        if (xOffset == 0) {
            x1 -= BOMBER_SIZE;
            x2 += BOMBER_SIZE;
        } else {
            y1 -= BOMBER_SIZE;
            y2 += BOMBER_SIZE;
        }
        boolean obstacleOnTheLeft = Arena.matrix[x1 / SQUARE_SIZE][y1 / SQUARE_SIZE] == State.WALL;
        boolean obstacleOnTheRight = Arena.matrix[x2 / SQUARE_SIZE][y2 / SQUARE_SIZE] == State.WALL;

        Point actual = getSquareCoords(b);
        Point next = getSquareCoords(b.getNextStep(xOffset * BOMBER_SIZE, yOffset * BOMBER_SIZE));
        boolean changeSquare = !actual.equals(next);
        boolean isInFreeSquare = getSquare(actual) == State.FREE;
        boolean isGoingInBomb = getSquare(next) == State.BOMB;

        if (!obstacleOnTheLeft && !obstacleOnTheRight && !(isInFreeSquare && isGoingInBomb)
                && !(!isInFreeSquare && isGoingInBomb && changeSquare)) {
            b.moveOf(xOffset, yOffset);
        }
    }

    public void bomberPlaceBomb(Bomber b) {
        Point square = getSquareCoords(b);
        if (Arena.matrix[square.x][square.y] != State.BOMB && b.getBombs() > 0) {
            Arena.matrix[square.x][square.y] = State.BOMB;
            Bomber.Bomb bomb = b.placeBomb(square);
            this.add(bomb);
        }
    }
    

    //**************** Get Square Location ****************
    public static Point getSquareLocation(int x, int y) {
        return new Point(x * SQUARE_SIZE + SQUARE_SIZE / 2, y * SQUARE_SIZE + SQUARE_SIZE / 2);
    }

    public static Point getSquareLocation(Point p) {
        return Arena.getSquareLocation(p.x, p.y);
    }

    public static Point getSquareLocation(Bomber b) {
        return b.getFeetLocation();
    }
    
    
    //**************** Get Square Coords ****************
    public static Point getSquareCoords(int x, int y) {
        return new Point(x / SQUARE_SIZE, y / SQUARE_SIZE);
    }

    public static Point getSquareCoords(Point p) {
        return getSquareCoords(p.x, p.y);
    }

    public static Point getSquareCoords(Bomber b) {
        return getSquareCoords(b.getFeetLocation());
    }

    
    //****************** Get Square ******************
    public static State getSquare(int x, int y) {
        if (x < 0 || x >= Arena.matrix.length || y < 0 || y >= Arena.matrix[0].length){
            return State.WALL;
        }
        return Arena.matrix[x][y];
    }

    public static State getSquare(Point p) {
        return Arena.getSquare(p.x, p.y);
    }

    public static State getSquare(Bomber b) {
        return Arena.getSquare(Arena.getSquareCoords(b));
    }

    
    
    public static void setSquare(int x, int y, State s) {
        Arena.matrix[x][y] = s;
    }

    public static int getStageWidth() {
        return Arena.matrix[0].length;
    }

    public static int getStageHeight() {
        return Arena.matrix.length;
    }
}
