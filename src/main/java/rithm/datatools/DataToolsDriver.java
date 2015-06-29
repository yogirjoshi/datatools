package rithm.datatools;
import rithm.core.*;
import rithm.defaultcore.DefaultProgramState;
public class DataToolsDriver {
	public static void main(String args[])
	{
		DataFactory rdf;
		rdf = new CSVDataFactory("/home/y2joshi/Input2.csv");
//		DefaultProgramState JSONStr;
		String JSONStr;
		do
		{
//			JSONStr = (DefaultProgramState)rdf.getNextProgState();
//			if(JSONStr == null)
//				break;
			JSONStr = rdf.getNextJSONEvent();
			if(JSONStr == "{}")
				break;         
//			System.out.println(JSONStr.toString() );
			System.out.println(JSONStr);
		}while(JSONStr != null);
	}
	
}
