package eu.javaspecialists.books.dynamicproxies.ch01;

import java.awt.event.KeyEvent;
import java.util.EventListener;

public class Listing1_0 {
    // tag::listing[]
    public interface KeyListener extends EventListener {
        public void keyTyped(KeyEvent e);
        public void keyPressed(KeyEvent e);
        public void keyReleased(KeyEvent e);
    }
    public abstract class KeyAdapter implements KeyListener {
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
    }
    // end::listing[]
}
