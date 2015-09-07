/**
 * 
 */
package rithm.datatools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import rithm.defaultcore.*;

import com.google.gson.Gson;

import rithm.core.ProgState;

/**
 * @author y2joshi
 *
 */
public class CSVDataFactory extends rithm.core.DataFactory{

	protected BufferedReader csvFileReader;
	protected boolean isFileOpened;
	final static Logger logger = Logger.getLogger(CSVDataFactory.class);
	public CSVDataFactory(String inputfilename)
	{
		isFileOpened=true;
		try
		{
//			System.out.println(inputfilename);
			logger.debug("Initialized with " + inputfilename);
			if(inputfilename == null)
				csvFileReader = new BufferedReader(new InputStreamReader(System.in));
			else
				csvFileReader = new BufferedReader(new FileReader(inputfilename));
		}
		catch(FileNotFoundException fe)
		{
			logger.fatal(fe.getMessage());
			isFileOpened=false;
			try {
				csvFileReader.close();
			} catch (IOException e) {
				logger.fatal(e.getMessage());
			}
		}
	}

	@Override
	public String getNextJSONEvent() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		DefaultProgramState dpState = (DefaultProgramState)getNextProgState();
		if(dpState != null)
			return gson.toJson(dpState);
		return "{}";
	}

	@Override
	public void setFile(String Inputfilename) {
		// TODO Auto-generated method stub
		isFileOpened=true;
		try
		{
			csvFileReader = new BufferedReader(new FileReader(Inputfilename));
		}
		catch(FileNotFoundException fe)
		{
			try {
				csvFileReader.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			isFileOpened=false;
		}
	}

	@Override
	public void export(String Outputfilename) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ProgState getNextProgState() {
		// TODO Auto-generated method stub
		String line;
		DefaultProgramState defProgState = new DefaultProgramState(0);
		if(isFileOpened){
			try
			{
				if((line = csvFileReader.readLine())!=null)
				{
					String[] tokens = line.split(",");
					for(String token:tokens)
					{
						String[] pair = token.split("=");
						defProgState.setValue(pair[0], pair[1]);
						if(pair[0].equals("timestamp"))
						{
							try{
								Double doubleTs = Double.parseDouble(pair[1]);
								defProgState.setTimestamp(doubleTs);
							}catch(NumberFormatException ne){
								logger.debug("Could not parse as timestamp !!" + pair[1]);
							}
						}
					}
					return defProgState;
				}
			}
			catch(IOException ie)
			{
				ie.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean resetFilePosition() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public String getJSONAt(long i) {
		// TODO Auto-generated method stub
		String ret_json;
		try
		{
			Gson gson = new Gson();
			ret_json = gson.toJson(eventQueue.get((int)i));
		}
		catch(IndexOutOfBoundsException ie)
		{
			ret_json = "{}";
		}
		return ret_json;
	}

	@Override
	public ProgState getProgStateAt(long i) {
		// TODO Auto-generated method stub
		try
		{
			ProgState p = (eventQueue.get((int)i));
			return p;
		}
		catch(IndexOutOfBoundsException ie)
		{
			return null;
		}
	}

	@Override
	public String getJSONAtTimeStamp(double Ts) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public ProgState getProgStateAtTimeStamp(double Ts) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void clearBuffer() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public boolean closeDataSource() {
		// TODO Auto-generated method stub
		if(isFileOpened){
			try{
				csvFileReader.close();
			}catch(IOException ie){
				logger.fatal(ie.getMessage());
				return false;
			}
			return true;
		}
		return false;
	}
	
}
