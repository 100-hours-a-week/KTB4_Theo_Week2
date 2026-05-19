package theozoo.domain.animal;

public enum Species {
    MAMMAL(1,"Mammal"),
    BIRD(2,"Bird"),
    REPTILE(3,"Reptile");

    private final int number;
    private final String name;

    Species(int number,String name){
        this.number = number;
        this.name = name;
    }

    public int getNumber(){
        return number;
    }

    public String getName(){
        return name;
    }

    public static Species findSpecies(int number){
        for(Species s : Species.values()){
            if(s.number == number){
                return s;
            }
        }
        return null;
    }
}
