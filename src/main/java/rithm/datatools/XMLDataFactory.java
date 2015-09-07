/**
 * 
 */
package rithm.datatools;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import rithm.defaultcore.*;
import rithm.core.*;
/**
 * @author y2joshi
 *
 */

public class XMLDataFactory extends rithm.core.DataFactory{
	static final String toplevelname = "Trace";
	static final String eventname = "State";
	static final String fieldname = "Key";
	static final String timestampField = "timestamp";
	static final String fieldvalue = "Value";
	private XMLEventReader eventReader;
	protected XMLInputFactory inputFactory;
	protected XMLEvent curr_event;
	protected DefaultProgramState curr_state;
	protected String filename;
	protected long counter;
	protected InputStream xml_input_stream;
	protected String curr_key = null, curr_value = null;
	protected boolean new_event_found;
	protected int QueueClearCount;
	final static Logger logger = Logger.getLogger(XMLDataFactory.class);
	public XMLDataFactory(String filename)
	{
		this.filename = filename;
		counter = 0;
		new_event_found = false;
		eventQueue = new DefaultProgStateCollection();
		inputFactory = XMLInputFactory.newInstance();
		try
		{
			if(filename == null)
				xml_input_stream = System.in;
			else
				xml_input_stream  = new FileInputStream(filename);
			eventReader = inputFactory.createXMLEventReader(xml_input_stream);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		QueueClearCount = 0;
	}

	@Override
	public String getNextJSONEvent() {
		// TODO Auto-generated method stub
		String ret_json;
		new_event_found = false;
		ReadNextState();
		if(curr_event != null && new_event_found )
		{
			Gson gson = new Gson();
			ret_json = gson.toJson(curr_state);
		}
		else
			ret_json = "{}";
//		System.err.println(ret_json);
		return ret_json;
	}
	private void ReadNextState()
	{
		boolean processed_an_event = false;
		
		while(!processed_an_event){
			
			if(eventReader.hasNext())
			{
				try
				{
					curr_event = eventReader.nextEvent();
					
					if(curr_event.isStartElement())
					{
						String localpart = curr_event.asStartElement().getName().getLocalPart();
						if(System.getProperty("DebugMode")!= null)
						{
							System.err.println(localpart);
						}
						if(localpart.contains(eventname))
						{
							curr_state = new DefaultProgramState(counter);
							
						}
						if(localpart.contains(fieldname))
						{
							curr_key = getCharacterData(curr_event,eventReader);
							if(System.getProperty("DebugMode")!= null)
							{
								System.err.println(curr_key);
							}
						}
						if(localpart.contains(timestampField))
						{
							String timeStamp = getCharacterData(curr_event,eventReader);
							curr_state.setValue("timestamp", timeStamp);
							double tsAsDouble = 0;
							try {
								tsAsDouble = Double.parseDouble(timeStamp);
								curr_state.setTimestamp(tsAsDouble);
							} 
							catch (NumberFormatException ne) {
								logger.debug("Could not parse as timestamp !!" + timeStamp);
							}
						}
						if(localpart.contains(fieldvalue))
						{
							curr_value= getCharacterData(curr_event,eventReader);
							curr_state.setValue(curr_key, curr_value);
							if(System.getProperty("DebugMode")!= null)
							{
								System.err.println(curr_key + "=" + curr_value);
							}
						}
					}
					if(curr_event.isEndElement())
					{
	
						if(curr_event.asEndElement().getName().getLocalPart().contains(eventname))
						{
							processed_an_event = true;
							if(curr_state != null)
							{
								new_event_found = true;
								eventQueue.add(curr_state);
								counter++;
							}

						}
						else if(curr_event.asEndElement().getName().getLocalPart().contains(toplevelname))
						{
							processed_an_event = true;
						}
					}
				}
				catch(XMLStreamException xe)
				{
					System.err.println(xe.getMessage());
				}
			}
			else
			{
				curr_event = null;
				processed_an_event = true;
				try {
					xml_input_stream.close();
				} catch (Exception e) {
					// TODO: handle exception
					System.err.println(e.getMessage());
				}
	
			}
		}
	}
    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        curr_event = eventReader.nextEvent();
        if (curr_event.isCharacters()) {
            result = curr_event.asCharacters().getData();
        }
        return result;
    }
	@Override
	public void setFile(String Inputfilename) {
		// TODO Auto-generated method stub
	}

	@Override
	public void export(String Outputfilename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProgState getNextProgState() {
		// TODO Auto-generated method stub
		new_event_found = false;
		ReadNextState();
		if(curr_event != null && new_event_found )
			return curr_state;
		else
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
	public void clearBuffer()
	{
		eventQueue.clear();
		QueueClearCount++;
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
	public boolean closeDataSource() {
		// TODO Auto-generated method stub
		try{
			eventReader.close();
		}catch(XMLStreamException ie){
			logger.fatal(ie.getMessage());
			return false;
		}
		return true;
	}
}
