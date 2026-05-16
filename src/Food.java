public class Food {
    private String name;
    private int fullnessPoint;

    Food(String name, int fullnessPoint){
        this.name = name;
        this.fullnessPoint = fullnessPoint;
    }

    public String getName(){
        return name;
    }

    public int getFullnessPoint(){
        return fullnessPoint;
    }
}
