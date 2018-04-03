import java.util.Scanner;

/**
 * Created by Nik on 08.08.16.
 */
public class Matrix {

    private int width;
    private int height;
    private double [][] matrix;

    // overload constructor to create n*n matrix through user supplied arguments
    public Matrix(int size) {
        width = size;
        height = size;
        matrix = new double[height][width];
    }

    // overload constructor to create n*m matrix through user supplied arguments
    protected Matrix(int width, int height) {
        this.width = width;
        this.height = height;
        matrix = new double[height][width];
    }

    public void fill() {
        Scanner input = new Scanner(System.in);

        for (int i = 0; i < height; i++) {
            System.out.println("Bitte Zeilenvektor #" + (i+1) + " der Matrix eingeben.");

            for (int k = 0; k < width; k++) {
                matrix[i][k] = input.nextDouble();
            }
        }
    }

    public void print() {
        System.out.println("Die Matrix sieht wie folgt aus:");

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                System.out.printf("%3.2f\t\t", matrix[i][k]);
            }

            System.out.print("\n");
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public double getValue(int y, int x) {
        return matrix[y][x];
    }

    public void setValue(int y, int x, double value) {
        matrix[y][x] = value;
    }

}