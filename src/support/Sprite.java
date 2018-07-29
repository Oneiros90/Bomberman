package support;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class Sprite extends Image {

    private Image[][] frames;
    private Thread animationThread;
    private int currentAnimation;

    public Sprite() {
    }

    public Sprite(String pName, int pBorder, Dimension frameSize) {
        this.setImage(pName, pBorder, frameSize);
        this.animationThread = new Thread();
        this.currentAnimation = -1;
    }

    public final void setImage(String pName, int pBorder, Dimension frameSize) {
        Image sprite = new Image(pName);
        int x = (sprite.getHeight() - pBorder) / (frameSize.height + pBorder);
        int y = (sprite.getWidth() - pBorder) / (frameSize.width + pBorder);
        this.frames = new Image[x][y];
        for (int i = 0; i < this.frames.length; i++) {
            for (int j = 0; j < this.frames[0].length; j++) {
                x = (j + 1) * pBorder + j * frameSize.width;
                y = (i + 1) * pBorder + i * frameSize.height;
                CropImageFilter cif = new CropImageFilter(x, y, frameSize.width, frameSize.height);
                this.frames[i][j] = new Image(createImage(new FilteredImageSource(sprite.getImage().getSource(), cif)));
            }
        }
        this.setImage(this.frames[0][2].getImage());
        this.setOpaque(false);
    }

    public void startAnimation(final int animation, final int sleep) {
        this.startAnimation(animation, -1, sleep, 0, this.frames[animation].length);
    }

    public void startAnimation(final int animation, final int times, final int sleep) {
        this.startAnimation(animation, times, sleep, 0, this.frames[animation].length);
    }

    public void startAnimation(final int animation, final int times, final int sleep, final int from, final int to) {
        if (animation != this.currentAnimation) {
            if (this.animationThread.isAlive()) {
                this.animationThread.interrupt();
            }
            this.currentAnimation = animation;
            this.animationThread = new Thread() {

                @Override
                public void run() {
                    try {
                        if (times < 0) {
                            while (true) {
                                animation();
                            }
                        } else {
                            int counter = 0;
                            while (counter < times) {
                                animation();
                                counter++;
                            }
                        }
                    } catch (InterruptedException ex) {
                    } finally {
                        animationEnding(animation);
                    }
                }

                private void animation() throws InterruptedException {
                    for (int i = from; i < to; i++) {
                        setImage(frames[animation][i].getImage());
                        sleep(sleep);
                    }
                }
            };
            this.animationThread.start();
        }
    }

    public void setFrame(int animation, int frame) {
        if (this.animationThread.isAlive()) {
            this.animationThread.interrupt();
            this.currentAnimation = -1;
        }
        this.setImage(this.frames[animation][frame].getImage());
    }

    protected void animationEnding(int animation) {
    }

    public void moveOf(int x, int y) {
        this.setLocation(this.getX() + x, this.getY() + y);
    }

    public void moveOf(Point p) {
        this.setLocation(this.getX() + p.x, this.getY() + p.y);
    }

    public int getCurrentAnimation() {
        return this.currentAnimation;
    }
}
