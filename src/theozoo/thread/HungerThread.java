package theozoo.thread;

import theozoo.domain.animal.Animal;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class HungerThread implements Runnable{
    private final List<Animal> animals;
    private final BlockingQueue<Animal> feedingQueue; // 음식을 주기 위한 작업 큐
    private final Set<Animal> requestedAnimalSet; // 배고픔 신호를 받은 동물들 (중복된 동물이 들어가는 것을 방지하기 위해 set 사용)
    private volatile boolean running = true;

    public HungerThread(List<Animal> animals, BlockingQueue<Animal> feedingQueue, Set<Animal> requestedAnimalSet){
        this.animals = animals;
        this.feedingQueue = feedingQueue;
        this.requestedAnimalSet = requestedAnimalSet;
    }

    @Override
    public void run(){
        while(running){
            try{
                Thread.sleep(1000);
                for(Animal animal : animals){
                    animal.decreaseFullness(); // 1초마다 모든 동물들의 포만감을 1 줄인다
                    System.out.println(animal.getName() + "의 포만감이 10 감소했습니다");
                    if(animal.isHungry()){ // 배고픈 동물을 큐에 삽입
                        boolean request = requestedAnimalSet.add(animal); // 이미 있는 동물이면 false 반환
                        if(request){
                            feedingQueue.put(animal);
                            System.out.println("HungerThread: " + animal.getName()+"의 포만감이 낮아 먹이 요청 큐에 추가했습니다.");
                        }
                    }
                }
            }catch (InterruptedException e){ // 중단 명령을 받으면 스레드 종료
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shutdown(){
        running = false;
    }
}
