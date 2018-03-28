# HumidAir

This is the english language port of my feuchte-luft repository (german)

#Translation in progress, help welcome

Calculations for humid air in different programming languages

It works for Excel and Libre Office Calc version >= 5.1 without
any other plugins. The VBA code is not protected. If there is any
protection on a worksheet, it is without password.

Python 3, library

Java 1.8 library with simple Mollier Chart (HXDiagram.java)

Formulations for the equations of state come from publicly available 
Textbooks for Thermodynamics. The whole library and functions use SI
Units like common on the european continent.

Humid air is treated like an ideal gas.

Just run the jar file with java 8 like :
::

	java -jar HumidAir.jar

The sources can be built using ant in the src directory :
::

	ant compile jar run
	

(c) 2010-2018 Reiner Mayers 
This code comes under the GNU Copyleft (GPL) V3.0
