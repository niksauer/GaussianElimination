import java.util.*;

public class ProceduralGauss {

    public static Scanner input = new Scanner(System.in);
    public static int size = 0;

    public static void main(String[] args) {
        double[][] A = newMatrix();
        double[] B = newVector(size);

        Pivot PivObj = pivotMatrix(A);
        double[][] P = PivObj.P;
        double[][] PA = PivObj.PA;

        double[] PB = VectorMultiplication(P, B);

        double[] X = LUSplit(PA, PB);

        printVector(X);
    }

    public static double[][] newMatrix() {
        System.out.println("Welche Größe soll die Matrix haben? (n x n)");
        size = input.nextInt();

        double[][] matrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            System.out.println("Bitte Zeilenvektor #" + (i+1) + " eingeben.");
            for (int k = 0; k < size; k++) {
                matrix[i][k] = input.nextDouble();
            }
        }

        return matrix;
    }

    public static void printMatrix(double matrix[][]) {
        System.out.println("Die Matrix sieht wie folgt aus:");
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                System.out.print(matrix[i][k] + "\t\t");
            }
            System.out.print("\n");
        }
    }

    public static double[] newVector(int size) {
        System.out.println("Bitte Koordinaten des Vektors, gegen die, die Matrix gelöst werden soll (Ax = b), eingeben. (1x" + size + ")");
        double[] vector = new double[size];

        for (int i = 0; i < size; i++) {
            System.out.println("Koordinate #" + (i+1));
            vector[i] = input.nextDouble();
        }

        return vector;
    }

    public static void printVector(double vector[]) {
        System.out.println("Der Vector sieht wie folgt aus:");
        for (int i = 0; i < vector.length; i++) {
            System.out.println(vector[i]);
        }
    }

    public static double[] VectorMultiplication(double A[][], double V[]) {
        double[] MV = new double[size];
        double result = 0;

        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                result += (A[i][k] * V[k]);
            }

            MV[i] = result;
            result = 0;
        }

        return MV;
    }

    public static double[][] MatrixMultiplication(double A1[][], double A2[][]) {
        double[][] MM = new double[size][size];
        double result = 0;

        for (int z = 0; z < size; z++) {
            for (int i = 0; i < size; i++) {
                for (int k = 0; k < size; k++) {
                    //System.out.println("A1: " + A1[i][k] + " A2: " + A2[k][z]);
                    result += (A1[i][k] * A2[k][z]);
                }

                MM[i][z] = result;
                //System.out.println(MM[i][z]);
                result = 0;
            }
        }

        return MM;
    }

    public static Pivot pivotMatrix(double A[][]) {
        double[][] InterP = createID();
        double[][] InterPA = A;
        double dia;
        int columnCount = size;

        for (int c = 0; c < columnCount; c++) {
            dia = InterPA[c][c];
            int curR = c;

            //System.out.println("dia:" + dia);

            for (int r = c; r < columnCount; r++) {
                //System.out.println("curE: " + A[r+c][c]);
                if (InterPA[r+c][c] > dia) {
                    curR = r+c;
                }
            }

            if (curR != c) {
                //System.out.println("switch rows: " + curR + "|" + c);
                InterPA = MatrixMultiplication(createT(curR, c), InterPA);
                InterP = MatrixMultiplication(createT(curR, c), InterP);
            }

            columnCount--;

            //printMatrix(InterPA);
            //printMatrix(InterP);
        }

        return (new Pivot(InterP, InterPA));
    }

    public static double [] LUSplit(double A[][], double B[]) {
        double IL[][] = createID();
        double R[][] = A;
        double dia, current, alpha;

        for (int c = 0; c < size; c++) {
            dia = R[c][c];
            //System.out.println("diaR: " + c + "diaC: " + c + "diaE: " + dia);

            for (int r = c+1; r < size; r++) {
                current = R[r][c];
                //System.out.println("curR: " + r + "curC: " + c + "curE: " + current);

                if (current != 0) {
                    alpha = (0-current)/dia;
                    IL = MatrixMultiplication(IL, createIL(r, c, alpha));
                    R = MatrixMultiplication(createIL(r, c, alpha), R);
                }
            }
        }

        double[][] L = invertIL(IL);

        printMatrix(L);
        printMatrix(R);

        double[] Y = solveY(L, B);
        double[] X = solveX(R, Y);

        return X;
    }

    public static double[] solveY(double[][] L, double[] B) {
        double[] Y = new double[size];

        //printMatrix(L);
        //printVector(B);

        //Y[0] = (B[0]) / L[0][0];
        //Y[1] = (B[1] - L[1][0] * Y[0]) / L[1][1];
        //Y[2] = (B[2] - L[2][1] * Y[1] - L[2][0] * Y[0]) / L[2][2];

        for (int i = 0; i < size; i++) {
            double result = B[i];
            int k = i;

            //System.out.println("Y-Coordinate #" + i + "Inter-Res: " + result);

            while (k >= 1) {
                //System.out.println("L: " + L[i][k - 1] + " Y:" + Y[k-1]);
                result -= (L[i][k-1] * Y[k-1]);
                k--;
            }

            Y[i] = (result /= L[i][i]);
            //System.out.println("Final Res: " + Y[i]);
        }

        return Y;
    }

    public static double[] solveX(double[][] R, double[] Y) {
        double[] X = new double[size];

        //printMatrix(R);
        //printVector(Y);

        //X[2] = Y[2] / R[2][2];
        //X[1] = (Y[1] - R[1][2] * Y[2]) / R[1][1];
        //X[0] = (Y[0] - R[0][2] * Y[2] - R[0][1] * Y[1]) / R[0][0];

        for (int i = size-1; i >= 0; i--) {
            double result = Y[i];
            int k = size-1;

            //System.out.println("X-Coordinate #" + i + " Inter-Res: " + result);

            while (k > i) {
                //System.out.println("R: " + R[i][k] + " X: " + X[k]);
                result -= (R[i][k] * X[k]);
                k--;
            }

            X[k] = (result /= R[k][k]);
            //System.out.println("Final-Res: " + X[k]);
        }

        return X;
    }

    public static double[][] createT(int maxR, int curR) {
        double[][] T = createID();

        if (maxR != curR) {
            T[curR][maxR] = 1;
            T[maxR][curR] = 1;
            T[curR][curR] = 0;
            T[maxR][maxR] = 0;
        }

        return T;
    }

    public static double[][] createID() {
        double[][] ID = new double[size][size];

        for (int i = 0; i < size; i++) {
            ID[i][i] = 1;
        }

        return ID;
    }

    public static double[][] createIL(int curR, int diaR, double alpha) {
        double[][] LA = createID();

        LA[curR][diaR] = alpha;

        return LA;
    }

    public static double[][] invertIL(double[][] IL) {
        double[][] L = IL;

        for (int i = 0; i < size; i++) {
            for (int k = i+1; k < size; k++) {
                L[k][i] *= (-1);
            }
        }

        return L;
    }

}