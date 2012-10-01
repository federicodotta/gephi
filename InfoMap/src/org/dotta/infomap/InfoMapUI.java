/*
 * Your license here
 */

package org.dotta.infomap;

import javax.swing.JPanel;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * See http://wiki.gephi.org/index.php/HowTo_write_a_metric#Create_StatisticsUI
 * 
 * @author Your Name <your.name@your.company.com>
 */
@ServiceProvider(service = StatisticsUI.class)
public class InfoMapUI implements StatisticsUI {

    private InfoMapPanel panel;
    private InfoMap myMetric;

    @Override
    public JPanel getSettingsPanel() {
        panel = new InfoMapPanel();
        return panel; //null if no panel exists
    }

    @Override
    public void setup(Statistics statistics) {
        this.myMetric = (InfoMap) statistics;
        if (panel != null) {
            panel.setDirected(myMetric.isDirected()); //Remove it if not useful
        }
    }

    @Override
    public void unsetup() {
        if (panel != null) {
            myMetric.setDirected(panel.isDirected()); //Remove it if not useful
            //Mappa la funzione dell'interfaccia con quella dell'algoritmo
            myMetric.setReorderCommunities(panel.getReorderCommunities());
        }
        panel = null;
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return InfoMap.class;
    }

    @Override
    public String getValue() {
        //Returns the result value on the front-end. 
        //If your metric doesn't have a single result value, return null.
        return null;
    }

    @Override
    public String getDisplayName() {
        return "InfoMap";
    }

    @Override
    public String getCategory() {
        //The category is just where you want your metric to be displayed: NODE, EDGE or NETWORK.
        //Choose between:
        //- StatisticsUI.CATEGORY_NODE_OVERVIEW
        //- StatisticsUI.CATEGORY_EDGE_OVERVIEW
        //- StatisticsUI.CATEGORY_NETWORK_OVERVIEW
        return StatisticsUI.CATEGORY_NETWORK_OVERVIEW;
    }

    @Override
    public int getPosition() {
        //The position control the order the metric front-end are displayed. 
        //Returns a value between 1 and 1000, that indicates the position. 
        //Less means upper.
        return 800;
    }
    
    @Override
    public String getShortDescription() {
        return "";
    }

}
