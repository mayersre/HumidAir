/* 
 */
package HumidAir;
/**
 * @author mayers
 *
 */
import java.lang.*;
//
import static HumidAir.Constants.* ;
import static HumidAir.Water.* ;
//
public interface Air {

	    static public double HumidityRatioSaturated (double temperature, double pressure) {
	        /**
	         * Calculates and returns the humidity ratio in kg/kg for saturated air
	         * takes temperature in °C and pressure in bar absolute
	         * 
	         * pws Pressure Water Saturated
	         */
	    	double x = 0 ;
	    	double pws = saturationPressure(temperature) ;
	    	//
	    	x = R_AIR_STEAM * pws /  ( pressure - pws ) ;
	    	return x ;
	    	}
	    
	    static public double HumidityRatio_p_t_phi (double pressure, double temperature, double relativehumidity ) {
	        /**
	         * Calculates the humidity ratio x in kg/kg
	         * pressure            bar     airpressure in bar absolute
	         * temperature         °C      Temperature in degrees centigrade
	         * relativehumidity    0..1    Relative humidity 1=100% 0=0%
	         */
	    	double x = 0 ;
	    	double pws = saturationPressure(temperature) ;
	    	//
	        x = R_AIR_STEAM * relativehumidity * pws /  ( pressure - pws * relativehumidity ) ;
	    	return x ;
	    	}
	    
	    static public double HumidityRatio_p_pws_phi (double airpressure, double saturationpressure, double relativehumidity ) {
	        /**
	         * Calculates the humidity ratio x in kg/kg
	         * airpressure         bar     airpressure in bar absolute
	         * saturationpressure  pressure of saturated water
	         * relativehumidity    0..1    Relative humidity 1=100% 0=0%
	         */
	    	double x = 0 ;
	    	//
	        x = R_AIR_STEAM * relativehumidity * saturationpressure /  ( airpressure - saturationpressure * relativehumidity );
	    	return x ;
	    	}
	    
	    static public double HumidityRatioSat_p_pws (double airpressure, double saturationpressure ) {
	        /**
	         * Calculates the humidity ratio x in kg/kg using airpressure and steam partial
	         * pressure of watergas for saturated air 100% RH
	         * 
	         * airpressure         bar     airpressure in bar absolute
	         * saturationpressure  pressure of saturated water
	         */
	    	double x = 0 ;
	    	//
	        x = R_AIR_STEAM * saturationpressure /  ( airpressure - saturationpressure );
	    	return x ;
	    	}

	    static public double EnthalpyDry(double pressure, double density) {
	        /**
	         * Takes pressure in bar absolute and density in kg/m³
	         * 
	         * returns specific enthalpy of dry air in kJ/kg
	         * 
	         */
	    	double enthalpy = (pressure*100000*CP_AIR)/(density*R_AIR)- CP_AIR * K_0 ; 
	    	return enthalpy ;
	    	}
	    
	    static public double Enthalpy_p_t_x(double pressure, double temperature, double humidityratio ) {
	        /**
	         * Takes pressure in bar absolute, 
	         * Temperature in degrees centigrade
	         * humidity ratio in kg/kg
	         * 
	         * returns specific enthalpy of air in kJ/kg
	         * It is calculated differently for saturation, wet area and ice area
	         */
	    	double enthalpy = 0 ; 
	    	double xs_l = HumidityRatioSaturated(temperature,pressure) ;
	    	//
	        if  ( humidityratio <= xs_l ) {
	            /*
	             * not- or just saturated humid air
	            */
	        	enthalpy = CP_AIR * temperature + humidityratio *  ( R_0 + CP_STEAM * temperature );
	        	}
	        else {
	            if (temperature >= 0) {
	                /*
	                * fog area, overly saturated humid air
	                */
	            	enthalpy = CP_AIR * temperature + xs_l *  ( R_0 + CP_STEAM * temperature )  +  ( humidityratio - xs_l )  * CP_WATER * temperature ;
	            	}
	            else {
	                /*
	                * ice area
	                */
	                enthalpy = CP_AIR * temperature + xs_l *  ( R_0 + CP_STEAM * temperature )  +  ( humidityratio - xs_l )  *  ( R_ICE - CP_ICE * temperature );
	            	}
	        	}
	    	return enthalpy ;
	    	}   
	    
