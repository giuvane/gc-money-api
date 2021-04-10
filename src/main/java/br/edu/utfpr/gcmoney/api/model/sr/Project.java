package br.edu.utfpr.gcmoney.api.model.sr;

import java.util.List;

public class Project {

	private String id;
	private String name;
	private String description;
	private Person person;
	private Double area;
	private List<LayerAdb> layers;
	private Extras extras;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public List<LayerAdb> getLayers() {
		return layers;
	}
	public void setLayers(List<LayerAdb> layers) {
		this.layers = layers;
	}
	public Extras getExtras() {
		return extras;
	}
	public void setExtras(Extras extras) {
		this.extras = extras;
	}
	
}
