package br.edu.utfpr.gcmoney.api;

import java.awt.event.ActionEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.Parameter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.StyleLayer;
import org.geotools.process.Process;
import org.geotools.process.ProcessExecutor;
import org.geotools.process.Processors;
import org.geotools.process.Progress;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.style.ContrastMethod;

import com.google.gson.Gson;

import br.edu.utfpr.gcmoney.api.model.sr.DatasetAdb;
import br.edu.utfpr.gcmoney.api.model.sr.Extras;
import br.edu.utfpr.gcmoney.api.model.sr.LayerAdb;

public class TesteLink {
	
	private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    private JMapFrame frame;
    private AbstractGridCoverage2DReader reader;
    private Process process;
    
    private static final int BUFFER_SIZE = 4096;
	
	public static void main(String[] args) throws Exception {
		TesteLink tl = new TesteLink();
        tl.getLayersAndDisplay();
	}
	
	// Recuperar File de Link utilizando Apache Commons: https://dicasdejava.com.br/java-como-fazer-download-de-um-arquivo/
	private void getLayersAndDisplay() throws Exception {
        
		String link = "http://api.agromonitoring.com/data/1.0/1225e2cd680/5f3282c4714b524c7de0dd68?appid=6475da62dd1776f8852048627272aad0";
		
		URL url = new URL(link);
        File file = new File("temp");

        FileUtils.copyURLToFile(url, file);
		
        displayLayers(file);
    }
	
	/**
     * Displays a GeoTIFF file overlaid with a Shapefile
     *
     * @param rasterFile the GeoTIFF file
     * @param shpFile the Shapefile
     */
    private void displayLayers(File rasterFile) throws Exception {
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        // this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        reader = format.getReader(rasterFile, hints);
        
        GridCoverage2D coverage = reader.read(null);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
        
        // Extrair Features de objeto GridCoverage2D utilizando Process
        ProcessExecutor engine = Processors.newProcessExecutor(2);
        Name name = new NameImpl("ras", "PolygonExtraction");
        process = Processors.createProcess(name);
        
        Map<String, Object> input = new KVP("data", coverage);
        Progress working = engine.submit(process, input);

        Map<String, Object> result = working.get();
        SimpleFeatureCollection features = (SimpleFeatureCollection) result.get("result");
	    
        SimpleFeatureIterator iterator = features.features();
        
        List<DatasetAdb> datasets = new ArrayList<DatasetAdb>();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                
                // process feature
                Geometry g = (Geometry) feature.getAttribute( 0 ); // Atributo geométrico
                Double i = (Double) feature.getAttribute( 1 ); // Valor do pixel
                
                DatasetAdb ds = new DatasetAdb();
                
                if (i != -9999.0) {
                	double[] coords = new double[2];
                    coords[0] = g.getCentroid().getCoordinate().x;
                    coords[1] = g.getCentroid().getCoordinate().y;
                    
                    ds.setCoordinates(coords);
                    ds.setData(i);
                    datasets.add(ds);
                }
                
                // System.out.println("pixel: " + feature.getID() + " x: " + g.getCoordinate().x + ", y: " + g.getCoordinate().y + " Valor IV: " + i);
            }
        } finally {
            iterator.close();
        }
        
        System.out.println("CRS: " + crs.getCoordinateSystem());
        
        this.criarJson(datasets);
        
    }
    
    private void criarJson(List<DatasetAdb> datasets) {
    	LayerAdb layer = new LayerAdb();
    	layer.setName("Teste Adb-SR");
    	layer.setDescription("Testando Adb-SR");
    	layer.setDataset(datasets);
    	Extras ex = new Extras();
    	ex.setDatum("EPSG:3857"); // Recuperar datum da imagem
    	layer.setExtras(ex);
    	layer.setType("SAMPLE");
    	
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(layer);
    	
    	System.out.println(json);
    }
    
//    /**
//     * Downloads a file from a URL
//     * @param fileURL HTTP URL of the file to be downloaded
//     * @param saveDir path of the directory to save the file
//     * @throws IOException
//     */
//    public static void downloadFile(String fileURL, String saveDir)
//            throws IOException {
//        URL url = new URL(fileURL);
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        int responseCode = httpConn.getResponseCode();
// 
//        // always check HTTP response code first
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            String fileName = "";
//            String disposition = httpConn.getHeaderField("Content-Disposition");
//            String contentType = httpConn.getContentType();
//            int contentLength = httpConn.getContentLength();
// 
//            if (disposition != null) {
//                // extracts file name from header field
//                int index = disposition.indexOf("filename=");
//                if (index > 0) {
//                    fileName = disposition.substring(index + 10,
//                            disposition.length() - 1);
//                }
//            } else {
//                // extracts file name from URL
//                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
//                        fileURL.length());
//            }
// 
//            System.out.println("Content-Type = " + contentType);
//            System.out.println("Content-Disposition = " + disposition);
//            System.out.println("Content-Length = " + contentLength);
//            System.out.println("fileName = " + fileName);
// 
//            // opens input stream from the HTTP connection
//            InputStream inputStream = httpConn.getInputStream();
//            String saveFilePath = saveDir + File.separator + fileName;
//             
//            // opens an output stream to save into file
//            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
// 
//            int bytesRead = -1;
//            byte[] buffer = new byte[BUFFER_SIZE];
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
// 
//            outputStream.close();
//            inputStream.close();
// 
//            System.out.println("File downloaded");
//        } else {
//            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
//        }
//        httpConn.disconnect();
//    }
}
