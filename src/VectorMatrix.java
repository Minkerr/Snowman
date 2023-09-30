class Vector3
{
    public double x;
    public double y;
    public double z;

    public Vector3 (double x, double y, double z) //construction
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public static double Len(Vector3 v)
    {
        return (Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z)); // ne NUZNO PEREPISAT';
    }

    public static Vector3 VxR(Vector3 v, double r)
    {
        return new Vector3(v.x * r, v.y * r, v.z * r);
    }

    public static Vector3 VplusV(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vector3 VminusV(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static double VdotV(Vector3 v1, Vector3 v2)
    {
        return (v1.x * v2.x +v1.y * v2.y +v1.z * v2.z); //ne NUZNO PEREPISAT';
    }

    public static Vector3 Norm(Vector3 v)
    {
        double l = (Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z));
        Vector3 u = new Vector3(v.x / l, v.y / l,  v.z / l);
        if(l == 0) u = new Vector3(0, 0, 0);
        return u;
    }

    public static Vector3 VxV(Vector3 v1, Vector3 v2)
    {
        return new Vector3(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v2.z*v1.x, v1.x*v2.y - v1.y*v2.x);
    }

    @Override
    public String toString()
    {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}

class Matrix{
    private double [][] m = new double[3][3];

    //public Matrix(){ m = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}; }

    public Matrix(){
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1;
    }

    public static Matrix mI(){
        return new Matrix();
    }

    public Matrix(double [][] m)
    {
        this.m = m;
    }

    public Matrix(Matrix m){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                this.m[i][j] = m.m[i][j];
            }
        }
    }

    public static Matrix MxR(Matrix m, double a){
        Matrix s = new Matrix(m);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                s.m[i][j] *= a;
            }
        }
        return s;
    }

    public static Matrix MplusM(Matrix m, Matrix n){
        Matrix s = new Matrix(m);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                s.m[i][j] += n.m[i][j];
            }
        }
        return s;
    }

    public static Matrix MminusM(Matrix m, Matrix n){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                m.m[i][j] -= n.m[i][j];
            }
        }
        return m;
    }

    public static Vector3 MxV(Matrix m, Vector3 v){
        double x, y, z;
        x = m.m[0][0]*v.x + m.m[0][1]*v.y + m.m[0][2]*v.z;
        y = m.m[1][0]*v.x + m.m[1][1]*v.y + m.m[1][2]*v.z;
        z = m.m[2][0]*v.x + m.m[2][1]*v.y + m.m[2][2]*v.z;
        return new Vector3(x, y, z);
    }

    public static Matrix MxM(Matrix m, Matrix n){
        Matrix r = new Matrix();
        r.m[0][0] = 0; r.m[0][1] = 0; r.m[0][2] = 0;
        r.m[1][0] = 0; r.m[1][1] = 0; r.m[1][2] = 0;
        r.m[2][0] = 0; r.m[2][1] = 0; r.m[2][2] = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    r.m[i][j] += (m.m[i][k] * n.m[k][j]);
                }
            }
        }
        return r;
    }

    public static Matrix MRot(Vector3 v, double a){
        Matrix i = mI();
        Matrix s = new Matrix();
        s.m[0][0] = 0;    s.m[0][1] =  v.z; s.m[0][2] = -v.y;
        s.m[1][0] = -v.z; s.m[1][1] =  0;   s.m[1][2] =  v.x;
        s.m[2][0] =  v.y; s.m[2][1] = -v.x; s.m[2][2] =  0;
        return MplusM( MplusM(i, MxR(s, Math.sin(a))), MxR(MxM(s, s),(1 - Math.cos(a))));
    }

    public static Matrix Mtrans(Matrix m){
        double t;
        t = m.m[0][1];
        m.m[0][1] = m.m[1][0];
        m.m[1][0] = t;

        t = m.m[0][2];
        m.m[0][2] = m.m[2][0];
        m.m[2][0] = t;

        t = m.m[2][1];
        m.m[2][1] = m.m[1][2];
        m.m[1][2] = t;

        return m;
    }

    @Override
    public String toString()
    {
        return String.format("((%.2f, %.2f, %.2f),\n (%.2f, %.2f, %.2f),\n (%.2f, %.2f, %.2f))",
                m[0][0], m[0][1], m[0][2], m[1][0], m[1][1], m[1][2], m[2][0], m[2][1], m[2][2]);
    }
}