import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class CameraFrame extends JFrame implements ActionListener{
	
	CameraPanel cp;
	
	public CameraFrame(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture list = new VideoCapture();
		cp = new CameraPanel();
		Thread thread = new Thread(cp);
		
		JMenu camera = new JMenu("Camera");
		
		JMenuBar bar = new JMenuBar();
		bar.add(camera);
		int i =1;
		while(list.isOpened()){
			JMenuItem cam = new JMenuItem("Camera" +i);
			cam.addActionListener(this);
			camera.add(cam);
			list.release();
			list = new VideoCapture();
			i++;
		}
		thread.start();
		add(cp);
		setJMenuBar(bar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
	}
	public static void main(String[] args){
		CameraFrame cameraFrame = new CameraFrame();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JMenuItem source = (JMenuItem) e.getSource();
		int num = Integer.parseInt(source.getText().substring(7))-1;
		cp.switchCamera(num);
	}
}
