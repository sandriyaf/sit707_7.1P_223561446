package sit707_week7;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {
    
    private TemperatureSensor temperatureSensor;
    private CloudService cloudService;
    private NotificationSender notificationSender;
    private BodyTemperatureMonitor monitor;
    
    @Before
    public void setUp() {
        // Create mocks using Mockito
        temperatureSensor = Mockito.mock(TemperatureSensor.class);
        cloudService = Mockito.mock(CloudService.class);
        notificationSender = Mockito.mock(NotificationSender.class);
        
        // Initialize the BodyTemperatureMonitor with the mocks
        monitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);
    }

    @Test
    public void testStudentIdentity() {
        String studentId = "s223561446";
        Assert.assertNotNull("Student ID is null", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Sandriya Fernandes";
        Assert.assertNotNull("Student name is null", studentName);
    }

    @Test
    public void testReadTemperatureNegative() {
        // Stub the temperatureSensor to return a negative temperature value
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(-3.0);
        
        // Call the method under test
        double temperature = monitor.readTemperature();
        
        // Assert the expected result
        Assert.assertEquals("Unexpected temperature", -3.0, temperature, 0.01);
    }
    
    @Test
    public void testReadTemperatureZero() {
        // Stub the temperatureSensor to return a temperature of 0
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);
        
        // Call the method under test
        double temperature = monitor.readTemperature();
        
        // Assert the expected result
        Assert.assertEquals("Unexpected temperature", 0.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureNormal() {
        // Stub the temperatureSensor to return a normal temperature value
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(36.5);
        
        // Call the method under test
        double temperature = monitor.readTemperature();
        
        // Assert the expected result
        Assert.assertEquals("Unexpected temperature", 36.5, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureAbnormallyHigh() {
        // Stub the temperatureSensor to return an abnormally high temperature value
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(40.0);
        
        // Call the method under test
        double temperature = monitor.readTemperature();
        
        // Assert the expected result
        Assert.assertEquals("Unexpected temperature", 40.0, temperature, 0.01);
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        // Create a dummy temperature reading
        TemperatureReading temperatureReading = new TemperatureReading();
        temperatureReading.bodyTemperature = 37.0;
        
        // Call the method under test
        monitor.reportTemperatureReadingToCloud(temperatureReading);
        
        // Verify that cloudService.sendTemperatureToCloud() was called once with the expected argument
        Mockito.verify(cloudService).sendTemperatureToCloud(temperatureReading);
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        // Stub the cloudService to return "NORMAL" status
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("NORMAL");
        
        // Call the method under test
        monitor.inquireBodyStatus();
        
        // Verify that notificationSender.sendEmailNotification() was called once with the fixedCustomer
        Mockito.verify(notificationSender).sendEmailNotification(monitor.getFixedCustomer(), "Thumbs Up!");
    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        // Stub the cloudService to return "ABNORMAL" status
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("ABNORMAL");
        
        // Call the method under test
        monitor.inquireBodyStatus();
        
        // Verify that notificationSender.sendEmailNotification() was called once with the familyDoctor
        Mockito.verify(notificationSender).sendEmailNotification(monitor.getFamilyDoctor(), "Emergency!");
    }
}
