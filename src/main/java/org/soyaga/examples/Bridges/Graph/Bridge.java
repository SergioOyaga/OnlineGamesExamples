package org.soyaga.examples.Bridges.Graph;

import org.openqa.selenium.WebElement;

public record Bridge(int id, WebElement element, String type, Rule ruleFrom, Rule ruleTo) {
}
