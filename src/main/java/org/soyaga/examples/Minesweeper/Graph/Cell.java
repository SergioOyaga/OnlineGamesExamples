package org.soyaga.examples.Minesweeper.Graph;

import org.openqa.selenium.WebElement;

public record Cell(int row, int coll, boolean isDecided, Integer value, WebElement element) {
}
