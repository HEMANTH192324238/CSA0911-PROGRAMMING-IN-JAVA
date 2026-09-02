class Animal {
    String name;

    Animal(String name) {
        this.name = name;
    }

    void makeSound() {
        System.out.println("Some generic animal sound");
    }
}

class Dog extends Animal {
    String sound = "Woof! Woof!";

    Dog(String name) {
        super(name);
    }

    @Override
    void makeSound() {
        System.out.println(sound);
    }

    void displayInfo() {
        System.out.print(super.name + " says: ");
        super.makeSound();
        System.out.print("Actually, dog sound is: ");
        this.makeSound();
    }
}

public class AnimalDogDemo {
    public static void main(String[] args) {
        Dog dog = new Dog("Buddy");
        dog.displayInfo();
    }
}
