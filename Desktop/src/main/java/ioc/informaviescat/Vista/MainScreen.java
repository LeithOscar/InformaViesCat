
package ioc.informaviescat.Vista;

import ioc.informaviescat.Controller.IncidencesManagement;
import ioc.informaviescat.Controller.UsersManagement;
import ioc.informaviescat.Entities.Incident;
import ioc.informaviescat.Entities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Pau Cors Bardolet
 */
public class MainScreen extends javax.swing.JFrame {

    ImageIcon img = new ImageIcon("ICONS/icon.png");
    User user;
    String[][] dataFromUsers;
    String[][] dataValidar;
    String[][] dataValidated;
    String[][] dataAssigned;
    String[][] dataToClose;
    String[][] dataClosed;
    
    List<User> llistaUsuaris;
    
    List<Incident> llistaIncidencies;
    List<Incident> llistaPerValidar = new ArrayList<>();
    List<Incident> llistaValidades = new ArrayList<>();
    List<Incident> llistaAssignades = new ArrayList<>();
    List<Incident> llistaReparades = new ArrayList<>();
    List<Incident> llistaTancades = new ArrayList<>();
    
    /**
     * Modifica els paràmetres de la pantalla principal segons el tipus d'usuari que entra a l'aplicació, si és tècnic o administrador.
     *
     * @param user Usuari per al qual s'inicia la sessió
     * @throws java.io.IOException
     */
    public void setSessionType(User user) throws IOException{
        this.user = user;
        labelUser.setText(user.getName()+" "+user.getLastName());
        llistaIncidencies = IncidencesManagement.getIncidenceList();
        
        for(Incident incident: llistaIncidencies){
            if(incident.getIncidentTypeId() == 1){
                llistaPerValidar.add(incident);
            }
            else if(incident.getIncidentTypeId() == 2){
                llistaValidades.add(incident);
            }
            else if(incident.getIncidentTypeId() == 3){
                llistaAssignades.add(incident);
            }
            else if(incident.getIncidentTypeId() == 4){
                llistaReparades.add(incident);
            }
            else if(incident.getIncidentTypeId() == 5){
                llistaTancades.add(incident);
            }
        }
        
        if(user.getRolId() == 2){
            jTabbedPane2.setEnabledAt(0, false);
            jTabbedPane2.setEnabledAt(3, false);
            jTabbedPane2.setEnabledAt(4, false);
            jTabbedPane2.setEnabledAt(5, false);
            jTabbedPane2.setSelectedIndex(1);
            labelLevel.setText("Tècnic/a");
            
            int dimension = 0;
            String[][] data = new String[dimension][5];
            tableObertes.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableReparacio.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
        }
        else if(user.getRolId() == 1){
            labelLevel.setText("Administrador/a");
            int dimension = llistaPerValidar.size();
            dataValidar = new String[dimension][5];
            for(int i = 0; i < llistaPerValidar.size(); i++){
                dataValidar[i][0] = String.valueOf(llistaPerValidar.get(i).getId());
                dataValidar[i][1] = llistaPerValidar.get(i).getRoadName();
                dataValidar[i][2] = llistaPerValidar.get(i).getStartDate();
                dataValidar[i][3] = IncidencesManagement.getUrgency(llistaPerValidar.get(i));
                dataValidar[i][4] = llistaPerValidar.get(i).getDescription();
            }
            tableConfirmar.setModel(new javax.swing.table.DefaultTableModel(
            dataValidar,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableConfirmar.getColumnModel().getColumn(0).setMinWidth(75);
            tableConfirmar.getColumnModel().getColumn(0).setMaxWidth(75);
            tableConfirmar.getColumnModel().getColumn(1).setMinWidth(150);
            tableConfirmar.getColumnModel().getColumn(1).setMaxWidth(150);
            tableConfirmar.getColumnModel().getColumn(2).setMaxWidth(120);
            tableConfirmar.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaValidades.size();
            dataValidated = new String[dimension][5];
            for(int i = 0; i < llistaValidades.size(); i++){
                dataValidated[i][0] = String.valueOf(llistaValidades.get(i).getId());
                dataValidated[i][1] = llistaValidades.get(i).getRoadName();
                dataValidated[i][2] = llistaValidades.get(i).getStartDate();
                dataValidated[i][3] = IncidencesManagement.getUrgency(llistaValidades.get(i));
                dataValidated[i][4] = llistaValidades.get(i).getDescription();
            }
            tableObertes.setModel(new javax.swing.table.DefaultTableModel(
            dataValidated,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableObertes.getColumnModel().getColumn(0).setMinWidth(75);
            tableObertes.getColumnModel().getColumn(0).setMaxWidth(75);
            tableObertes.getColumnModel().getColumn(1).setMinWidth(150);
            tableObertes.getColumnModel().getColumn(1).setMaxWidth(150);
            tableObertes.getColumnModel().getColumn(2).setMaxWidth(120);
            tableObertes.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaAssignades.size();
            dataAssigned = new String[dimension][5];
            for(int i = 0; i < llistaAssignades.size(); i++){
                dataAssigned[i][0] = String.valueOf(llistaAssignades.get(i).getId());
                dataAssigned[i][1] = llistaAssignades.get(i).getRoadName();
                dataAssigned[i][2] = llistaAssignades.get(i).getStartDate();
                dataAssigned[i][3] = IncidencesManagement.getUrgency(llistaAssignades.get(i));
                dataAssigned[i][4] = llistaAssignades.get(i).getDescription();
            }
            tableReparacio.setModel(new javax.swing.table.DefaultTableModel(
            dataAssigned,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableReparacio.getColumnModel().getColumn(0).setMinWidth(75);
            tableReparacio.getColumnModel().getColumn(0).setMaxWidth(75);
            tableReparacio.getColumnModel().getColumn(1).setMinWidth(150);
            tableReparacio.getColumnModel().getColumn(1).setMaxWidth(150);
            tableReparacio.getColumnModel().getColumn(2).setMaxWidth(120);
            tableReparacio.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaReparades.size();
            dataToClose = new String[dimension][5];
            for(int i = 0; i < llistaReparades.size(); i++){
                dataToClose[i][0] = String.valueOf(llistaReparades.get(i).getId());
                dataToClose[i][1] = llistaReparades.get(i).getRoadName();
                dataToClose[i][2] = llistaReparades.get(i).getStartDate();
                dataToClose[i][3] = IncidencesManagement.getUrgency(llistaReparades.get(i));
                dataToClose[i][4] = llistaReparades.get(i).getDescription();
            }
            tableTancar.setModel(new javax.swing.table.DefaultTableModel(
            dataToClose,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableTancar.getColumnModel().getColumn(0).setMinWidth(75);
            tableTancar.getColumnModel().getColumn(0).setMaxWidth(75);
            tableTancar.getColumnModel().getColumn(1).setMinWidth(150);
            tableTancar.getColumnModel().getColumn(1).setMaxWidth(150);
            tableTancar.getColumnModel().getColumn(2).setMaxWidth(120);
            tableTancar.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaTancades.size();
            dataClosed = new String[dimension][5];
            for(int i = 0; i < llistaTancades.size(); i++){
                dataClosed[i][0] = String.valueOf(llistaTancades.get(i).getId());
                dataClosed[i][1] = llistaTancades.get(i).getRoadName();
                dataClosed[i][2] = llistaTancades.get(i).getStartDate();
                dataClosed[i][3] = IncidencesManagement.getUrgency(llistaTancades.get(i));
                dataClosed[i][4] = llistaTancades.get(i).getDescription();
            }
            tableTancades.setModel(new javax.swing.table.DefaultTableModel(
            dataClosed,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableTancades.getColumnModel().getColumn(0).setMinWidth(75);
            tableTancades.getColumnModel().getColumn(0).setMaxWidth(75);
            tableTancades.getColumnModel().getColumn(1).setMinWidth(150);
            tableTancades.getColumnModel().getColumn(1).setMaxWidth(150);
            tableTancades.getColumnModel().getColumn(2).setMaxWidth(120);
            tableTancades.getColumnModel().getColumn(3).setMaxWidth(100);
            
            String[][] dataUsers = UsersManagement.getUserData();
            dataFromUsers = dataUsers;
            llistaUsuaris = UsersManagement.getUserList();
            tableUsuaris.setModel(new javax.swing.table.DefaultTableModel(
            dataUsers,
            new String [] {
                "ID", "Rol", "Nom", "Cognoms", "Usuari", "Està Logat"
                }
            ));
        }
        
    }
    /**
     * Creates new form MainScreen
     */
    public MainScreen() {
        initComponents();
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        labelImatge = new javax.swing.JLabel();
        labelUsuari = new javax.swing.JLabel();
        labelNivell = new javax.swing.JLabel();
        botoRefresh = new javax.swing.JButton();
        labelUser = new javax.swing.JLabel();
        labelLevel = new javax.swing.JLabel();
        botoSortir = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableConfirmar = new javax.swing.JTable();
        buttonVisualizeIncident = new javax.swing.JButton();
        buttonModifyIncident = new javax.swing.JButton();
        buttonCreateIncident = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableObertes = new javax.swing.JTable();
        buttonVisualizeIncident2 = new javax.swing.JButton();
        buttonModifyIncident2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableReparacio = new javax.swing.JTable();
        buttonVisualizeIncident3 = new javax.swing.JButton();
        buttonModifyIncident3 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableTancar = new javax.swing.JTable();
        buttonVisualizeIncident4 = new javax.swing.JButton();
        buttonModifyIncident4 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableTancades = new javax.swing.JTable();
        buttonVisualizeIncident5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableUsuaris = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        botoAddUser = new javax.swing.JButton();
        botoModifyUser = new javax.swing.JButton();
        botoDeleteUser = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Informa Vies CAT");

        jPanel7.setBackground(new java.awt.Color(26, 63, 148));

        labelImatge.setIcon(new javax.swing.ImageIcon("src\\main\\java\\ioc\\informaviescat\\Pictures\\logo_screen.png"));

        labelUsuari.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelUsuari.setForeground(new java.awt.Color(255, 255, 255));
        labelUsuari.setText("Usuari: ");

        labelNivell.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelNivell.setForeground(new java.awt.Color(255, 255, 255));
        labelNivell.setText("Nivell: ");

        botoRefresh.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botoRefresh.setText("Refrescar dades");
        botoRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoRefreshActionPerformed(evt);
            }
        });

        labelUser.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelUser.setForeground(new java.awt.Color(255, 255, 255));
        labelUser.setText("jLabel4");

        labelLevel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        labelLevel.setForeground(new java.awt.Color(255, 255, 255));
        labelLevel.setText("jLabel5");

        botoSortir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botoSortir.setText("Sortir");
        botoSortir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoSortirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelImatge, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelUsuari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNivell, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLevel, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botoRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botoSortir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelImatge, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelUsuari)
                                .addComponent(labelUser))
                            .addComponent(botoRefresh))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelNivell)
                                .addComponent(labelLevel))
                            .addComponent(botoSortir))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        tableConfirmar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableConfirmar.setShowGrid(true);
        jScrollPane2.setViewportView(tableConfirmar);

        buttonVisualizeIncident.setText("Visualitzar Incidència");
        buttonVisualizeIncident.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVisualizeIncidentActionPerformed(evt);
            }
        });

        buttonModifyIncident.setText("Modificar Incidència");
        buttonModifyIncident.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifyIncidentActionPerformed(evt);
            }
        });

        buttonCreateIncident.setText("Crear Incidència");
        buttonCreateIncident.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCreateIncidentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonCreateIncident)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonModifyIncident)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonVisualizeIncident)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonVisualizeIncident)
                    .addComponent(buttonModifyIncident)
                    .addComponent(buttonCreateIncident))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidències en Validació", jPanel8);

        tableObertes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableObertes.setShowGrid(true);
        jScrollPane1.setViewportView(tableObertes);

        buttonVisualizeIncident2.setText("Visualitzar Incidència");
        buttonVisualizeIncident2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVisualizeIncident2ActionPerformed(evt);
            }
        });

        buttonModifyIncident2.setText("Modificar Incidència");
        buttonModifyIncident2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifyIncident2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonModifyIncident2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonVisualizeIncident2)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonVisualizeIncident2)
                    .addComponent(buttonModifyIncident2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidències Validades", jPanel9);

        tableReparacio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableReparacio.setShowGrid(true);
        jScrollPane3.setViewportView(tableReparacio);

        buttonVisualizeIncident3.setText("Visualitzar Incidència");
        buttonVisualizeIncident3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVisualizeIncident3ActionPerformed(evt);
            }
        });

        buttonModifyIncident3.setText("Modificar Incidència");
        buttonModifyIncident3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifyIncident3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonModifyIncident3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonVisualizeIncident3)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonVisualizeIncident3)
                    .addComponent(buttonModifyIncident3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidències Assignades", jPanel10);

        tableTancar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableTancar.setShowGrid(true);
        jScrollPane4.setViewportView(tableTancar);

        buttonVisualizeIncident4.setText("Visualitzar Incidència");
        buttonVisualizeIncident4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVisualizeIncident4ActionPerformed(evt);
            }
        });

        buttonModifyIncident4.setText("Modificar Incidència");
        buttonModifyIncident4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModifyIncident4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonModifyIncident4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonVisualizeIncident4)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonVisualizeIncident4)
                    .addComponent(buttonModifyIncident4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidències Resoltes", jPanel11);

        tableTancades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableTancades.setShowGrid(true);
        jScrollPane5.setViewportView(tableTancades);

        buttonVisualizeIncident5.setText("Visualitzar Incidència");
        buttonVisualizeIncident5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVisualizeIncident5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonVisualizeIncident5)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonVisualizeIncident5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Incidències Tancades", jPanel12);

        jPanel1.setLayout(new java.awt.BorderLayout());

        tableUsuaris.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableUsuaris.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableUsuaris.setShowGrid(true);
        jScrollPane6.setViewportView(tableUsuaris);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        botoAddUser.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botoAddUser.setText("Afegir usuari");
        botoAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoAddUserActionPerformed(evt);
            }
        });

        botoModifyUser.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botoModifyUser.setText("Modificar usuari");
        botoModifyUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoModifyUserActionPerformed(evt);
            }
        });

        botoDeleteUser.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        botoDeleteUser.setText("Eliminar usuari");
        botoDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoDeleteUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botoAddUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botoModifyUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botoDeleteUser)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botoDeleteUser)
                    .addComponent(botoModifyUser)
                    .addComponent(botoAddUser))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jTabbedPane2.addTab("Gestió d'usuaris", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName("Avaries en Validació");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * L'acció del botó refresh: Refresca les dades obtingudes des del servidor
     *
     * *@param evt event del botó apretat
     */
    private void botoRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoRefreshActionPerformed
        
        llistaIncidencies = IncidencesManagement.getIncidenceList();
        
        llistaPerValidar.clear(); llistaValidades.clear(); llistaAssignades.clear(); llistaReparades.clear(); llistaTancades.clear();
        
        for(Incident incident: llistaIncidencies){
            if(incident.getIncidentTypeId() == 1){
                llistaPerValidar.add(incident);
            }
            else if(incident.getIncidentTypeId() == 2){
                llistaValidades.add(incident);
            }
            else if(incident.getIncidentTypeId() == 3){
                llistaAssignades.add(incident);
            }
            else if(incident.getIncidentTypeId() == 4){
                llistaReparades.add(incident);
            }
            else if(incident.getIncidentTypeId() == 5){
                llistaTancades.add(incident);
            }
        }
        
        if(user.getRolId() == 1){
            String[][] dataUsers = null;
            try {
                dataUsers = UsersManagement.getUserData();
                dataFromUsers = dataUsers;
                llistaUsuaris = UsersManagement.getUserList();
            } catch (IOException ex) {
                Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableUsuaris.setModel(new javax.swing.table.DefaultTableModel(
                dataUsers,
                new String [] {
                    "ID", "Rol", "Nom", "Cognoms", "Usuari", "Està Logat"
                    }
            ));
            
            int dimension = llistaPerValidar.size();
            dataValidar = new String[dimension][5];
            for(int i = 0; i < llistaPerValidar.size(); i++){
                dataValidar[i][0] = String.valueOf(llistaPerValidar.get(i).getId());
                dataValidar[i][1] = llistaPerValidar.get(i).getRoadName();
                dataValidar[i][2] = llistaPerValidar.get(i).getStartDate();
                dataValidar[i][3] = IncidencesManagement.getUrgency(llistaPerValidar.get(i));
                dataValidar[i][4] = llistaPerValidar.get(i).getDescription();
            }
            tableConfirmar.setModel(new javax.swing.table.DefaultTableModel(
            dataValidar,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableConfirmar.getColumnModel().getColumn(0).setMinWidth(75);
            tableConfirmar.getColumnModel().getColumn(0).setMaxWidth(75);
            tableConfirmar.getColumnModel().getColumn(1).setMinWidth(150);
            tableConfirmar.getColumnModel().getColumn(1).setMaxWidth(150);
            tableConfirmar.getColumnModel().getColumn(2).setMaxWidth(120);
            tableConfirmar.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaValidades.size();
            dataValidated = new String[dimension][5];
            for(int i = 0; i < llistaValidades.size(); i++){
                dataValidated[i][0] = String.valueOf(llistaValidades.get(i).getId());
                dataValidated[i][1] = llistaValidades.get(i).getRoadName();
                dataValidated[i][2] = llistaValidades.get(i).getStartDate();
                dataValidated[i][3] = IncidencesManagement.getUrgency(llistaValidades.get(i));
                dataValidated[i][4] = llistaValidades.get(i).getDescription();
            }
            tableObertes.setModel(new javax.swing.table.DefaultTableModel(
            dataValidated,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableObertes.getColumnModel().getColumn(0).setMinWidth(75);
            tableObertes.getColumnModel().getColumn(0).setMaxWidth(75);
            tableObertes.getColumnModel().getColumn(1).setMinWidth(150);
            tableObertes.getColumnModel().getColumn(1).setMaxWidth(150);
            tableObertes.getColumnModel().getColumn(2).setMaxWidth(120);
            tableObertes.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaAssignades.size();
            dataAssigned = new String[dimension][5];
            for(int i = 0; i < llistaAssignades.size(); i++){
                dataAssigned[i][0] = String.valueOf(llistaAssignades.get(i).getId());
                dataAssigned[i][1] = llistaAssignades.get(i).getRoadName();
                dataAssigned[i][2] = llistaAssignades.get(i).getStartDate();
                dataAssigned[i][3] = IncidencesManagement.getUrgency(llistaAssignades.get(i));
                dataAssigned[i][4] = llistaAssignades.get(i).getDescription();
            }
            tableReparacio.setModel(new javax.swing.table.DefaultTableModel(
            dataAssigned,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableReparacio.getColumnModel().getColumn(0).setMinWidth(75);
            tableReparacio.getColumnModel().getColumn(0).setMaxWidth(75);
            tableReparacio.getColumnModel().getColumn(1).setMinWidth(150);
            tableReparacio.getColumnModel().getColumn(1).setMaxWidth(150);
            tableReparacio.getColumnModel().getColumn(2).setMaxWidth(120);
            tableReparacio.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaReparades.size();
            dataToClose = new String[dimension][5];
            for(int i = 0; i < llistaReparades.size(); i++){
                dataToClose[i][0] = String.valueOf(llistaReparades.get(i).getId());
                dataToClose[i][1] = llistaReparades.get(i).getRoadName();
                dataToClose[i][2] = llistaReparades.get(i).getStartDate();
                dataToClose[i][3] = IncidencesManagement.getUrgency(llistaReparades.get(i));
                dataToClose[i][4] = llistaReparades.get(i).getDescription();
            }
            tableTancar.setModel(new javax.swing.table.DefaultTableModel(
            dataToClose,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableTancar.getColumnModel().getColumn(0).setMinWidth(75);
            tableTancar.getColumnModel().getColumn(0).setMaxWidth(75);
            tableTancar.getColumnModel().getColumn(1).setMinWidth(150);
            tableTancar.getColumnModel().getColumn(1).setMaxWidth(150);
            tableTancar.getColumnModel().getColumn(2).setMaxWidth(120);
            tableTancar.getColumnModel().getColumn(3).setMaxWidth(100);
            
            dimension = llistaTancades.size();
            dataClosed = new String[dimension][5];
            for(int i = 0; i < llistaTancades.size(); i++){
                dataClosed[i][0] = String.valueOf(llistaTancades.get(i).getId());
                dataClosed[i][1] = llistaTancades.get(i).getRoadName();
                dataClosed[i][2] = llistaTancades.get(i).getStartDate();
                dataClosed[i][3] = IncidencesManagement.getUrgency(llistaTancades.get(i));
                dataClosed[i][4] = llistaTancades.get(i).getDescription();
            }
            tableTancades.setModel(new javax.swing.table.DefaultTableModel(
            dataClosed,
            new String [] {
                "ID", "Carretera", "Data Inici", "Prioritat", "Descripció"
                }
            ));
            tableTancades.getColumnModel().getColumn(0).setMinWidth(75);
            tableTancades.getColumnModel().getColumn(0).setMaxWidth(75);
            tableTancades.getColumnModel().getColumn(1).setMinWidth(150);
            tableTancades.getColumnModel().getColumn(1).setMaxWidth(150);
            tableTancades.getColumnModel().getColumn(2).setMaxWidth(120);
            tableTancades.getColumnModel().getColumn(3).setMaxWidth(100);
            
        }
        else if(user.getRolId() == 2){
            
        }
        String[][] dataUsers = null;
        try {
            dataUsers = UsersManagement.getUserData();
            dataFromUsers = dataUsers;
            llistaUsuaris = UsersManagement.getUserList();
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableUsuaris.setModel(new javax.swing.table.DefaultTableModel(
            dataUsers,
            new String [] {
                "ID", "Rol", "Nom", "Cognoms", "Usuari", "Està Logat"
                }
        ));
    }//GEN-LAST:event_botoRefreshActionPerformed
    
    /**
     * L'acció del botó Sortir: Fa logout de l'usuari, tanca la pantalla i torna al login 
     *
     * *@param evt event del botó apretat
     */
    private void botoSortirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoSortirActionPerformed
        UsersManagement.logout(user.getSessionId(), user.getId());
        this.dispose();
        //Es crea l'objecta finestra de login
        guiLogin guilogin = new guiLogin();
        //Es fica en posició centrada a la pantalla
        guilogin.setLocationRelativeTo(null);
        //Se li assigna la icona
        guilogin.setIconImage(img.getImage());
        //Es fa visible
        guilogin.setVisible(true);
    }//GEN-LAST:event_botoSortirActionPerformed
    
    /**
     * L'acció del botó Crear usuari: Obre la finestra de crear nou usuari
     *
     * *@param evt event del botó apretat
     */
    private void botoAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoAddUserActionPerformed
        CreateNewUser createUser = new CreateNewUser();
        createUser.setLocationRelativeTo(null);
        createUser.setIconImage(img.getImage());
        createUser.setVisible(true);
    }//GEN-LAST:event_botoAddUserActionPerformed

    /**
     * L'acció del botó Modificar usuari: Obre la finestra de modificar un usuari
     *
     * *@param evt event del botó apretat
     */
    private void botoModifyUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoModifyUserActionPerformed
        //Es busca quin usuari s'ha de modificar
        int seleccio = tableUsuaris.getSelectedRow();
        System.out.println(seleccio);
        User usuariAModificar = null;
        try{
            String idUser = dataFromUsers[seleccio][0];
            int intIdUser = Integer.parseInt(idUser);
            System.out.println("ID usuari: "+idUser);
            for(User user: llistaUsuaris){
                if(user.getId() == intIdUser){
                    usuariAModificar = user;
                }
            }
        } catch(Exception e){
            showMessageDialog(null, "Has de sel·leccionar un usuari!");
        }
        
        //Es crida una finestra de modificació
        ModifyUser modificacioUsuari = new ModifyUser();
        modificacioUsuari.setUserToModify(usuariAModificar);
        modificacioUsuari.setLocationRelativeTo(null);
        modificacioUsuari.setIconImage(img.getImage());
        modificacioUsuari.setVisible(true);
        
    }//GEN-LAST:event_botoModifyUserActionPerformed

    /**
     * L'acció del botó Eliminar usuari: Obre la finestra de Eliminar un usuari
     *
     * *@param evt event del botó apretat
     */
    private void botoDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoDeleteUserActionPerformed
        //Es busca quin usuari s'ha d'eliminar
        int seleccio = tableUsuaris.getSelectedRow();
        System.out.println(seleccio);
        User usuariAEliminar = null;
        try{
            String idUser = dataFromUsers[seleccio][0];
            int intIdUser = Integer.parseInt(idUser);
            System.out.println("ID usuari: "+idUser);
            for(User user: llistaUsuaris){
                if(user.getId() == intIdUser){
                    usuariAEliminar = user;
                }
            }
        } catch(Exception e){
            showMessageDialog(null, "Has de sel·leccionar un usuari!");
        }
        
        DeleteUser eliminacioUsuari = new DeleteUser();
        eliminacioUsuari.setUserToDelete(usuariAEliminar);
        eliminacioUsuari.setLocationRelativeTo(null);
        eliminacioUsuari.setIconImage(img.getImage());
        eliminacioUsuari.setVisible(true);
    }//GEN-LAST:event_botoDeleteUserActionPerformed

    /**
     * L'acció del botó Crear incidència, obre una finestra per a crear-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonCreateIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateIncidentActionPerformed
        CreateIncident createIncident = new CreateIncident();
        createIncident.setUserId(this.user.getId());
        createIncident.setLocationRelativeTo(null);
        createIncident.setIconImage(img.getImage());
        createIncident.setVisible(true);
    }//GEN-LAST:event_buttonCreateIncidentActionPerformed

    /**
     * L'acció del botó Visualitzar incidència, obre una finestra per a visualitzar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonVisualizeIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVisualizeIncidentActionPerformed
        accioBotoVisualitzar(tableConfirmar, llistaPerValidar, dataValidar);
    }//GEN-LAST:event_buttonVisualizeIncidentActionPerformed

    /**
     * L'acció del botó Visualitzar incidència 2, obre una finestra per a visualitzar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonVisualizeIncident2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVisualizeIncident2ActionPerformed
        accioBotoVisualitzar(tableObertes, llistaValidades, dataValidated);
    }//GEN-LAST:event_buttonVisualizeIncident2ActionPerformed

    /**
     * L'acció del botó Visualitzar incidència 3, obre una finestra per a visualitzar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonVisualizeIncident3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVisualizeIncident3ActionPerformed
        accioBotoVisualitzar(tableReparacio, llistaAssignades, dataAssigned);
    }//GEN-LAST:event_buttonVisualizeIncident3ActionPerformed

    /**
     * L'acció del botó Visualitzar incidència 4, obre una finestra per a visualitzar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonVisualizeIncident4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVisualizeIncident4ActionPerformed
        accioBotoVisualitzar(tableTancar, llistaReparades, dataToClose);
    }//GEN-LAST:event_buttonVisualizeIncident4ActionPerformed

    /**
     * L'acció del botó Visualitzar incidència 5, obre una finestra per a visualitzar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonVisualizeIncident5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVisualizeIncident5ActionPerformed
        accioBotoVisualitzar(tableTancades, llistaTancades, dataClosed);
    }//GEN-LAST:event_buttonVisualizeIncident5ActionPerformed

    /**
     * L'acció del botó Modificar incidència, obre una finestra per a modificar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonModifyIncidentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifyIncidentActionPerformed
        accioBotoModificar(tableConfirmar, llistaPerValidar, dataValidar);
    }//GEN-LAST:event_buttonModifyIncidentActionPerformed

    /**
     * L'acció del botó Modificar incidència 2, obre una finestra per a modificar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonModifyIncident2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifyIncident2ActionPerformed
        accioBotoModificar(tableObertes, llistaValidades, dataValidated);
    }//GEN-LAST:event_buttonModifyIncident2ActionPerformed

    /**
     * L'acció del botó Modificar incidència 3, obre una finestra per a modificar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonModifyIncident3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifyIncident3ActionPerformed
        accioBotoModificar(tableReparacio, llistaAssignades, dataAssigned);
    }//GEN-LAST:event_buttonModifyIncident3ActionPerformed

    /**
     * L'acció del botó Modificar incidència 4, obre una finestra per a modificar-la
     *
     * *@param evt event del botó apretat
     */
    private void buttonModifyIncident4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModifyIncident4ActionPerformed
        accioBotoModificar(tableTancar, llistaReparades, dataToClose);
    }//GEN-LAST:event_buttonModifyIncident4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }
    
    /**
     * La funció que executaran tots els botons de visualitzar incidència
     *
     * @param table Taula d'on extreure la incidència seleccionada
     * @param list Llista d'on treure l'objecte incident
     * @param stringList String[][] d'on extreure la incidència
     */
    public void accioBotoVisualitzar(JTable table, List<Incident> list, String[][] stringList){
        int seleccio = table.getSelectedRow();
        System.out.println(seleccio);
        Incident incidentToVisualize = null;
        try{
            String idIncidence = stringList[seleccio][0];
            int intIdIncidence = Integer.parseInt(idIncidence);
            System.out.println("ID incidència: "+idIncidence);
            for(Incident incident: list){
                if(incident.getId() == intIdIncidence){
                    incidentToVisualize = incident;
                }
            }
        } catch(Exception e){
            showMessageDialog(null, "Has de sel·leccionar una incidència!");
        }
        
        VisualizeIncident visualizeIncident = new VisualizeIncident();
        visualizeIncident.setLocationRelativeTo(null);
        visualizeIncident.setIconImage(img.getImage());
        visualizeIncident.setIncident(incidentToVisualize);
        visualizeIncident.setVisible(true);
    }
    
    /**
     * La funció que executaran tots els botons de modificar incidència
     *
     * @param table Taula d'on extreure la incidència seleccionada
     * @param list Llista d'on treure l'objecte incident
     * @param stringList String[][] d'on extreure la incidència
     */
    public void accioBotoModificar(JTable table, List<Incident> list, String[][] stringList){
        int seleccio = table.getSelectedRow();
        System.out.println(seleccio);
        Incident incidentToModify = null;
        try{
            String idIncidence = stringList[seleccio][0];
            int intIdIncidence = Integer.parseInt(idIncidence);
            System.out.println("ID incidència: "+idIncidence);
            for(Incident incident: list){
                if(incident.getId() == intIdIncidence){
                    incidentToModify = incident;
                }
            }
        } catch(Exception e){
            showMessageDialog(null, "Has de sel·leccionar una incidència!");
        }
        
        ModifyIncident modifyIncident = new ModifyIncident();
        modifyIncident.setLocationRelativeTo(null);
        modifyIncident.setIconImage(img.getImage());
        modifyIncident.setIncidentToModify(incidentToModify);
        modifyIncident.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botoAddUser;
    private javax.swing.JButton botoDeleteUser;
    private javax.swing.JButton botoModifyUser;
    private javax.swing.JButton botoRefresh;
    private javax.swing.JButton botoSortir;
    private javax.swing.JButton buttonCreateIncident;
    private javax.swing.JButton buttonModifyIncident;
    private javax.swing.JButton buttonModifyIncident2;
    private javax.swing.JButton buttonModifyIncident3;
    private javax.swing.JButton buttonModifyIncident4;
    private javax.swing.JButton buttonVisualizeIncident;
    private javax.swing.JButton buttonVisualizeIncident2;
    private javax.swing.JButton buttonVisualizeIncident3;
    private javax.swing.JButton buttonVisualizeIncident4;
    private javax.swing.JButton buttonVisualizeIncident5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel labelImatge;
    private javax.swing.JLabel labelLevel;
    private javax.swing.JLabel labelNivell;
    private javax.swing.JLabel labelUser;
    private javax.swing.JLabel labelUsuari;
    private javax.swing.JTable tableConfirmar;
    private javax.swing.JTable tableObertes;
    private javax.swing.JTable tableReparacio;
    private javax.swing.JTable tableTancades;
    private javax.swing.JTable tableTancar;
    private javax.swing.JTable tableUsuaris;
    // End of variables declaration//GEN-END:variables
}
