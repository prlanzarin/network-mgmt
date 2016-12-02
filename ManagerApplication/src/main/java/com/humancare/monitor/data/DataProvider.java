package com.humancare.monitor.data;

import java.util.Collection;
import java.util.Date;

import com.humancare.monitor.domain.DashboardNotification;
import com.humancare.monitor.domain.User;

/**
 * QuickTickets Dashboard backend API.
 */
public interface DataProvider {
    
    /**
     * @param userName
     * @param password
     * @return Authenticated used.
     */
    User authenticate(String userName, String password);

}
