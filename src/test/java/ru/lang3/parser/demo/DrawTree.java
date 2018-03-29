/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: DrawTree.java 2018-03-28 9:49 paramonov $
 *****************************************************************/
package ru.lang3.parser.demo;

import ru.lang3.parser.Tree;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class DrawTree extends JPanel {
    private  Tree root;

    public DrawTree(Tree root) {
        this.root = root;
    }

    private Graphics graphics;

    public DrawTree() {
    }

    private Set<Point> set = new LinkedHashSet<>();

    static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point translate(int x, int y) {
            return new Point(this.x + x, this.y + y);
        }
        Point translateX(int x) {
            return new Point(this.x + x, this.y );
        }
        Point translateY(int y) {
            return new Point(this.x, this.y + y);
        }
        Point setX(int x) {
            return new Point(x, this.y);
        }
        Point setY(int y) {
            return new Point(this.x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {

            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        this.graphics = g;
        // Draw Tree Here
       // graphics.drawOval(5, 5, 25, 25);
        //STree sTree = new STree("label");
        //sTree.setValue("value");
        //final Point pointStart = new Point(5, 25);
/*        final Point pointCenter1 = paintNode(sTree, pointStart);
        final Point pointCenter2 = paintNode(sTree, pointStart.translateY(25));*/
       // paintLine(pointCenter1, pointCenter2);

        paintNode(this.root,  new Point(400, 25));
        int w = getWidth();
        int h = getHeight();
        setPreferredSize(new Dimension(w, h));


    }

    private void paintLine(Point p1, Point p2) {
        graphics.drawLine(p1.x, p1.y, p2.x, p2.y);
    }


    private void paintNode(Tree treeNode, Point point) {



        final String label = treeNode.getLabel()!=null?treeNode.getLabel():"null";
        final String value = treeNode.getValue()!=null? treeNode.getValue():"null";
        int widthLabel = graphics.getFontMetrics().stringWidth(label);

        int widthValue = graphics.getFontMetrics().stringWidth(value);
        final int widthDelimiter = graphics.getFontMetrics().charWidth(':');
        final int gap = graphics.getFontMetrics().charWidth(' ');

        graphics.drawString(label, point.x, point.y);

        graphics.drawString(":", point.x + widthLabel + gap, point.y);

        graphics.drawString(value, point.x + widthLabel + gap + widthDelimiter + gap, point.y);
        final Point centerPoint = new Point(point.x + (widthLabel + gap + widthDelimiter + gap + widthValue) / 2, point.y);

        int counter = 0;
        final Tree[] children = treeNode.getChildren();
        if (children==null) {
            return;
        }
        for (Tree child : children) {
            final Point centerPointNext = checkPoint(centerPoint.translateY(100).setX(counter * 150));
            paintLine(centerPoint, centerPointNext);
            paintNode(child, centerPointNext);

            counter++;
        }


    }

    private Point checkPoint(Point p) {
        if (set.contains(p)) {
           return p.translateY(100);
        } else {
            set.add(p);
            return p;
        }
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.add(new DrawTree());
        jFrame.setSize(500, 500);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void draw(Tree treeNode) {

        JFrame jFrame = new JFrame();
        jFrame.add(new DrawTree(treeNode));
        jFrame.setSize(800, 1500);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*JFrame jFrame = new JFrame();

        final DrawTree panel = new DrawTree(treeNode);
       *//* panel.setBorder(BorderFactory.createLineBorder(Color.red));
        panel.setPreferredSize(new Dimension(2000, 800));*//*

        final JScrollPane scroll = new JScrollPane(panel);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(scroll, BorderLayout.CENTER);
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);*/
    }


}