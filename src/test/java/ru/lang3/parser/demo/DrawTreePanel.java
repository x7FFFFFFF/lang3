/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: DrawTreePanel.java 2018-03-28 9:49 paramonov $
 *****************************************************************/
package ru.lang3.parser.demo;

import ru.lang3.parser.Tree;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DrawTreePanel extends JPanel {
    private  Tree root;
    private  Visitor visitor;
   /* private final BlockingQueue<TaskContainer>  queue= new ArrayBlockingQueue<>(10);

    static final class TaskContainer {
        final long timestamp;
        final Runnable  task;

        public TaskContainer(Runnable task) {
            this.timestamp = System.currentTimeMillis();
            this.task = task;
        }
    }*/

  /*  final class Consumer implements Runnable {

       // private final long delta;

       *//* Consumer(BlockingQueue<TaskContainer> q) {
            queue = q;
            //this.delta = delta;
        }*//*
        public void run() {
            try {
                while (true) {
                    consume(queue.take());
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        void consume(TaskContainer taskContainer) {
            EventQueue.invokeLater(taskContainer.task);
            queue.clear();
        }
    }*/


    public DrawTreePanel(Tree root, Visitor visitor) {
        this.root = root;
        this.visitor = visitor;
        //new Thread(new Consumer(), "event-customer-thread").start();
    }

    private Graphics graphics;

    public DrawTreePanel() {
    }

    private Set<Point> set = new LinkedHashSet<>();




    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.graphics = g;

      /*  try {
            queue.put(new TaskContainer(() -> {
                set.clear();
                paintNode(root,  new Point(400, 25));

            }));
        } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }*/

        this.set.clear();
        paintNode(this.root,  new Point(400, 25));
        int w = getWidth();
        int h = getHeight();
        setPreferredSize(new Dimension(w, h));


    }




    private Point paintNode(Tree treeNode, Point point) {
        System.out.println(" paintNode ");
        final Point centerPoint = this.visitor.visitNode(graphics, point, treeNode);
        int counter = 0;
        final Tree[] children = treeNode.getChildren();
        if (children==null) {
            return centerPoint;
        }
        for (Tree child : children) {
            final Point centerPointNext = checkPoint(centerPoint.dY(100).setX(counter * 150));
            final Point center2 = paintNode(child, centerPointNext);
            this.visitor.visitLine(this.graphics, centerPoint, center2);
            counter++;
        }
        return centerPoint;
    }

    private Point checkPoint(Point p) {
        if (set.contains(p)) {
           return p.dX(500);
        } else {
            set.add(p);
            return p;
        }
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.add(new DrawTreePanel());
        jFrame.setSize(500, 500);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    static void draw(Tree treeNode) {

        JFrame jFrame = new JFrame();
        final DrawTreePanel panel = new DrawTreePanel(treeNode, new Visitor());
        panel.setPreferredSize(new Dimension(2000, 2000));
        jFrame.add( new JScrollPane(panel));
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


}