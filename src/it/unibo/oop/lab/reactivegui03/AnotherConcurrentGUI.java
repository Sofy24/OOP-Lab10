package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;




public class AnotherConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JLabel label = new JLabel("0");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private static final  double W = 0.2;
    private static final  double H = 0.1;
    private static final int WAITSECS = 10_000;
    private final Agent agent = new Agent();

    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * W), (int) (screenSize.getHeight() * H));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel canvas = new JPanel();
        this.getContentPane().add(canvas);
        canvas.add(up);
        canvas.add(down);
        canvas.add(stop);
        canvas.add(label);
        this.setVisible(true);



        new Thread(() -> {
            try {
                Thread.sleep(WAITSECS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.stopCounting();
        }).start();
        new Thread(agent).start();



        stop.addActionListener(t -> {
                agent.stopCounting();
                down.setEnabled(false);
                up.setEnabled(false);
                stop.setEnabled(false);

        });

        down.addActionListener(t -> agent.isIncreasing(false));
        up.addActionListener(t -> agent.isIncreasing(true));


}


    private void stopCounting() {
        agent.stopCounting();
        down.setEnabled(false);
        up.setEnabled(false);
        stop.setEnabled(false);

} 




        class Agent implements Runnable {

            private volatile boolean stop;
            private volatile int counter;
            private volatile boolean inc = true;


            @Override
            public void run() {
                while (!this.stop) {
                    try {

                        if (this.inc) {
                            this.counter++;
                        } else {
                            this.counter--;
                        }
                        SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.label.setText(Integer.toString(counter)));


                        Thread.sleep(100);
                    } catch (InvocationTargetException | InterruptedException ex) {

                        ex.printStackTrace();
                    }
                }
            }


            public void stopCounting() {
                this.stop = true;
            }

            public void isIncreasing(final boolean val) {
                this.inc = val;
            }

        }
    }









