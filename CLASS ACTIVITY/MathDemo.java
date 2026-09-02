class MathConstants {
    final double PI = 3.14159;
    final double E;

    MathConstants(double eValue) {
        this.E = eValue;
    }

    final double calculateCircleArea(double radius) {
        return PI * radius * radius;
    }
}

class ExtendedMath extends MathConstants {
    ExtendedMath(double eValue) {
        super(eValue);
    }

    /*
    @Override
    double calculateCircleArea(double radius) {
        return radius * radius;
    }
    */
}

public class MathDemo {
    public static void main(String[] args) {
        ExtendedMath math = new ExtendedMath(2.71828);
        System.out.println("PI: " + math.PI);
        System.out.println("E: " + math.E);
        System.out.println("Area of circle (r=5): " + math.calculateCircleArea(5));
    }
}
