package com.arizon.productcommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name ="tbl_context_filters")
public class PCContextFilters {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name ="context_filter_id")
	    private Long contextFilterId;

	    @Column(name = "destination_category_id")
	    private Integer destinationCategoryId;

	    @Column(name = "destination_channel_id")
	    private Integer channelId;

	    @Column(name = "destination_filter_id")
	    private String destinationFilterId;

	    @Column(name = "display_name")
	    private String displayName;

	    @Column(name = "type")
	    private String type;

	    @Column(name = "sort_by")
	    private String sortBy;

	    @Column(name = "item_count")
	    private Integer itemCount;

	    @Column(name = "collapse")
	    private Boolean collapse;

	    @Column(name = "display_count")
	    private Boolean displayCount;

	    @Column(name = "is_enabled")
	    private Boolean isEnabled;

	    @Column(name = "facet_id")
	    private Integer facetId;

	    @Column(name = "facet_key")
	    private String facetKey;    
	    
	    @Column(name ="is_active")
	    private Boolean isActive;
	    
	    @Column(name = "is_deleted")
	    private Boolean isDeleted;
	}

