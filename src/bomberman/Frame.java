package bomberman;

import support.*;
import java.awt.event.KeyEvent;

public class Frame extends SuperFrame {

    public Frame() {
        this.setResizable(false);

        final Arena stage1 = new Arena(1);
        this.add(stage1);
        this.setSizeWithoutInsets(stage1.getSize());
        this.centerFrame();

        final Bomber bomber = new Bomber("whiteBomber.png");
        bomber.setFrame(0, 2);
        bomber.setVisible(true);
        bomber.setFeetLocation(Arena.getSquareLocation(1, 1));
        stage1.add(bomber);

        this.addKeyListener(new GameKeyListener(10) {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                this.animation();
                this.placeBomb(e);
            }

            @Override
            protected void keyHeld(int keyCode) {
                this.move();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (!this.holdingOneKey()) {
                    this.stand(e);
                } else {
                    this.animation();
                }
            }

            private void animation() {
                switch (getHeldKey()) {
                    case KeyEvent.VK_UP:
                        bomber.startAnimation(2, 100);
                        break;
                    case KeyEvent.VK_DOWN:
                        bomber.startAnimation(0, 100);
                        break;
                    case KeyEvent.VK_RIGHT:
                        bomber.startAnimation(1, 100);
                        break;
                    case KeyEvent.VK_LEFT:
                        bomber.startAnimation(3, 100);
                        break;
                }
            }

            private void move() {
                switch (getHeldKey()) {
                    case KeyEvent.VK_UP:
                        stage1.bomberMoves(bomber, 0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                        stage1.bomberMoves(bomber, 0, 1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        stage1.bomberMoves(bomber, 1, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        stage1.bomberMoves(bomber, -1, 0);
                        break;
                }

            }

            private void stand(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        bomber.setFrame(2, 2);
                        break;
                    case KeyEvent.VK_DOWN:
                        bomber.setFrame(0, 2);
                        break;
                    case KeyEvent.VK_RIGHT:
                        bomber.setFrame(1, 2);
                        break;
                    case KeyEvent.VK_LEFT:
                        bomber.setFrame(3, 2);
                        break;
                }
            }

            private void placeBomb(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    stage1.bomberPlaceBomb(bomber);
                }
            }
        });
    }

    public static void main(String[] args) {
        new Frame().setVisible(true);
    }
}
