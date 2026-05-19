package theozoo.domain.animal;

public class Snake extends Reptile{
    public Snake(String name, int age, int fullness){
        super(name, age, fullness);
    }

    @Override
    public void makeSound(){
        System.out.println("쉬이익!!");
    }
}
