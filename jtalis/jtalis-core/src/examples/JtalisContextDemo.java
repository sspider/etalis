import com.jtalis.core.JtalisContextImpl;
import com.jtalis.core.event.provider.DefaultInputProvider;
import com.jtalis.core.event.provider.DefaultOutputProvider;
import com.jtalis.core.plengine.JPLEngineWrapper;
import com.jtalis.core.plengine.PrologEngineWrapper;

/**
 * This example shows basic configuration and how to run Jtalis.
 * 
 * <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class JtalisContextDemo {

	public static void main(String[] args) throws Exception {
		PrologEngineWrapper<?> engine = new JPLEngineWrapper(false);

		JtalisContextImpl ctx = new JtalisContextImpl(engine);
		ctx.addEventTrigger("a/_", "d/_");
		ctx.addDynamicRule("d(X) <- a(X) where X > 25");

		ctx.registerOutputProvider(new DefaultOutputProvider());
		ctx.registerInputProvider(new DefaultInputProvider(), 1000);

		ctx.waitForInputProviders();
		ctx.shutdown();
	}
}
