package org.endeavourhealth.informationmodel.api.metrics;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;

public class EdsInstrumentedFilterContextListener extends InstrumentedFilterContextListener {

    public static final MetricRegistry REGISTRY = SharedMetricRegistries.getOrCreate("EdsRegistry");

    @Override
    protected MetricRegistry getMetricRegistry() {
        return REGISTRY;
    }
}
