import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.jogamp.opengl.awt.GLCanvas;

public class Main implements Runnable, KeyListener {
    private static Thread displayT = new Thread(new Main());
    private static float ROTATION_SPEED = 3f;
    private static float ZOOM_SPEED = 1.05f;
    private static boolean bQuit = false;
    public static float rotationX = 0f;
    public static float rotationY = 0f;
    public static float rotationZ = 0f;
    public static float zoomChange = 1.0f;
    public static boolean wireFrame = false;

    public static void main(String[] args) {
        displayT.start();
    }

    public void run() {
        Frame frame = new Frame("Jogl 3D Shape/Rotation");
        frame.setLocation(0,0);
        GLCanvas canvas = new GLCanvas();
        int size = frame.getExtendedState();

        canvas.addGLEventListener(new JavaRenderer());
        frame.add(canvas);
        frame.setUndecorated(true);
        size |= Frame.MAXIMIZED_BOTH;
        frame.setExtendedState(size);
        canvas.addKeyListener(this);
        frame.pack();
        //frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                bQuit = true;
                System.exit(0);
            }
        });

        frame.setVisible(true);
        canvas.requestFocus();
        while( !bQuit ) {
            canvas.display();
        }
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            displayT = null;
            bQuit = true;
            System.exit(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_W) {
            rotationX = ROTATION_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            rotationX = -ROTATION_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_A) {
            rotationY = ROTATION_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            rotationY = -ROTATION_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_Q) {
            rotationZ = ROTATION_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_E) {
            rotationZ = -ROTATION_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_MINUS) {
            zoomChange = 1/ZOOM_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_EQUALS) {
            zoomChange = ZOOM_SPEED;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            wireFrame = !wireFrame;
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) {
            rotationX = 0f;
        }
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
            rotationY = 0f;
        }
        if(e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_E) {
            rotationZ = 0f;
        }
        if(e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            zoomChange = 1.0f;
        }
    }

    public void keyTyped(KeyEvent e) {
    }
}
/*
//import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class Main {
    public static void main(String args[]) {
        // Точка входа.
        JFrame frame = new JFrame("MyWindow");
        // Создаем окно.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Выход по умолчанию.
        frame.add(new GLCanvas());
        // Добавляем GLCanvas на окно.

        frame.setSize(800, 600);
        // Устанавливаем размер окна.
        frame.setVisible(true);
        // Собираем и делаем видимым окно.
    }
}
*/
/*public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
    }
}*/
