package org.endeavourhealth.informationmanager.api.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;


public class InformationManagerMetricListener extends MetricsServlet.ContextListener {
    public static final MetricRegistry informationModelMetricRegistry = InformationManagerInstrumentedFilterContextListener.REGISTRY;

    @Override
    protected MetricRegistry getMetricRegistry() {
        return informationModelMetricRegistry;
    }
}
