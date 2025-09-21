package hw3;

public class AvatarFactory {
	public static Avatar createAvatar(char type, TerrainMap tm, GridPoint g) throws BadAvatarException {
		if(type == 't') { //creates avatar of different types based on letters typed
			return new Toucan(tm, g);
		} else if(type == 'a'){
			return new Alligator(tm, g);
		} else if(type == 's') {
			return new Sheep(tm, g);
		} else if(type == 'h') {
			return new Human(tm, g);
		} else if(type == 'c') {
			return new Cow(tm, g);
		} else {
			throw new BadAvatarException("Error Avatar type not real"); //throws custom exception if letter is not one of the avatars.
		}
	}
}
