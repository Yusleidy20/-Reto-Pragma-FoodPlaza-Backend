package com.example.foodplaza_users.infrastructure.configuration;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final Long ADMIN_ROLE_ID =1L ;
    public static final Long OWNER_ROLE_ID = 2L;
    public static final Long EMPLOYEE_ROLE_ID = 3L;
    public static final Long CUSTOMER_ROLE_ID = 4L;


    public static final String ROLE_ADMIN = "Administrator";
    public static final String ROLE_OWNER = "Owner";
    public static final String ROLE_EMPLOYEE = "Employee";
    public static final String ROLE_CUSTOMER = "Customer";


    public static final String ROLE_CUSTOMER_DESCRIPTION = "food court customer";
}
