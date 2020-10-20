package flappy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class NewFlappyBird implements ActionListener, MouseListener, KeyListener
{
    public static NewFlappyBird flappy;
    public final int WIDTH = 600, HEIGHT = 800;
    public Graph renderer;
    public Rectangle birdy;
    public ArrayList<Rectangle> columns;
    public int gg, y, score;
    public boolean GameOver, Start;
    public Random random;

    public NewFlappyBird()
    {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Graph();
        random = new Random();

        jframe.add(renderer);
        jframe.setTitle("Welcome to my New Flappy Bird!");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        birdy = new Rectangle(WIDTH / 2 , HEIGHT / 2 , 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start)
    {
        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start)
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else
        {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column)
    {
        g.setColor(Color.blue.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump()
    {
        if (GameOver)
        {
            birdy = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            y = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            GameOver = false;
        }

        if (!Start)
        {
            Start = true;
        }
        else if (!GameOver)
        {
            if (y > 0)
            {
                y = 0;
            }
            y -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        int speed = 10;
        gg++;
        if (Start)
        {
            for (int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if (gg % 2 == 0 && y < 15)
            {
                y += 2;
            }

            for (int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);

                if (column.x + column.width < 0)
                {
                    columns.remove(column);

                    if (column.y == 0)
                    {
                        addColumn(false);
                    }
                }
            }

            birdy.y += y;

            for (Rectangle column : columns)
            {
                if (column.y == 0 && birdy.x + birdy.width / 2 > column.x + column.width / 2 - 10 && birdy.x + birdy.width / 2 < column.x + column.width / 2 + 10)
                {
                    score++;
                }

                if (column.intersects(birdy))
                {
                    GameOver = true;

                    if (birdy.x <= column.x)
                    {
                        birdy.x = column.x - birdy.width;

                    }
                    else
                    {
                        if (column.y != 0)
                        {
                            birdy.y = column.y - birdy.height;
                        }
                        else if (birdy.y < column.height)
                        {
                            birdy.y = column.height;
                        }
                    }
                }
            }

            if (birdy.y > HEIGHT - 120 || birdy.y < 0)
            {
                GameOver = true;
            }

            if (birdy.y + y >= HEIGHT - 120)
            {
                birdy.y = HEIGHT - 120 - birdy.height;
                GameOver = true;
            }
        }

        renderer.repaint();
    }

    public void repaint(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.darkGray.brighter());
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.blue.brighter());
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        g.setColor(Color.pink.darker());
        g.fillRect( birdy.x,  birdy.y , birdy.width , birdy.height);

        for (Rectangle column : columns)
        {
            paintColumn(g, column);
        }

        g.setColor(Color.magenta.darker());
        g.setFont(new Font("Comic", 1, 30));

        if (!Start)
        {
            g.drawString("Press space to play!", 150, HEIGHT/4 +75);
        }

        if (GameOver)
        {
            g.drawString("Game Over! Your score: " + score, 90, HEIGHT / 2 - 50);
        }

        if (!GameOver && Start)
        {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }

    public static void main(String[] args)
    {
        flappy = new NewFlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        jump();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            jump();
        }
    }

    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e){}
}