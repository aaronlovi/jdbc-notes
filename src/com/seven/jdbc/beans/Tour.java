package com.seven.jdbc.beans;

import java.io.Serializable;

public class Tour implements Serializable {

	private static final long serialVersionUID = -7898874010015650583L;

	private int tourId;
	private int packageId;
	private String tourName;
	private String blurb;
	private String description;
	private double price;
	private String difficulty;
	private String graphic;
	private int length;
	private String region;
	private String keywords;

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public String getTourName() {
		return tourName;
	}

	public void setTourName(String tourName) {
		this.tourName = tourName;
	}

	public String getBlurb() {
		return blurb;
	}

	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getGraphic() {
		return graphic;
	}

	public void setGraphic(String graphic) {
		this.graphic = graphic;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	///////////////////////////////////////////////////////////////////////////
	// PUBLIC INTERFACE
	///////////////////////////////////////////////////////////////////////////
	
	public static Tour createDummyTourBean() {
		Tour tour = new Tour();
		
		tour.setBlurb("New Tour blurb");
		tour.setDescription("New Tour Description");
		tour.setDifficulty("Medium");
		tour.setGraphic("1.jpg");
		tour.setKeywords("New,Tour");
		tour.setLength(1);
		tour.setPackageId(1);
		tour.setPrice(50.0);
		tour.setRegion("Alabama");
		tour.setTourName("New Tour");
		
		return tour;
	}

}
