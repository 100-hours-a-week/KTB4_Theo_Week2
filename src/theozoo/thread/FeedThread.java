package theozoo.thread;

import theozoo.domain.animal.Animal;
import theozoo.domain.food.Food;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class FeedThread implements Runnable{
    private final BlockingQueue<Animal> feedingQueue; // 배고픈 동물이 담긴 큐
    private final List<Food> foods; // 줄 음식 종류들
    private final Set<Animal> requestedAnimalSet; // 먹이 요청을 받은 동물 목록
    private volatile boolean running = true;

    public FeedThread(BlockingQueue<Animal> feedingQueue, List<Food> foods, Set<Animal> requestedAnimalSet){
        this.feedingQueue = feedingQueue;
        this.foods = foods;
        this.requestedAnimalSet = requestedAnimalSet;
    }

    @Override
    public void run(){
        while(running){
            Animal animal = null;
            try{ // HungerThread 가 feedingQueue에 동물을 넣으면 그때 깨어나서 작업을 시작한다.
                animal = feedingQueue.take();
                // 음식 리스트 중에서 하나를 랜덤으로 선택한다.
                Food randomFood = getRandomFood();
                System.out.println("FeedThread: " + animal.getName() + "에게 먹이를 주기 시작합니다.");
                animal.feed(randomFood); // 동물에게 먹이주기
            }catch (InterruptedException e){
                running = false;
                Thread.currentThread().interrupt();
            }finally{
                requestedAnimalSet.remove(animal); // feed() 하는 도중에 오류 걸려도 제거는 반드시 진행해야함!
            }
        }
    }

    public Food getRandomFood(){
        // ThreadLocalRandom 을 사용함으로써 각 스레드 별 seed를 각각 부여받음 -> 스레드 충돌상황을 예방 -> 성능개선
        int randomIndex = ThreadLocalRandom.current().nextInt(foods.size());
        return foods.get(randomIndex);
    }

    public void shutdown(){
        running = false;
    }
}
