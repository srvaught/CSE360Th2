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
    public static Vitals fromString(String data) 
    {
        String[] parts = data.split(":");
        Vitals vitals = new Vitals();
        vitals.temperature = Double.parseDouble(parts[0]);
        vitals.bloodPressure = parts[1];
        vitals.heartRate = Integer.parseInt(parts[2]);
        vitals.respiratoryRate = Integer.parseInt(parts[3]);
        vitals.oxygenSaturation = Double.parseDouble(parts[4]);
        vitals.height = Double.parseDouble(parts[5]);
        vitals.weight = Double.parseDouble(parts[6]);
        vitals.calculateBMI();
        return vitals;
    }
}