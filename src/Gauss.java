/**
 * Created by Nik on 08.08.16.
 */
public class Gauss {

    private static int size;

    public static Vector solveLinearEquation(Matrix M, Vector V) {
        size = V.getHeight();
        Pivot pivot = new Pivot(M);

        Vector PB = vektorMultiplication(pivot.P, V);
        Vector X = LUSplit(pivot.PA, PB);

        return X;
    }

    public static Vector vektorMultiplication(Matrix M, Vector V) {
        if (M.getWidth() == V.getHeight()) {
            int size = V.getHeight();
            Vector R = new Vector(size);
            double result = 0;

            for (int i = 0; i < size; i++) {
                for (int k = 0; k < size; k++) {
                    result += (M.getValue(i, k) * V.getValue(k, 0));
                }

                R.setValue(i, 0, result);
                result = 0;
            }

            return R;
        } else {
            System.out.println("Matrix and Vector have unequal sizes.");
            return null;
        }
    }

    public static Matrix matrixMultiplication(Matrix M1, Matrix M2) {
        if (M1.getWidth() == M2.getHeight()) {
            int size = M2.getHeight();
            Matrix R = new Matrix(size);
            double result = 0;

            for (int z = 0; z < size; z++) {
                for (int i = 0; i < size; i++) {
                    for (int k = 0; k < size; k++) {
                        result += (M1.getValue(i, k) * M2.getValue(k, z));
                    }

                    R.setValue(i, z, result);
                    result = 0;
                }
            }

            return R;
        } else {
            System.out.println("Matrices have unequal sizes.");
            return null;
        }
    }

    // returns identity matrix
    private static Matrix createID() {
        Matrix ID = new Matrix(size);

        for (int i = 0; i < size; i++) {
            ID.setValue(i, i, 1);
        }

        return ID;
    }

    private static Matrix createIL(int curR, int diaR, double alpha) {
        Matrix LA = createID();
        LA.setValue(curR, diaR, alpha);

        return LA;
    }

    private static Matrix createT(int maxR, int curR) {
        Matrix T = createID();

        if (maxR != curR) {
            T.setValue(curR, maxR, 1);
            T.setValue(maxR, curR, 1);
            T.setValue(curR, curR, 0);
            T.setValue(maxR, maxR, 0);
        }

        return T;
    }

    private static Matrix invertIL(Matrix IL) {
        Matrix L = IL;

        for (int i = 0; i < size; i++) {
            for (int k = i+1; k < size; k++) {
                L.setValue(k, i, L.getValue(k, i) * (-1));
            }
        }

        return L;
    }

    private static Vector LUSplit(Matrix M, Vector V) {
        Matrix IL = createID();
        Matrix R = M;
        double dia, current, alpha;

        for (int c = 0; c < size; c++) {
            dia = R.getValue(c, c);
            //System.out.println("diaR: " + c + "diaC: " + c + "diaE: " + dia);

            for (int r = c+1; r < size; r++) {
                current = R.getValue(r, c);
                //System.out.println("curR: " + r + "curC: " + c + "curE: " + current);

                if (current != 0) {
                    alpha = (0-current)/dia;
                    IL = matrixMultiplication(IL, createIL(r, c, alpha));
                    R = matrixMultiplication(createIL(r, c, alpha), R);
                }
            }
        }

        Matrix L = invertIL(IL);

        Vector Y = solveY(L, V);
        Vector X = solveX(R, Y);

        return X;
    }

    private static Vector solveY(Matrix L, Vector B) {
        Vector Y = new Vector(size);

        //Y[0] = (B[0]) / L[0][0];
        //Y[1] = (B[1] - L[1][0] * Y[0]) / L[1][1];
        //Y[2] = (B[2] - L[2][1] * Y[1] - L[2][0] * Y[0]) / L[2][2];

        for (int i = 0; i < size; i++) {
            double result = B.getValue(i, 0);
            int k = i;

            //System.out.println("Y-Coordinate #" + i + "Inter-Res: " + result);

            while (k >= 1) {
                //System.out.println("L: " + L[i][k - 1] + " Y:" + Y[k-1]);
                result -= (L.getValue(i, k-1) * Y.getValue(k-1, 0));
                k--;
            }

            result /= L.getValue(i, i);
            Y.setValue(i, 0, result);
            //System.out.println("Final Res: " + Y[i]);
        }

        return Y;
    }

    private static Vector solveX(Matrix R, Vector Y) {
        Vector X = new Vector(size);

        //X[2] = Y[2] / R[2][2];
        //X[1] = (Y[1] - R[1][2] * Y[2]) / R[1][1];
        //X[0] = (Y[0] - R[0][2] * Y[2] - R[0][1] * Y[1]) / R[0][0];

        for (int i = size-1; i >= 0; i--) {
            double result = Y.getValue(i, 0);
            int k = size-1;

            //System.out.println("X-Coordinate #" + i + " Inter-Res: " + result);

            while (k > i) {
                //System.out.println("R: " + R[i][k] + " X: " + X[k]);
                result -= (R.getValue(i, k) * X.getValue(k, 0));
                k--;
            }

            result /= R.getValue(k, k);
            X.setValue(k, 0, result);
            //System.out.println("Final-Res: " + X[k]);
        }

        return X;
    }

    static class Pivot {
        static Matrix P;
        static Matrix PA;

        Pivot(Matrix M) {
            Matrix InterP = createID();
            Matrix InterPA = M;
            double dia;
            int columnCount = size;

            for (int c = 0; c < columnCount; c++) {
                dia = InterPA.getValue(c, c);
                int curR = c;

                //System.out.println("dia:" + dia);

                for (int r = c; r < columnCount; r++) {
                    //System.out.println("curE: " + A[r+c][c]);
                    if (InterPA.getValue(r+c, c) > dia) {
                        curR = r+c;
                    }
                }

                if (curR != c) {
                    //System.out.println("switch rows: " + curR + "|" + c);
                    InterPA = matrixMultiplication(createT(curR, c), InterPA);
                    InterP = matrixMultiplication(createT(curR, c), InterP);
                }

                columnCount--;
            }

            P = InterP;
            PA = InterPA;
        }
    }

}