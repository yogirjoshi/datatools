package rithm.datatools;
import rithm.core.*;
import rithm.defaultcore.DefaultProgramState;
public class DataToolsDriver {
	public static void main(String args[])
	{
		DataFactory rdf;
		rdf = new CSVDataFactory("/home/y2joshi/Input1.xml");
		DefaultProgramState JSONStr;
		do
		{
			JSONStr = (DefaultProgramState)rdf.getNextProgState();
			if(JSONStr == null)
				break;
			System.out.println(JSONStr.toString() );
		}while(JSONStr != null);
	}
	
}
