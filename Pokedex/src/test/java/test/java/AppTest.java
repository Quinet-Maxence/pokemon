package test.java;

import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;

import main.java.App;
import main.java.DB;
import spark.Spark;

import org.junit.*;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Rule
  public DatabaseRule DatabaseRuleCreation() {
	  DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pokedex_test", null, null);
	  try(Connection con = DB.sql2o.open()) {
	      String deletePokemonsQuery = "DELETE FROM pokemons *;";
	      String deleteMovesQuery = "DELETE FROM moves *;";
	      String deleteMovesPokemonsQuery = "DELETE FROM moves_pokemons *;";
	      con.createQuery(deletePokemonsQuery).executeUpdate();
	      con.createQuery(deleteMovesQuery).executeUpdate();
	      con.createQuery(deleteMovesPokemonsQuery).executeUpdate();
	    }
	return DBRules;
  }
  DatabaseRule DBRules = DatabaseRuleCreation();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  
  public static ServerRule ServerRuleCreation() {
	  String[] args = {};
	    App.main(args);
	    Spark.stop();
		return server;
	  
  }
  public static ServerRule server = ServerRuleCreation();

  /**
   * This test checks access to the Web app and therefore to its Source page which is called "Pokedex". 
   * It is an Unit test
   */
  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Pokedex");
  }

  /**
   * This test checks access to the site, then to the Pokedex and checks if the pokedex contains certain Pokemon. This is an Integration Test
   */
  @Test
  public void allPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/");
    click("#viewDex");
    assertThat(pageSource().contains("Ivysaur"));
    assertThat(pageSource().contains("Charizard"));
  }

  /**
   * This test goes on the 6th page of the pokédex and checks if it is indeed the Pokemon "Charizard". 
   * This is an Unit Test
   */
  @Test
  public void individualPokemonPageIsDisplayed() {
    goTo("http://localhost:4567/pokepage/6");
    assertThat(pageSource().contains("Charizard"));
  }

  /**
   * This test goes to the 6th page of the Pokédex and clicks on the attack that contains the triangle icon, 
   * and checks if it is the "Squirtle" attack.
   * This is an Integration Test.
   */
  @Test
  public void arrowsCycleThroughPokedexCorrectly() {
    goTo("http://localhost:4567/pokepage/6");
    click(".glyphicon-triangle-right");
    assertThat(pageSource().contains("Squirtle"));
  }

  /**
   * This test goes into the Pokedex and looks for a pokemon that contains the string "char" in its name. 
   * This is an Integration Test
   */
  @Test
  public void searchResultsReturnMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("char");
    assertThat(pageSource().contains("Charizard"));
  }

  /**
   * This test goes into the Pokedex and looks for a pokemon that contains the string "x" in its name. 
   * This is an Integration Test
   */
  @Test
  public void searchResultsReturnNoMatches() {
    goTo("http://localhost:4567/pokedex");
    fill("#name").with("x");
    assertThat(pageSource().contains("No matches for your search results"));
  }

}
