/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.view.form;

import com.humancare.monitor.snmp.PatientDataManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import static com.vaadin.ui.Notification.TYPE_HUMANIZED_MESSAGE;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author amanda
 */
public class FormView extends VerticalLayout implements View {

    private PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private CssLayout panels;

    public FormView() {
        setSizeFull();
        setMargin(true);
        addStyleName("form");

        addComponent(buildHeader());

        Component content = buildContent();
        addComponent(content);
        setExpandRatio(content, 1);

    }

    private Component buildHeader() {

        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Register Patient Agent");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;

    }

    private Component buildContent() {

        panels = new CssLayout();
        panels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(panels);

        panels.addComponent(buildForm());

        return panels;
    }

    private Component buildForm() {

        FormLayout form = new FormLayout();
        form.setSizeFull();
        form.setResponsive(true);

        TextField ipField = new TextField("IP");
        ipField.setId("ip");
        ipField.setRequired(true);
        form.addComponent(ipField);

        TextField portField = new TextField("Port");
        portField.setId("port");
        portField.setRequired(true);
        form.addComponent(portField);

        TextField nameField = new TextField("Name");
        nameField.setId("usrName");
        nameField.setRequired(true);
        form.addComponent(nameField);

        TextField ageField = new TextField("Age");
        ageField.setId("usrAge");
        ageField.setRequired(true);
        form.addComponent(ageField);

        TextField genderField = new TextField("Gender");
        genderField.setId("usrGender");
        genderField.setRequired(true);
        form.addComponent(genderField);

        form.addComponent(new Button("Send", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (patientDataManager.addPatientToMib(form)) {
                    Notification notif = new Notification("Patient registered succesfully",
                        TYPE_HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.show(Page.getCurrent());
                } else {
                    Notification notif = new Notification("There was an error while connecting to the agent!",
                        TYPE_HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.show(Page.getCurrent());
                }
            }
        }));

        return form;

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
