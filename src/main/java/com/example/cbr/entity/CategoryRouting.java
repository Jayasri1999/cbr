package com.example.cbr.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category_routing")
@IdClass(CategoryRoutingId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRouting {
	 	@Id
	    @Column(name = "category_name")
	    private String category_name;


	    @Id
	    @Column(name = "subcategory_name")
	    private String subcategory_name;

	    @Column(name = "catProcessFlow")
	    private String catProcessFlow;
	    
	    @Column(name = "xslt_content", columnDefinition = "TEXT")
	    private String xslt_content;



}
