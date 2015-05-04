package com.jtalis.core.terms;

import java.io.Serializable;

import com.jtalis.core.event.EtalisEvent;
import com.jtalis.core.plengine.logic.CompoundTerm;
import com.jtalis.core.plengine.logic.Term;

/**
 * AssertTEventTermerm
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class EventTerm extends CompoundTerm implements Serializable {

	private static final long serialVersionUID = -4711210975087839013L;
	String timeStartsToString,timeEndsToString;

	public EventTerm(){
		super();
	}



	public EventTerm(EtalisEvent event) {
		super("event", event);//super("event",event);       
		this.timeStartsToString = String.valueOf(event.getTimeStarts().getTime())+".0";
		this.timeEndsToString = String.valueOf(event.getTimeEnds().getTime())+".0";
	}


	@Override
	public String getPrologString() {
		if (getArity() == 0) {
			return this.getName();
		}
		StringBuilder builder = new StringBuilder(this.getName()).append("(");
		for (Term t : this.getTerms()) {
			builder.append(t.getPrologString()).append(", ");
		}

		if(this.timeStartsToString == null){
			builder.replace(builder.length() - 2, builder.length(), ")");
			return builder.toString();
		}
		else if(this.timeEndsToString == null){
			builder.append("[");
			builder.append(this.timeStartsToString);
			builder.append(",");
			builder.append(this.timeStartsToString);
			builder.append("]");

			builder.append(")");
			return builder.toString();
		}
		else{
			builder.append("[");

			builder.append(this.timeStartsToString);
			builder.append(", ");
			builder.append(this.timeEndsToString);
			builder.append("]");
			builder.append(")");
			//System.out.println("*************  "+builder.toString());
			return builder.toString();           
		}

	}
}