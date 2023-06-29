package it.polito.tdp.genes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private Map<String, Genes> IdMappageni;
	
	public Model() {
		
		dao = new GenesDao();
		
	}
	
	public void creaGrafo() {
		
		List<Genes> GenesList = dao.getAllGenes();
		
		grafo = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Integer> listaVertici = dao.getAllVertices();
		Graphs.addAllVertices(grafo, listaVertici);
		
		this.IdMappageni = new HashMap<>();
		for(Genes g : GenesList) {
			this.IdMappageni.put(g.getGeneId(), g);
		}
		
		List<Interactions> interactionsList = dao.getAllInteractions(IdMappageni);
		
		for(Interactions i : interactionsList) {
			
			Integer crom1 = this.IdMappageni.get(i.getGene1().getGeneId()).getChromosome();
			Integer crom2 = this.IdMappageni.get(i.getGene2().getGeneId()).getChromosome();

				if(this.grafo.getEdge(crom1, crom2)==null) {
					Graphs.addEdgeWithVertices(this.grafo, crom1, crom2, i.getExpressionCorr());
				}else {
					DefaultWeightedEdge e = this.grafo.getEdge(crom1, crom2);
					double peso = this.grafo.getEdgeWeight(e);
					//System.out.println("\n"+peso);
					this.grafo.setEdgeWeight(e, peso+i.getExpressionCorr());
			}
		}
		
	}
	
	public int getNVertici(){
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi(){
		return this.grafo.edgeSet().size();
	}

}