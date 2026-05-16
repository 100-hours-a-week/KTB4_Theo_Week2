public class Eagle extends Bird{
    Eagle(String name, int age, String species, int fullness){
        super(name, age, species, fullness);
    }

    @Override
    public void makeSound(){
        System.out.println("끼에엑!!");
    }
}