	    static public double Enthalpy_p_t_phi(double pressure, double temperature, double relativehumidity ) {
	        /**
	         * Takes pressure in bar absolute, 
	         * Temperature in degrees centigrade
	         * relativehumidity in 0..1 0=0% 1=100%
	         * 
	         * returns specific enthalpy of air in kJ/kg
	         * It is calculated differently for saturation, wet area and ice area
	         */
	    	double enthalpy = 0 ; 
	    	double xs_l = HumidityRatioSaturated(temperature,pressure) ;
	    	double humidityratio = HumidityRatio_p_t_phi(pressure, temperature, relativehumidity ) ;
	    	//
	        if  ( humidityratio <= xs_l ) {
	            /*
	             * not- or just saturated humid air
	            */
	        	enthalpy = CP_AIR * temperature + humidityratio *  ( R_0 + CP_STEAM * temperature );
	        	}
	        else {
	            if (temperature >= 0) {
	                /*
	                * fog area, overly saturated humid air
	                */
	            	enthalpy = CP_AIR * temperature + xs_l *  ( R_0 + CP_STEAM * temperature )  +  ( humidityratio - xs_l )  * CP_WATER * temperature ;

	            	}
	            else {
	                /*
	                * ice area
	                */
	                enthalpy = CP_AIR * temperature + xs_l *  ( R_0 + CP_STEAM * temperature )  +  ( humidityratio - xs_l )  *  ( R_ICE - CP_ICE * temperature );
	                }
	        	}
	    	return enthalpy ;
	    	}   
	    
	    static public double Enthalpy_p_phi_x(double pressure, double relativehumidity, double humidityratio) {
	        /**
	         * Takes pressure in bar absolute, 
	         * Temperature in degrees centigrade
	         * relativehumidity ratio in 0..1 0=0% 1=100%
	         * 
	         * returns specific enthalpy of air in kJ/kg
	         * It is calculated differently for saturation, wet area and ice area
	         */
	    	double pws = humidityratio /  ( R_AIR_STEAM + humidityratio )  * pressure / relativehumidity ;
	    	//
	    	double temperature = saturationTemperature(pws) ;
	    	double enthalpy = Enthalpy_p_t_x(pressure, temperature, humidityratio) ;
	    	//
	    	return enthalpy ;
	    	}
		    
	    static public double Temperature_h_x(double enthalpy, double humidityratio) {
	        /**
	         * Takes Temperature in degrees centigrade
	         * humidity ratio in kg/kg
	         * 
	         * Returns Degrees Celsius
	         * Calculates the Temperature from given enthalpy and humidity ratio
	         */
	    	double temperature = ( enthalpy - humidityratio * R_0 )  /  ( CP_AIR + humidityratio * CP_STEAM ) ;

	    	return temperature ;
	    	}
	    
    static public double PartialPressureWater(double pressure, double humidityratio) {
        /**
         * Takes pressure in bar absolute, 
         * humidity ratio in kg/kg
         * 
         * Returns the partial pressure of steam in the air
         */
    	double ppressure = pressure * humidityratio /  ( R_AIR_STEAM + humidityratio ) ;

    	return ppressure ;
    	}
    
	static public double PartialPressureAir(double pressure, double humidityratio) {
	    /**
	     * Takes pressure in bar absolute, 
	     * humidity ratio in kg/kg
	     * 
	     * Returns the partial pressure of air in the air
	     */
		double ppressure = pressure * R_AIR_STEAM /  ( R_AIR_STEAM + humidityratio ) ;
	
		return ppressure ;
		}
    
