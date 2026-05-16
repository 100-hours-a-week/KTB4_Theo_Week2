public class Animal {
    private String name;
    private int age;
    private String species;
    private int fullness;

    Animal(String name, int age, String species, int fullness){
        this.name = name;
        this.age = age;
        this.species = species;
        this.fullness = fullness;
    }

    public String getName(){
        return name;
    }

    public String getSpecies(){
        return species;
    }

    public int getFullness(){
        return fullness;
    }

    public void printInfo(){
        System.out.println("이름: " + name);
        System.out.println("나이: " + age);
        System.out.println("종: " + species);
        System.out.println("포만감: " + getFullnessStatus());
        System.out.println("=======================");
    }

    public void feed(Food food) { // 동물이 먹이 먹기
        if (fullness >= 70) {
            System.out.println(name + "는 이미 배부른 상태입니다.");
            return;
        }
        fullness += food.getFullnessPoint();
        if (fullness>100) {
            fullness = 100;
        }
        System.out.println(name + "에게 " + food.getName() + "을/를 주었습니다.");
        System.out.println("현재 상태 : " + getFullnessStatus()); // 포만감 출력
    }

    public String getFullnessStatus() { // 동물의 배고픔 상태 출력하기
        if (fullness <= 49) {
            return "배고픔";
        } else if (fullness <= 69) {
            return "조금 배고픔";
        } else {
            return "배부름";
        }
    }

    public void makeSound(){
        System.out.println("동물 소리");
    }
}
