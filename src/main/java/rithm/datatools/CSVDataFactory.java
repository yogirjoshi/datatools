/**
 * 
 */
package rithm.datatools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	public CSVDataFactory(String inputfilename)
	{
		isFileOpened=true;
		try
		{
			csvFileReader = new BufferedReader(new FileReader(inputfilename));
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
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
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
						defProgState.SetVal(pair[0], pair[1]);
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
	
		return false;
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
		return null;
	}

	@Override
	public ProgState getProgStateAtTimeStamp(double Ts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearBuffer() {
		// TODO Auto-generated method stub
		
	}

}
