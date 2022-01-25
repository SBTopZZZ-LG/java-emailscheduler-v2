package Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animator {
    public interface AnimatorOptions {
        void stop();
    }

    public static void valueAnimate(long durationInMillis, ValueAnimatorListener listener) {
        valueAnimate(durationInMillis, 60, listener);
    }
    public static void valueAnimate(long durationInMillis, int frameRate, ValueAnimatorListener listener) {
        final long[] elapsed = new long[1];
        int delay = 1000 / frameRate;

        final AnimatorOptions[] options = new AnimatorOptions[1];
        Timer main = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (options[0] == null)
                    options[0] = new AnimatorOptions() {
                        @Override
                        public void stop() {
                            ((Timer) e.getSource()).stop();
                        }
                    };

                if (elapsed[0] >= durationInMillis)
                    ((Timer) e.getSource()).stop();

                float animate = (float)elapsed[0] / durationInMillis;
                if (animate > 1)
                    animate = 1;

                listener.animate(animate, options[0]);
                elapsed[0] += delay;
            }
        });
        main.start();
    }
    public interface ValueAnimatorListener {
        void animate(float value, AnimatorOptions options);
    }
}
