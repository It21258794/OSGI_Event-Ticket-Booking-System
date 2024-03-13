package profitdisplaysubscriber;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ProfitDisplyActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		ProfitDisplyActivator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ProfitDisplyActivator.context = null;
	}

}
