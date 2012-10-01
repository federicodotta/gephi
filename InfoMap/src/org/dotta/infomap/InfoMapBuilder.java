/*
 * Your license here
 */

package org.dotta.infomap;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * See http://wiki.gephi.org/index.php/HowTo_write_a_metric#Create_StatisticsBuilder
 * 
 * @author Your Name <your.name@your.company.com>
 */
@ServiceProvider(service = StatisticsBuilder.class)
public class InfoMapBuilder implements StatisticsBuilder {

    @Override
    public String getName() {
        return "InfoMap";
    }

    @Override
    public Statistics getStatistics() {
        return new InfoMap();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return InfoMap.class;
    }

}
