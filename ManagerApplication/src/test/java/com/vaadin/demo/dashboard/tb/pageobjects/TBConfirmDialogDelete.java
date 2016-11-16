package com.vaadin.demo.dashboard.tb.pageobjects;

import com.humancare.monitor.view.form.FormView;
import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class TBConfirmDialogDelete extends TestBenchTestCase {

   // private final WindowElement scope;

   /* public TBConfirmDialogDelete(WebDriver driver) {
        setDriver(driver);
        scope = $(WindowElement.class).id(1);
    }/*/

    public void discard() {
        $(ButtonElement.class).caption("Discard Changes").first().click();
    }

}
