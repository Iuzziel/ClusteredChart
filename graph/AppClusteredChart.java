package graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

/**
 * ClusteredBarChart maison qui s'utilise avec un
 * setList(List<AppCatDatasetObject> list) qui fournit les
 * valeur,serie,categorie au barchart, puis un initControl(String title, String
 * categoryAxisLabel, String valueAxisLabel) pour rafraichir le graphique.
 */
public class AppClusteredChart extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<AppCatDatasetObject> list = new ArrayList<AppCatDatasetObject>();
	private Vector<String> vCat = new Vector<String>();
	private XYPlot plot;

	public AppClusteredChart() {
		initControl("ClusteredChart", "Domaine", "Valeur");
	}

	/**
	 * Sert a initialiser le panel contenant le graph, recupere donc les
	 * valeures de la list en donnee membre (prealablement charger avec un
	 * setList() depuis l'appelant), et fais le revalidate() repaint() a la fin.
	 * 
	 * @param title
	 *            Titre du graph
	 * @param categoryAxisLabel
	 *            Label general axe des X
	 * @param valueAxisLabel
	 *            Label general axe des Y
	 */
	public void initControl(String title, String categoryAxisLabel,
			String valueAxisLabel) {
		removeAll();
		JFreeChart chart = createClusteredChart(title, categoryAxisLabel,
				valueAxisLabel);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setRangeZoomable(true);
		chartPanel.setDomainZoomable(false);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setMouseZoomable(true);
		// chartPanel.setPreferredSize(new Dimension(500, 270));
		add(chartPanel);
		revalidate();
		repaint();
	}

	/**
	 * Cree un XYSeriesCollection a partir d'une list de AppCatDatasetObject
	 * 
	 * @param list
	 *            AppCatDatasetObject
	 * @return XYSeriesCollection
	 */
	public XYSeriesCollection createIntervalXYDataset(
			List<AppCatDatasetObject> list) {
		XYSeriesCollection datasetSerieCollection = new XYSeriesCollection();
		List<XYSeries> lSerie = new ArrayList<XYSeries>();

		// Boucle qui remplie la lSerie avec des new Serie ayant pour cle les
		// getSerie() de la listChart
		for (AppCatDatasetObject appCatDO : list) {
			boolean newSerie = true;
			for (XYSeries xySeries : lSerie)
				if (xySeries.getKey().equals(
						String.valueOf(appCatDO.getSerie())))
					newSerie = false;
			if (newSerie)
				lSerie.add(new XYSeries(String.valueOf(appCatDO.getSerie())));
		}
		// Remplie vCat des string de categories
		vCat = new Vector<String>();
		for (AppCatDatasetObject appCatDO : list)
			for (XYSeries xySeries : lSerie)
				if (xySeries.getKey().equals(
						String.valueOf(appCatDO.getSerie())))
					if (vCat.size() == 0) {
						vCat.add(appCatDO.getCategorie());
					} else {
						boolean newCat = true;
						for (String catDeVCat : vCat)
							if (appCatDO.getCategorie().equals(catDeVCat))
								newCat = false;
						if (newCat)
							vCat.add(appCatDO.getCategorie());
					}

		for (AppCatDatasetObject appCatDO : list)
			for (XYSeries xySeries : lSerie)
				if (xySeries.getKey().equals(appCatDO.getSerie()))
					for (int k = 0; k < vCat.size(); k++)
						if (appCatDO.getCategorie().equals(vCat.get(k)))
							xySeries.add(k, appCatDO.getValue());

		for (XYSeries xySeries : lSerie)
			datasetSerieCollection.addSeries(xySeries);

		return datasetSerieCollection;
	}

	private JFreeChart createClusteredChart(String title,
			String categoryAxisLabel, String valueAxisLabel) {
		IntervalXYDataset dataset = createIntervalXYDataset(list);

		ValueAxis valueAxis = new NumberAxis(valueAxisLabel);
		valueAxis.setRange(0, 100);

		String[] sv = new String[vCat.size()];

		for (int i = 0; i < sv.length; i++)
			sv[i] = vCat.get(i);
		SymbolAxis domainAxis = new SymbolAxis(categoryAxisLabel, sv);
		domainAxis.setTickUnit(new NumberTickUnit(1));
		domainAxis.setVerticalTickLabels(true);
		domainAxis.setLowerMargin(0.25);
		domainAxis.setUpperMargin(0.25);

		XYBarRenderer renderer = new ClusteredXYBarRenderer();
		renderer.setMargin(0.10);
		renderer.setShadowVisible(false);

		// plot = new XYPlot(dataset, domainAxis, valueAxis, renderer);
		plot = new XYPlot();
		// On ajoute un index au dataset pour pouvoir plus facilement ajouter
		// des graphs
		plot.setDataset(1, dataset);
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(valueAxis);
		plot.setRenderer(renderer);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setNoDataMessage("--Pas de valeur à afficher--");

		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);

		return chart;
	}

	public static void appAddMarker(XYPlot plot, int what, int id) {
    // Get your own
    double objectif = 50;

		Marker marker = new ValueMarker(objectif);
		marker.setPaint(Color.RED);
		marker.setLabel("Objectif TRG "
				+ list.get(0).getIntitule_ligneMachine());
		marker.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
		marker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		plot.addRangeMarker(marker);
	}

	public List<AppCatDatasetObject> getList() {
		return list;
	}

	public void setList(List<AppCatDatasetObject> list) {
		this.list = list;
	}

	public XYPlot getPlot() {
		return plot;
	}

	public void setPlot(XYPlot plot) {
		this.plot = plot;
	}
}
