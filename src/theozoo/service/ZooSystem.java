package theozoo.service;

import theozoo.domain.animal.*;
import theozoo.domain.food.Food;

import java.util.*;
import java.util.concurrent.*;

public class ZooSystem {
    private final int FEEDING_QUEUE_CAPACITY = 100;
    private final int FEED_THREAD_COUNT = 3;
    Scanner sc = new Scanner(System.in);

    private final List<Animal> animals = new ArrayList<>();
    private final List<Food> foods = new ArrayList<>();
    private final BlockingQueue<Animal> feedingQueue = new LinkedBlockingQueue<>(FEEDING_QUEUE_CAPACITY);
    private final Set<Animal> requestedAnimalSet = ConcurrentHashMap.newKeySet();

    private Thread hungerThread;
    private ExecutorService feedExecutor;
    // private Thread feedThread; // 위 ExecutorService로 개선으로 인한 주석처리
    private volatile boolean running = false; // 실행 여부 (작업 반복여부)

    // hungerThread 와 feedThread 가 모두 현재 업무의 실행여부의 최신 정보를 확인할 수 있도록 volatile 사용 (가시성)
    private void startThreads() {
        if (running) {
            System.out.println("이미 자동 업무가 실행 중입니다!");
            return;
        }

        running = true; // 작업 반복가능상태

        hungerThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);

                    for (Animal animal : animals) {
                        animal.decreaseFullness();
                        System.out.println(animal.getName() + "의 포만감이 10 감소했습니다!");

                        if (animal.isHungry()) {
                            boolean request = requestedAnimalSet.add(animal);

                            if (request) {
                                feedingQueue.put(animal);
                                System.out.println("HungerThread: " + animal.getName()
                                        + "의 포만감이 낮아 먹이 요청 큐에 추가했습니다.");
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("HungerThread가 종료되었습니다!");
        }, "HungerThread");
//        feedThread = new Thread(() -> { // feedThread 작업내용
//            while (running) {
//                Animal animal = null;
//
//                try {
//                    animal = feedingQueue.take();
//
//                    Food randomFood = getRandomFood();
//                    System.out.println("FeedThread: " + animal.getName() + "에게 먹이를 주기 시작합니다.");
//                    animal.feed(randomFood);
//                    System.out.println("6을 입력하면 작업이 중단됩니다!");
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    break;
//
//                } finally {
//                    if (animal != null) {
//                        requestedAnimalSet.remove(animal);
//                    }
//                }
//            }
//
//            System.out.println(Thread.currentThread().getName() + " 종료");
//        }, "FeedThread");
        hungerThread.start();
        feedExecutor = Executors.newFixedThreadPool(FEED_THREAD_COUNT);
        // feedThread.start();
        for (int i = 0; i < FEED_THREAD_COUNT; i++) {
            feedExecutor.execute(()-> {
                while (running) {
                    Animal animal = null;
                    try {
                        animal = feedingQueue.take();
                        Food randomFood = getRandomFood();
                        System.out.println("FeedThread: " + animal.getName() + "에게 먹이를 주기 시작합니다.");
                        animal.feed(randomFood);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } finally {
                        if (animal != null) {
                            requestedAnimalSet.remove(animal);
                        }
                    }
                }
                System.out.println(Thread.currentThread().getName() + " 종료");
            });
        }
    }

    public Food getRandomFood(){
        // ThreadLocalRandom 을 사용함으로써 각 스레드 별 seed를 각각 부여받음 -> 스레드 충돌상황을 예방 -> 성능개선
        int randomIndex = ThreadLocalRandom.current().nextInt(foods.size());
        return foods.get(randomIndex);
    }

