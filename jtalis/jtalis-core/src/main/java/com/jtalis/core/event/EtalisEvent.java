package com.jtalis.core.event;

import java.io.Serializable;
import java.util.Arrays;

import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.TermBuilder;

/**
 * Represents main ETALIS event. It is actually a compound term.  
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EtalisEvent extends CompoundTerm implements Serializable {

	private static final long	serialVersionUID	= 4488805239273426604L;

	/** Rule ID (for persistence) */
	private String rid;

	/** Date started (for complex events) */
	private EventTimestamp timeStarts;

	/** Date ended (for complex events) */
	private EventTimestamp timeEnds;

	
	public EtalisEvent() {
		super();
	}

	/**
	 * Constructs event with the passed name and arguments
	 * @param name
	 * @param args
	 */
	public EtalisEvent(String name, Object...args) {
		this(name, new EventTimestamp(), new EventTimestamp(), args);
	}

	/**
	 * Constructs event with the passed name, date of creation and arguments
	 * @param name
	 * @param starts
	 * @param ends
	 * @param args
	 */
	public EtalisEvent(String name, EventTimestamp starts, EventTimestamp ends, Object...args) {
		super(name, TermBuilder.transformObjectsToTerms(args));
		this.timeStarts = starts;
		this.timeEnds = ends;
	}

	/**
	 * Constructs event with the passed name, date of creation and arguments
	 * @param name
	 * @param rid
	 * @param starts
	 * @param ends
	 * @param args
	 */
	public EtalisEvent(String name, String rid, EventTimestamp starts, EventTimestamp ends, Object...args) {
		super(name, TermBuilder.transformObjectsToTerms(args));
		this.rid = rid;
		this.timeStarts = starts;
		this.timeEnds = ends;
	}

	public EventTimestamp getTimeStarts() {
		return timeStarts;
	}

	public void setTimeStarts(EventTimestamp timeStarts) {
		this.timeStarts = timeStarts;
	}

	public EventTimestamp getTimeEnds() {
		return timeEnds;
	}

	public void setTimeEnds(EventTimestamp timeEnds) {
		this.timeEnds = timeEnds;
	}

	public String getRuleID() {
		return rid;
	}

	public void setRuleID(String rid) {
		this.rid = rid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	/**
	 * Returns indexed argument. e.g. if this event represents "snack('Chips', 1)", 
	 * {@code getArgument(0)} would return "Chips".
	 * 
	 * @param idx argument index
	 * @return argument at position {@code idx}, or {@code null} if idx < 0 or idx >= arity()
	 */
	public Object getProperty(int idx) {
		if (idx != -1) {
			return super.getTerm(idx).getValue();
		}
		return null;
	}

	/**
	 * Returns all properties' values
	 */
	public Object[] getProperties() {
		Object[] ret = new Object[getArity()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = getProperty(i);
		}
		return ret;
	}

	/**
	 * Returns indexed double argument. e.g. if this event represents "stock('APPL', "+0.12")", 
	 * {@code getArgument(1)} would return 0.12.
	 * 
	 * @param idx argument index
	 * @return argument at position {@code idx}, or {@code null} if idx < 0 or idx >= arity() or the actual argument is not double 
	 */
	public Double getDoubleProperty(int idx) {
		Object o = getProperty(idx);
		if (o instanceof Double) {
			return (Double) o;
		}
		return null;
	}

	/**
	 * Returns indexed string property. e.g. if this event represents "user('John', 34)", 
	 * {@code getProperty(1)} would return the string "34".
	 * 
	 * @param idx argument index
	 * @return argument behind key
	 */
	public String getStringProperty(int idx) {
		return String.valueOf(getProperty(idx));
	}

	@Override
	public String toString() {
		return "Event: [" + getPrologString() + ", " + getTimeStarts() + ", " + getTimeEnds() + "]";
	}

	@Override
	public int hashCode() {
		return getName().hashCode() ^ Arrays.hashCode(getProperties()); 
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof EtalisEvent) {
			EtalisEvent other = (EtalisEvent) obj;
			if (getName().equals(other.getName())) {
				return Arrays.equals(getProperties(), other.getProperties());
			}
		}
		return false;
	}

	//pka: serialization fix, see http://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html
	//does not work with glassfish 3, since serialize does not produce a suitable outputStream :(
/*	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException {
		new DefaultEventIO().serialize(this, out);
	}
	
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException, InvalidEventFormatException {
		
		try(InputStreamReader isr = new InputStreamReader(in); BufferedReader br = new BufferedReader(isr)) {
			boolean success = false;
			boolean timeout = false;
			long t0 = System.currentTimeMillis();
			while(!br.ready()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			System.out.println(br.readLine());
			
		}
		EtalisEvent that = new DefaultEventIO().deserialize(in);
		//pka: this=that ;)
		this.setName(that.getName());
		this.setRid(that.getRid());
		this.setTerms(that.getTerms());
		
		EventTimestamp startTimeStamp = new EventTimestamp();
		startTimeStamp.setIndex(that.getTimeStarts().getIndex());
		startTimeStamp.setTime(that.getTimeStarts().getTimeWithoutIndex());
		this.setTimeStarts(startTimeStamp);
		
		EventTimestamp endTimeStamp = new EventTimestamp();
		endTimeStamp.setIndex(that.getTimeEnds().getIndex());
		endTimeStamp.setTime(that.getTimeEnds().getTimeWithoutIndex());
		this.setTimeEnds(endTimeStamp);
	}
	
	@SuppressWarnings("unused")
	private void readObjectNoData() 
			throws ObjectStreamException{

	}*/
}
