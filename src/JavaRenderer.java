import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class JavaRenderer implements GLEventListener {
    private float angleX = 0.0f;
    private float angleY = 0.0f;
    private float angleZ = 0.0f;
    private float zoom = 1.0f;
    private static final GLU glu = new GLU();
    private int[] textureIdGround = new int[1]; // создаем массив для хранения номера текстуры
    private int[] textureIdSky = new int[1]; // создаем массив для хранения номера текстуры
    private int listNum;
    private double objectX = 0.0f;
    private double objectY = 0.0f;
    private double objectZ = getZ(64, 64);
    private int windowWidth = 1920;
    private int windowHeight = 1080;
    private float aspectRatio = (float)windowWidth/windowHeight;
    private long lastTime;
    private long currentTime;
    public long deltaT;

    private void sphere(GL2 gl, double R){
        sphere(gl, R, 0, 1,1);
    }
    private void sphere(GL2 gl, double R, double r, double g, double b){
        double r1, r2;
        for(int j = -25; j < 25; j++){
            gl.glBegin(gl.GL_QUAD_STRIP);
            gl.glColor3d(r, g, b);
            r2 = R * Math.cos(Math.PI * (j + 1) / 48);
            r1 = R * Math.cos(Math.PI * j / 48);
            for (int i = 0; i < 49; i++) {
                float [] v1 = {(float) (r1 * Math.cos(2 * Math.PI * i / 48)), (float) (r1 * Math.sin(2 * Math.PI * i / 48)), (float) (R * Math.sin(Math.PI * j / 48))};
                gl.glNormal3fv(FloatBuffer.wrap(v1));
                gl.glVertex3d(r1 * Math.cos(2 * Math.PI * i / 48),r1 * Math.sin(2 * Math.PI * i / 48), R * Math.sin(Math.PI * j / 48));
                float [] v2 = {(float) (r2 * Math.cos(2 * Math.PI * i / 48)), (float) (r2 * Math.sin(2 * Math.PI * i / 48)), (float) (R * Math.sin(Math.PI * (j+1) / 48))};
                gl.glNormal3fv(FloatBuffer.wrap(v2));
                gl.glVertex3d(r2 * Math.cos(2 * Math.PI * i / 48),r2 * Math.sin(2 * Math.PI * i / 48), R * Math.sin(Math.PI * (j + 1) / 48));
            }
            gl.glEnd();

        }
        /*
        for(int j = -25; j < 0; j++){
            gl.glBegin(gl.GL_QUAD_STRIP);
            gl.glColor3d(r, g, b);
            r2 = R * Math.cos(Math.PI * (j + 1) / 48);
            r1 = R * Math.cos(Math.PI * j / 48);
            for (int i = 0; i < 49; i++) {
                gl.glVertex3d(r1 * Math.cos(2 * Math.PI * i / 48),r1 * Math.sin(2 * Math.PI * i / 48), R * Math.sin(Math.PI * j / 48));
                gl.glVertex3d(r2 * Math.cos(2 * Math.PI * i / 48),r2 * Math.sin(2 * Math.PI * i / 48), R * Math.sin(Math.PI * (j + 1) / 48));
            }
            gl.glEnd();
        }
        */
    }

    private void tor(GL2 gl, double R, double r){
        for(int j = 0; j <= 49; j++){
            double a1 = 2 * Math.PI * j / 48;
            gl.glBegin(gl.GL_QUAD_STRIP);
            gl.glColor3f(1, 0, 0);
            for(int i = 0; i <= 49; i++){
                double a2 = 2 * Math.PI * i / 48, a3 = 2 * Math.PI / 48;
                float [] v1 = {(float) (Math.cos(a1) * (R + r * Math.cos(a2))), (float) (Math.sin(a1) * (R + r * Math.cos(a2))), (float) (r * Math.sin(a2))};
                gl.glNormal3fv(FloatBuffer.wrap(v1));
                gl.glVertex3d(Math.cos(a1) * (R + r * Math.cos(a2)), Math.sin(a1) * (R + r * Math.cos(a2)), r * Math.sin(a2));
                float [] v2 = {(float) (Math.cos(a1 + a3) * (R + r * Math.cos(a2))), (float) (Math.sin(a1 + a3) * (R + r * Math.cos(a2))), (float) (r * Math.sin(a2))};
                gl.glNormal3fv(FloatBuffer.wrap(v2));
                gl.glVertex3d(Math.cos(a1 + a3) * (R + r * Math.cos(a2)), Math.sin(a1 + a3) * (R + r * Math.cos(a2)), r * Math.sin(a2));
            }
            gl.glEnd();
        }
    }

    private void halfsphere(GL2 gl, double R){
        double r1, r2;
        for(int j = 0; j < 25; j++){
            gl.glBegin(gl.GL_QUAD_STRIP);
            gl.glColor3f(0, 1, 1);
            r2 = R * Math.cos(Math.PI * (j + 1) / 48);
            r1 = R * Math.cos(Math.PI * j / 48);
            for (int i = 0; i < 49; i++) {
                gl.glVertex3d(r1 * Math.cos(2 * Math.PI * i / 48),r1 * Math.sin(2 * Math.PI * i / 48), R * Math.sin(Math.PI * j / 48));
                gl.glVertex3d(r2 * Math.cos(2 * Math.PI * i / 48),r2 * Math.sin(2 * Math.PI * i / 48), R * Math.sin(Math.PI * (j + 1) / 48));
            }
            gl.glEnd();

        }

        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(1, 0, 1);
        gl.glVertex3d( 0, 0, 0);
        for (int i = 0; i < 49; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48), R * Math.sin(2 * Math.PI * i / 48), 0);
        }
        gl.glEnd();
    }

    private void conus(GL2 gl, double R){
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(1, 0.4f, 0);
        gl.glVertex3d( 0, 0, 0.5);
        for (int i = 0; i < 49; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();

        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(1, 0.4f, 0);
        gl.glVertex3d( 0, 0, -0.5);
        for (int i = 0; i < 49; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
    }

    private void cylinder(GL2 gl, double R){
        cylinder(gl, R, 0, 0.5f ,1);
    }
    private void cylinder(GL2 gl, double R, double r, double g, double b){
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3d(r, g, b);
        gl.glVertex3d( 0, 0, -0.5);
        for (int i = 0; i < 49; i++) {
            float [] v1 = {(float) (R * Math.cos(2 * Math.PI * i / 48)), (float) (R * Math.sin(2 * Math.PI * i / 48)), -0.5f};
            gl.glNormal3fv(FloatBuffer.wrap(v1));
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3d(r, g, b);
        gl.glVertex3d( 0, 0, 0.5);
        for (int i = 0; i < 49; i++) {
            float [] v1 = {(float) (R * Math.cos(2 * Math.PI * i / 48)), (float) (R * Math.sin(2 * Math.PI * i / 48)), 0.5f};
            gl.glNormal3fv(FloatBuffer.wrap(v1));
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), 0.5);
        }
        gl.glEnd();
        gl.glBegin(gl.GL_QUAD_STRIP);
        gl.glColor3d(r, g, b);
        for (int i = 0; i < 49; i++) {
            float [] v1 = {(float) (R * Math.cos(2 * Math.PI * i / 48)), (float) (R * Math.sin(2 * Math.PI * i / 48)), 0.5f};
            gl.glNormal3fv(FloatBuffer.wrap(v1));
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), 0.5);
            float [] v2 = {(float) (R * Math.cos(2 * Math.PI * i / 48)), (float) (R * Math.sin(2 * Math.PI * i / 48)), -0.5f};
            gl.glNormal3fv(FloatBuffer.wrap(v2));
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
    }

    private void halfcylinder(GL2 gl, double R){
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(0, 1, 1);
        gl.glVertex3d( 0, 0, -0.5);
        for (int i = 0; i < 25; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(0, 1, 1);
        gl.glVertex3d( 0, 0, 0.5);
        for (int i = 0; i < 25; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), 0.5);
        }
        gl.glEnd();
        gl.glBegin(gl.GL_QUAD_STRIP);
        gl.glColor3f(1, 0, 1);
        for (int i = 0; i < 25; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), 0.5);
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
        /* DON'T WORK/ NADO CHINIT'
        gl.glBegin(gl.GL_QUADS);
        gl.glColor3f(0, 1, 1);
        gl.glVertex3d( 0.5, 0, 0.5);
        gl.glVertex3d( 0.5, 0, -0.5);
        gl.glVertex3d( -0.5, 0, -0.5);
        gl.glVertex3d( -0.5, 0, 0.5);
        gl.glEnd();
        */
    }

    private void cutedconus(GL2 gl, double R){
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(1, 1, 0);
        gl.glVertex3d( 0, 0, -0.5);
        for (int i = 0; i < 49; i++) {
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
        gl.glBegin(gl.GL_TRIANGLE_FAN);
        gl.glColor3f(1, 0, 1);
        gl.glVertex3d( 0, 0, 0.5);
        for (int i = 0; i < 49; i++) {
            gl.glVertex3d(0.5 * R * Math.cos(2 * Math.PI * i / 48),0.5 * R * Math.sin(2 * Math.PI * i / 48), 0.5);
        }
        gl.glEnd();
        gl.glBegin(gl.GL_QUAD_STRIP);
        gl.glColor3f(0, 1, 1);
        for (int i = 0; i < 49; i++) {
            gl.glVertex3d(0.5 * R * Math.cos(2 * Math.PI * i / 48),0.5 * R * Math.sin(2 * Math.PI * i / 48), 0.5);
            gl.glVertex3d(R * Math.cos(2 * Math.PI * i / 48),R * Math.sin(2 * Math.PI * i / 48), -0.5);
        }
        gl.glEnd();
    }

    private void snowman(GL2 gl){
        gl.glPushMatrix();
        gl.glTranslated(0, 0, 19);
        tor(gl, 0.8, 0.2);//obamasnowman
        gl.glPushMatrix();
        gl.glTranslated(0, 0, -2);
        sphere(gl, 2);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 0, 1);
        sphere(gl, 1.3);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 0, 2.7);
        cylinder(gl, 0.6);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 0, 2.2);
        gl.glScaled(1, 1, 0.1);
        //gl.glRotated(180, 0, 1, 0);
        cylinder(gl, 0.9, 0, 0.5, 1);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0.9, 0.9, 1.3);
        gl.glColor3f(0, 0, 0);
        sphere(gl, 0.3, 0, 0, 0);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(-0.9, 0.9, 1.3);
        sphere(gl, 0.3, 0, 0, 0);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 2.3, 0.8);
        gl.glRotated(90, -1, 0, 0);
        gl.glScaled(1, 1, 4);
        conus(gl,0.3);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 0.9, 0.4);
        tor(gl,0.3, 0.1);
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    private int ost(int a, int b){
        int res;
        if(a%b == 0) res = b;
        else res = a%b;
        return res;
    }

    private double getZ(double x, double y){
        int X = (int)x;
        int Y = (int)y;
        double z = 0;
        int x1, x2, x3, y1, y2, y3;
        if((X+Y)%2 == 0){ // определяем тип квадрата в котором точка. |/|
            if(x - X < y - Y){//верхний треугольник |/
                x1 = X; y1 = Y;
                x2 = X; y2 = Y+1;
                x3= X+1; y3 = Y+1;
            }
            else{ // нижний   --  /|
                x1 = X; y1 = Y;
                x2 = X+1; y2 = Y;
                x3= X+1; y3 = Y+1;
            }
        }
        else { // квадрат  |\|
            if(1 - x + X < y - Y){//верхний треугольник \|
                x1 = X+1; y1 = Y;
                x2 = X; y2 = Y+1;
                x3= X+1; y3 = Y+1;
            }
            else{ // нижний   --  |\
                x1 = X; y1 = Y;
                x2 = X; y2 = Y+1;
                x3= X+1; y3 = Y;
            }
        }
        if (x1 < 0 || x2 < 0 || x3 < 0 || y1 < 0 || y2 < 0 || y3 < 0)
            System.out.println("(" + x1 + "," + y1 + ") " +"(" + x2 + "," + y2 + ") " +"(" + x3 + "," + y3 + ") " );
        //Мы получили три координаты треугольника в котором находится точка
        Vector3 v12 = Vector3.VminusV(new Vector3((double)x2, (double)y2, (double)Main.H[x2][y2]),
                                      new Vector3((double)x1, (double)y1, (double)Main.H[x1][y1]));
        Vector3 v13 = Vector3.VminusV(new Vector3((double)x3, (double)y3, (double)Main.H[x3][y3]),
                                      new Vector3((double)x1, (double)y1, (double)Main.H[x1][y1]));
        Vector3 N = Vector3.VxV(v12, v13);
        double D = -(N.x*x1 + N.y*y1 + N.z*Main.H[x1][y1]);
        z = - (N.x*x + N.y*y + D)/N.z;
        return z;
    }

    Camera camera = new Camera();
    public void display(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, aspectRatio, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -5.0f);

        if (Main.wireFrame)
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
        else
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);

        //angleX += Main.rotationX;
        //angleY += Main.rotationY;
        //angleZ += Main.rotationZ;

        if(Main.keyApressed) camera.roll(-0.02);
        if(Main.keyDpressed) camera.roll( 0.02);
        if(Main.keyShiftpressed) camera.pitch( -0.02);
        if(Main.keyCtrlpressed) camera.pitch( 0.02);
        if(Main.keyEpressed) camera.yaw( 0.02);
        if(Main.keyQpressed) camera.yaw( -0.02);
        if(Main.keyWpressed) camera.move( 0.5);
        if(Main.keySpressed) camera.move( -0.5);


        glu.gluLookAt(camera.pos.x, camera.pos.y, camera.pos.z,
                camera.pos.x + camera.dir.x, camera.pos.y + camera.dir.y, camera.pos.z + camera.dir.z,
                camera.up.x, camera.up.y, camera.up.z);

        zoom *= Main.zoomChange;
        //gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
        //gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
        //gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f);
        gl.glScalef(zoom, zoom, zoom);

        currentTime = System.currentTimeMillis();
        deltaT = currentTime - lastTime;
        lastTime = System.currentTimeMillis();

        if(Main.keyLeftpressed) objectX += 0.1;
        if(Main.keyRightpressed) objectX-=0.1;
        if(Main.keyUppressed) objectY+=0.1;
        if(Main.keyDownpressed) objectY-=0.1;
        //objectZ = getZ(objectX+64, objectY+64);


                // CODE HERE

        gl.glEnable(gl.GL_LIGHTING);
        float [] amb = {0.5f, 0.5f, 0.5f, 1};
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, FloatBuffer.wrap(amb));
        float [] dif = {0.8f, 0.8f, 0.8f, 1};
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, FloatBuffer.wrap(dif));
        float [] lightPos = {-76, -128, 68, 1};
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, FloatBuffer.wrap(lightPos));
        gl.glEnable(gl.GL_LIGHT0);
        gl.glEnable(gl.GL_COLOR_MATERIAL);

        /*искусственное солнце
        gl.glPushMatrix();
        gl.glTranslated(-76, -128, 68);
        sphere(gl, 1, 1, 1, 0.9);
        gl.glPopMatrix();
        */

        gl.glPushMatrix();
        if(Main.keyFpressed) objectZ += 24;
        if(!Main.keyFpressed) while(objectZ > getZ(objectX+64, objectY+64)) objectZ-=0.001;
        //if(objectZ - 1 <= getZ(objectX+64, objectY+64))gl.glTranslated(objectX, objectY, objectZ - 16);
        snowman(gl);
        gl.glPopMatrix();

        gl.glColor3f(1,1,1);
        gl.glCallList(listNum);//Вызываем дисплейный список с скайбоксом и ландшафтом


        gl.glEnable(gl.GL_BLEND); // Всё что поверх экрана (прицел)
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4d(1, 1, 1, 0.5);
        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(gl.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRectd(-0.0055, -0.01, 0.0055, 0.01);
        gl.glBegin(gl.GL_LINE_LOOP );
        gl.glEnd();
        gl.glDisable(gl.GL_BLEND);


    }



    public void init(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT,GL.GL_NICEST);
        lastTime = System.currentTimeMillis();
        objectZ = getZ(objectX+64, objectY+64);

        gl.glEnable(GL.GL_TEXTURE_2D); // Разрешаем текстурирование
        gl.glGenTextures(1, textureIdSky, 0); // Получаем свободный ID текстуры
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdSky[0]); // выбираем ID текстуры
// устанавливаем параметры выбора пискеля при увеличении/уменьшении текстуры
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        BufferedImage image;
        try {
            image = ImageIO.read(new File("leaf.jpg"));
            int widthTexture = image.getWidth(); // ширина текстуры
            int heightTexture = image.getHeight(); // высота текстуры
            // извлечение пикселей из считанного изображения
            DataBufferByte dataBufferByte =
                    (DataBufferByte) image.getData().getDataBuffer();
            // приведение их к подходящему внутреннему виду
            ByteBuffer pixels = ByteBuffer.wrap(dataBufferByte.getData());
            byte r,b; // временные переменные – яркость синего и красного
            // перебираем все пиксели изображения
            for (int i = 0 ; i < heightTexture * widthTexture ; i++) {
                // меняем местами синюю и красную компоненты
                b = pixels.get(3 * i);
                r = pixels.get(3 * i + 2);
                pixels.put(3 * i, r);
                pixels.put(3 * i + 2, b);
            }
            // загружаем пиксели текстуры в текущий выбранный ID текстуры
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, widthTexture,
                    heightTexture, 0,GL.GL_RGB, GL.GL_UNSIGNED_BYTE, pixels);
            //Эта штука загружает скайбокс
        } catch (IOException e) {
            e.printStackTrace();
        }

        //А эта загружает текстуру ландшафта
        gl.glGenTextures(1, textureIdGround, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdGround[0]);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

        try {
            image = ImageIO.read(new File("ground.jpg"));
            int widthTexture = image.getWidth(); // ширина текстуры
            int heightTexture = image.getHeight(); // высота текстуры
            // извлечение пикселей из считанного изображения
            DataBufferByte dataBufferByte =
                    (DataBufferByte) image.getData().getDataBuffer();
            // приведение их к подходящему внутреннему виду
            ByteBuffer pixels = ByteBuffer.wrap(dataBufferByte.getData());
            byte r,b; // временные переменные – яркость синего и красного
            // перебираем все пиксели изображения
            for (int i = 0 ; i < heightTexture * widthTexture ; i++) {
                // меняем местами синюю и красную компоненты
                b = pixels.get(3 * i);
                r = pixels.get(3 * i + 2);
                pixels.put(3 * i, r);
                pixels.put(3 * i + 2, b);
            }
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, widthTexture,
                    heightTexture, 0,GL.GL_RGB, GL.GL_UNSIGNED_BYTE, pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }


        listNum = gl.glGenLists(1);
        gl.glNewList(listNum, gl.GL_COMPILE); //Тут мы рисуем то что не двигаем, так оно работает быстрее
        // В этом дисплейном списке рисуется скайбокс и ландшафт
        //SKYBOX
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdSky[0]);

        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2d(0.25,0.5); gl.glVertex3f(128, -128, 128);
        gl.glTexCoord2d(0.25,1); gl.glVertex3f(128, -128, -128);
        gl.glTexCoord2d(0.5,1); gl.glVertex3f(-128,-128, -128);
        gl.glTexCoord2d(0.5,0.5); gl.glVertex3f(-128,-128, 128);
        gl.glEnd();

        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2d(0.5,0.5); gl.glVertex3f(-128, -128, 128);
        gl.glTexCoord2d(0.5,1); gl.glVertex3f(-128, -128, -128);
        gl.glTexCoord2d(0.75,1); gl.glVertex3f(-128,128, -128);
        gl.glTexCoord2d(0.75,0.5); gl.glVertex3f(-128,128, 128);
        gl.glEnd();

        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2d(0.75,0.5); gl.glVertex3f(-128, 128, 128);
        gl.glTexCoord2d(0.75,1); gl.glVertex3f(-128, 128, -128);
        gl.glTexCoord2d(1,1); gl.glVertex3f(128,128, -128);
        gl.glTexCoord2d(1,0.5); gl.glVertex3f(128,128, 128);
        gl.glEnd();

        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2d(0,0.5); gl.glVertex3f(128, 128, 128);
        gl.glTexCoord2d(0,1); gl.glVertex3f(128, 128, -128);
        gl.glTexCoord2d(0.25,1); gl.glVertex3f(128,-128, -128);
        gl.glTexCoord2d(0.25,0.5); gl.glVertex3f(128,-128, 128);
        gl.glEnd();

        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2d(0.75,0.5); gl.glVertex3f(-128, 128, -128);
        gl.glTexCoord2d(0.5,0.5); gl.glVertex3f(128, 128, -128);
        gl.glTexCoord2d(0.5,0); gl.glVertex3f(128,-128, -128);
        gl.glTexCoord2d(0.75,0); gl.glVertex3f(-128,-128, -128);//+x для обрезания полей
        gl.glEnd();

        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2d(0.5,0.5); gl.glVertex3f(-128, -128, 128);
        gl.glTexCoord2d(0.25,0.5); gl.glVertex3f(128, -128, 128);
        gl.glTexCoord2d(0.25,0); gl.glVertex3f(128,128, 128);
        gl.glTexCoord2d(0.5,0); gl.glVertex3f(-128,128, 128);//+x для обрезания полей
        gl.glEnd();

        gl.glDisable(GL.GL_TEXTURE_2D);
        //korobka
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIdGround[0]);






