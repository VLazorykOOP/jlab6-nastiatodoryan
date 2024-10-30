import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AnimationExamples extends JPanel implements ActionListener {
    private Timer timer;
    private Random random = new Random();
    
    // For Task 1: Moving Strings
    private String[] strings = {"Hello", "Java", "Animation", "Example"};
    private int[] xPositions = new int[strings.length];
    private int[] yPositions = new int[strings.length];
    private int[] xDirections = new int[strings.length];
    private int[] yDirections = new int[strings.length];
    
    // For Task 2: Bouncing Circle
    private int circleX = 50, circleY = 50;
    private int circleDX = 3, circleDY = 3;
    private int circleRadius = 30;
    
    // For Task 3: Approaching and Receding Circle
    private int approachingCircleRadius = 10;
    private boolean circleApproaching = true;

    public AnimationExamples() {
        // Initialize random directions for strings
        for (int i = 0; i < strings.length; i++) {
            xPositions[i] = random.nextInt(300);
            yPositions[i] = random.nextInt(300);
            xDirections[i] = random.nextBoolean() ? 1 : -1;
            yDirections[i] = random.nextBoolean() ? 1 : -1;
        }
        
        timer = new Timer(30, this); // Updates every 30 ms
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Task 1: Moving Strings
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 0; i < strings.length; i++) {
            g.drawString(strings[i], xPositions[i], yPositions[i]);
        }
        
        // Task 2: Bouncing Circle
        g.setColor(Color.BLUE);
        g.fillOval(circleX, circleY, circleRadius * 2, circleRadius * 2);
        
        // Task 3: Approaching and Receding Circle
        g.setColor(Color.RED);
        g.fillOval(200 - approachingCircleRadius, 200 - approachingCircleRadius,
                   approachingCircleRadius * 2, approachingCircleRadius * 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update Task 1: Moving Strings
        for (int i = 0; i < strings.length; i++) {
            xPositions[i] += xDirections[i] * 2;
            yPositions[i] += yDirections[i] * 2;
            
            if (xPositions[i] < 0 || xPositions[i] > getWidth() - 50) {
                xDirections[i] *= -1;
            }
            if (yPositions[i] < 0 || yPositions[i] > getHeight() - 20) {
                yDirections[i] *= -1;
            }
        }
        
        // Update Task 2: Bouncing Circle
        circleX += circleDX;
        circleY += circleDY;
        
        if (circleX < 0 || circleX > getWidth() - circleRadius * 2) {
            circleDX *= -1;
        }
        if (circleY < 0 || circleY > getHeight() - circleRadius * 2) {
            circleDY *= -1;
        }
        
        // Update Task 3: Approaching and Receding Circle
        if (circleApproaching) {
            approachingCircleRadius += 1;
            if (approachingCircleRadius >= 50) circleApproaching = false;
        } else {
            approachingCircleRadius -= 1;
            if (approachingCircleRadius <= 10) circleApproaching = true;
        }
        
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Animation Examples");
        AnimationExamples animation = new AnimationExamples();
        frame.add(animation);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}