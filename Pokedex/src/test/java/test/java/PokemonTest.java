package test.java;

import static org.junit.Assert.*;

import org.junit.*;
import org.sql2o.*;

import main.java.Move;
import main.java.Pokemon;

public class PokemonTest {

	@Rule
	public DatabaseRule database = new DatabaseRule();

	/**
	 * This test checks the correct creation of a pokemon. This is an Unit Test
	 */
	@Test
	public void Pokemon_instantiatesCorrectly_true() {
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		assertEquals(true, myPokemon instanceof Pokemon);
	}

	/**
	 * This test checks the name of a pokemon with a getter. This is an Unit Test
	 */
	@Test
	public void getName_pokemonInstantiatesWithName_String() {
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		assertEquals("Squirtle", myPokemon.getName());
	}

	/**
	 * This test checks if the list of pokemon is empty. It is an Unit test
	 */
	@Test
	public void all_emptyAtFirst() {
		assertEquals(Pokemon.all().size(), 0);
	}

	/**
	 * This test compares pokémons, surely in order to avoid duplicates. It creates
	 * 2 pokémons, and compares them with the .equals () function It is an Unit Test
	 */
	@Test
	public void equals_returnsTrueIfPokemonAreTheSame_true() {
		Pokemon firstPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		Pokemon secondPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		assertTrue(firstPokemon.equals(secondPokemon));
	}

	/**
	 * This test checks if the pokemon has been added to the database. This is an
	 * Unit Test
	 */
	@Test
	public void save_savesPokemonCorrectly_1() {
		Pokemon newPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		newPokemon.save();
		assertEquals(1, Pokemon.all().size());
	}

	/**
	 * This test checks if the pokemon has been added to the database and if it can be found. This is an Integration Test
	 */
	@Test
	public void find_findsPokemonInDatabase_true() {
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		myPokemon.save();
		Pokemon savedPokemon = Pokemon.find(myPokemon.getId());
		assertTrue(myPokemon.equals(savedPokemon));
	}

	/**
	 * This test checks if you can add an attack to the database and add it to a pokemon. This is an Integration Test
	 */
	@Test
	public void addMove_addMoveToPokemon() {
		Move myMove = new Move("Punch", "Normal", 50.0, 100);
		myMove.save();
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		myPokemon.save();
		myPokemon.addMove(myMove);
		Move savedMove = myPokemon.getMoves().get(0);
		assertTrue(myMove.equals(savedMove));
	}
	
	/**
	 * This test checks if you can suppress a Pokémon and if these attacks can also be suppressed with it. This is an Integration Test
	 */
	@Test
	public void delete_deleteAllPokemonAndMovesAssociations() {
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		myPokemon.save();
		Move myMove = new Move("Bubble", "Water", 50.0, 100);
		myMove.save();
		myPokemon.addMove(myMove);
		myPokemon.delete();
		assertEquals(0, Pokemon.all().size());
		assertEquals(0, myPokemon.getMoves().size());
	}

	/**
	 * This test checks if you can add a Pokémon to the database and if you can find it by name. This is an Integration Test
	 */
	@Test
	public void searchByName_findAllPokemonWithSearchInputString_List() {
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "None", "A cute turtle", 50.0, 12, 16, false);
		myPokemon.save();
		assertEquals(myPokemon, Pokemon.searchByName("squir").get(0));
	}

	/**
	 * This test verifies the correct functioning of the reduction of the points of life according to the attack. 
	 * You could say that this is a test that simulates the strengthening training of a pokemon. 
	 * This is a Validation Test
	 */
	@Test
	public void fighting_damagesDefender() {
		Pokemon myPokemon = new Pokemon("Squirtle", "Water", "Normal", "A cute turtle", 50.0, 12, 16, false);
		myPokemon.save();
		myPokemon.hp = 500;
		Move myMove = new Move("Bubble", "Water", 50.0, 100);
		myMove.attack(myPokemon);
		System.out.println(myPokemon.hp);
		myMove.attack(myPokemon);
		System.out.println(myPokemon.hp);
		myMove.attack(myPokemon);
		System.out.println(myPokemon.hp);
		myMove.attack(myPokemon);
		assertEquals(400, myPokemon.hp);
	}
	
	/**
	 * Question 4 with fight between 2 pokemons.
	 */
	@Test
	public void fighting_damagesDefender2() {
		Pokemon Squirtle = new Pokemon("Squirtle", "Water", "Normal", "A cute turtle", 50.0, 12, 16, false);
		Squirtle.save();
		Squirtle.hp = 500;
		Move bubble = new Move("Bubble", "Water", 50.0, 100);
		bubble.save();
		Squirtle.addMove(bubble);
		
		Pokemon Charmander = new Pokemon("Charmander", "Fire", "Normal", "A cute drake", 50.0, 12, 18, false);
		Charmander.save();
		Charmander.hp = 300;
		Move flames = new Move("Jet of flames", "Fire", 50.0, 100);
		flames.save();
		Charmander.addMove(flames);
		
		Charmander.getMoves().get(0).attack(Squirtle);
		System.out.println(Squirtle.hp);
		Squirtle.getMoves().get(0).attack(Charmander);
		System.out.println(Charmander.hp);
		Charmander.getMoves().get(0).attack(Squirtle);
		System.out.println(Squirtle.hp);
		Squirtle.getMoves().get(0).attack(Charmander);
		System.out.println(Charmander.hp);
		Charmander.getMoves().get(0).attack(Squirtle);
		System.out.println(Squirtle.hp);
		Squirtle.getMoves().get(0).attack(Charmander);
		System.out.println(Charmander.hp);
		Charmander.getMoves().get(0).attack(Squirtle);
		System.out.println(Squirtle.hp);
		Squirtle.getMoves().get(0).attack(Charmander);
		System.out.println(Charmander.hp);

		assertEquals(300, Squirtle.hp);
	}

}
