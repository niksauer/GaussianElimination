/**
 * Created by Nik on 09.08.16.
 */
public class Main {

    public static void main(String args[]) {
        // equation: Ax = b
        //
        // A =
        // (01 | 01 | 01)
        // (00 | 02 | 05)
        // (02 | 05 | -1)
        //
        // b = (06 | -4 | 27)
        // x = (05 | 03 | -2)

        Matrix A = new Matrix(3);
        A.fill();
        //A.print();

        Vector b = new Vector(3);
        b.fill();
        //b.print();

        Vector x = Gauss.solveLinearEquation(A, b);
        x.print();
    }

}