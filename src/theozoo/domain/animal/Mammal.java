package theozoo.domain.animal;

public abstract class Mammal extends Animal {
    Mammal(String name,int age, int fullness){
        super(name,age,fullness);
    }

    @Override
    public Species getSpecies(){
        return Species.MAMMAL;
    }
}
