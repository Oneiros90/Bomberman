package support;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class GameKeyListener extends KeyAdapter {

    private class HoldingKeyThread extends Thread {

        private final int refresh;

        public HoldingKeyThread(int milli) {
            this.refresh = milli;
        }

        @Override
        public void run() {
            try {
                do {
                    keyHeld(getHeldKey());
                    HoldingKeyThread.sleep(this.refresh);
                } while (!buffer.isEmpty());
            } catch (InterruptedException ex) {
            }
        }
    }
    private HoldingKeyThread thread;
    private LinkedList<Integer> buffer;

    public GameKeyListener(int milli) {
        this.thread = new HoldingKeyThread(milli);
        this.buffer = new LinkedList<>();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean bufferIsEmpty = this.buffer.isEmpty();
        int code = e.getKeyCode();
        if (!this.buffer.contains(code)) {
            this.buffer.add(code);
            if (bufferIsEmpty) {
                this.thread.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.buffer.remove((Integer) e.getKeyCode());
        if (this.buffer.isEmpty()) {
            this.thread.interrupt();
            this.thread = new HoldingKeyThread(this.thread.refresh);
        }
    }

    protected void keyHeld(int keyCode) {
    }

    public int getHeldKey() {
        if (this.buffer.isEmpty()) {
            return -1;
        }
        return this.buffer.getLast();
    }

    public boolean holdingOneKey() {
        return !this.buffer.isEmpty();
    }
}
