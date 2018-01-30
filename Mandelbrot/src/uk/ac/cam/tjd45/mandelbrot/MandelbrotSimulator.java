package uk.ac.cam.tjd45.mandelbrot;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class MandelbrotSimulator {

	private int maxit = 1000;
	private double immin = -1.2;
	private double immax = 1.2;
	private double remin = -2;
	private double remax = 0.6;
	private int magfac = 1;
	private int threshold = 4;
	
	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MandelbrotSimulator window = new MandelbrotSimulator();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private int Mandelbrot(int x, int y,int width, int height){
		double im;
		double re;
		
		re = remin + (((double)x/width)*(remax-remin));
		im = immin + (((double)y/height)*(immax-immin));
		
		double zr = 0.0;
		double zi = 0.0;
		
		int iterations = 0;
		double dist = 0;
		double newre = 0;
		double newim = 0;
		while(dist<4 && iterations<maxit){
			dist = (zr*zr)+(zi*zi);
			newre=(zr*zr)-(zi*zi)+re;
			newim=(2*zr*zi)+im;
			zr=newre;
			zi=newim;
			iterations++;
		}
		
		
		if(dist<4){
			return 0;
		}
			
		return iterations;
	}
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1200, 850);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Canvas canvas = new Canvas(composite, SWT.NONE);
		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Rectangle clientArea = canvas.getClientArea();
				double X = clientArea.width/2.0;
				double Y = clientArea.height/2.0;
				
				double reSF = (remax-remin)/clientArea.width;
				double imSF = (immax-immin)/clientArea.height;
				remin = remin - (reSF*X);
				remax = remax + (reSF*(clientArea.width-X));
				immin = immin - (imSF*Y);
				immax = immax + (imSF*(clientArea.height-Y));
				magfac = magfac/2;
				
				if(magfac<threshold){
					maxit /= 5;
					threshold /=2;
				}
				
				canvas.redraw();
			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Rectangle clientArea = canvas.getClientArea();
				double reSF = (remax-remin)/clientArea.width;
				double imSF = (immax-immin)/clientArea.height;
				remin = remin + 0.5*(reSF*e.x);
				remax = remax - 0.5*(reSF*(clientArea.width-e.x));
				immin = immin + 0.5*(imSF*e.y);
				immax = immax - 0.5*(imSF*(clientArea.height-e.y));
				magfac = magfac*2;
				
				if(magfac>threshold){
					maxit *= 5;
					threshold *=2;
				}
				
				canvas.redraw();
				
			}
			
		});
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = canvas.getClientArea();
				
				Rectangle rect = new Rectangle(10, 10, clientArea.width-20, clientArea.height-20);
				
				canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
				e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
				e.gc.fillRectangle(rect);
				
				for(int x = 10; x<clientArea.width-10;x++){
					for(int y = 10; y<clientArea.height-10;y++){
						int check = Mandelbrot(x,y,clientArea.width, clientArea.height);
						if(check==0){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
							e.gc.drawPoint(x, y);
						}else if(check>100){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_DARK_BLUE));
							e.gc.drawPoint(x, y);
						}else if(check>90){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
							e.gc.drawPoint(x, y);
						}else if(check>80){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_DARK_CYAN));
							e.gc.drawPoint(x, y);
						}else if(check>70){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_CYAN));
							e.gc.drawPoint(x, y);
						}else if(check>60){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN));
							e.gc.drawPoint(x, y);
						}else if(check>50){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_GREEN));
							e.gc.drawPoint(x, y);
						}else if(check>40){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_DARK_MAGENTA));
							e.gc.drawPoint(x, y);
						}else if(check>30){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_MAGENTA));
							e.gc.drawPoint(x, y);
						}else if(check>20){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_RED));
							e.gc.drawPoint(x, y);
						}else if(check>10){
							e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_DARK_RED));
							e.gc.drawPoint(x, y);
						}
						
					
					}
				}
				
				
			}
		});

	}
}
