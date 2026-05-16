public class Snake extends Reptile{
    Snake(String name, int age, String species, int fullness){
        super(name, age, "Reptile", fullness);
    }

    @Override
    public void makeSound(){
        System.out.println("쉬이익!!");
    }
}
