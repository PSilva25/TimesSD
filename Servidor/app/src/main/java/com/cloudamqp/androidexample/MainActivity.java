package com.cloudamqp.androidexample;


import java.io.UnsupportedEncodingException;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private MessageConsumer mConsumer;
    private TextView mOutput;
    private String QUEUE_NAME2 = "PALMEIRAS";
    private String QUEUE_NAME = "CORINTHIANS";
    private String EXCHANGE_NAME = "noticias";
    private String message = "";
    private String name = "";
    private String[] corinthias = {"Corinthians", "corinthians", "Timão", "timão", "timao", "corintia", "Corintia"};
    private String[] palmeiras = {"Palmeiras", "palmeiras", "Palmeira", "palmeira", "Palmera", "palmera"};
    private TextView teste;
    EditText etv1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "RabbitMQ Chat Service!",
                Toast.LENGTH_LONG).show();
        teste = (TextView) findViewById(R.id.textView);
        etv1 = (EditText) findViewById(R.id.editText);

    }


    public void conectar(View v) {

        message = etv1.getText().toString();
        int test = separa(message);

        if (test == 1) {
            sendCorinthians cnt = new sendCorinthians();
            cnt.execute(message);
            Toast.makeText(getApplicationContext(), "Corinthians!", Toast.LENGTH_LONG).show();

        }

        if (test == 2) {
            sendPalmeiras cnt2 = new sendPalmeiras();
            cnt2.execute(message);
            Toast.makeText(getApplicationContext(), "Palmeiras!", Toast.LENGTH_LONG).show();

        }

        if (test == 3) {
            sendCorinthians cnt = new sendCorinthians();
            cnt.execute(message);

            sendPalmeiras cnt2 = new sendPalmeiras();
            cnt2.execute(message);

            Toast.makeText(getApplicationContext(), "Palmeiras e Corinthians!", Toast.LENGTH_LONG).show();

        }

        Toast.makeText(getApplicationContext(), "Conectado!", Toast.LENGTH_LONG).show();

        etv1.setText("");




        Toast.makeText(getApplicationContext(), "Conectado!", Toast.LENGTH_LONG).show();


    }


    public int separa(String Mensagem) {
        String[] parts = Mensagem.split("\\s+");
        int test = 0;
        int c = 0, p = 0;
        int x, y;

        for (x = 0; x < parts.length; x++) {
            for (y = 0; y < corinthias.length; y++)
                if (parts[x].equals(corinthias[y])) {
                    c = 1;
                }
        }

        for (x = 0; x < parts.length; x++) {
            for (y = 0; y < palmeiras.length; y++)
                if (parts[x].equals(palmeiras[y])) {
                    p = 1;
                }
        }

        if (c == 1 && p == 0)
            test = 1;
        if (c == 0 && p == 1)
            test = 2;
        if (c == 1 && p == 1)
            test = 3;

        return test;

    }

    private class sendCorinthians extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {

                ConnectionFactory factory = new ConnectionFactory();
                factory.setUri("amqp://vtfpvxxk:gJl7ZJYCzJbLKkK85i2j0SnM5z-ppgHL@rhino.rmq.cloudamqp.com/vtfpvxxk");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.confirmSelect();

                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

                channel.close();

                connection.close();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

    }

    private class sendPalmeiras extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {

                ConnectionFactory factory = new ConnectionFactory();
                factory.setUri("amqp://vtfpvxxk:gJl7ZJYCzJbLKkK85i2j0SnM5z-ppgHL@rhino.rmq.cloudamqp.com/vtfpvxxk");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.confirmSelect();

                channel.basicPublish("", QUEUE_NAME2, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

                channel.close();

                connection.close();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

    }


    private class consumerconnect extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {


                // Connect to broker
                mConsumer.connectToRabbitMQ();


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onPause();
        new consumerconnect().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConsumer.dispose();
    }
}