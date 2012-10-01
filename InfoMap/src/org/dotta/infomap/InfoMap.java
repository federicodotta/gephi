/*
 * Your license here
 */

package org.dotta.infomap;

import java.util.LinkedList;
import java.util.Random;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;


/**
 *
 * See http://wiki.gephi.org/index.php/HowTo_write_a_metric#Create_Statistics
 * 
 * @author Federico Dotta <dottafederico@gmail.com>
 */
public class InfoMap implements Statistics, LongTask {

    protected int lastCommunityId = 0;
    
    private boolean cancel = false;
    private ProgressTicket progressTicket;

    private boolean directed;
    private boolean reorderCommunities;

    public static final String COMMUNITY_ID = "community_id";
    public static final String WEIGHT = "weight";
    
    private double entropy_initial;
    private double entropy;
    private double entropy_delta1;
    private double entropy_control;
    
    @Override
    public void execute(GraphModel graphModel, AttributeModel attributeModel) {
        
        // Caso diretto
        /*if(isDirected()) {
            SparseVector sp;
            
            
            
            
        } else {*/
            
        Graph graph = graphModel.getGraphVisible();
        
        int currentNeightbourCommunityID;
        int currentNodeCommunityID;
        
        // Blocco il grafo
        graph.readLock();
        
        /*
         * Aggiungo l'attributo Community Id agli attributi dei nodi
         * 
         */        
        AttributeTable nodeTable = attributeModel.getNodeTable();
        AttributeColumn colCommunityID = nodeTable.getColumn(COMMUNITY_ID);
        if (colCommunityID == null) {
            colCommunityID = nodeTable.addColumn(COMMUNITY_ID, "Community ID", AttributeType.INT, AttributeOrigin.COMPUTED, 0);
        } else {
            nodeTable.removeColumn(colCommunityID);
            colCommunityID = nodeTable.addColumn(COMMUNITY_ID, "Community ID", AttributeType.INT, AttributeOrigin.COMPUTED, 0);
        }
        // Può essere utile. Vediamo poi se levarlo
        AttributeColumn colWeight = nodeTable.getColumn(WEIGHT);
        if (colWeight == null) {
            colWeight = nodeTable.addColumn(WEIGHT, "Total weight", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, 0.0);
        } else {
            nodeTable.removeColumn(colWeight);
            colWeight = nodeTable.addColumn(WEIGHT, "Total weight", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, 0.0);        
        }
        
        
        // Calcolo il peso totale degli archi per il calcolo dei pesi relativi
        Edge[] edges = graph.getEdges().toArray();
        double total_weight = 0.0;
        for (Edge e : edges) {
            total_weight += e.getWeight();        
        }
        total_weight *= 2;
        System.out.println("total_weight: "+total_weight);
        
        
        // Inizializzazione
        /*
         * In questa fase creo una community per ogni nodo.
         * 
         */
        //Community[] communities = new Community[graph.getNodeCount()];
        LinkedList<Community> communities = new LinkedList<Community>();
        //System.out.println("Total: " + graph.getNodeCount());
        int counter = 0;
        Node[] nodes = graph.getNodes().toArray();
        Edge[] tempEdgesNeightbors;
        for (Node n : nodes) {
            //System.out.println("Counter: " + counter);
            communities.add(counter,new Community());
            communities.get(counter).addNode(n);            
            //communities[counter] = new Community();
            //communities[counter].addNode(n);
            
            tempEdgesNeightbors = graph.getEdges(n).toArray();
            for(Edge e : tempEdgesNeightbors) {
                // Setto il peso di uscita totale della comunità
                communities.get(counter).setExitWeight(communities.get(counter).getExitWeight() + (e.getWeight()/total_weight));                
                // Setto il peso totale della comunità
                communities.get(counter).setWeight(communities.get(counter).getWeight() + (e.getWeight()/total_weight));    
                // Setto il peso totale del nodo 
                n.getAttributes().setValue(colWeight.getIndex(), ((Double)n.getAttributes().getValue(colWeight.getIndex())) + (e.getWeight()/total_weight));
            }            
            
            //Store the community id in the node attribute
            n.getAttributes().setValue(colCommunityID.getIndex(), counter);
            counter++;
        }

            
        Random random = new Random();
        
        int exIndex;
        
        double total_W_exit = 0.0; // W exit
        
        double partial_W_alpha = 0.0; // W_alpha
        double partial_W_i = 0.0;  // W_i
        double partial_W_i_exit = 0.0; // W_i exit
        
        double total_W_i_exit = 0.0;
        double total_W_alpha = 0.0;
        double total_W_i_exit_W_i = 0.0;
        
        
        
        /*
         * Calcolo iniziale dell'entropia
         * 
         */       
        Node[] nodesCurrentCommunity;
        
        for(exIndex=0;exIndex<communities.size();exIndex++) {
        //while(indexes.size()!=0) {
        
            //exIndex = indexes.remove(random.nextInt(indexes.size()));           
            
            nodesCurrentCommunity = communities.get(exIndex).getNodes();
            currentNodeCommunityID = communities.get(exIndex).getId();
            
            partial_W_i = 0;
            partial_W_i_exit = 0;    
            
            for(Node n : nodesCurrentCommunity) {
            
            
                tempEdgesNeightbors = graph.getEdges(n).toArray();
            
                partial_W_alpha = 0;      
                
                
                for(Edge e : tempEdgesNeightbors) {
                    
                
                    partial_W_alpha += (e.getWeight()/total_weight);
                    partial_W_i += (e.getWeight()/total_weight);                
                
                
                    // Se la comunità del nodo destinazione dell'arco è diversa da quella del nodo sorgente
                    // arco che esce dalla comunità
                    if(e.getSource().getId() != n.getId())
                        currentNeightbourCommunityID = (Integer)e.getSource().getAttributes().getValue(colCommunityID.getIndex());
                    else
                        currentNeightbourCommunityID = (Integer)e.getTarget().getAttributes().getValue(colCommunityID.getIndex());
                    
                    if(currentNeightbourCommunityID != currentNodeCommunityID) {
                        
                        partial_W_i_exit += (e.getWeight()/total_weight);
                        total_W_exit += (e.getWeight()/total_weight);
                        
                    }  
                    
                    
                }   
                
                
                total_W_alpha += plogp(partial_W_alpha);
                             
                partial_W_alpha = 0.0;
                
            }
            
            total_W_i_exit += plogp(partial_W_i_exit);
            total_W_i_exit_W_i += plogp(partial_W_i+partial_W_i_exit);
            
            partial_W_i = 0.0;
            partial_W_i_exit = 0.0;
            
        }
        
        entropy_delta1 = total_W_exit;
        total_W_exit = plogp(total_W_exit);
        // Entropia iniziale:
        entropy = total_W_exit - (2*total_W_i_exit) - total_W_alpha + total_W_i_exit_W_i;
        entropy_initial = entropy;
        
        System.out.println("Entropia iniziale: " + entropy);
        System.out.println("Delta1 iniziale: " + entropy_delta1);
        
        System.out.println("Initial entropy: ");
        System.out.println("Delta1: "+total_W_exit);
        System.out.println("Delta2: "+(-2*total_W_i_exit));
        System.out.println("Delta3: "+(-total_W_alpha));
        System.out.println("Delta4: "+total_W_i_exit_W_i);    

        
        LinkedList<Integer> indexes = new LinkedList<Integer>(); 
        
        
                 
        double currentNodeExitEdges;
        double currentNeightbourExitEdges;
        double currentNodeEdges;
        double neightbourToNodeEdges;
        double nodeToOwnCommunityEdges;
            
        double delta1;
        double delta2;
        double delta3;
        double delta4;
        double deltaTot;        
        
        double bestDelta;
        int bestCommunityID;
        double bestNodeExitEdges;
        double bestNeightbourExitEdges;
        double bestDelta1;
        
        int currentTargetCommunityID;
        
        int iteration = 0;
        boolean changed;
        do {
            iteration++;
            changed = false;
            
            System.out.println("########### ITERAZIONE "+iteration+" ##############");
            
            counter = 0;
            for (Node n: nodes) {
                indexes.add(counter);
                counter++;
            }
            
        
            // POSSIBILITA DA VALUTARE: Creare una linkedlist con gli indici delle comunità da eliminare e alla fine di
            // ogni ciclo di iterazione li elimino
            while(indexes.size()!=0) {
        
                exIndex = indexes.remove(random.nextInt(indexes.size()));    
                //exIndex = 0;    //DA LEVARE
            
                currentNodeCommunityID = (Integer)nodes[exIndex].getAttributes().getValue(colCommunityID.getIndex());
            
            
                bestDelta = 0;
                bestDelta1 = 0;
                bestNodeExitEdges = 0;
                bestNeightbourExitEdges = 0;
                bestCommunityID = currentNodeCommunityID;
                
                tempEdgesNeightbors = graph.getEdges(nodes[exIndex]).toArray();
                
                
                
                for(Edge e : tempEdgesNeightbors) {
                    
                    currentNodeExitEdges = 0.0;
                    currentNeightbourExitEdges = 0.0;
                    currentNodeEdges = 0.0;
                    neightbourToNodeEdges = 0.0;
                    nodeToOwnCommunityEdges = 0.0;
                    
                    delta1 = 0;
                    delta2 = 0;
                    delta3 = 0;
                    delta4 = 0;
                    deltaTot = 0;
                    
                    
                    if(e.getSource().getId() != nodes[exIndex].getId())
                        currentNeightbourCommunityID = (Integer)e.getSource().getAttributes().getValue(colCommunityID.getIndex());
                    else
                        currentNeightbourCommunityID = (Integer)e.getTarget().getAttributes().getValue(colCommunityID.getIndex());
                    
                    if(currentNeightbourCommunityID != currentNodeCommunityID) {
                        
                        for(Edge f : tempEdgesNeightbors) {
                            
                            if(f.getSource().getId() != nodes[exIndex].getId())
                                currentTargetCommunityID = (Integer)f.getSource().getAttributes().getValue(colCommunityID.getIndex());
                            else
                                currentTargetCommunityID = (Integer)f.getTarget().getAttributes().getValue(colCommunityID.getIndex());
                            
                            currentNodeEdges += (f.getWeight()/total_weight);
                            
                            if(currentTargetCommunityID != currentNeightbourCommunityID) {
                                
                                currentNeightbourExitEdges += (f.getWeight()/total_weight);
                                
                            } else {
                                
                                neightbourToNodeEdges += (f.getWeight()/total_weight);
                                
                            }
                            
                            if(currentTargetCommunityID != currentNodeCommunityID) {
                                
                                currentNodeExitEdges += (f.getWeight()/total_weight);
                                
                            } else {
                                
                                nodeToOwnCommunityEdges += (f.getWeight()/total_weight);
                                
                            }
                            
                            
                        }
                        
                        // Nuovi delta
                        delta1 = plogp(entropy_delta1 - currentNodeExitEdges + nodeToOwnCommunityEdges - neightbourToNodeEdges + currentNeightbourExitEdges);
                        delta1 = delta1 - plogp(entropy_delta1);
                        delta2 = 2*(plogp(communities.get(currentNodeCommunityID).getExitWeight())) + 2*(plogp(communities.get(currentNeightbourCommunityID).getExitWeight())) -2 * (plogp(communities.get(currentNodeCommunityID).getExitWeight() - currentNodeExitEdges + nodeToOwnCommunityEdges) + plogp(communities.get(currentNeightbourCommunityID).getExitWeight() - neightbourToNodeEdges + currentNeightbourExitEdges));
                        delta3 = 0;
                        delta4 = - plogp(communities.get(currentNodeCommunityID).getExitWeight() + communities.get(currentNodeCommunityID).getWeight()) - plogp(communities.get(currentNeightbourCommunityID).getExitWeight() + communities.get(currentNeightbourCommunityID).getWeight()) + plogp(communities.get(currentNodeCommunityID).getExitWeight() - currentNodeExitEdges + nodeToOwnCommunityEdges + communities.get(currentNodeCommunityID).getWeight() - currentNodeEdges) + plogp(communities.get(currentNeightbourCommunityID).getExitWeight() - neightbourToNodeEdges + currentNeightbourExitEdges + communities.get(currentNeightbourCommunityID).getWeight() + currentNodeEdges); 
                                              
                        deltaTot = delta1 + delta2 + delta3 + delta4;
                        
                        if(bestDelta > deltaTot) {
                                                        
                            bestCommunityID = currentNeightbourCommunityID;
                            bestDelta = deltaTot;
                            bestNodeExitEdges = currentNodeExitEdges - nodeToOwnCommunityEdges;
                            bestNeightbourExitEdges = currentNeightbourExitEdges - neightbourToNodeEdges;
                            bestDelta1 = entropy_delta1 - currentNodeExitEdges + nodeToOwnCommunityEdges - neightbourToNodeEdges + currentNeightbourExitEdges;
                            
                            /*System.out.println("####################################");
                            System.out.println("bestCommunityID: "+ bestCommunityID);
                            System.out.println("bestDelta: "+ bestDelta);
                            System.out.println("bestNodeExitEdges: "+ bestNodeExitEdges);
                            System.out.println("bestNeightbourExitEdges: "+ bestNeightbourExitEdges);
                            System.out.println("bestDelta1: "+ bestDelta1);
                            System.out.println("####################################");*/
                            
                        }
                        
                        currentNodeEdges = 0;
                        currentNeightbourExitEdges = 0;
                        currentNodeExitEdges = 0;
                        neightbourToNodeEdges = 0;
                        nodeToOwnCommunityEdges = 0;
                        
                    }
                    
                }
                //System.out.println("BestDelta: " + bestDelta);
                if(bestDelta != 0) {
                    
                    changed = true;
                    
                    // Modifica dell'entropia globale
                    //System.out.println("Old entropy: " + entropy);
                    entropy += bestDelta;
                    //System.out.println("New entropy: " + entropy);
                    
                    entropy_delta1 = bestDelta1;
                    
                    // Modifica delle comunità
                    //System.out.println("currentNodeCommunityID: " + currentNodeCommunityID);
                    //System.out.println("bestCommunityID: " + bestCommunityID);
                    communities.get(currentNodeCommunityID).removeNode(nodes[exIndex]);
                    communities.get(bestCommunityID).addNode(nodes[exIndex]);
                    nodes[exIndex].getAttributes().setValue(colCommunityID.getIndex(), bestCommunityID);
                    
                    // Modifica dei parametri delle singole comunità (probabilmente non è necessario il weight del nodo)                    
                    communities.get(bestCommunityID).setWeight(communities.get(bestCommunityID).getWeight() + (Double)nodes[exIndex].getAttributes().getValue(colWeight.getIndex()) );                    
                    communities.get(bestCommunityID).setExitWeight(communities.get(bestCommunityID).getExitWeight() + bestNeightbourExitEdges);          
                    
                    communities.get(currentNodeCommunityID).setWeight(communities.get(currentNodeCommunityID).getWeight() - (Double)nodes[exIndex].getAttributes().getValue(colWeight.getIndex()) );
                    communities.get(currentNodeCommunityID).setExitWeight(communities.get(currentNodeCommunityID).getExitWeight() - bestNodeExitEdges);
                                        
                }
                  
                
            }
            
        } while(changed);
        
              
        
        
         /*
         * Calcolo finale dell'entropia (controllo)
         * 
         */ 
        
        // ######################################################
               
        
        total_W_i_exit = 0.0;
        total_W_alpha = 0.0;
        total_W_i_exit_W_i = 0.0;
        total_W_exit = 0.0;
        
        //Node[] nodesCurrentCommunity;
        
        for(exIndex=0;exIndex<communities.size();exIndex++) {
        //while(indexes.size()!=0) {            
            
            //exIndex = indexes.remove(random.nextInt(indexes.size()));           
            //System.out.println("exIndex: "+exIndex);
            
            nodesCurrentCommunity = communities.get(exIndex).getNodes();
            currentNodeCommunityID = communities.get(exIndex).getId();
            
            partial_W_i = 0;
            partial_W_i_exit = 0;    
            
            for(Node n : nodesCurrentCommunity) {
            
            
                //currentNodeCommunityID = (Integer)n.getAttributes().getValue(colCommunityID.getIndex());
            
                tempEdgesNeightbors = graph.getEdges(n).toArray();
            
                partial_W_alpha = 0;
                    
                
                
                for(Edge e : tempEdgesNeightbors) {
                    
                    //System.out.println("Weight: " + e.getWeight());
                
                    partial_W_alpha += (e.getWeight()/total_weight);
                    partial_W_i += (e.getWeight()/total_weight);
                
                
                
                
                    // Se la comunità del nodo destinazione dell'arco è diversa da quella del nodo sorgente
                    // arco che esce dalla comunità
                    if(e.getSource().getId() != n.getId())
                        currentNeightbourCommunityID = (Integer)e.getSource().getAttributes().getValue(colCommunityID.getIndex());
                    else
                        currentNeightbourCommunityID = (Integer)e.getTarget().getAttributes().getValue(colCommunityID.getIndex());
                    
                    if(currentNeightbourCommunityID != currentNodeCommunityID) {
                        
                        partial_W_i_exit += (e.getWeight()/total_weight);
                        total_W_exit += (e.getWeight()/total_weight);
                        
                    }  
                    
                    //System.out.println("partial_W_i: " + partial_W_i);
                    //System.out.println("partial_W_i_exit: " + partial_W_i_exit);
                    //System.out.println("partial_W_alpha: " + partial_W_alpha);
                    
                }   
                
                
                total_W_alpha += plogp(partial_W_alpha);
                
                //System.out.println("total_W_i_exit: " + total_W_i_exit);
                //System.out.println("total_W_alpha: " + total_W_alpha);
                //System.out.println("total_W_i_exit_W_i: " + total_W_i_exit_W_i);
                
                
                partial_W_alpha = 0.0;
                
            }
            
            //System.out.println("partial_W_i: " + partial_W_i);
            //System.out.println("partial_W_i_exit: " + partial_W_i_exit);
            
            total_W_i_exit += plogp(partial_W_i_exit);
            total_W_i_exit_W_i += plogp(partial_W_i+partial_W_i_exit);
            
            partial_W_i = 0.0;
            partial_W_i_exit = 0.0;
            
        }
        
        total_W_exit = plogp(total_W_exit);
        // Entropia iniziale:
        //double entropy = total_W_exit - (2*total_W_i_exit) - total_W_alpha + total_W_i_exit_W_i;
        entropy_control = total_W_exit - (2*total_W_i_exit) - total_W_alpha + total_W_i_exit_W_i;       
        System.out.println("Entropy control:");
        System.out.println("Delta1: "+total_W_exit);
        System.out.println("Delta2: "+(-2*total_W_i_exit));
        System.out.println("Delta3: "+(-total_W_alpha));
        System.out.println("Delta4: "+total_W_i_exit_W_i);  
        
        printCommunities(communities);
        System.out.println("Reorder communities: "+reorderCommunities);
        
        
        // ###################################################
        
        
        if(reorderCommunities) {
            Community c;
            int lastCommunity = 0;
            
            for(int i=0;i<communities.size();i++) {
                
                if(communities.get(i).size()!=0) {
                    c = communities.get(i);
                    nodes = c.getNodes();
                    for(Node n : nodes) {
                        n.getAttributes().setValue(colCommunityID.getIndex(), lastCommunity);
                    }
                    c.setId(lastCommunity);
                    lastCommunity++;
                } else {
                    System.out.println("Removing community "+i);
                    communities.remove(i);
                    i--;
                }
                
            }
        }
        
        //printCommunities(communities);
        
        nodeTable.removeColumn(colWeight);
        
        graph.readUnlockAll();

        
        
        /*
        //Your algorithm
        //See http://wiki.gephi.org/index.php/HowTo_write_a_metric#Implementation_help
        try {
            Progress.start(progressTicket, graph.getNodeCount());

            for (Node n : graph.getNodes()) {
                //do something
                Progress.progress(progressTicket);
                if (cancel) {
                    break;
                }
            }
            graph.readUnlockAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Unlock graph
            graph.readUnlockAll();
        }
         * 
         */
        //}
    }
    
