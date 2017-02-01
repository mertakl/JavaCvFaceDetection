import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class CameraPanel extends JPanel implements Runnable, ActionListener{

	BufferedImage image;
	VideoCapture capture;
	JButton screenShot;
	CascadeClassifier faceDetector;
	MatOfRect faceDetections;
	public CameraPanel(){
		faceDetector = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
		faceDetections = new MatOfRect();
		screenShot = new JButton("Screenshot");
		screenShot.addActionListener(this);
		add(screenShot);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		File output = new File("screenshot1.png");
		int i = 0;
		while(output.exists()){
			i++;
			output = new File("screenshot"+i+".png");
		}
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		capture = new VideoCapture(0);
		Mat webcam_image = new Mat();
		
		if(capture.isOpened()){
			while(true){
				capture.read(webcam_image);
				if(!webcam_image.empty()){
					JFrame topFrame =(JFrame) SwingUtilities.getWindowAncestor(this);
					topFrame.setSize(webcam_image.width()+40, webcam_image.height()+110);
					matToBufferedImage(webcam_image);
					faceDetector.detectMultiScale(webcam_image, faceDetections);
					repaint();
				}
			}
		}
	}


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(image == null) return;
		g.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);
		
		g.setColor(Color.green);
		
		for(Rect rect : faceDetections.toArray()){
			g.drawRect(rect.x + 10, rect.y + 40, rect.width, rect.height);
		}
	}
	
	private void matToBufferedImage(Mat matBGR) {
		// TODO Auto-generated method stub
		int width = matBGR.width();
		int height = matBGR.height();
		int channels = matBGR.channels();
		byte[] source =  new byte[width * height * channels];
		
		matBGR.get(0, 0, source);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		final byte[] target = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		
		System.arraycopy(source, 0, target, 0, source.length);
		
	}

	public void switchCamera(int x) {
		// TODO Auto-generated method stub
		capture = new VideoCapture(x);
	}
	

}
