package streuli;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.kitfox.svg.Circle;
import com.kitfox.svg.Group;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.ShapeElement;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.app.beans.SVGIcon;

class DynamicIconPanel2 extends JPanel {
	public static final long serialVersionUID = 0;

	final SVGIcon icon;
	URI uri;

	LinkedList<Circle> extraElements = new LinkedList<Circle>();

	public DynamicIconPanel2() throws IOException {
		final InputStream svgImage = this.getClass().getClassLoader().getResourceAsStream("map/switzerland_simple.svg");
		this.uri = SVGCache.getSVGUniverse().loadSVG(svgImage, "map_switzerland_simple");

		this.icon = new SVGIcon();
		this.icon.setAntiAlias(true);
		this.icon.setSvgURI(this.uri);

		this.setPreferredSize(new Dimension(400, 400));
	}

	@Override
	public void paintComponent(Graphics g) {
		final int width = this.getWidth();
		final int height = this.getHeight();

		g.setColor(this.getBackground());
		g.fillRect(0, 0, width, height);

		this.icon.setPreferredSize(new Dimension(400, 257));
		this.icon.setScaleToFit(true);
		this.icon.paintIcon(this, g, 0, 0);
	}

	public void addCircle() {
		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Group group = (Group) diagram.getElement("tspsolver.nodes");

		final Circle circle = new Circle();
		try {
			final int cx = (int) (Math.random() * 400);
			final int cy = (int) (Math.random() * 257);

			circle.addAttribute("cx", AnimationElement.AT_XML, "" + cx);
			circle.addAttribute("cy", AnimationElement.AT_XML, "" + cy);
			circle.addAttribute("r", AnimationElement.AT_XML, "5");

			group.loaderAddChild(null, circle);

			// Update animation state or group and it's decendants so that it reflects new animation values.
			// We could also call diagram.update(0.0) or SVGCache.getSVGUniverse().update(). Note that calling
			// circle.update(0.0) won't display anything since even though it will update the circle's state,
			// it won't update the parent group's state.
			group.updateTime(0.0);

			// Keep track of circles so we can remove them later
			this.extraElements.add(circle);
		}
		catch (final SVGException e) {
			e.printStackTrace();
		}

	}

	public void removeElement() {
		final int size = this.extraElements.size();
		if (size == 0) {
			return;
		}

		final int idx = (int) (Math.random() * size);
		final ShapeElement shapeElement = this.extraElements.remove(idx);

		final SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(this.uri);
		final Group group = (Group) diagram.getElement("tspsolver.nodes");

		try {
			group.removeChild(shapeElement);
		}
		catch (final SVGException e) {
			e.printStackTrace();
		}

	}
}

/**
 * 
 * @author kitfox
 */
public class MyTest extends javax.swing.JFrame {
	public static final long serialVersionUID = 0;

	DynamicIconPanel2 panel;

	/**
	 * Creates new form SVGIconDemo
	 * 
	 * @throws IOException
	 */
	public MyTest() throws IOException {

		this.panel = new DynamicIconPanel2();

		this.initComponents();

		this.panel_display.add(this.panel, BorderLayout.CENTER);

		this.pack();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		this.panel_display = new javax.swing.JPanel();
		this.jPanel2 = new javax.swing.JPanel();
		this.jPanel3 = new javax.swing.JPanel();
		this.bn_add = new javax.swing.JButton();
		this.bn_remove = new javax.swing.JButton();
		this.jPanel1 = new javax.swing.JPanel();
		this.jLabel1 = new javax.swing.JLabel();

		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.panel_display.setLayout(new java.awt.BorderLayout());

		this.getContentPane().add(this.panel_display, java.awt.BorderLayout.CENTER);

		this.jPanel2.setLayout(new javax.swing.BoxLayout(this.jPanel2, javax.swing.BoxLayout.Y_AXIS));

		this.bn_add.setText("Add Circle");
		this.bn_add.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MyTest.this.bn_addActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.bn_add);

		this.bn_remove.setText("Remove");
		this.bn_remove.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MyTest.this.bn_removeActionPerformed(evt);
			}
		});

		this.jPanel3.add(this.bn_remove);

		this.jPanel2.add(this.jPanel3);

		this.jLabel1.setText("Text");
		this.jPanel1.add(this.jLabel1);

		this.jPanel2.add(this.jPanel1);

		this.getContentPane().add(this.jPanel2, java.awt.BorderLayout.SOUTH);

		this.pack();
	}// </editor-fold>

	private void bn_removeActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.removeElement();
		this.repaint();
	}

	private void bn_addActionPerformed(java.awt.event.ActionEvent evt) {
		this.panel.addCircle();
		this.repaint();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new MyTest().setVisible(true);
				}
				catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Variables declaration - do not modify
	private javax.swing.JButton bn_add;
	private javax.swing.JButton bn_remove;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel panel_display;
	// End of variables declaration

}