    /** Only useful if the algorithm takes graph type into account. */

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }
        
    public void setReorderCommunities(boolean bool) {
        this.reorderCommunities = bool;
    }

    /** ----------------------------------------------------------- */

    @Override
    public String getReport() {
        //Write the report HTML string here
        String report = "Initial entropy is " + entropy_initial + "\n";
        report += "Calculated entropy is " + entropy + "\n";
        report += "Control entropy is " + entropy_control;

        return report;
    }

    @Override
    public boolean cancel() {
        cancel = true;
        return true;
    }

    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progressTicket = progressTicket;
    }
    
    public static double plogp(double p) {
        return p*log(p);
    }
    
    public static double log(double p) {
        if(p!=0)
            return (Math.log(p) / Math.log(2));
        else
            return 0;
    }
    
    public static void printCommunities(LinkedList<Community> communities) {
        
        for(Community c : communities) {
            c.print();            
            System.out.println();
        }
    
    }

    
    private class Community {
        
        private int id;
        private LinkedList<Node> nodes;
        private double exitWeight;
        private double weight;
        
        public Community() {
            id = lastCommunityId;
            lastCommunityId++;
            nodes = new LinkedList<Node>();
            exitWeight = 0.0;
            weight = 0.0;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public Node[] getNodes() {
            Node[] n = new Node[nodes.size()];
            return nodes.toArray(n);
        }
        
        public int size() {
            return nodes.size();
        }
        
        public void addNode(Node n) {
            nodes.add(n);
        }
        
        public boolean removeNode(Node n) {
            return nodes.remove(n);
        }
        
        public double getExitWeight() {
            return exitWeight;
        }
        
        public void setExitWeight(double w) {
            exitWeight = w;
        }
        
        public double getWeight() {
            return weight;
        }
        
        public void setWeight(double w) {
            weight = w;
        }
        
        public void print() {
            System.out.println("Community " + id + " - " + "Weight: " + weight + " - " + "Exit weight: " + exitWeight);
            System.out.print("Nodi: ");
            for(int i=0;i<nodes.size();i++) {
                System.out.print(nodes.get(i).getId() + ",");
            }
            System.out.println();
        
        }
    
    }
}