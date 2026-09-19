package com.amore.springboot.explore.test;
class Animal {
    private static int x1 = show("static Animal.x initialized");

    private int first = 9;

    protected int second;

    public Animal() {
        System.out.println("first = " + first + ", second = " + second);
    }

    static int show(String str) {
        System.out.println(str);
        return 47;
    }
}

public class Dog extends Animal{
    private static int x2 = Animal.show("static Dog.x2 initialized");
    private int third = Animal.show("Dog.third initialized");

    public Dog() {
        System.out.println("third = " + third);
        System.out.println("second = " + second);
    }

    public static void main(String[] args) {
        System.out.println("Dog constructor");
        Dog dog = new Dog();
        System.out.println("Main Left");
    }

}


