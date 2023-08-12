package com.dbvertex.job.peresentation.dashboard.searchfreelancers

data class Search_freelancer_item_list(
    val id: String,
    val name: String,
    val image_url: String?,
    val category: String,
    val verified: Boolean
)
