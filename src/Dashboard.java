import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.math3.stat.inference.OneWayAnova;

import java.util.ArrayList;
import org.apache.commons.math3.distribution.NormalDistribution;

public class Dashboard extends Application {
	
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		// First Item
		VBox root = new VBox();
		root.setMaxSize(250, 250);
		Button btn = new Button("GO!");
		btn.setMaxWidth(root.getMaxWidth());
		root.getChildren().add(btn);

		//Second Item
		Text txt = new Text("Enter the mean");
		root.getChildren().add(txt);
		TextField nMean = new TextField ();
		nMean.setText("Null Hypothesis");
		Scene scene = new Scene(root, 250, 250);
		root.getChildren().add(nMean);
		
		TextField aMean = new TextField ();
		aMean.setText("Alternative");
		root.getChildren().add(aMean);
		

		//Third item
		Text txt2 = new Text("Enter the Deviation");
		root.getChildren().add(txt2);
		TextField nDev = new TextField ();
		nDev.setText("Null Hypothesis");
		root.getChildren().add(nDev);
		
		TextField aDev = new TextField();
		aDev.setText("Alternative");
		root.getChildren().add(aDev);

		//Forth Item
		Text txt3 = new Text("Enter N");
		root.getChildren().add(txt3);
		TextField tf3 = new TextField ();
		tf3.setText("");
		root.getChildren().add(tf3);
		
		

		btn.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				double nullMean = Double.parseDouble(nMean.getText());
				double nullDeviation = Double.parseDouble(nDev.getText());
				double alterMean = Double.parseDouble(aMean.getText());
				double alterDeviation = Double.parseDouble(aDev.getText());
				int numofelmnts = Integer.parseInt(tf3.getText());
				
				NormalDistribution ndNull = new NormalDistribution (nullMean,nullDeviation);
				NormalDistribution ndAlt = new NormalDistribution(alterMean, alterDeviation);
				
				ArrayList<Double> ar = new ArrayList<Double>();
				for(int i=0;i<numofelmnts;i++) {
				ar.add(ndNull.sample());				
				}
				
				double[] arr = new double[ar.size()];
				for(int i=0;i<arr.length;i++) {
					arr[i]=ar.get(i);
				}
				
				ArrayList<Double> ar2 = new ArrayList<Double>();
				for(int i=0;i<numofelmnts;i++) {
				ar2.add(ndAlt.sample());				
				}
				
				double[] arr2 = new double[ar2.size()];
				for(int i=0;i<arr2.length;i++) {
					arr2[i]=ar2.get(i);
				}
				
				
				
				OneWayAnova owa = new OneWayAnova();
				ArrayList<double[]> ar3 = new ArrayList<>();
				ar3.add(arr);
				ar3.add(arr2);
				double pValue = owa.anovaPValue(ar3);
				

				Stage newStage = new Stage();
				TextField tf4 = new TextField();
				tf4.setText(String.format("Mean1: %f  Sigma1: %f  Mean2:%f  p-value:%f" , 
						ndNull.getMean(), ndNull.getStandardDeviation(), ndAlt.getMean(), pValue));
				
				//defining the axes
		        final NumberAxis xAxis = new NumberAxis();
		        final NumberAxis yAxis = new NumberAxis();
		        xAxis.setLabel("Number of Month");
		        //creating the chart
		        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		        lineChart.setTitle("Deviation of 2 data sets");
		        
		        XYChart.Series series = new XYChart.Series();
		        series.setName("Set 1");
		        double lower = ndNull.getMean() - (3* ndNull.getStandardDeviation());
		        double upper = ndNull.getMean() + (3* ndNull.getStandardDeviation());

		        for(double i = lower;	 i < upper;	i += (upper-lower) / 30 )
				{    series.getData().add(new XYChart.Data(i, ndNull.density(i)));      }
		        
		        
		        XYChart.Series series2 = new XYChart.Series();
		        series2.setName("Set 2");
		        double lower2 = ndAlt.getMean() - (3* ndAlt.getStandardDeviation());
		        double upper2 = ndAlt.getMean() + (3* ndAlt.getStandardDeviation());

		        for(double i = lower2;	 i < upper2;	i += (upper2-lower2) / 30 )
				{    series2.getData().add(new XYChart.Data(i, ndAlt.density(i)));      }
		        
		        
		        
				
				BorderPane gp = new BorderPane();
				gp.setTop(tf4);
				
				lineChart.getData().addAll(series,series2); 
				if(pValue>0.05) {
					Node line = series.getNode().lookup(".chart-series-line");
					line.setStyle("-fx-stroke: #0000FFFF;-fx-stroke-width: 10px;");
					Node line2 = series2.getNode().lookup(".chart-series-line");  
					line2.setStyle("-fx-stroke: #FF0000B0;-fx-stroke-width: 2px;"); 
					
				}
				else {
					Node line = series.getNode().lookup(".chart-series-line");
					line.setStyle("-fx-stroke: #FF00FFB0;-fx-stroke-width: 2px;");
					Node line2 = series2.getNode().lookup(".chart-series-line"); 
					line2.setStyle("-fx-stroke: #00FF00FF;-fx-stroke-width: 10px;");
				}
				gp.setCenter(lineChart);
				Scene newScene = new Scene(gp,590,400);
				newStage.setScene(newScene);
				newStage.show();

				
			}
		});



		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);

	}

}
