import java.util.ArrayList;
import java.util.Scanner;

public class ZooSystem {
    Scanner sc = new Scanner(System.in);

    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<Food> foods = new ArrayList<>();

    public void start(){ // 동물원 시스템 시작
        // 동물 리스트와 음식 리스트 초기화
        animals.add(new Lion("Max",7, "Mammal", 40));
        animals.add(new Eagle("Phoenix",4,"Bird", 50));
        animals.add(new Snake("Leo",3,"Reptile", 70));

        foods.add(new Food("Meat",40));
        foods.add(new Food("Fish",30));
        foods.add(new Food("Fruit",20));
        foods.add(new Food("Vegetable",15));

        System.out.println("Theo 동물원 시스템 시작!");
        System.out.println("시스템 옵션 중에서 선택할 활동을 결정해주세요!");
        while (true) {
            System.out.println();
            System.out.println("===== TheoZoo 동물 관리 시스템 =====");
            System.out.println("1. 전체 동물 목록 조회");
            System.out.println("2. 종별 동물 조회");
            System.out.println("3. 동물 선택 메뉴");
            System.out.println("4. 프로그램 종료");
            System.out.print("메뉴 선택: ");

            int menu = sc.nextInt();

            if (menu == 1) {
                showAllAnimals();
            } else if (menu == 2) {
                showAnimalsBySpecies();
            } else if (menu == 3) {
                animalActionMenu();
            } else if (menu == 4) {
                System.out.println("TheoZoo 동물 관리 시스템을 종료합니다.");
                break;
            } else {
                System.out.println("잘못된 메뉴 번호입니다. 다시 선택해주세요.");
            }
        }
    }

    public void showAllAnimals(){ // 모든 동물 조회
        System.out.println("=======================");
        System.out.println("저희 동물원 동물들을 소개합니다!");
        for(Animal animal : animals){
            animal.printInfo();
        }
    }

    public void showAnimalsBySpecies(){ // 종의 종류에 해당하는 동물 검색하기
        System.out.println("1. 포유류");
        System.out.println("2. 조류");
        System.out.println("3. 파충류");
        System.out.print("조회할 동물의 종을 선택하세요 : ");

        int choice = sc.nextInt();
        String selectedSpecies;

        if (choice == 1) {
            selectedSpecies = "Mammal";
        } else if (choice == 2) {
            selectedSpecies = "Bird";
        } else if (choice == 3) {
            selectedSpecies = "Reptile";
        } else {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        System.out.println("===== " + selectedSpecies + " 동물 목록 =====");

        for (Animal animal : animals) {
            if (animal.getSpecies().equals(selectedSpecies)) {
                animal.printInfo();
            }
        }
    }

    public void animalActionMenu(){ // 동물에게 행위를 할 수 있는 메뉴
        Animal selectedAnimal = selectAnimal();

        if (selectedAnimal == null) {
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("선택한 동물: " + selectedAnimal.getName() + " // 포만감 상태 수치: " + selectedAnimal.getFullness());
            System.out.println("1. 먹이 주기");
            System.out.println("2. 울음소리 듣기");
            System.out.println("3. 메인 메뉴로 돌아가기");
            System.out.print("행동 선택: ");

            int choice = sc.nextInt();

            switch(choice){
                case 1:
                    if(selectedAnimal.getFullness()>=70){
                        System.out.println("해당 동물은 이미 배가불러서 먹이를 줄 수 없습니다!");
                        break;
                    }
                    feedAnimal(selectedAnimal);
                    break;
                case 2:
                    listenAnimalSound(selectedAnimal);
                    break;
                case 3:
                    System.out.println("메인 메뉴로 돌아갑니다.");
                    return;
            }
        }
    }

    public Animal selectAnimal(){ // 행위를 지정할 동물 선택하기
        System.out.println("==== 동물 선택 ====");

        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
            System.out.println((i + 1) + ". " + animal.getName() + " (" + animal.getSpecies() + ") // 배고픔 상태: " + animal.getFullnessStatus());
        }

        int choice;
        while(true){
            System.out.print("선택할 동물 번호를 입력하세요: ");
            choice = sc.nextInt();
            if(choice<1 || choice > animals.size()){ // 잘못된 번호 입력 시 다시 입력받기.
                System.out.println("== 잘못된 번호입니다. 다시 선택해주세요 ==");
            }else{
                break;
            }
        }
        return animals.get(choice-1);
    }

    public void feedAnimal(Animal animal) { // 동물에게 먹이주기 (먹이 선택 과정 포함)
        // 먹이종류 선택하기
        System.out.println("줄 먹이를 선택해주세요!");

        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            System.out.println("번호: " + (i+1) + " // 음식: " + food.getName() +
                    " // 포만감 증가량: " + food.getFullnessPoint());
        }
        System.out.print("줄 먹이 번호 입력 : ");

        int choice = sc.nextInt();
        Food selectedFood = foods.get(choice-1);

        animal.feed(selectedFood);
        System.out.println("성공적으로 먹이를 주었습니다!");
    }

    public void listenAnimalSound(Animal animal) {
        animal.makeSound();
    }
}