    public void start(){ // 동물원 시스템 시작
        // 동물 리스트와 음식 리스트 초기화
        animals.add(new Lion("Max",7, 40));
        animals.add(new Eagle("Phoenix",4, 50));
        animals.add(new Snake("Leo",3, 70));

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
            System.out.println("5. 자동 업무 시작");
            System.out.println("6. 자동 업무 중단");
            System.out.print("메뉴 선택: ");

            int menu = sc.nextInt();

            // 자동 작업중일 떄는 6번 아니면 4번만 선택가능하도록 제한
            if (running && menu != 6 && menu != 4) {
                System.out.println("자동 업무 실행 중에는 6번(자동 업무 중단) 또는 4번(프로그램 종료)만 선택할 수 있습니다.");
                continue;
            }

            if (menu == 1) {
                showAllAnimals();
            } else if (menu == 2) {
                showAnimalsBySpecies();
            } else if (menu == 3) {
                animalActionMenu();
            } else if (menu == 4) {
                System.out.println("TheoZoo 동물 관리 시스템을 종료합니다.");
                stopThreads();
                break;
            } else if (menu == 5){
                System.out.println("동물 먹이주기 자동화 작업을 시작합니다.");
                System.out.println("6을 입력해야 작업이 중단됩니다!");
                startThreads();
            } else if(menu == 6){
                System.out.println("동물 먹이주기 자동화 작업중단을 시작합니다.");
                stopThreads();
            }else {
                System.out.println("잘못된 메뉴 번호입니다. 다시 선택해주세요.");
            }
        }
    }


    private void stopThreads() {
        if (!running) {
            System.out.println("현재 실행 중인 자동 업무가 없습니다.");
            return;
        }

        running = false; // 실행상태 false로 바꿔 작업반복에서 벗어나도록하기.

        if (hungerThread != null) {
            hungerThread.interrupt();
        }

//        if (feedThread != null) { // feedThread가 존재했을 때 정지처리
//            feedThread.interrupt();
//        }

        if(feedExecutor != null){
            feedExecutor.shutdownNow();
        }

        try {
            // HungerThread가 완전히 종료될 때까지 대기
            if (hungerThread != null) {
                hungerThread.join();
            }

            // feedExecutor의 스레드이 완전히 종료될 때까지 대기
            if (feedExecutor != null) {
                boolean terminated = feedExecutor.awaitTermination(3, TimeUnit.SECONDS);

                if (!terminated) { // timeout 시간 내에 shutdown이 되지 않았으면 작동 : false
                    System.out.println("일부 FeedThread가 아직 종료되지 않았습니다.");
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Queue와 Set 비워주기
        feedingQueue.clear();
        requestedAnimalSet.clear();

        System.out.println("동물 먹이주기 자동화 작업을 중단 완료했습니다.");
    }

    public void showAllAnimals(){ // 모든 동물 조회
        System.out.println("=======================");
        System.out.println("저희 동물원 동물들을 소개합니다!");
        for(Animal animal : animals){
            animal.printInfo();
        }
    }

    public void showAnimalsBySpecies(){ // 종의 종류에 해당하는 동물 검색하기
        while(true){
            // 직접 번호와 종의 이름을 호출해서 출력
            for(Species s : Species.values()){
                System.out.println(s.getNumber() + ": " + s.getName());
            }
            System.out.print("조회할 동물의 종을 선택하세요 : ");
            try {
                int choice = sc.nextInt();
                Species selectedSpecies = Species.findSpecies(choice);
                if (selectedSpecies == null) {
                    System.out.println("해당하는 종이 없습니다. 다시 입력해주세요!");
                    continue;
                }
                System.out.println("===== " + selectedSpecies + " 동물 목록 =====");
                for(Animal animal : animals) {
                    if (animal.getSpecies() == selectedSpecies) {
                        animal.printInfo();
                    }
                }
                break;
            }catch(InputMismatchException e){
                System.out.println("원하는 종을 숫자로만 입력해주세요!!");
                sc.nextLine(); // 잘못 입력된 문자열 입력버퍼에서 제거
            }
        }
    }

    public void animalActionMenu(){ // 동물에게 행위를 할 수 있는 메뉴
        Animal selectedAnimal = selectAnimal();

        if (selectedAnimal == null) {
            return;
        }

        while (true) {System.out.println();
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
            System.out.println((i + 1) + ". " + animal.getName() + " (" + animal.getSpecies() + ") " +
                    "// 배고픔 상태: " + animal.getFullnessStatus());
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
        while (true) {
            // 먹이종류 선택하기
            System.out.println("줄 먹이를 선택해주세요!");

            for (int i = 0; i < foods.size(); i++) {
                Food food = foods.get(i);
                System.out.println("번호: " + (i+1) + " // 음식: " + food.getName() +
                        " // 포만감 증가량: " + food.getFullnessPoint());
            }
            System.out.print("줄 먹이 번호 입력 : ");
            try{
                int choice = sc.nextInt();
                if(1>choice || choice>foods.size()){
                    System.out.println("주어진 먹이 번호중에서 입력해주세요!");
                    continue;
                }
                Food selectedFood = foods.get(choice-1);
                animal.feed(selectedFood);
                System.out.println("성공적으로 먹이를 주었습니다!");
                break;
            }catch(InputMismatchException e){
                System.out.println("먹이 숫자로만 입력해주세요!!");
                sc.nextLine(); // 잘못 입력된 문자열 입력버퍼에서 제거
            }
        }
    }

    public void listenAnimalSound(Animal animal) {
        animal.makeSound();
    }
}
