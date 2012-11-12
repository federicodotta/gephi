/*
 * Your license here
 *
 * If you don't need it, remove this file and edit MyMetricUI accordingly.
 */

package org.dotta.infomap;

/**
 *
 * See http://wiki.gephi.org/index.php/HowTo_write_a_metric#Create_StatisticsUI
 * @author Your Name <your.name@your.company.com>
 */
public class InfoMapPanel extends javax.swing.JPanel {

    /** Creates new form MyMetricPanel */
    public InfoMapPanel() {
        initComponents();
    }

    /** Add here setters and getters for all properties users can edit. */


    
    /** Only useful if the algorithm takes graph type into account. */
    
    public boolean isDirected() {
        return directedRadioButton.isSelected();
    }

    public void setDirected(boolean directed) {
        directedButtonGroup.setSelected(directed ? directedRadioButton.getModel() : undirectedRadioButton.getModel(), true);
    }
    
    /*public boolean isHierarchical() {
        return jRadioButton1.isSelected();
    }*/

    /*public void setHierarchical(boolean hierarchical) {
        buttonGroup1.setSelected(hierarchical ? jRadioButton1.getModel() : jRadioButton2.getModel(), true);
    }*/

    /** ----------------------------------------------------------- */

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        directedButtonGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        directedRadioButton = new javax.swing.JRadioButton();
        undirectedRadioButton = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(456, 218));

        jPanel1.setBackground(new java.awt.Color(253, 248, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Cantarell", 1, 15));
        jLabel1.setText(org.openide.util.NbBundle.getMessage(InfoMapPanel.class, "InfoMapPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(InfoMapPanel.class, "InfoMapPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        directedButtonGroup.add(directedRadioButton);
        directedRadioButton.setText(org.openide.util.NbBundle.getMessage(InfoMapPanel.class, "InfoMapPanel.directedRadioButton.text")); // NOI18N

        directedButtonGroup.add(undirectedRadioButton);
        undirectedRadioButton.setText(org.openide.util.NbBundle.getMessage(InfoMapPanel.class, "InfoMapPanel.undirectedRadioButton.text")); // NOI18N
        undirectedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undirectedRadioButtonActionPerformed(evt);
            }
        });

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(InfoMapPanel.class, "InfoMapPanel.jCheckBox1.text")); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(directedRadioButton)
                    .addComponent(undirectedRadioButton))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(directedRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(undirectedRadioButton)
                .addGap(32, 32, 32)
                .addComponent(jCheckBox1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        /*
        if(jCheckBox1.getSelectedObjects() != null) {
            jCheckBox1.setSelected(true);
            System.out.println("Selezionato");
        } else {
            jCheckBox1.setSelected(false);
            System.out.println("Deselezionato");
        }*/
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void undirectedRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undirectedRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_undirectedRadioButtonActionPerformed

    public boolean getReorderCommunities() {
        return jCheckBox1.isSelected();
    
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup directedButtonGroup;
    protected javax.swing.JRadioButton directedRadioButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    protected javax.swing.JRadioButton undirectedRadioButton;
    // End of variables declaration//GEN-END:variables

}