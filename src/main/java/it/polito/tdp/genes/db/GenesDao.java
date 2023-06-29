package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Integer> getAllVertices(){
		String sql = "select distinct `Chromosome` from genes where `Chromosome`<>0";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getInt("Chromosome"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Interactions> getAllInteractions(Map<String, Genes> idMap){
		String sql = "select g1.`Chromosome`, g2.`Chromosome`, i.* "
				+ "from genes g1, genes g2, interactions i "
				+ "where g1.`Chromosome`<>0 and g2.`Chromosome`<>0 and g1.`Chromosome`<>g2.`Chromosome`  "
				+ "and g1.`GeneID`=i.`GeneID1`and g2.`GeneID`=i.`GeneID2` "
				+ "order by g1.`Chromosome`, g2.`Chromosome`";
		List<Interactions> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Genes g1 = idMap.get(res.getString("i.GeneID1"));
				Genes g2 = idMap.get(res.getString("i.GeneID2"));
				
				result.add(new Interactions(g1, g2, res.getString("i.Type"), res.getDouble("i.Expression_Corr")));
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	


	
}
