package theozoo.domain.animal;

public abstract class Bird extends Animal {
    Bird(String name,int age,int fullness){
        super(name,age,fullness);
    }

    @Override
    public Species getSpecies(){
        return Species.BIRD;
    }
}
