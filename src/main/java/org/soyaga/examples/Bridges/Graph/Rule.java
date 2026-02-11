package org.soyaga.examples.Bridges.Graph;

import org.openqa.selenium.WebElement;

import java.util.HashSet;

public class Rule {
    public final int row, col;
    public final int value;
    public final HashSet<Bridge> bridges;
    public final WebElement element;

    public Rule(int row, int col, int value, WebElement element) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.bridges = new HashSet<>();
        this.element = element;
    }
}
