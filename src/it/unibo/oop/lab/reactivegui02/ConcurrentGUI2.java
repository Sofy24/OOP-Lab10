package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;



public class ConcurrentGUI2 extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JLabel label = new JLabel("0");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private static final  double W = 0.2;
    private static final  double H = 0.1;

    public ConcurrentGUI2() {
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

        final Agent agent = new Agent();
        new Thread(agent).start();



        stop.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                agent.stopCounting();
                down.setEnabled(false);
                up.setEnabled(false);
                stop.setEnabled(false);

            }
        });

        down.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
                agent.isIncreasing(false);
            }
        });

        up.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
                agent.isIncreasing(true);
            }
        });

    }

        class Agent implements Runnable {

            private volatile boolean stop;
            private volatile int counter;
            private volatile boolean inc = true;

            @Override
            public void run() {
                while (!this.stop) {
                    try {

                        SwingUtilities.invokeAndWait(() -> ConcurrentGUI2.this.label.setText(Integer.toString(counter)));
                        if (this.inc) {
                            this.counter++;
                        } else {
                            this.counter--;
                        }

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
