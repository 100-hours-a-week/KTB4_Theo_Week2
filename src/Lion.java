public class Lion extends Mammal{
    Lion(String name, int age, String species, int fullness){
        super(name, age, species, fullness);
    }

    @Override
    public void makeSound(){
        System.out.println("크아앙!!");
    }
}
