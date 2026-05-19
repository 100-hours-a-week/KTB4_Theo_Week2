package theozoo.domain.animal;

public abstract class Reptile extends Animal {
    Reptile(String name,int age, int fullness){
        super(name,age,fullness);
    }

    @Override
    public Species getSpecies(){
        return Species.REPTILE;
    }
}
