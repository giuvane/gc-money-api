package br.edu.utfpr.gcmoney.api;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Parameter;
import org.geotools.data.ogr.OGRDataStore;
import org.geotools.data.ogr.OGRDataStoreFactory;
import org.geotools.data.ogr.jni.JniOGRDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.StyleLayer;
import org.geotools.process.ProcessExecutor;
import org.geotools.process.Processors;
import org.geotools.process.Progress;
import org.geotools.process.vector.TransformProcess;
import org.geotools.process.Process;
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
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.style.ContrastMethod;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;

public class MeusTestes {
	
	private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    private JMapFrame frame;
    private AbstractGridCoverage2DReader reader;
    private Process process;
    
    public static void main(String[] args) throws Exception {
    	MeusTestes me = new MeusTestes();
        me.getLayersAndDisplay();
    }
    
    /**
     * Prompts the user for a GeoTIFF file and a Shapefile and passes them to the displayLayers
     * method
     */
    private void getLayersAndDisplay() throws Exception {
        List<Parameter<?>> list = new ArrayList<>();
        list.add(
                new Parameter<>(
                        "image",
                        File.class,
                        "Image",
                        "GeoTiff or World+Image to display as basemap",
                        new KVP(Parameter.EXT, "tif", Parameter.EXT, "jpg")));

        JParameterListWizard wizard =
                new JParameterListWizard("Image Lab", "Fill in the following layers", list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            System.exit(0);
        }
        File imageFile = (File) wizard.getConnectionParameters().get("image");
        displayLayers(imageFile);
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
        
        // Initially display the raster in greyscale using the
        // data from the first image band
        Style rasterStyle = createGreyscaleStyle(1);

        // Set up a MapContent with the layers
        final MapContent map = new MapContent();
        map.setTitle("ImageLab");

        Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);
        
        map.addLayer(rasterLayer);
        
        
        // Extrair valor de pixels e coordenadas da imagem raster
        // https://www.guj.com.br/t/ler-imagens/47727/15
        GridCoverage2D coverage = reader.read(null);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
        Envelope env = coverage.getEnvelope();
        RenderedImage image = coverage.getRenderedImage();
        Raster raster = image.getData();
        
        float coresIv[] = new float[255];
        int coresRgb[] = new int[255];  
	    // esse vetor "cores[]" vai armazenar as cores RGB  
	    // [0] será Red(R), [1] será Green(G) e [2] será Blue(B)
        
        int count = 0;
	    // Verifica se o tif é RGB ou IV
	    int numBands = coverage.getNumSampleDimensions();
        if (numBands < 3) { // É um IV com uma banda
        	for (int x = 0; x < raster.getWidth(); x++) {  
            	for (int y = 0; y < raster.getHeight(); y++) {
            		raster.getPixel(x, y, coresIv); // captura o valor de IV de cada pixel da imagem 
            		
            		if (coresIv[0] >= -1) { // Desconsidera pixels fora da imagem
            			System.out.println("Pixel: "+ count + " IV("+coresIv[0]+")"); // Testar a saída
        	            count++;
            		}
    	            
    	        }  
    	    }
        } else { // é um RGB com 3 bandas
        	for (int x = 0; x < raster.getWidth(); x++) {  
            	for (int y = 0; y < raster.getHeight(); y++) {  
            		raster.getPixel(x, y, coresRgb); // captura da combinação de cor do pixel  
    	            System.out.println("R("+coresRgb[0]+") G("+coresRgb[1]+") B("+coresRgb[2]+")");  
    	            // essa saída é só pra você testar e ver que dá certo, rsrrs  
    	            // lembrando que isso vai fazer o processo ficar bem mais lento  
    	        }  
    	    }
        }
        
        // Extrair Features de arquivo raster utilizando Process
        ProcessExecutor engine = Processors.newProcessExecutor(2);
        Name name = new NameImpl("ras", "PolygonExtraction");
        process = Processors.createProcess(name);
        
        Map<String, Object> input = new KVP("data", coverage);
        Progress working = engine.submit(process, input);

