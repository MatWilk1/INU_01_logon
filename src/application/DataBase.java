package application;

import java.util.HashMap;
import java.util.HashSet;

import org.w3c.dom.UserDataHandler;

public class DataBase {

	HashMap<Environment, HashSet<String>> envirUsers = new HashMap<Environment, HashSet<String>>();
	HashMap<String, String> userPassword = new HashMap<String, String>();

	public DataBase() {

		User user1 = new User("Adam Adamski", "Adam");
		User user2 = new User("Bogdan Bog", "Bogdan");
		User user3 = new User("Cezary Cez", "Cezary");
		User user4 = new User("Dariusz Dar", "Dariusz");
		User user5 = new User("Emilia Emilska", "Emilia");
		User user6 = new User("Franciszek Fran", "Franciszek");
		User user7 = new User("Grzegorz Grzeg", "Grzegorz");

		HashSet<String> testList = new HashSet<String>();
		testList.add(user1.getName());
		testList.add(user2.getName());

		HashSet<String> prodList = new HashSet<String>();
		prodList.add(user3.getName());
		prodList.add(user4.getName());

		HashSet<String> dewList = new HashSet<String>();
		dewList.add(user5.getName());
		dewList.add(user6.getName());
		dewList.add(user7.getName());

		envirUsers.put(Environment.Testowe, testList);
		envirUsers.put(Environment.Produkcyjne, prodList);
		envirUsers.put(Environment.Deweloperskie, dewList);

		userPassword.put(user1.getName(), user1.getPassword());
		userPassword.put(user2.getName(), user2.getPassword());
		userPassword.put(user3.getName(), user3.getPassword());
		userPassword.put(user4.getName(), user4.getPassword());
		userPassword.put(user5.getName(), user5.getPassword());
		userPassword.put(user6.getName(), user6.getPassword());
		userPassword.put(user7.getName(), user7.getPassword());

	}

	// Zwraca listê u¿ytkowników dla podanego œrodowiska
	public HashSet<String> getEnvirUsers(Environment envir) {
		return envirUsers.get(envir);
	}


	// Sprawdza czy istnieje taki u¿ytkownik i weryfikuje has³o
	public int passwordCheck(String name, String password) {

		if (!userPassword.containsKey(name)) {
			return -1;
		} else if (userPassword.get(name).equals(password)) {
			return 1;
		} else if (!userPassword.get(name).equals(password)) {
			return 0;
		} else
			return -1;

	}

}