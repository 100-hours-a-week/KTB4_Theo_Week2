package theozoo.domain.animal;

public class Eagle extends Bird {
    public Eagle(String name, int age, int fullness){
        super(name, age, fullness);
    }

    @Override
    public void makeSound(){
        System.out.println("끼에엑!!");
    }
}
