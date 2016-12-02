package com.humancare.monitor.data.dummy;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.humancare.monitor.data.DataProvider;
import com.humancare.monitor.domain.DashboardNotification;
import com.humancare.monitor.domain.User;

/**
 * A dummy implementation for the backend API.
 */
public class DummyDataProvider implements DataProvider {

    private static Date lastDataUpdate;

    private final Collection<DashboardNotification> notifications = null;

    /**
     * Initialize the data for this application.
     */
    public DummyDataProvider() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {            
            lastDataUpdate = new Date();
        }
    }

    @Override
    public User authenticate(String userName, String password) {
        User user = new User();
        user.setFirstName("Admin");
        user.setLastName("");
        user.setRole("admin");
        String email = user.getFirstName().toLowerCase() + "@teste.com";
        user.setLocation("BR");
        user.setBio("Quis aute iure reprehenderit in voluptate velit esse."
                + "Cras mattis iudicium purus sit amet fermentum.");
        return user;
    }   

}
