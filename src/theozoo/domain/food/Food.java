package theozoo.domain.food;

public class Food {
    private final String name;
    private final int fullnessPoint;
    final int FULLNESS_POINT_MIN_LIMIT = 0;

    public Food(String name, int fullnessPoint){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("먹이 이름은 비어 있을 수 없습니다.");
        }

        if (fullnessPoint < FULLNESS_POINT_MIN_LIMIT) {
            throw new IllegalArgumentException("포만감 증가량은 반드시 1 이상이어야 합니다.");
        }
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
