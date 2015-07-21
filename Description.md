[![](http://iabin-threats.googlecode.com/svn/wiki/resources/gb.gif)](http://code.google.com/p/iabin-threats/wiki/Description?wl=en)
[![](http://iabin-threats.googlecode.com/svn/wiki/resources/es.gif)](http://code.google.com/p/iabin-threats/wiki/Description?wl=es)

## Introduction ##

The **Inter-American Biodiversity Information Network (IABIN)** has the mandate of providing a networking information infrastructure as well as primary biodiversity data on a number of topics in order to improve decision-making, particularly for issues at the interface of human development and biodiversity conservation. During the late 2010, IABIN has approved funds for projects aiming to (a) develop IABIN; and (b) provide the necessary instruments to draw knowledge from the information contained in the network mentioned to support sound decision-making concerning the conservation and sustainable use of biodiversity.

Under that framework, IABIN has funded the Decision and Policy Analysis Program (DAPA) at the International Center for Tropical Agriculture (CIAT) on a project entitled **"Improve primary data and use it for threat assessment and in situ conservation planning in South America"**. More specifically, we intend to assess and improve the geographic quality of the primary data provided by the different IABIN Thematic Networks (TNs) using a scientifically rigorous and mostly automated approach. The data will then be used in a variety of modelling and statistical exercises aiming to derive practical conclusions on the conservation status of Biodiversity, and finally, a web interface for policy-makers to navigate throughout all our results will be provided. More specifically, the project will deliver:

  1. Automatic cross-checking scripts
  1. Automatic georeferencing scripts
  1. Results on both the cross-checking and georeferencing of the TNs with suitable databases for such purposes, namely:
    1. Species and Specimens Thematic Network (SSTN)
    1. Invasive Species Thematic Network (I3N)
    1. Pollinators Thematic Network (PTN)
  1. Automatic species distribution models training scripts
  1. An assessment of level of both anthropogenic threats and conservation status for a number of species (for which geographic distributions can be modelled)
  1. A Google-maps based navigation tool for all the modelling results (from [5](5.md))


## Rationale ##

Recent developments in Geographic Information Systems (GIS) and ecological niche modeling (ENM) applications have added value to data, and permit researchers to provide highly policy-relevant analyses to direct conservation interventions. Through the most recent ENM techniques, potential distributions of species can be analyzed using fairly limited or biased datasets containing primary data to determine current biodiversity hotspots (Phillips et al. 2006; Elith et al. 2006; Hijmans and Graham 2006), and likely threats from anthropogenic activities (Jarvis et al. 2009) and climate change (IPCC 2007) in order to deﬁne current and future in situ and ex situ conservation priorities (Walker et al. 2009; Bass et al. 2010).

However, one of the most relevant issues regarding the analyses and latter conclusions derived from the usage of primary biodiversity data is the reliance on its quality. Poor quality biodiversity data could lead to incorrect and biased conclusions as well as cause ineficient and/or wrong investment of the available resources and inadequate policy development. Checking of biodiversity data quality as well as adequate use is a key issue in order to aid decision-making processes, and equally important is increasing the impact of such data on policy-making processes and the society. Making use of freely online available spatial datasets plus some programming this is now doable not only at a large scale but also in a mostly automated way. Following sections will briefly describe each of the deliverables of this project.

We acknowledge the importance of the relationship between biodiversity and socio-economy, and the reliance of human beings on biodiversity and high quality ecosystem services. We, therefore, consider it fundamental to assess the level at which biodiversity is threatened and conserved across the globe, and particularly (given IABIN’s focus) in Latin America, where the presence of signiﬁcant biodiversity ‘hotspots’ (e.g. Brazilian Atlantic Forest, Ecuador’s Yasuní National Park, the Guyana Shield, the Amazon forest, among others) make conservation strategies critical towards the near and long term future. Strong methodologies to assess the impacts of anthropogenic activities will be developed and at least one peer reviewed publication will be pursued using the results of the modeling and cross-checking of coordinates. For further information, please contact Andy Jarvis, leader of the Decision and Policy Analysis program at CIAT (a.jarvis@cgiar.org).


## Phase 1: Development of automated tools, assessment and improvement of IABIN TNs primary biodiversity data ##

One of the most relevant issues regarding the analyses and latter conclusions derived from the usage of primary biodiversity data is the reliance on its quality. We have built automated algorithms (Figure 1) developed in the Java programming language that allow a thorough coordinate verification process (error detection) and georeferencing process (error correction). Through this process we intend to develop an automated platform for IABIN’s TNs to assess their own data whenever more data is incorporated on any of their databases.

![http://iabin-threats.googlecode.com/svn/wiki/resources/diagram-quality-improvement.png](http://iabin-threats.googlecode.com/svn/wiki/resources/diagram-quality-improvement.png)

**Figure 1** Assessment and improvement of IABIN TNs databases using automated algorithms

But, how to make the data reliable enough? We have implemented algorithms that:

  * Verify coordinates at different levels, say:
    * Are the records where they say they are?
    * Are the records inside land areas (for terrestrial plant species only)
    * Are all the records within the environmental niche of the taxon?
  * Correct wrong references
  * Add references to those that do not have
  * Cross-check with curators and feedback the database

Adequate documentation on how to build, configure and run the tools is also provided in order to better manage knowledge transfer between and within TNs, and between TNs and project developers (see the table of contents in the left of this page). This googlecode project has been created with the aim of storing and providing access to all the source code and documentation, and also providing a platform where where bugs and problems can be raised as issues and discussed with developers.


### Phase 1a: Cross-checking of coordinates ###

We are concerned about the geographic errors. Common errors in primary biodiversity databases are (1) misspellings of country, state, county and locality names, (2) swapping of latitude or longitude, (3) assignation of the value zero when missing data is found, (4) coordinates in different systems or unknown systems, (5) wrong usage of decimal places or truncation of all decimals, (6) usage of different coordinate formats (e.g. degrees-minutes-seconds vs. decimal degrees) without proper documenting, among others.

In view of that we have developed and implemented automated Java software that runs in batch mode and assesses primary biodiversity data from large databases automatically, and provides statistics on the quality of the data. Our software verifies geographic references (coordinates) at three different levels (Figure 2):
  1. Continental level
  1. Country level
  1. Environmental level

http://iabin-threats.googlecode.com/svn/wiki/resources/script-process.PNG
**Figure 2** Flow chart of the coordinate verification process

To verify at the continental level, we use a high resolution land areas mask from the SRTM Digital Elevation Model coastlines (Jarvis et al., 2008); to verify at the country level, we use the data from the Global Administrative Areas (Hijmans, 2010); and to verify at the environmental level, we use the Tukey outlier test (Tukey, 1977) in a twenty-dimensional space given by 19 bioclimatic indices (Ramirez &  Bueno-Cabrera, 2009) derived from the WorldClim dataset (Hijmans et al., 2005) and the elevation (Jarvis et al., 2008). We flag a record as reliable only if it:

  1. Is located in the country where it is reported to have been collected and/or observed
  1. Being a record from a terrestrial species, falls within land areas, and
  1. Is not flagged as an outlier for a given species in less than 80% of the 20 environmental variables used to describe the environment


### Phase 1b: Correction of coordinates ###

Even when a coordinate verification process is implemented, it does not directly imply that the best-bet is being done from the available data. A quality improvement process is still required in order to be able to use the best-shaped data in further analyses. Geographically speaking, this means the retrieval of coordinates when they are either unavailable or erroneous. Using biogeomancer (Guralnick &  Hill, 2009, Guralnick et al., 2006, Hill et al., 2009), one can retrieve the coordinates of a particular location by means of the collecting place information provided in the database (e.g. country, state, county, and locality name). Though the location information is not available in all the cases when also the coordinates are lacking, it is available in a number of cases and can be retrieved with a considerable degree of confidence (Hill et al., 2009)

In this particular case, we have developed software that uses all the records flagged as not reliable (from the automatic data filtering and cross-checking), is capable of

  1. Identifying the records that have enough location information to retrieve a coordinate,
  1. querying the biogeomancer service at http://bg.berkeley.edu/latest/
  1. interpreting the result from the biogeomancer service, and
  1. adding the retrieved coordinate to the database.


## Phase 2: Modelling species distributions and assessing threat and conservation status ##

We acknowledge the importance of the relationship between biodiversity and socio-economy, and the reliance of human beings on biodiversity and high quality ecosystem services. We, therefore, consider it fundamental to assess the level at which biodiversity is threatened and conserved across the globe, and particularly (given IABIN’s thematic networks focus) in Latin America, where the presence of significant biodiversity ‘hotspots’ (e.g. Brazilian Atlantic Forest, Ecuador’s Yasuní National Park, the Guyana Shield, the Amazon forest, among others) make conservation strategies critical towards the near and long term future. Using the highest quality primary biodiversity data from the IABIN TNs databases we:

  * Develop species distributions using a modelling technique,
  * Overlay these distributions with a set of spatially explicit anthropogenic threats,
  * Overlay these distributions with the current distribution of protected areas, and
  * Calculate the proper metrics to both validate the models developed and to assess the set of species regarding threat and conservation status

A variety of tools and datasets are propsed for usage in this particular context (Figure 3)

http://iabin-threats.googlecode.com/svn/wiki/resources/niche-modeling-workflow.PNG
**Figure 3** Flow chart of the species modelling and threat and conservation assessment process

A Java based script will be generated for the training of niche models for all terrestrial taxa with at least 10 unique presence records within the IABIN network. To this purpose, the Maximum Entropy model (Phillips et al., 2006) will be used to achieve this. This approach has been largely evaluated by several authors and has demonstrated to perform very well under several sampling bias conditions or with very limited information, with increased accuracy compared with other niche models (Elith et al., 2006; Hijmans & Graham, 2006).

Niche models will be trained using a baseline climatic scenario as provided by WorldClim (Hijmans et al., 2005), at 5 arc-minute spatial resolution. Selected model performance metrics will be provided for all the modeled species (i.e. area under the ROC curve, correlation, logistic deviation and euclidean distance).

The potential distributions will be thresholded to the average probability of presence records (prevalence), and binary (presence/absence) as well as thresholded probability distribution grids in ESRI-ASCII format will be generated. This will ensure the potential distributions of the species are not over-estimating the range of the modeled taxa. A biodiversity layer (species richness and/or weighted diversity) for flora, fauna and all species together will be produced based on the binary layers.

A set of spatial datasets depicting the influence of environmental, social and economic factors on ecosystems where a taxon or a group of taxa might be present will be used (Jarvis et al. 2010) to calculate the level of threat of all biodiversity using the comprised species richness layer and each of the threat layers. Threats for individual species will also be created by means of a threat index. In addition to that, species distributions will also be overlaid with the protected area networks in South America, both at the aggregate level (species richness) and at the individual species level. The level of protection of all modeled taxa will therefore be assessed using the comprised species richness/diversity layer by calculating the average, maximum and minimum amount of taxa held in each protected area, and the percent of different diversity-category areas which is protected and unprotected.


## Phase 3: Development of web visualisation to increase the impact of research outputs ##

Equally important to doing science is delivering it to the proper public. Not only scientific publications and grey literature (i.e. reports, working papers) need to be done, but also easy access to data needs to be provided. The best way to deliver data is providing a platform where users:

  * Can navigate through and visualise the data,
  * can dinamically play around with the data, and
  * can download the data,

We are pursuing the development of a Google-Maps based interface to select a desired taxon, navigate through the niche modeling results as well as through the occurrence data, with the ability to differentiate between incorrect, corrected, new (georreferenced) and discarded locations (due to errors). This interface will also allow to query the threat assessment and conservation status metrics calculated in **Phase 2** and stored in a database, and will allow showing it as a small box for the selected taxon. A query on the total biodiversity layers can also be dynamically analyzed, and the metrics queried as well.


## Contacts and project staff ##

  * Dr. Andy Jarvis (a.jarvis@cgiar.org), project coordinator

  * Julian Ramirez-Villegas (j.r.villegas@cgiar.org), spatial analysis supervision
  * Louis Reymondin (louis.reymondin@gmail.com), development supervision

  * Daniel Amariles (d.a.amariles@cgiar.org), developer
  * Hector Tobon (h.f.tobon@cgiar.org), developer
  * Johannes Signer (j.m.signer@gmail.com), ecologist and spatial analyst
  * Jhon Jairo Tello (jhoanse@gmail.com), developer
  * Jorge Camacho (lotvxx@gmail.com), developer


## References ##

Bass MS, Finer M, Jenkins CN, Kreft H, Cisneros-Herrera DF, et al. (2010) Global Conservation Significance of Ecuador’s Yasuní National Park. PLoS ONE 5(1): e8767. doi: 10.1371/journal.pone.0008767

Elith J, Graham CH, Anderson RP, Dudik M., Ferrier S., et al. (2006) Novel methods improve prediction of species' distributions from occurrence data. Ecography 29: 129-151.

Guralnick R, Hill A (2009) Biodiversity informatics: automated approaches for documenting global biodiversity patterns and processes. Bioinformatics, 25, 421-428.

Guralnick RP, Wieczorek J, Beaman R, Hijmans RJ, The Biogeomancer Working G (2006) BioGeomancer: Automated Georeferencing to Map the World's Biodiversity Data. PLoS Biol, 4, e381.

Hijmans RJ, Cameron SE, Parra JL, Jones PG, Jarvis A (2005) Very high resolution interpolated climate surfaces for global land areas. International Journal of Climatology, 25, 1965-1978.

Hijmans RJ, Graham CH (2006) The ability of climate envelope models to predict the effect of climate change on species distributions. Global Change Biology 12: 2271-2281.

Hijmans RJ (2010) Global Administrative Areas (GADM).  (ed Zoology MOV) pp Page, Berkeley.

Hill A, Guralnick R, Flemons P et al. (2009) Location, location, location: utilizing pipelines and services to more effectively georeference the world's biodiversity data. BMC Bioinformatics, 10, 1-9.

Intergovernmental Panel on Climate Change (2007) IPCC Fourth Assessment Report: Climate Change 2007, IPCC, Geneva.

Jarvis A, Reuter HI, Nelson A, Guevara E (2008) Hole-filled  seamless SRTM data V4.  (ed (Ciat) ICFTA) pp Page, International Center for Tropical Agriculture (CIAT).

Jarvis A, Trouval JL, Castro-Schmitz M, Sotomayor L, Hyman GG (2010) Assessment of threats to ecosystems in South America. Journal for Nature Conservation. Journal for Nature Conservation,

Phillips SJ, Anderson RP, Schapire RE (2006) Maximum entropy modeling of species geographic distributions. Ecological Modelling, 190: 231-259.

Ramirez J, Bueno-Cabrera A (2009) Working with climate data and niche modeling I. Creation of bioclimatic variables.  pp Page, Cali, Colombia, International Center for Tropical Agriculture (CIAT).

Tukey JW (1977) Exploratory Data Analysis, Addison-Wesley.

Walker R, Moore NJ, Arima E, Perz S, Simmons C, Caldas M, Vergara D, Bohrer C (2009) Protecting the Amazon with Protected Areas. PNAS, vol. 106, no. 26, 10582-10586.