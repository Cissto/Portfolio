package demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import javazoom.jl.player.Player;

public class MusicPlayerV1 extends JFrame implements ActionListener {

	private JButton choose, play, stop ,resume , pause;
	private JTextField input;
	private JLabel h1, nowPlay,songname;
	private File selectedFile;
	private Player player;
	long totalLength, pauseLength;
	private FileInputStream fis;
	private BufferedInputStream buffis;
	private Thread playThread, resumeThread;
	private JFileChooser fileChooser;
	String filename;
    ImageIcon iconPlay, iconPause, iconResume, iconStop,iconFrame;
	
	public MusicPlayerV1() {
		super("Music Player");
		
		iconPlay = new ImageIcon(getClass().getResource("/play.png"));
        iconPause = new ImageIcon(getClass().getResource("/pause.png"));
        iconResume = new ImageIcon(getClass().getResource("/resume.png"));
        iconStop = new ImageIcon(getClass().getResource("/stop.png"));
        iconFrame = new ImageIcon(getClass().getResource("/frame.png"));
        

		input = new JTextField(25);
		h1 = new JLabel("IMPORT ");
		nowPlay = new JLabel(" Select a file!",SwingConstants.CENTER);
		songname = new JLabel("",SwingConstants.CENTER);
		choose = new JButton("Choose");
		play = new JButton(iconPlay);
		stop = new JButton(iconStop);
		resume = new JButton(iconResume);
		pause = new JButton(iconPause);
		playThread = new Thread(runmusic);
		resumeThread = new Thread(resumemusic);
		
		
		//按鈕顏色
        play.setBackground(Color.WHITE);
        pause.setBackground(Color.WHITE);
        resume.setBackground(Color.WHITE);
        stop.setBackground(Color.WHITE);


		setLayout(new BorderLayout());
		// 選擇檔案
		JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 7));
		add(top, BorderLayout.NORTH);
		top.add(h1);
		top.add(input);
		top.add(choose);
		

		// 控制板
		
		JPanel con = new JPanel(new FlowLayout(FlowLayout.CENTER,10 ,10));
		add(con, BorderLayout.SOUTH);
		con.add(stop); con.add(play); 
		con.add(pause); con.add(resume); 
		
		JPanel show = new JPanel(new BorderLayout());
		add(show, BorderLayout.CENTER);
		show.add(nowPlay,BorderLayout.NORTH);
		show.add(songname,BorderLayout.CENTER);
		
		

		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				choosefile();
			}
		});
		
		
		// 視窗設定
		setBackground(Color.gray);
		setSize(500, 200);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(iconFrame.getImage());
		
		addActionEvents();

	}
	

	public static void main(String[] args) {
		new MusicPlayerV1();

	}
	
	
	

public void addActionEvents() {	
	
	play.addActionListener(this);
	stop.addActionListener(this);
	pause.addActionListener(this);
    resume.addActionListener(this);
    
}
	
	
public void actionPerformed(ActionEvent e) {
	
		if(e.getSource().equals(play)){
			if (filename != null) {
                playThread.start();
                songname.setText(filename);
                play.setEnabled(false);
            } else {
                songname.setText("No File was selected!");
            }
		}
		if (e.getSource().equals(pause)) {
            //code for pause button
            if (player != null && filename != null) {
                try {
                    pauseLength = fis.available();
                    player.close();
                    nowPlay.setText("Music Stop");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (e.getSource().equals(resume)) {
            //starting resume thread
            if (filename != null) {
            	
					resumeThread.start();
					nowPlay.setText("NOW PLAYING：");
					//resume.setEnabled(false);
				
                
            } else {
                songname.setText("No File was selected!");
            }
        }
		
		
		if (e.getSource().equals(stop)){
            //code for pause button
            if (player != null && filename != null) {
                try {
                    player.close();
                    input.setText("");
                    nowPlay.setText(" Select a file!");
                    songname.setText("");
                    playThread = new Thread(runmusic);
                    play.setEnabled(true);
                } catch (Exception e2) {
                	e2.printStackTrace();
                }
            }
        }
	}


	
	

public void choosefile() {
	fileChooser = new JFileChooser();// 宣告filechooser
	fileChooser.setDialogTitle("Select a file");
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files", "mp3"));
	int returnValue = fileChooser.showOpenDialog(null);// 叫出filechooser
	if (returnValue == JFileChooser.APPROVE_OPTION) // 判斷是否選擇檔案
	{
		selectedFile = fileChooser.getSelectedFile();// 指派給File
		input.setText(selectedFile.getAbsolutePath());
		filename = fileChooser.getSelectedFile().getName();
		nowPlay.setText("NOW PLAYING：");
		songname.setText(filename);
	}

}

Runnable runmusic = new Runnable() {
	
	public void run() {
		try {
			fis = new FileInputStream(selectedFile);
			buffis = new BufferedInputStream(fis);
			player = new Player(buffis);
			totalLength = fis.available();
            player.play();//starting music
			
		}catch (Exception e){
			System.out.println(e);
		}
	}
};

Runnable resumemusic = new Runnable() {
    @Override
    public void run() {
        try {
            //code for resume button
           fis = new FileInputStream(selectedFile);
           buffis = new BufferedInputStream(fis);
           player = new Player(buffis);
           fis.skip(totalLength - pauseLength);
           player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
};

}


