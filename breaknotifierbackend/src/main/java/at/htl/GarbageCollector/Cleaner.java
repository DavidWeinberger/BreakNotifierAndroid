package at.htl.GarbageCollector;

import at.htl.Client.Client;
import at.htl.Client.ClientRepository;

public class Cleaner implements Runnable {
    @Override
    public void run() {
        System.out.println("----Started Cleaner----");
        try {
            while (true) {
                for (Client client : ClientRepository.clients) {
                    if (!client.getThread().isAlive()) {
                        ClientRepository.clients.remove(client);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
