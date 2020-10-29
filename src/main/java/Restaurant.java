import java.util.LinkedList;
import java.util.Queue;

public class Restaurant {
    private final long cookingThreshold;
    private final long orderingThreshold;
    private final long eatingThreshold;
    private final long orderDeliveryThreshold;
    private final Queue<Client> clients = new LinkedList<>();
    private final Food food = new Food();

    public Restaurant(long cookingThreshold, long orderingThreshold, long eatingThreshold, long orderDeliveryThreshold) {
        this.cookingThreshold = cookingThreshold;
        this.orderingThreshold = orderingThreshold;
        this.eatingThreshold = eatingThreshold;
        this.orderDeliveryThreshold = orderDeliveryThreshold;
        System.out.println("Повар на работе!");
    }

    public void waiterServesClient() {
        System.out.printf("%s на работе!\n", Thread.currentThread().getName());
        while (true) {//(!Thread.interrupted()) {
            synchronized (clients) {
                if (clients.size() == 0) {
                    try {
                        clients.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.printf("%s взял заказ\n", Thread.currentThread().getName());
            clients.poll();
            try {
                System.out.printf("Заказ готовится\n");
                Thread.sleep(cookingThreshold);
                System.out.printf("%s несёт заказ\n", Thread.currentThread().getName());
                Thread.sleep(orderDeliveryThreshold);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (food) {
                food.notify();
            }
            if (clients.size() == 0) {
                break;
            }
        }
    }

    public void clientMakesOrder() {
        System.out.printf("%s в ресторане\n", Thread.currentThread().getName());
        try {
            Thread.sleep(orderingThreshold);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (clients) {
            clients.offer(new Client());
            System.out.printf("%s сделал заказ\n", Thread.currentThread().getName());
            clients.notify();
        }
        synchronized (food) {
            try {
                food.wait();
                System.out.printf("%s приступил к еде\n", Thread.currentThread().getName());
                Thread.sleep(eatingThreshold);
                System.out.printf("%s вышел из ресторана\n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Food {}
}
