package theozoo.domain.animal;

import theozoo.domain.food.Food;

public abstract class Animal {
    private final String name;
    private final int age;
    private int fullness;
    private static final int FULLNESS_HUNGRY_LIMIT = 49; // 배고픔 상태
    private static final int FULLNESS_NORMAL_LIMIT = 69; // 조금 배고픔 상태
    private static final int FULLNESS_MAX_LIMIT = 100; // 포만감 한계 상태
    private static final int FULLNESS_MIN_LIMIT = 0; // 포만감 최소 상태

    public Animal(String name, int age, int fullness) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("동물 이름은 비어 있을 수 없습니다.");
        }

        if (age < 0) {
            throw new IllegalArgumentException("동물 나이는 음수일 수 없습니다.");
        }

        if (fullness < FULLNESS_MIN_LIMIT || fullness > FULLNESS_MAX_LIMIT) {
            throw new IllegalArgumentException("포만감은 0 이상 100 이하만 가능합니다.");
        }

        this.name = name;
        this.age = age;
        this.fullness = fullness;
    }

    public String getName(){
        return name;
    }

    public int getFullness(){
        return fullness;
    }

    public void printInfo(){
        System.out.println("이름: " + name);
        System.out.println("나이: " + age);
        System.out.println("포만감: " + getFullnessStatus());
        System.out.println("=======================");
    }

    synchronized public void feed(Food food) { // 동물이 먹이 먹기
        if (fullness > FULLNESS_NORMAL_LIMIT) {
            System.out.println(name + "는 이미 배부른 상태입니다.");
            return;
        }
        fullness += food.getFullnessPoint();
        if (fullness>FULLNESS_MAX_LIMIT) {
            fullness = FULLNESS_MAX_LIMIT;
        }
        System.out.println(name + "에게 " + food.getName() + "을/를 주었습니다.");
        System.out.println(name + "의 현재 상태 : " + getFullnessStatus() + " / 숫자 : " + getFullness()); // 포만감 출력
    }

    public String getFullnessStatus() { // 동물의 배고픔 상태 출력하기
        if (fullness <= FULLNESS_HUNGRY_LIMIT) {
            return "배고픔";
        } else if (fullness <= FULLNESS_NORMAL_LIMIT) {
            return "조금 배고픔";
        } else {
            return "배부름";
        }
    }

    public synchronized void decreaseFullness(){ // HungryThread 가 호출할 메서드 (초당 10씩 포만감 감소)
        fullness = Math.max(fullness-10,FULLNESS_MIN_LIMIT); // 음수가 되지 않도록 처리
    }

    public synchronized boolean isHungry(){ // HungryThread 가 배고픈 동물을 찾기 위한 메서드
        return fullness<=FULLNESS_HUNGRY_LIMIT;
    }

    public abstract void makeSound();

    public abstract Species getSpecies();
}
