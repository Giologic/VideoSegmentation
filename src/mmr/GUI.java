package mmr;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.SwingConstants;
import java.awt.Font;

public class GUI extends JFrame{
	private JTextField textFieldTCo;
	private JTextField textFieldAlpha;
	private JLabel lblStatus;
	private Video v;
	private JScrollPane scrollPane1, scrollPane2, scrollPane3, scrollPane4;
	
	public GUI() {
		setTitle("Video Segmentation");
		
		getContentPane().setLayout(null);
		this.setVisible(true);
		this.setSize(690, 590);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panelInput = new JPanel();
		panelInput.setBounds(10, 11, 655, 63);
		panelInput.setLayout(null);
		getContentPane().add(panelInput);
		
		JLabel lblEnterAlpha = new JLabel("Enter alpha:");
		lblEnterAlpha.setBounds(10, 11, 79, 14);
		panelInput.add(lblEnterAlpha);
		
		JLabel lblEnterTcoefficient = new JLabel("Enter ts-coefficient:");
		lblEnterTcoefficient.setBounds(10, 38, 151, 14);
		panelInput.add(lblEnterTcoefficient);
		
		textFieldTCo = new JTextField();
		textFieldTCo.setBounds(134, 35, 86, 20);
		panelInput.add(textFieldTCo);
		textFieldTCo.setColumns(10);
		
		textFieldAlpha = new JTextField();
		textFieldAlpha.setColumns(10);
		textFieldAlpha.setBounds(134, 8, 86, 20);
		panelInput.add(textFieldAlpha);
		
		JLabel lblChooseVideo = new JLabel("Choose video:");
		lblChooseVideo.setBounds(230, 11, 104, 14);
		panelInput.add(lblChooseVideo);
		
		JComboBox cbVid = new JComboBox();
		cbVid.setModel(new DefaultComboBoxModel(new String[] {"Uni", "Mjack", "777"}));
		cbVid.setSelectedIndex(0);
		cbVid.setBounds(326, 8, 181, 20);
		panelInput.add(cbVid);
		
		JButton btnGo = new JButton("Go!");
		btnGo.setBackground(Color.GREEN);
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int flag = 0;
                int index = 0;
                if (e.getSource() == btnGo) {
                    try {
                        flag = 0;
                        Double alpha = Double.parseDouble(textFieldAlpha.getText());
                        Double t = Double.parseDouble(textFieldTCo.getText());
                        index = cbVid.getSelectedIndex() + 1;
                        v = new Video(VideoData.getHist(index), alpha, t);
                        v.twinComparison();
                        if (alpha > 1 || alpha < 0 || t > 1 || t < 0) {
                            flag = 1;
                            lblStatus.setText("Alpha/ts-coefficient value should be from 0-1 only.");
                        }
                    } catch (Exception a) {
                        flag = 1;
                        //a.printStackTrace();
                        lblStatus.setText("Alpha/ts-coefficient value should be from 0-1 only.");
                        repaint();
                    }
                    if (flag == 0) {
                    	lblStatus.setText("");
                    	JPanel outerPanel = new JPanel();
                    	outerPanel.setLayout(new FlowLayout());
                        outerPanel.setLocation(1, 1);
                        outerPanel.setSize(800, 1500);
                         //outerPanel.setLayout(new GridLayout(v.shotBoundaries.size(), 1));
                         for(int i=0; i<v.shotBoundaries.size(); i++){
                             int shot = v.shotBoundaries.get(i);
                             int shot2 = v.nextFrames.get(i);
                             JPanel p = new JPanel();
                             String buff = shot<10 ? "000" : shot<100 ? "00" : shot<1000 ? "0" : "";
                             String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot+".jpg";
                             buff = shot2<10 ? "000" : shot2<100 ? "00" : shot2<1000 ? "0" : "";
                             String q2 = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot2+".jpg";
                             JLabel l1 = new JLabel(new ImageIcon(q));
                             l1.setText(q);
                             l1.setHorizontalTextPosition(JLabel.CENTER);
                             l1.setVerticalTextPosition(JLabel.BOTTOM);
                             p.add(l1);
                             JLabel l2 = new JLabel(new ImageIcon(q2));
                             l2.setText(q2);
                             l2.setHorizontalTextPosition(JLabel.CENTER);
                             l2.setVerticalTextPosition(JLabel.BOTTOM);
                             p.add(l2);
                             outerPanel.add(p);
                         }
                         
                         outerPanel.setVisible(true);
                         scrollPane1.setViewportView(outerPanel);
                         scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                         scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                         
                         outerPanel = new JPanel();
                         outerPanel.setLayout(new FlowLayout());
                         for(int i=0; i<v.abruptKeyframes.size(); i++){
                             int keyframe = v.abruptKeyframes.get(i);
                             JPanel p = new JPanel();
                             String buff = keyframe<10 ? "000" : keyframe<100 ? "00" : keyframe<1000 ? "0" : "";
                             String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+keyframe+".jpg";
                             JLabel l1 = new JLabel(new ImageIcon(q));
                             l1.setText(q);
                             l1.setHorizontalTextPosition(JLabel.CENTER);
                             l1.setVerticalTextPosition(JLabel.BOTTOM);
                             p.add(l1);
                             outerPanel.add(p);
                         }
                         
                         scrollPane2.setViewportView(outerPanel);
                         scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                         scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                         
                         outerPanel = new JPanel();
                         outerPanel.setLayout(new FlowLayout());
                         for(int i=0; i<v.gtStart.size(); i++){
                             int shot = v.gtStart.get(i);
                             int shot2 = v.gtEnd.get(i);
                             JPanel p = new JPanel();
                             String buff = shot<10 ? "000" : shot<100 ? "00" : shot<1000 ? "0" : "";
                             String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot+".jpg";
                             buff = shot2<10 ? "000" : shot2<100 ? "00" : shot2<1000 ? "0" : "";
                             String q2 = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot2+".jpg";
                             JLabel l1 = new JLabel(new ImageIcon(q));
                             l1.setText(q);
                             l1.setHorizontalTextPosition(JLabel.CENTER);
                             l1.setVerticalTextPosition(JLabel.BOTTOM);
                             p.add(l1);
                             JLabel l2 = new JLabel(new ImageIcon(q2));
                             l2.setText(q2);
                             l2.setHorizontalTextPosition(JLabel.CENTER);
                             l2.setVerticalTextPosition(JLabel.BOTTOM);
                             p.add(l2);
                             outerPanel.add(p);
                         }
                         
                         scrollPane3.setViewportView(outerPanel);
                         scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                         scrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                         
                         outerPanel = new JPanel();
                         outerPanel.setLayout(new FlowLayout());
                         for(int i=0; i<v.gradualKeyframes.size(); i++){
                             int keyframe = v.gradualKeyframes.get(i);
                             JPanel p = new JPanel();
                             String buff = keyframe<10 ? "000" : keyframe<100 ? "00" : keyframe<1000 ? "0" : "";
                             String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+keyframe+".jpg";
                             JLabel l1 = new JLabel(new ImageIcon(q));
                             l1.setText(q);
                             l1.setHorizontalTextPosition(JLabel.CENTER);
                             l1.setVerticalTextPosition(JLabel.BOTTOM);
                             p.add(l1);
                             outerPanel.add(p);
                         }
                         
                         scrollPane4.setViewportView(outerPanel);
                         scrollPane4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                         scrollPane4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    
                         repaint();
         				 revalidate();
                    }
                    repaint();
    				revalidate();
                }
                repaint();
				revalidate();
            }
		});
		btnGo.setBounds(517, 7, 128, 48);
		panelInput.add(btnGo);
		
		lblStatus = new JLabel("");
		lblStatus.setForeground(Color.RED);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(230, 38, 277, 14);
		panelInput.add(lblStatus);
		
		scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(10, 103, 324, 205);
		getContentPane().add(scrollPane1);
		
		scrollPane3 = new JScrollPane();
		scrollPane3.setBounds(10, 335, 324, 205);
		getContentPane().add(scrollPane3);
		
		scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(341, 103, 324, 205);
		getContentPane().add(scrollPane2);
		
		scrollPane4 = new JScrollPane();
		scrollPane4.setBounds(341, 335, 324, 205);
		getContentPane().add(scrollPane4);
		
		JLabel lblNewLabel = new JLabel("Abrupt Transitions");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(102, 85, 131, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblAbruptTransitionsW = new JLabel("Abrupt Transitions w/ Keyframes");
		lblAbruptTransitionsW.setHorizontalAlignment(SwingConstants.CENTER);
		lblAbruptTransitionsW.setBounds(388, 85, 241, 14);
		getContentPane().add(lblAbruptTransitionsW);
		
		JLabel lblGradualTransitionsW = new JLabel("Gradual Transitions w/ Keyframes");
		lblGradualTransitionsW.setHorizontalAlignment(SwingConstants.CENTER);
		lblGradualTransitionsW.setBounds(388, 319, 241, 14);
		getContentPane().add(lblGradualTransitionsW);
		
		JLabel lblGradualTransitions = new JLabel("Gradual Transitions");
		lblGradualTransitions.setHorizontalAlignment(SwingConstants.CENTER);
		lblGradualTransitions.setBounds(102, 319, 131, 14);
		getContentPane().add(lblGradualTransitions);
		
	}
	
	public static void main(String[] args){
		VideoData.init();
		new GUI().repaint();
	}
}
