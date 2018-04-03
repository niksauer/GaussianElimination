import java.util.Scanner;

/**
 * Created by Nik on 08.08.16.
 */
public class Vector extends Matrix {

    public Vector(int height) {
        super(1, height);
    }

    public double getValue(int y) {
        return super.getValue(y, 0);
    }

    public void setValue(int y, double value) {
        super.setValue(y, 0, value);
    }

    @Override
    public void print() {
        System.out.println("Der Vector sieht wie folgt aus:");

        for (int i = 0; i < getHeight(); i++) {
            for (int k = 0; k < getWidth(); k++) {
                System.out.printf("%3.2f\t\t", getValue(i, k));
            }

            System.out.print("\n");
        }
    }

    @Override
    public void fill() {
        Scanner input = new Scanner(System.in);

        for (int i = 0; i < getHeight(); i++) {
            System.out.println("Bitte Wert der " + (i+1) + ". Zeile des Vektors eingeben.");

            for (int k = 0; k < getWidth(); k++) {
                setValue(i, k, input.nextDouble());
            }
        }
    }

}