    static public double RelativeHumidity_p_x_pws(double pressure, double humidityratio, double saturationpressure) {
        /**
         * Takes pressure in bar absolute, 
         * humidity ratio in kg/kg
         * pressure of saturated water
         * 
         * returns relative humidity in 0..1 = 0%..100%
         */
    	double phi = ( pressure / saturationpressure )  * humidityratio /  (R_AIR_STEAM + humidityratio );
    	//
    	return phi ;
    	}
    
    static public double RelativeHumidity_p_t_x(double pressure, double temperature, double humidityratio) {
        /**
         * Takes pressure in bar absolute, 
         * Temperature in degrees centigrade
         * humidity ratio in kg/kg
         * 
         * returns relative humidity in 0..1 = 0%..100%
         */
    	double pws = saturationPressure(temperature) ;
    	//
    	double phi = ( pressure / pws )  * humidityratio /  ( R_AIR_STEAM + humidityratio );
    	//
    	return phi ;
    	}
    
    static public double RelativeHumidity_t_tDew(double temperature, double dewpoint_temperature) {
        /**
         * Takes pressure in bar absolute, 
         * Temperature in degrees centigrade
         * relativehumidity ratio in 0..1 0=0% 1=100%
         * 
         * returns relative humidity in 0..1 = 0%..100%
         */
    	double pws     = saturationPressure(temperature) ;
    	double pws_dew = saturationPressure(dewpoint_temperature) ;
    	//
    	double phi = pws_dew / pws ;
    	//
    	return phi ;
    	}
    
    static public double RelativeHumidity_p_t_twb(double pressure, double temperature, double wetbulb_temperature) {
        /**
         * Takes pressure in bar absolute, 
         * Temperature in degrees centigrade
         * Wetbulb temperature in degrees centigrade
         * 
         * returns relative humidity in 0..1 = 0%..100%
         */
    	double xs_fk = HumidityRatioSaturated(wetbulb_temperature, pressure) ;
    	double h1    = Enthalpy_p_t_x(pressure, wetbulb_temperature, xs_fk) ;
    	double h2    = 0;
    	// iterate for a solution quite simply
    	double xs_wanted = xs_fk ;
    	boolean GoOn = true ;
    	//
    	while (GoOn) {
    		xs_wanted = xs_wanted - 0.001 ;
    		h2 = Enthalpy_p_t_x(pressure, temperature, xs_wanted) ;
    		if (h2 < h1); {
    			GoOn = false ;
    			}
    		}
    	// one more
    	GoOn = true ;
    	while (GoOn) {
    		xs_wanted = xs_wanted + 0.00001 ;
    		h2 = Enthalpy_p_t_x(pressure, temperature, xs_wanted) ;
    		if (h2 < h1) {
    			GoOn = false ; // close enough
    			}
    		}
    	double phi = RelativeHumidity_p_t_x(pressure, temperature, xs_wanted) ;
    	//
    	return phi ;
    	}
    
    static public double WetBulbTemperature(double pressure, double temperature, double relativehumidity ) {
        /**
         * Takes pressure in bar absolute, 
         * Temperature in degrees centigrade
         * relativehumidity in 0..1 0=0% 1=100%
         * 
         * returns WetBulbTemperature in °C
         */
    	double wbt = -50 ;
    	double wbh =   0 ;
        double h1  = Enthalpy_p_t_phi(pressure, temperature, relativehumidity) ;
        /*
         * We look for the saturation temperature like we would follow the isenthalp line
         * with our finger to 100% in the Mollier diagram
         */
    	boolean GoOn = true ;
    	//
    	while (GoOn) {
    		wbt = wbt +1 ;
    		wbh = Enthalpy_p_t_phi(pressure, wbt, 1) ;
    		if (h1 < wbh); {
    			GoOn = false ;
    			}
    		}
    	// one more
    	GoOn = true ;
    	//
    	while (GoOn) {
    		wbt = wbt - 0.001 ;
    		wbh = Enthalpy_p_t_phi(pressure, wbt, 1) ;
    		if (h1 > wbh); {
    			GoOn = false ;
    			}
    		}
    		// we are in the range of 1 mK away ...
    	return wbt ;
    	}
    
}
	    