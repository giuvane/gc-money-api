package br.edu.utfpr.gcmoney.api.model.sr;

import java.time.LocalDate;
import java.util.List;

public class LayerAdb {

	private String id;
	private String type;
	private String name;
	private String description;
	private List<DatasetAdb> dataset;
	private Project project;
	private LocalDate lastUpdate;
	private LocalDate createdAt;
	private Extras extras;
	
	
	public Extras getExtras() {
		return extras;
	}
	public void setExtras(Extras extras) {
		this.extras = extras;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DatasetAdb> getDataset() {
		return dataset;
	}
	public void setDataset(List<DatasetAdb> dataset) {
		this.dataset = dataset;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public LocalDate getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	
}
