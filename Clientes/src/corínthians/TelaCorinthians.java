package corínthians;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public final class TelaCorinthians extends javax.swing.JFrame {

    private JTextArea Noticias;
    ArrayList<String> x = new ArrayList();

    public TelaCorinthians() {
        super("Corínthians");
        initComponents();
    }

    private void initComponents() {
        Container c = this.getContentPane();
        c.setLayout(null);
        Noticias = new JTextArea();

        Noticias.setBounds(30, 35, 530, 500);
        Noticias.setBackground(Color.WHITE);
        Noticias.setBorder(new LineBorder(Color.BLACK));
        Noticias.setEditable(false);
        add(Noticias);

        setTitle("Notícias do Corínthians");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        Enviar();
    }

    private static final String QUEUE_NAME = "CORINTHIANS";

    private void Enviar() {
        System.out.println("Starting the receiver program!");

        try {
            String msg = null;
            // Create the connection.
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://vtfpvxxk:gJl7ZJYCzJbLKkK85i2j0SnM5z-ppgHL@rhino.rmq.cloudamqp.com/vtfpvxxk");
            Connection connection = factory.newConnection();

            // Create the channel and the queue.
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Create the consumer.
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                String dois = new String(delivery.getBody());

                Noticias.append(dois + "\n\n");
            }
        } catch (ConsumerCancelledException | ShutdownSignalException | IOException | InterruptedException | URISyntaxException | KeyManagementException | NoSuchAlgorithmException | TimeoutException e) {
            System.out.println(String.format("An error occurs [%s]. [%s]", e.getMessage(), e));
        }
    }

    public static void main(String args[]) {
        new TelaCorinthians().setVisible(true);
    }

}
