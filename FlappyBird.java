import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class FlappyBird extends JPanel implements ActionListener {
    private final int BIRD_SIZE = 40; // Increased bird size
    private int birdY;
    private int velocity = 0;
    private List<Rectangle> pipes;
    private Timer timer;
    private boolean gameOver = false;
    private int screenWidth, screenHeight;
    private int score = 0; // Score variable

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        frame.setUndecorated(true); // Remove title bar
        frame.add(this);
        frame.setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        birdY = screenHeight / 2;

        pipes = new ArrayList<>();
        timer = new Timer(20, this);
        timer.start();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    velocity = -10;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
                    restartGame();
                }
            }
        });

        addInitialPipes();
    }

    private void addPipe(int xOffset) {
        int gap = 300; // Increased gap between pipes
        int pipeWidth = 100; // Increased pipe width
        int pipeHeight = (int) (Math.random() * (screenHeight - gap));
        pipes.add(new Rectangle(screenWidth + xOffset, 0, pipeWidth, pipeHeight));
        pipes.add(new Rectangle(screenWidth + xOffset, pipeHeight + gap, pipeWidth, screenHeight - pipeHeight - gap));
    }

    private void addInitialPipes() {
        for (int i = 0; i < 4; i++) {
            addPipe(i * (screenWidth / 4));
        }
    }

    private void restartGame() {
        birdY = screenHeight / 2;
        velocity = 0;
        pipes.clear();
        addInitialPipes();
        gameOver = false;
        score = 0; // Reset score
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        birdY += velocity;
        velocity += 1;

        for (Rectangle pipe : pipes) {
            pipe.x -= 5;
        }

        if (pipes.get(0).x + pipes.get(0).width < 0) {
            pipes.remove(0);
            pipes.remove(0);
            addPipe(0);
            score++; // Increase score when pipes are removed
        }

        // Check for collisions
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE))) {
                gameOver = true;
                timer.stop();
                break;
            }
        }

        if (birdY > screenHeight || birdY < 0) {
            gameOver = true;
            timer.stop();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(Color.orange);
        g.fillRect(100, birdY, BIRD_SIZE, BIRD_SIZE);

        g.setColor(Color.green);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.white); // Set score color to white
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Score: " + score, 20, 40); // Draw score in top left corner

        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", screenWidth / 2 - 150, screenHeight / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Press Enter to Restart", screenWidth / 2 - 150, screenHeight / 2 + 50);
        }
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}
