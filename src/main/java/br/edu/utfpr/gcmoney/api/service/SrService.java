package br.edu.utfpr.gcmoney.api.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.process.Process;
import org.geotools.process.ProcessExecutor;
import org.geotools.process.Processors;
import org.geotools.process.Progress;
import org.geotools.util.KVP;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.edu.utfpr.gcmoney.api.model.Pessoa;
import br.edu.utfpr.gcmoney.api.model.sr.AgroApiKey;
import br.edu.utfpr.gcmoney.api.model.sr.DatasetAdb;
import br.edu.utfpr.gcmoney.api.model.sr.Extras;
import br.edu.utfpr.gcmoney.api.model.sr.LayerAdb;
import br.edu.utfpr.gcmoney.api.repository.SrRepository;

@Service
public class SrService {
	
	@Autowired
	private SrRepository srRepository;
	
	private AbstractGridCoverage2DReader reader;
    private Process process;
    
    public AgroApiKey salvar(AgroApiKey agroapikey) {
		
		return srRepository.save(agroapikey);
	}
	
	public String getJsonFromFile(File rasterFile, String nomeLayer) throws Exception {
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
            }
        } finally {
            iterator.close();
        }
        
        //System.out.println("CRS: " + crs.getCoordinateSystem());
        
        //System.out.println(rasterFile.getPath());
        
        String json = this.criarJson(datasets, rasterFile.getPath(), nomeLayer);
        
        return json;
	}
	
	private String criarJson(List<DatasetAdb> datasets, String nomeArquivo, String nomeLayer) {
		LayerAdb layer = new LayerAdb();
    	layer.setName(nomeLayer);
    	layer.setDescription(nomeLayer);
    	layer.setDataset(datasets);
    	Extras ex = new Extras();
    	ex.setDatum("EPSG:3857");
    	layer.setExtras(ex);
    	layer.setType("SAMPLE");
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(layer);
    	
    	/*
    	FileWriter writeFile = null;
    	    	
    	try{
			writeFile = new FileWriter(nomeArquivo +".json");
			//Escreve no arquivo conteudo do Objeto JSON
			writeFile.write(json);
			writeFile.close();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		*/
    	
    	System.out.println(json);
    	
    	return json;
    }

}
