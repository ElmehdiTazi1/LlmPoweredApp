package org.mql.llm.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String sentiment;

    private String brand;

    private String product;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Comment(Long id, String content, String sentiment, String brand, String product, LocalDateTime createdAt) {
		this.id = id;
		this.content = content;
		this.sentiment = sentiment;
		this.brand = brand;
		this.product = product;
		this.createdAt = createdAt;
	}

	public Comment() {
		

	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", sentiment=" + sentiment + ", brand=" + brand
				+ ", product=" + product + ", createdAt=" + createdAt + "]";
	}
	

    
}