        Map<String, Object> result = working.get();
        SimpleFeatureCollection features = (SimpleFeatureCollection) result.get("result");
	    
        SimpleFeatureIterator iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // process feature
                System.out.println(feature.toString());
                
            }
        } finally {
            iterator.close();
        }
        
        /*
        // Convertendo SimpleFeatureCollection em GeoJSON
        File file = new File("my.geojson");
        Map<String, String> connectionParams = new HashMap<String, String>();
        connectionParams.put("DriverName", "GeoJSON");
        connectionParams.put("DatasourceName", file.getAbsolutePath());
        OGRDataStoreFactory factory = new JniOGRDataStoreFactory();
        OGRDataStore dataStore = (OGRDataStore) factory.createNewDataStore(connectionParams);
        dataStore.createSchema(features, true, null);
        System.out.println(file);
        System.out.println(dataStore);
        */
        
        // Testando conversão de Polygon para Point
        String transform = "the_geom=the_geom\\n\" + \"name=name\\n\" + \"area=area( the_geom )";

        TransformProcess process = new TransformProcess();

        SimpleFeatureCollection featuresTransformed = process.execute(features, transform);
        
        SimpleFeatureIterator iteratorTransformed = featuresTransformed.features();
        try {
            while (iteratorTransformed.hasNext()) {
                SimpleFeature feature = iteratorTransformed.next();
                // process feature
                System.out.println(feature.toString());
                
            }
        } finally {
            iterator.close();
        }

        // Create a JMapFrame with a menu to choose the display style for the
        frame = new JMapFrame(map);
        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        // frame.enableTool(JMapFrame.Tool.ZOOM, JMapFrame.Tool.PAN, JMapFrame.Tool.RESET);
        frame.enableToolBar(true);
        
        frame.enableLayerTable(true);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Raster");
        menuBar.add(menu);

        menu.add(
                new SafeAction("Grayscale display") {
                    public void action(ActionEvent e) throws Throwable {
                        Style style = createGreyscaleStyle();
                        if (style != null) {
                            ((StyleLayer) map.layers().get(0)).setStyle(style);
                            frame.repaint();
                        }
                    }
                });

        menu.add(
                new SafeAction("RGB display") {
                    public void action(ActionEvent e) throws Throwable {
                        Style style = createRGBStyle();
                        if (style != null) {
                            ((StyleLayer) map.layers().get(0)).setStyle(style);
                            frame.repaint();
                        }
                    }
                });
        // Finally display the map frame.
        // When it is closed the app will exit.
        frame.setVisible(true);
    }
    
    /**
     * Create a Style to display a selected band of the GeoTIFF image as a greyscale layer
     *
     * @return a new Style instance to render the image in greyscale
     */
    private Style createGreyscaleStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        int numBands = cov.getNumSampleDimensions();
        Integer[] bandNumbers = new Integer[numBands];
        for (int i = 0; i < numBands; i++) {
            bandNumbers[i] = i + 1;
        }
        Object selection =
                JOptionPane.showInputDialog(
                        frame,
                        "Band to use for greyscale display",
                        "Select an image band",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        bandNumbers,
                        1);
        if (selection != null) {
            int band = ((Number) selection).intValue();
            return createGreyscaleStyle(band);
        }
        return null;
    }
    
    /**
     * Create a Style to display the specified band of the GeoTIFF image as a greyscale layer.
     *
     * <p>This method is a helper for createGreyScale() and is also called directly by the
     * displayLayers() method when the application first starts.
     *
     * @param band the image band to use for the greyscale display
     * @return a new Style instance to render the image in greyscale
     */
    private Style createGreyscaleStyle(int band) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }
    
    /**
     * This method examines the names of the sample dimensions in the provided coverage looking for
     * "red...", "green..." and "blue..." (case insensitive match). If these names are not found it
     * uses bands 1, 2, and 3 for the red, green and blue channels. It then sets up a raster
     * symbolizer and returns this wrapped in a Style.
     *
     * @return a new Style object containing a raster symbolizer set up for RGB image
     */
    private Style createRGBStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = {-1, -1, -1};
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }
 
}
