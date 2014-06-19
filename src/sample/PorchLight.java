/*
 * Copyright (c) 2012 Gerald Bortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package edu.uci.ics.sdcl.porchlight;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import edu.uci.ics.sdcl.porchlight.graph.BugGraph;
import edu.uci.ics.sdcl.porchlight.graph.Neo4jBugGraphImpl;
import edu.uci.ics.sdcl.porchlight.importer.JiraXmlToNeo4jImporter;
import edu.uci.ics.sdcl.porchlight.importer.SvnToNeo4jImporter;
import edu.uci.ics.sdcl.porchlight.model.Bug;
import edu.uci.ics.sdcl.porchlight.model.BugTagSet;
import edu.uci.ics.sdcl.porchlight.model.Project;
import edu.uci.ics.sdcl.porchlight.ui.Functions;
import edu.uci.ics.sdcl.porchlight.ui.components.Frame;

public class PorchLight {

	private static BugGraph bugGraph;

	private static Frame frame;

	public static Project currentProject = null;
	public static Bug currentBug = null;

	public static Map<String, BugTagSet> tagSets = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			Enumeration<Object> keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = UIManager.get(key);
				if (value instanceof FontUIResource) {
					Font font = new Font("Segoe UI", Font.PLAIN, 12);
					UIManager.put(key, new FontUIResource(font));
				}
			}
			ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.questionIcon");
			UIManager.put("TagSettings.helpIcon", new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Set up all UI Components
				frame = new Frame();
				frame.setMinimumSize(new Dimension(1000, 700));
				// Get the size of the screen
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

				frame.setSize((int) (dim.width * .90), (int) (dim.height * .90));

				// Determine the new location of the window
				int w = frame.getSize().width;
				int h = frame.getSize().height;
				int x = (dim.width - w) / 2;
				int y = (dim.height - h) / 2;

				// Move the window
				frame.setLocation(x, y);

				frame.addComponentListener(new ComponentAdapter() {
					// This method is called after the component's size changes
					@Override
					public void componentResized(ComponentEvent evt) {
						Functions.updateSummaryTags();
					}
				});
			}
		});
	}

	public static void importToGraph(String graphDir, String file, String projectName, boolean open) {
		try {
		    	JiraXmlToNeo4jImporter importer = new JiraXmlToNeo4jImporter();
			importer.populateGraph(graphDir, file, projectName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void importRepository(String graphDir, String projectURL, String userName, String password, String projectName) {
		SvnToNeo4jImporter svnImporter = new SvnToNeo4jImporter();
		svnImporter.populateGraph(graphDir, projectURL, userName, password, projectName);
	}

	public static void initializeBugGraph(String graphDir) {
		bugGraph = new Neo4jBugGraphImpl();
		bugGraph.connect(graphDir);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				bugGraph.disconnect();
			}
		});

		// Load projects from the data manager
		Functions.loadProjects(bugGraph);

	}

	/**
	 * Get the main frame
	 * 
	 * @return
	 */
	public static Frame getFrame() {
		return frame;
	}

	public static BugGraph getBugGraph() {
		return bugGraph;
	}

}
