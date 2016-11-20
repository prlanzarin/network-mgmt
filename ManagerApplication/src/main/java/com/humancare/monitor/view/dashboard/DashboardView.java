package com.humancare.monitor.view.dashboard;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.eventbus.Subscribe;
import com.humancare.monitor.DashboardUI;
import com.humancare.monitor.domain.DashboardNotification;
import com.humancare.monitor.event.DashboardEvent.CloseOpenWindowsEvent;
import com.humancare.monitor.event.DashboardEvent.NotificationsCountUpdatedEvent;
import com.humancare.monitor.event.DashboardEventBus;
import com.humancare.monitor.entities.PatientData;
import com.humancare.monitor.entities.RegisteredPatients;
import com.humancare.monitor.snmp.Manager;
import com.humancare.monitor.snmp.PatientDataManager;
import com.humancare.monitor.view.dashboard.DashboardEdit.DashboardEditListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.data.ListDataSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View,
        DashboardEditListener {

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;
    private Window notificationsWindow;
    
    private PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private Manager manager = Manager.getInsance();
    private PatientData patientData;
    private ComboBox<RegisteredPatients> patientSelect; 
    private ComboBox<Integer> daysSelect;
    
    private int numberOfDays = 7;
    private DashboardCharts dashboardCharts;
    
     
    // TODO: atributo e campo para definir numero de dias que deseja-se obter nos graficos 

    public DashboardView() {
                
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        
        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);
        
        initPatientSelect();

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new CloseOpenWindowsEvent());
            }
        });
        
        
    }
          
    private Component buildDaysSelector() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("daysSelector");
        toolbar.setSpacing(true);
        
        List<Integer> availableDays = Arrays.asList(7, 30, 60);        
        daysSelect = new ComboBox<>();
        daysSelect.setDataSource(new ListDataSource<>(availableDays));
        daysSelect.setCaption("Consulting days: ");
        daysSelect.setHeight("30px");
        daysSelect.setWidth("100px");
        daysSelect.addShortcutListener(
                new ShortcutListener("OK", KeyCode.ENTER, null) {
                    @Override
                    public void handleAction(final Object sender,
                            final Object target) {
                        numberOfDays = daysSelect.getValue();
                    }
                });

        final Button ok = new Button("OK");
        ok.setHeight("30px");
        ok.setEnabled(false);
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout(daysSelect, ok);
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        toolbar.addComponent(group);

        daysSelect.addSelectionListener(
                event -> ok.setEnabled(event.getValue() != null));


        return toolbar;
    }
    
    // Get all registered patients and put in combo box component
    private void initPatientSelect(){
        List<RegisteredPatients> regPatients = patientDataManager.getPatientList();
        if(regPatients != null){
            patientSelect.setDataSource(new ListDataSource<>(regPatients));
        }
        
    }
        
    private Component buildPatientSelector() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("patientSelector");
        toolbar.setSpacing(true);

        patientSelect = new ComboBox<>();
        patientSelect.setItemCaptionGenerator(RegisteredPatients::getName);
        patientSelect.setCaption("Select Patient:  ");
        patientSelect.setHeight("30px");
        patientSelect.addShortcutListener(
                new ShortcutListener("OK", KeyCode.ENTER, null) {
                    @Override
                    public void handleAction(final Object sender,
                            final Object target) {
                        updateCharts(patientSelect.getValue());
                    }
                });

        final Button ok = new Button("OK");
        ok.setHeight("30px");
        ok.setEnabled(false);
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout(patientSelect, ok);
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        toolbar.addComponent(group);

        patientSelect.addSelectionListener(
                event -> ok.setEnabled(event.getValue() != null));


        return toolbar;
    }
    
    //todo: atualizar graficos apos selecionar um paciente
    private void updateCharts(RegisteredPatients regPatients){
        manager.configManager(regPatients.getIp());
        patientDataManager.setCurrentPatient(regPatients);
    }
    

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        // add title and combo box to header
        titleLabel = new Label("Human Monitor");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponents(titleLabel, buildPatientSelector(), buildDaysSelector());

        // add notification button to header
        notificationsButton = buildNotificationsButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private NotificationsButton buildNotificationsButton() {
        NotificationsButton result = new NotificationsButton();
        result.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                openNotificationsPopup(event);
            }
        });
        return result;
    }

    private Component buildContent() {
        dashboardCharts = new DashboardCharts(numberOfDays);
        
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(dashboardCharts.getHeartRateChart());
        dashboardPanels.addComponent(dashboardCharts.getTemperatureChart());
        dashboardPanels.addComponent(dashboardCharts.getSPO2Chart());
        dashboardPanels.addComponent(dashboardCharts.getPressureChart());
        dashboardPanels.addComponent(dashboardCharts.getGlucoseChart());
        dashboardPanels.addComponent(dashboardCharts.getEnvTempChart());
        dashboardPanels.addComponent(dashboardCharts.getEnvTempChart());
        dashboardPanels.addComponent(dashboardCharts.getEnvOxyChart());
        
        return dashboardPanels;
    }

  

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

            @Override
            public void menuSelected(final MenuItem selectedItem) {
                if (!slot.getStyleName().contains("max")) {
                    selectedItem.setIcon(FontAwesome.COMPRESS);
                    toggleMaximized(slot, true);
                } else {
                    slot.removeStyleName("max");
                    selectedItem.setIcon(FontAwesome.EXPAND);
                    toggleMaximized(slot, false);
                }
            }
        });
        max.setStyleName("icon-only");
        MenuItem root = tools.addItem("", FontAwesome.COG, null);
        root.addItem("Configure", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });
        root.addSeparator();
        root.addItem("Close", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    private void openNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        Label title = new Label("Notifications");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);

        Collection<DashboardNotification> notifications = DashboardUI
                .getDataProvider().getNotifications();
        DashboardEventBus.post(new NotificationsCountUpdatedEvent());

        for (DashboardNotification notification : notifications) {
            VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.addStyleName("notification-item");

            Label titleLabel = new Label(notification.getFirstName() + " "
                    + notification.getLastName() + " "
                    + notification.getAction());
            titleLabel.addStyleName("notification-title");

            Label timeLabel = new Label(notification.getPrettyTime());
            timeLabel.addStyleName("notification-time");

            Label contentLabel = new Label(notification.getContent());
            contentLabel.addStyleName("notification-content");

            notificationLayout.addComponents(titleLabel, timeLabel,
                    contentLabel);
            notificationsLayout.addComponent(notificationLayout);
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        Button showAll = new Button("View All Notifications",
                new ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        Notification.show("Not implemented in this demo");
                    }
                });
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
            notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY()
                    - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
        notificationsButton.updateNotificationsCount(null);
    }

    @Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

    public static final class NotificationsButton extends Button {
        private static final String STYLE_UNREAD = "unread";
        public static final String ID = "dashboard-notifications";

        public NotificationsButton() {
            setIcon(FontAwesome.BELL);
            setId(ID);
            addStyleName("notifications");
            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            DashboardEventBus.register(this);
        }

        @Subscribe
        public void updateNotificationsCount(
                final NotificationsCountUpdatedEvent event) {
            setUnreadCount(DashboardUI.getDataProvider()
                    .getUnreadNotificationsCount());
        }

        public void setUnreadCount(final int count) {
            setCaption(String.valueOf(count));

            String description = "Notifications";
            if (count > 0) {
                addStyleName(STYLE_UNREAD);
                description += " (" + count + " unread)";
            } else {
                removeStyleName(STYLE_UNREAD);
            }
            setDescription(description);
        }
    }

}
