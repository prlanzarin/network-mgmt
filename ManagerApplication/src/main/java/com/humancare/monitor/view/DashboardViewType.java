package com.humancare.monitor.view;

import com.humancare.monitor.realtime.RealTimeView;
import com.humancare.monitor.view.dashboard.DashboardView;
import com.humancare.monitor.view.form.FormView;
import com.humancare.monitor.view.sales.SalesView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
    DASHBOARD("human monitor", DashboardView.class, FontAwesome.HOME, true), REALTIME(
            "real time", RealTimeView.class, FontAwesome.MEDKIT, false), STATISTICS(
            "statistics", SalesView.class, FontAwesome.BAR_CHART_O, false), REGISTER(
            "register", FormView.class, FontAwesome.TABLE, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
