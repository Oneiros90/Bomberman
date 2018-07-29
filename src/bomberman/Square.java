package bomberman;

import support.Image;

public class Square {

    public enum State {

        FREE, BOMB, FIRE, WALL, OBSTACLE, BONUS;
    }
    protected static int SQUARE_SIZE = 40;
    private State state;
    private Image image;

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
