public class Main {
    public static void main(String[] args) throws InterruptedException {
         final long cookingThreshold = 3000;
         final long orderingThreshold = 1500;
         final long eatingThreshold = 2000;
         final long orderDeliveryThreshold = 500;
         final long clientQueueThreshold = 1000;
         final int waitersCount = 3;
         final int clientsCount = 5;

         Restaurant restaurant = new Restaurant(cookingThreshold, orderingThreshold, eatingThreshold, orderDeliveryThreshold);

         for (int i = 0; i < waitersCount; i++) {
             new Thread(null, restaurant::waiterServesClient, "Официант " + (i + 1)).start();
         }

         for (int i = 0; i < clientsCount; i++) {
             Thread.sleep(clientQueueThreshold);
             new Thread(null, restaurant::clientMakesOrder, "Посетитель " + (i + 1)).start();
         }

    }
}
