package HumidAir;
/**
 * @author mayers
 *
 */
import java.lang.*;

import static HumidAir.Constants.* ;

public interface Water {

    static public double saturationPressure (double temperature) {
        /**
    	* This could be replaced with functions from IAPWS e.g. Refrop or Coolprop
    	*
    	* returns the saturation pressure of water in bar
        * input Temperature in degrees celsius
        *
        * -50 ... + 60  C Antoine Equation from Baehr/Kabelac / Thermodynamik
        * Above Equations from Bernd Glueck / Stoffwerte
        */
        double T = temperature + K_0;
        //
        double pressure = 0;
        //
        if (temperature >= -50 && temperature < 0.01)
        	{
            pressure = PWTR * Math.exp(22.5129 *  ( 1 - TWTR / T ));
        	}
        else if (temperature >= 0.01 && temperature < 60 )
        	{
            pressure = PWTR * Math.exp(17.2799 -  ( 4102.99 /  ( temperature + 237.431 ) ));
        	}
        else if (temperature >= 60 && temperature < 100)
        	{
            // Berechnung nach Bernd Glueck, Stoffwerte, 1.4
            //
            double g1 = - 0.000191275 ;
            double g2 = 0.07258 * temperature ;
            double g3 = - 0.0002939 * Math.pow(temperature, 2) ;
            double g4 = 0.0000009841 * Math.pow(temperature, 3) ; 
            double g5 = - 0.00000000192 * Math.pow(temperature, 4) ;
            //
            pressure = 611 * Math.exp(g1 + g2 + g3 + g4 + g5) / 100000 ;
        	}
            else if (temperature >= 100 && temperature <= 200)
            {
            // Berechnung nach Bernd Glueck, Stoffwerte, 1.7
            //
            double g1 = 0.00006 ;
            double g2 = 0.0713274 * temperature ;
            double g3 = - 0.0002581631 * Math.pow(temperature, 2) ;
            double g4 = 0.0000006311955 * Math.pow(temperature, 3) ;
            double g5 = - 7.167112E-10 * Math.pow(temperature, 4) ;
            //
            pressure = 611 * Math.exp(g1 + g2 + g3 + g4 + g5) / 100000 ;
            }
        else
        	{
            // Ausserhalb des Gueltigkeitsbereiches -1
            pressure = - 1 ;
        	}
		return pressure;
    } // saturationPressure


    static public double saturationTemperature (double pressure) {
        /**
    	*
	    * Returns Degrees Celsius
	    * Calculates the saturation Temperature of water from -50 to 200 Degrees Celsius
	    * using various models for different areas
	    * -50 ... + 60  C Antoine Gleichung laut Baehr/Kabelac / Thermodynamik nach T aufgeloest
	    * darueber bis 200 C Gleichungen von Bernd Glueck / Stoffwerte
	    * bei Bereichsueberschreitung ist der Rueckgabewert -300
	    *
	    */
	    double Temperature = -300 ;
	    double p_bar = pressure ;
	    double p_pa = pressure * 100000 ;
	    //
	    if (p_bar >= 0.000039 && p_bar < PWTR)
	    	{
	        //
	        // -50 bis Tripelpunkt
	        //
	    	Temperature = ( ( -22.5129 * TWTR )  /  ( Math.log(p_bar / PWTR ) - 22.5129 ) )  - K_0;
	        //
	    	}
	    else if (p_bar >= PWTR && p_bar < 0.19) {
	        //
	        // Tripelpunkt bis + 60  C
	        //
	    	Temperature = ( ( -4102.99 )  /  ( Math.log(p_bar / PWTR ) - 17.2799 ) )  - 237.431 ;
		    }
	    else if (p_bar >= 0.19 && p_bar < 1.0132) {
	        //
	        // Berechnung nach Bernd Glueck, Stoffwerte, 1.5
	        //
	        double g1 = - 63.16113 ;
	        double g2 =    5.36859    * Math.log(p_pa);
	        double g3 =    0.973587   * Math.pow( Math.log(p_pa) , 2) ;
	        double g4 = -  0.0738636  * Math.pow( Math.log(p_pa) , 3 );
	        double g5 =    0.00481832 * Math.pow( Math.log(p_pa) , 4 );
	        //
	        		Temperature = g1 + g2 + g3 + g4 + g5 ;
	    	}
	    else if (p_bar >= 1.0132 && p_bar < 15.6) {
	        //
	        // Berechnung nach Bernd Glueck, Stoffwerte, 1.8
	        //
	        double g1 = - 228.146 ;
	        double g2 =    31.97037    * Math.log(p_pa) ;
	        double g3 =     1.153295   * Math.pow( Math.log(p_pa), 2) ;
	        double g4 = -   0.27847109 * Math.pow( Math.log(p_pa), 3) ;
	        double g5 =     0.01319026 * Math.pow( Math.log(p_pa), 4) ;
	        //
	        		Temperature = g1 + g2 + g3 + g4 + g5 ;
	    	}
	    else {
	        //
	    	Temperature = - 300 ;
	    	}
	    return Temperature ;
    }
}



