package ru.lang3.parser.demo;

import ru.lang3.parser.Tree;

import java.awt.*;

/**
 * Created on 31.03.2018.
 */
public class Visitor {

    Point visitNode(Graphics graphics, Point point, Tree treeNode) {
        final String label = treeNode.getLabel()!=null?treeNode.getLabel():"null";
        final String value = treeNode.getValue()!=null? treeNode.getValue():"null";
        int widthLabel = graphics.getFontMetrics().stringWidth(label);

        int widthValue = graphics.getFontMetrics().stringWidth(value);
        final int widthDelimiter = graphics.getFontMetrics().charWidth(':');
        final int gap = graphics.getFontMetrics().charWidth(' ');

        graphics.drawString(label, point.x, point.y);

        graphics.drawString(":", point.x + widthLabel + gap, point.y);

        graphics.drawString(value, point.x + widthLabel + gap + widthDelimiter + gap, point.y);
        return new Point(point.x + (widthLabel + gap + widthDelimiter + gap + widthValue) / 2, point.y);
    }


    void visitLine(Graphics graphics, Point p1, Point p2) {

            graphics.drawLine(p1.x, p1.y, p2.x, p2.y);

    }



}
