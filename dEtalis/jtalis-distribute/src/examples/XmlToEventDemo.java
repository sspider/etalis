import java.io.File;
import java.io.FileInputStream;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.event.schema.EventSchema;
import com.jtalis.core.event.schema.SchemaConformedEvent;
import com.jtalis.core.event.schema.UnkownEventSchemaException;
import com.jtalis.core.event.xml.XMLEventIO;
import com.jtalis.core.event.xml.XMLEventSchema;

/**
 * One of the features provided by Jtalis is that it can convert ETALIS events
 * into XML and vice vers, according to a given XML Schema. This example shows
 * how to do that. 
 *
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class XmlToEventDemo {

	public static void main(String[] args) throws Exception {
		System.out.println("--- Read XSD ---");
		EventSchema xsd = new XMLEventSchema(new File("resources/UserEventSchema.xsd"));

		// get some more info about the schema
		for (String evt : xsd.getEvents()) {
			System.out.println(evt + ": " + xsd.getProperties(evt));
		}

		XMLEventIO io = new XMLEventIO(xsd);

		System.out.println("\n--- Write event ---");
		EtalisEvent event = new EtalisEvent("user", "Anna Kournikova", 23, new EtalisEvent("address", "Ann str.", 45));
		io.serialize(event, System.out);

		System.out.println("\n--- Read events from file ---");
		FileInputStream is = new FileInputStream(new File("resources/event.xml"));

		System.out.println(io.deserializeAll(is));

		System.out.println("\n--- Get event properties using the XSD ---");

		SchemaConformedEvent e = new SchemaConformedEvent(event, xsd);
		System.out.println(e.getProperty("name") + ":" + e.getProperty("name").getClass());
		System.out.println(e.getProperty("age") + ":" + e.getProperty("age").getClass());

		// You can use it with custom events
		UserDefinedEvent ue = new UserDefinedEvent(event, xsd);
		System.out.println(ue);
	}

	static class UserDefinedEvent extends SchemaConformedEvent {

		public UserDefinedEvent(EtalisEvent event, EventSchema schema) throws UnkownEventSchemaException {
			super(event, schema);
		}

		public int getAge() {
			return (Integer) getProperty("age");
		}

		public String getName() {
			return (String) getProperty("name");
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "[" + getName() + ", " + getAge() + "]";
		}
	}
}
