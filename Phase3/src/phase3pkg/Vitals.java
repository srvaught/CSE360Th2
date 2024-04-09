package phase3pkg;
public class Vitals 
{
    private double temperature;
    private String bloodPressure;
    private int heartRate;
    private int respiratoryRate;
    private double oxygenSaturation;
    private double height;
    private double weight;
    private double bmi;
    public void calculateBMI() 
    {
        if (height > 0 && weight > 0) 
        {
            this.bmi = weight / (height * height);
        } 
        else 
        {
            this.bmi = 0;
        }
    }
}