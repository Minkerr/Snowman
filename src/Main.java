import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.jogamp.opengl.awt.GLCanvas;

import javax.imageio.ImageIO;

class Camera
{

    public Vector3 pos = new Vector3(0, 0, 0);
    public Vector3 dir = new Vector3(0, -1, 0);
    public Vector3 up  = new Vector3(0, 0, 1);

    public void move(double speed){
        pos = Vector3.VplusV(pos, Vector3.VxR(dir, speed));
        return;
    }

    public void pitch(double a){ //DON'T WORK/ NADO CHINIT'
        Vector3 pov = Vector3.VxV(dir, up);
        dir = Matrix.MxV(Matrix.MRot(pov, a), dir);
        up = Matrix.MxV(Matrix.MRot(pov, a), up);
        return;
    }

    public void roll(double a){ //DON'T WORK/ NADO CHINIT'
        dir = Matrix.MxV(Matrix.MRot(up, a), dir);
        return;
    }

    public void yaw(double a){
        up = Matrix.MxV(Matrix.MRot(dir, a), up);
    }
}

public class Main implements Runnable, KeyListener {
    private static Thread displayT = new Thread(new Main());
    private static float ROTATION_SPEED = 3f;
    private static float ZOOM_SPEED = 1.05f;
    private static boolean bQuit = false;
    //public static float rotationX = 0f;
    //public static float rotationY = 0f;
    //public static float rotationZ = 0f;
    public static boolean keyWpressed = false;
    public static boolean keySpressed = false;
    public static boolean keyApressed = false;
    public static boolean keyDpressed = false;
    public static boolean keyQpressed = false;
    public static boolean keyEpressed = false;
    public static boolean keyShiftpressed = false;
    public static boolean keyCtrlpressed = false;
    public static boolean keyLeftpressed = false;
    public static boolean keyRightpressed = false;
    public static boolean keyUppressed = false;
    public static boolean keyDownpressed = false;
    public static boolean keyFpressed = false;
    public static float zoomChange = 1.0f;
    public static boolean wireFrame = false;
    public long deltaT;

    public static ByteBuffer pixels; // "массив" пикселей
    public static int widthTexture; // ширина текстуры
    public static int heightTexture; // высота текстур

    public static double [][] H = null;
    public static int heightMap;
    public static int widthMap;
    public static void main(String[] args) {

        BufferedImage image;
        try {
            image = ImageIO.read(new File("map.png"));
            heightMap = image.getHeight();
            widthMap = image.getWidth();
            H = new double[widthMap][heightMap];
            for (int x = 0 ; x < widthMap ; x++)
                for (int y = 0 ; y < heightMap ; y++)
                    H[x][y] = (char)image.getRGB(x,y) % 256 / 16.0;
        }
        catch(IOException e){
            e.printStackTrace();
        }

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
            keyWpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_F) {
            keyFpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            keySpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_A) {
            keyApressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            keyDpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_Q) {
            keyQpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_E) {
            keyEpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            keyShiftpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            keyCtrlpressed = true;
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
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            keyLeftpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keyRightpressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            keyUppressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            keyDownpressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W) {
            keyWpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_F) {
            keyFpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            keySpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_A) {
            keyApressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            keyDpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_Q) {
            keyQpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_E) {
            keyEpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            keyShiftpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            keyCtrlpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            zoomChange = 1.0f;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            keyLeftpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keyRightpressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            keyUppressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            keyDownpressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }
}

