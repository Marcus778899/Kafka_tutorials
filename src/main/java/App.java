import com.KafkaCustom;
import com.KafkaProduce;
import java.util.Scanner;

public class App {

    String groupID ;
    String key ;
    String topic ;
    String event ;

    public App(String topic, String event){
        this.groupID = "marcus-group";
        this.topic = topic;
        this.event = event;
    }

    public void Consumer(){
        KafkaCustom app = new KafkaCustom(groupID, topic);
        app.consumeMessage();
    }

    public void Producer(String key){
        KafkaProduce produce = new KafkaProduce();
        produce.generateTopic(topic, key, event);
        produce.closeProducer();
    }
        
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Kafka App");
        System.out.println("Please select a topic:");
        String topic = scanner.nextLine();
        System.out.println("1. Start as Producer");
        System.out.println("2. Start as Consumer");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Starting as Producer...");
            System.out.print("Enter key: ");
            String key = scanner.nextLine();
            while (key.isEmpty()) {
                System.out.println("Key cannot be empty");
                key = scanner.nextLine();
            }
            System.out.print("Enter event: ");
            String event = scanner.nextLine();
            while (event.isEmpty()) {
                System.out.println("Event cannot be empty");
                event = scanner.nextLine();
                }
                App app = new App(topic,event);
                app.Producer(key);
        } 
        else if (choice == 2) {
            System.out.println("Starting as Consumer...");
            App app = new App("",topic);
            app.Consumer();
        } 
        else {
            System.out.println("Invalid choice. Exiting...");
        }
        scanner.close();
    }
}