///* //PRORISOVKA KARTI
        for(int i = 1; i < 128; i+=2){
            for(int j = 1; j < 128; j+=2){
                gl.glPushMatrix();
                gl.glTranslated(-64, -64, 0);
                int k = 16; // сторона квадрата на который натягиваем текстуру
                gl.glBegin(gl.GL_TRIANGLE_FAN);
                //gl.glColor3d(Main.H[i][j], 16 - Main.H[i][j], 0);
                gl.glTexCoord2d((double)i%k/k,(double)j%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{1, 0, Main.H[i+1][j] - Main.H[i][j]}));
                gl.glVertex3d( i, j, Main.H[i][j]); // 0

                //gl.glColor3d(Main.H[i+1][j], 16 - Main.H[i+1][j], 0);
                gl.glTexCoord2d((double)ost(i+1, k)/k,(double)j%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{1, 0, Main.H[i+1][j] - Main.H[i][j]}));
                gl.glVertex3d( i+1, j, Main.H[i+1][j]);
                //gl.glColor3d(Main.H[i+1][j+1], 16 - Main.H[i+1][j+1], 0);
                gl.glTexCoord2d((double)ost(i+1, k)/k,(double)ost(j+1, k)/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{1, 1, Main.H[i+1][j+1] - Main.H[i][j]}));
                gl.glVertex3d( i+1, j+1, Main.H[i+1][j+1]);
                //gl.glColor3d(Main.H[i][j+1], 16 - Main.H[i][j+1], 0);
                gl.glTexCoord2d((double)i%k/k,(double)ost(j+1, k)/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{0, 1, Main.H[i][j+1] - Main.H[i][j]}));
                gl.glVertex3d( i, j+1, Main.H[i][j+1]);
                //gl.glColor3d(Main.H[i-1][j+1], 16 - Main.H[i-1][j+1 ], 0);
                gl.glTexCoord2d((double)(i-1)%k/k,(double)ost(j+1, k)/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{-1, 1, Main.H[i-1][j+1] - Main.H[i][j]}));
                gl.glVertex3d( i-1, j+1, Main.H[i-1][j+1]);

                //gl.glColor3d(Main.H[i-1][j], 16 - Main.H[i-1][j], 0);
                gl.glTexCoord2d((double)(i-1)%k/k,(double)j%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{-1, 0, Main.H[i-1][j] - Main.H[i][j]}));
                gl.glVertex3d( i-1, j, Main.H[i-1][j]);
                //gl.glColor3d(Main.H[i-1][j-1], 16 - Main.H[i-1][j-1], 0);
                gl.glTexCoord2d((double)(i-1)%k/k,(double)(j-1)%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{-1, -1, Main.H[i-1][j-1] - Main.H[i][j]}));
                gl.glVertex3d( i-1, j-1, Main.H[i-1][j-1]);
                //gl.glColor3d(Main.H[i][j-1], 16 - Main.H[i][j-1], 0);
                gl.glTexCoord2d((double)i%k/k,(double)(j-1)%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{0, -1, Main.H[i][j-1] - Main.H[i][j]}));
                gl.glVertex3d( i, j-1, Main.H[i][j-1]);
                //gl.glColor3d(Main.H[i+1][j-1], 16 - Main.H[i+1][j-1], 0);
                gl.glTexCoord2d((double)ost(i+1, k)/k,(double)(j-1)%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{1, -1, Main.H[i+1][j-1] - Main.H[i][j]}));
                gl.glVertex3d( i+1, j-1, Main.H[i+1][j-1]);

                //gl.glColor3d(Main.H[i+1][j], 16 - Main.H[i+1][j], 0);
                gl.glTexCoord2d((double)ost(i+1, k)/k,(double)j%k/k);
                //gl.glNormal3dv(DoubleBuffer.wrap(new double[]{1, 0, Main.H[i+1][j] - Main.H[i][j]}));
                gl.glVertex3d( i+1, j, Main.H[i+1][j]);

                gl.glEnd();
                gl.glPopMatrix();
            }
        }

        gl.glDisable(GL.GL_TEXTURE_2D);
//*/
        gl.glEndList();


    }

    public void reshape(GLAutoDrawable gLDrawable, int x,
                        int y, int width, int height) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        if(height <= 0) {
            height = 1;
        }
        aspectRatio = (float)width / (float)height;
        windowWidth = width;
        windowHeight = height;
        gl.glViewport(0,0,1920,1080);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, aspectRatio, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void dispose(GLAutoDrawable arg0) {

    }

}