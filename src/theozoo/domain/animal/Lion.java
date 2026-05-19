package theozoo.domain.animal;

public class Lion extends Mammal{
    public Lion(String name, int age, int fullness){
        super(name, age, fullness);
    }

    @Override
    public void makeSound(){
        System.out.println("크아앙!!");
    }
}
