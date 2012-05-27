package jp.redmine.redmineclient.external;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
abstract public class BaseParser<TYPE> {
	protected XmlPullParser xml;
	public volatile List<DataCreationHandler<TYPE>> handlerDataCreation = new ArrayList<DataCreationHandler<TYPE>>();
	public void setXml(XmlPullParser xml){
		this.xml = xml;
	}

	abstract public void parse() throws XmlPullParserException, IOException;

	protected String getNextText() throws XmlPullParserException, IOException{
		String work = "";
		if(xml.next() == XmlPullParser.TEXT){
			work = xml.getText();
		}
		if(work == null)	work = "";
		return work;
	}
	public void registerDataCreation(DataCreationHandler<TYPE> ev){
		this.handlerDataCreation.add(ev);
	}
	public void unregisterDataCreation(DataCreationHandler<TYPE> ev){
		this.handlerDataCreation.remove(ev);
	}

	protected void notifyDataCreation(TYPE data){
		//inherit from http://www.ibm.com/developerworks/jp/java/library/j-jtp07265/
		for(DataCreationHandler<TYPE> ev:this.handlerDataCreation){
			try {
				ev.onData(data);
			} catch (RuntimeException e) {
				//log("Unexpected exception in listener", e);
				this.handlerDataCreation.remove(ev);
			}
		}
	}

}