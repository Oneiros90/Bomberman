package bomberman;

import java.awt.Dimension;
import java.awt.Point;
import support.*;

public class Bomber extends Sprite {

    public class Bomb extends Sprite {

        private Point square;

        public Bomb(Point p) {
            super("bomb.png", 2, new Dimension(32, 34));
            this.square = p;
            this.setLocation(Arena.getSquareLocation(p));
        }

        @Override
        protected void animationEnding(int animation) {
            this.setVisible(false);
            Arena.setSquare(square.x, square.y, Arena.State.FREE);
            for (int i = 1; i <= fire; i++) {
                if (Arena.getSquare(square.x, square.y + i) == Arena.State.FREE && square.y + i < Arena.getStageHeight()) {
                    Arena.setSquare(square.x, square.y + i, Arena.State.FIRE);
                } else if (Arena.getSquare(square.x, square.y + i) == Arena.State.BOMB) {
                }
            }
            bombs++;
        }

        @Override
        public final void setLocation(Point p) {
            super.setLocation(p.x - this.getWidth() / 2, p.y - this.getHeight() / 2);
        }
    }
    public static final int STEP_LENGTH = 2;
    private static final int bWIDTH = 40;
    private static final int bHEIGHT = 62;
    private int bombs;
    private int fire;

    public Bomber() {
    }

    public Bomber(String pName) {
        super(pName, 2, new Dimension(bWIDTH, bHEIGHT));
        this.bombs = 12;
        this.fire = 2;
    }

    public Bomber.Bomb placeBomb(Point bombLocation) {
        this.bombs--;
        Bomber.Bomb bomb = new Bomber.Bomb(bombLocation);
        bomb.startAnimation(0, 4, 250);
        return bomb;
    }

    public int getBombs() {
        return this.bombs;
    }

    @Override
    public void moveOf(int xOffset, int yOffset) {
        super.moveOf(xOffset * STEP_LENGTH, yOffset * STEP_LENGTH);
    }

    public Point getNextStep(int xOffset, int yOffset) {
        Point f = this.getFeetLocation();
        return new Point(f.x + xOffset * STEP_LENGTH, f.y + yOffset * STEP_LENGTH);
    }

    public void setFeetLocation(int x, int y) {
        super.setLocation(x - bWIDTH / 2, y - (bHEIGHT - 8));
    }

    public void setFeetLocation(Point p) {
        this.setFeetLocation(p.x, p.y);
    }

    public Point getFeetLocation() {
        return new Point(this.getX() + bWIDTH / 2, this.getY() + bHEIGHT - 14);
    }
}
