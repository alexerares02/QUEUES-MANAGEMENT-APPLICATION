package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class SimulationFrame extends JFrame   {
    private JButton confirm;
    private JTextField NrQ;
    private JTextField NrT;
    private JTextField minA;
    private JTextField maxA;
    private JTextField minS;
    private JTextField maxS;
    private JLabel Q;
    private JLabel T;
    private JLabel miA;
    private JLabel maA;
    private JLabel miS;
    private JLabel maS;
    private JLabel timeSim;
    private JTextField time;

    private  int timeLimit;
    private Integer queues;
    private  Integer tasks;
    private  Integer minArrival;
    private  Integer maxArrival;
    private  Integer minService;
    private  Integer maxService;



    public SimulationFrame(){

        this.setTitle("Generator de clienti");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBounds(300,300,500,500);
        this.setLayout(null);


        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0,0,500,500);

        confirm=new JButton("Confirm");
        confirm.setBounds(250, 300, 200, 100);
        panel.add(confirm);
        this.add(panel);

        confirm.addActionListener(e -> {
            timeLimit=Integer.parseInt(time.getText());
            queues=Integer.parseInt(NrQ.getText());
            tasks=Integer.parseInt(NrT.getText());
            minArrival=Integer.parseInt(minA.getText());
            maxArrival=Integer.parseInt(maxA.getText());
            minService=Integer.parseInt(minS.getText());
            maxService=Integer.parseInt(maxS.getText());

            SimulationManager gen = new SimulationManager(timeLimit,queues,tasks,minArrival,maxService,minService,maxArrival);
            Thread t = new Thread(gen);
            t.start();
        });

        Q=new JLabel("Queues");
        T=new JLabel("Tasks");
        timeSim=new JLabel("Time Simulation");
        timeSim.setBounds(0,0,100,50);
        Q.setBounds(0,50,50,50);
        T.setBounds(0,100,50,50);
        panel.add(Q);
        panel.add(T);
        panel.add(timeSim);
        NrQ=new JTextField();
        NrT=new JTextField();
        time=new JTextField();
        time.setBounds(100,0,50,50);
        NrQ.setBounds(50,50,50,50);
        NrT.setBounds(50,100,50,50);
        panel.add(NrQ);
        panel.add(NrT);
        panel.add(time);

        miA=new JLabel("minA");
        maA=new JLabel("maxA");
        miA.setBounds(0,150,50,50);
        maA.setBounds(150,150,50,50);
        panel.add(miA);
        panel.add(maA);
        minA=new JTextField();
        maxA=new JTextField();
        minA.setBounds(50,150,50,50);
        maxA.setBounds(200,150,50,50);
        panel.add(minA);
        panel.add(maxA);

        miS=new JLabel("minS");
        maS=new JLabel("maxS");
        miS.setBounds(0,200,50,50);
        maS.setBounds(150,200,50,50);
        panel.add(miS);
        panel.add(maS);
        minS=new JTextField();
        maxS=new JTextField();
        minS.setBounds(50,200,50,50);
        maxS.setBounds(200,200,50,50);
        panel.add(minS);
        panel.add(maxS);

        this.setVisible(true);

    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if(e.getSource()==confirm){
//            timeLimit=Integer.parseInt(time.getText());
//            queues=Integer.parseInt(NrQ.getText());
//            tasks=Integer.parseInt(NrT.getText());
//            minArrival=Integer.parseInt(minA.getText());
//            maxArrival=Integer.parseInt(maxA.getText());
//            minService=Integer.parseInt(minS.getText());
//            maxService=Integer.parseInt(maxS.getText());
//
//            SimulationManager gen = new SimulationManager(timeLimit,queues,tasks,minArrival,maxService,minService,maxArrival);
//            Thread t = new Thread(gen);
//            t.start();
//        }
//    }
}
